# CI helpers
- `server-test.sh`: starts a Minecraft server with LogicLib, waits for it to finish starting, stops it, and fails if LogicLib didn't load or logged an error. Used by `.github/workflows/build.yml`.
