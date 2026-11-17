package com.cosmiclogic.logiclib.config;

import com.cosmiclogic.logiclib.LogicLib;
import com.cosmiclogic.logiclib.platform.Services;
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
 * A simple JSON config file in the game's config folder. Same API on every version.
 *
 * <pre>{@code
 * public static final LogicConfig<MyConfig> CONFIG = LogicConfig.create("mymod", MyConfig.class, MyConfig::new);
 * }</pre>
 *
 * New fields added in a mod update are written into existing files automatically, keeping the
 * player's other values. A broken file is backed up as {@code <name>.json.broken} and replaced
 * with defaults instead of crashing the game.
 */
public final class LogicConfig<T> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Path file;
    private final Class<T> type;
    private final Supplier<T> defaults;
    private T value;

    private LogicConfig(Path file, Class<T> type, Supplier<T> defaults) {
        this.file = file;
        this.type = type;
        this.defaults = defaults;
    }

    /** Creates and loads {@code config/<fileName>.json}. Usually fileName is your mod id. */
    public static <T> LogicConfig<T> create(String fileName, Class<T> type, Supplier<T> defaults) {
        LogicConfig<T> config = new LogicConfig<>(Services.PLATFORM.getConfigDir().resolve(fileName + ".json"), type, defaults);
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
        JsonObject merged = GSON.toJsonTree(defaults.get()).getAsJsonObject();

        if (Files.exists(file)) {
            try {
                String text = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
                JsonElement parsed = parse(text);
                if (parsed == null || !parsed.isJsonObject()) {
                    throw new IllegalStateException("not a JSON object");
                }
                for (Map.Entry<String, JsonElement> entry : parsed.getAsJsonObject().entrySet()) {
                    if (defaultsJson.has(entry.getKey())) {
                        merged.add(entry.getKey(), entry.getValue());
                    }
                }
                value = GSON.fromJson(merged, type);
            } catch (Exception e) {
                LogicLib.LOG.warn("Config {} is broken ({}). Backing it up and using defaults.", file.getFileName(), e.getMessage());
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

    @SuppressWarnings("deprecation")
    private static JsonElement parse(String text) {
        return new JsonParser().parse(text);
    }

    private void write(JsonObject json) {
        try {
            Files.createDirectories(file.getParent());
            Files.write(file, GSON.toJson(json).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LogicLib.LOG.warn("Could not save config {}: {}", file.getFileName(), e.getMessage());
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
