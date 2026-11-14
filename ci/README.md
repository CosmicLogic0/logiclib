# CI helpers
- `server-test.sh`: starts a Minecraft server with CosmicLib, waits for it to finish starting, stops it, and fails if CosmicLib didn't load or logged an error. Used by `.github/workflows/build.yml`.
