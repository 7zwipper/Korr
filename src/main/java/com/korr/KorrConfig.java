//pizda
package com.korr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public class KorrConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("korr.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static ConfigData data = new ConfigData();

    public static void load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String content = Files.readString(CONFIG_PATH, StandardCharsets.UTF_8);
                data = GSON.fromJson(content, ConfigData.class);
            } catch (Exception e) {
                Korr.LOGGER.error("[Korr] Failed to load config: " + e.getMessage());
            }
        }
        save();
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(data), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Korr.LOGGER.error("[Korr] Failed to save config: " + e.getMessage());
        }
    }

    public static class ConfigData {
        public float dayFogStart = 48f;
        public float dayFogEnd = 96f;
        public float nightFogStart = 24f;
        public float nightFogEnd = 48f;
        public float spikeFogEnd = 24f;
        public float spikeChance = 0.6f;
        public long spikeMinDelayMs = 180000L;
        public long spikeMaxDelayMs = 1500000L;
        public long spikeMinDurationMs = 8000L;
        public long spikeMaxDurationMs = 20000L;
        public boolean enabled = true;
        public boolean smoothTransitions = true;
        public float transitionSpeed = 0.05f;
    }
}
