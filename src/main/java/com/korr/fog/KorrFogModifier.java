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
            JsonObject atmospheric = fogTypeConfig.getAsJsonObject("ATMOSPHERIC");
            if (atmospheric == null) return;

            atmospheric.addProperty("enable", false);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Files.writeString(configPath, gson.toJson(root), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println("[Korr] Could not patch Sodium Extra config: " + e.getMessage());
        }
    }
}
