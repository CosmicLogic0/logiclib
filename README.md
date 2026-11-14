# LogicLib

Shared code library for CosmicLogic's Minecraft mods. Adds no gameplay on its own.
You only need it if one of CosmicLogic's mods lists it as required.

- Mod id: `logiclib` · Package: `com.cosmiclogic.logiclib` · License: MIT
- Loaders: Forge, NeoForge · First version: Minecraft 1.21.1

## Building
Needs Java 21. Run `gradlew build` (Windows: `gradlew.bat build`). Jars end up in `forge/build/libs` and `neoforge/build/libs`.
GitHub Actions builds both on every push (see the Actions tab).

## Using it in a mod (for CosmicLogic's mods)
During development: run `gradlew publishToMavenLocal` here, then in the mod:
```groovy
repositories { mavenLocal() }
dependencies {
    // common module: compileOnly against the common jar
    compileOnly "com.cosmiclogic.logiclib:logiclib-common-1.21.1:0.1.0"
    // loader modules: the matching loader jar
    implementation "com.cosmiclogic.logiclib:logiclib-neoforge-1.21.1:0.1.0"   // or -forge-
}
```
After LogicLib is on CurseForge, mods can use Curse Maven instead so GitHub Actions can build them.

### Modules
| Class | What it does |
|---|---|
| `platform.Services.PLATFORM` | Loader name, `isModLoaded`, config folder, `isClient` |
| `config.LogicConfig` | JSON config: `LogicConfig.create(MOD_ID, MyConfig.class, MyConfig::new).get()` |
| `server.CreatorServer` | `CreatorServer.request(MOD_ID, config.addCreatorServer)` adds Logic Survival once across all mods |
| `registry.LogicRegistry` | `LogicRegistry.register(Registries.ITEM, MOD_ID, "name", () -> new Item(...))` |
