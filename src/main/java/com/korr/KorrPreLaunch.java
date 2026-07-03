package com.korr;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class KorrPreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        try {
            int viewDistance = readViewDistance();
            boolean highDistance = viewDistance >= 15;
            int endPercent = highDistance ? 20 : 100;

            Path configPath = FabricLoader.getInstance().getConfigDir().resolve("sodium-extra-options.json");
            if (!Files.exists(configPath)) {
                return;
            }
            String content = Files.readString(configPath, StandardCharsets.UTF_8);
            JsonObject root = JsonParser.parseString(content).getAsJsonObject();

            JsonObject renderSettings = root.getAsJsonObject("render_settings");
            if (renderSettings == null) return;
            JsonObject fogTypeConfig = renderSettings.getAsJsonObject("fog_type_config");
            if (fogTypeConfig == null) return;
            JsonObject atmospheric = fogTypeConfig.getAsJsonObject("ATOSPHERIC");
            if (atmospheric == null) return;

            atmospheric.addProperty("enable", true);
            atmospheric.addProperty("environment_start_multiplier", 0);
            atmospheric.addProperty("environment_end_multiplier", endPercent);
            atmospheric.addProperty("render_distance_start_multiplier", 0);
            atmospheric.addProperty("render_distance_end_multiplier", endPercent);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.writeString(configPath, gson.toJson(root), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("[Korr] Could not patch Sodium Extra config: " + e.getMessage());
        }
    }

    private int readViewDistance() {
        try {
            Path optionsPath = FabricLoader.getInstance().getGameDir().resolve("options.txt");
            if (!Files.exists(optionsPath)) {
                return 12;
            }
            for (String line : Files.readAllLines(optionsPath, StandardCharsets.UTF_8)) {
                if (line.startsWith("renderDistance:")) {
                    String value = line.substring("renderDistance:".length()).trim();
                    return Integer.parseInt(value);
                }
            }
        } catch (Exception e) {
            System.out.println("[Korr] Could not read render distance: " + e.getMessage());
        }
        return 12;
    }
}
