package com.cosmiclogic.cosmiclib.config;

import com.cosmiclogic.cosmiclib.CosmicLib;
import com.cosmiclogic.cosmiclib.platform.Services;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A simple JSON config file in the game's config folder.
 *
 * <pre>{@code
 * public class MyConfig {
 *     public boolean addCreatorServer = true;
 *     public int range = 16;
 * }
 *
 * public static final CosmicConfig<MyConfig> CONFIG = CosmicConfig.create("mymod", MyConfig.class, MyConfig::new);
 * int range = CONFIG.get().range;
 * }</pre>
 *
 * New fields added in a mod update are written into existing files automatically, keeping the
 * player's other values. A broken file is backed up as {@code <name>.json.broken} and replaced
 * with defaults instead of crashing the game.
 */
public final class CosmicConfig<T> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Path file;
    private final Class<T> type;
    private final Supplier<T> defaults;
    private T value;

    private CosmicConfig(Path file, Class<T> type, Supplier<T> defaults) {
        this.file = file;
        this.type = type;
        this.defaults = defaults;
    }

    /** Creates and loads {@code config/<fileName>.json}. Usually fileName is your mod id. */
    public static <T> CosmicConfig<T> create(String fileName, Class<T> type, Supplier<T> defaults) {
        CosmicConfig<T> config = new CosmicConfig<>(Services.PLATFORM.getConfigDir().resolve(fileName + ".json"), type, defaults);
        config.load();
        return config;
    }

    public T get() {
        return value;
    }

    public Path getFile() {
        return file;
    }

    /** Reloads from disk, adding any missing fields with their default values. */
    public synchronized void load() {
        JsonObject defaultsJson = GSON.toJsonTree(defaults.get()).getAsJsonObject();
        JsonObject merged = defaultsJson.deepCopy();

        if (Files.exists(file)) {
            try {
                String text = Files.readString(file, StandardCharsets.UTF_8);
                JsonElement parsed = JsonParser.parseString(text);
                if (!parsed.isJsonObject()) {
                    throw new IllegalStateException("not a JSON object");
                }
                for (Map.Entry<String, JsonElement> entry : parsed.getAsJsonObject().entrySet()) {
                    if (defaultsJson.has(entry.getKey())) {
                        merged.add(entry.getKey(), entry.getValue());
                    }
                }
                value = GSON.fromJson(merged, type);
            } catch (Exception e) {
                CosmicLib.LOG.warn("Config {} is broken ({}). Backing it up and using defaults.", file.getFileName(), e.getMessage());
                backupBroken();
                merged = defaultsJson;
                value = GSON.fromJson(defaultsJson, type);
            }
        } else {
            value = GSON.fromJson(defaultsJson, type);
        }
        write(merged);
    }

    /** Saves the current values to disk (call after changing fields of {@link #get()}). */
    public synchronized void save() {
        write(GSON.toJsonTree(value).getAsJsonObject());
    }

    private void write(JsonObject json) {
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, GSON.toJson(json), StandardCharsets.UTF_8);
        } catch (IOException e) {
            CosmicLib.LOG.warn("Could not save config {}: {}", file.getFileName(), e.getMessage());
        }
    }

    private void backupBroken() {
        try {
            Files.copy(file, file.resolveSibling(file.getFileName() + ".broken"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ignored) {
            // Nothing else we can do; defaults will overwrite the broken file.
        }
    }
}
