package com.korr;

import net.fabricmc.api.ClientModInitializer;

public class KorrClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Korr.LOGGER.info("[Korr] Client initialized.");
    }
}
