package com.cosmiclogic.logiclib.platform;

import com.cosmiclogic.logiclib.platform.services.IPlatformHelper;

/** Loader-specific helpers. On 1.12.2 there is only Forge, so the helper is created directly. */
public final class Services {

    public static final IPlatformHelper PLATFORM = new ForgePlatformHelper();

    private Services() {
    }
}
