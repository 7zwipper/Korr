package com.korr.fog;

import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import java.util.concurrent.ThreadLocalRandom;

public class KorrFogModifier extends FogModifier {

    private long spikeUntilMs = 0L;
    private long nextSpikeCheckMs = 0L;

    private float currentStart = 0f;
    private float currentEnd = 0f;
    private boolean initialized = false;

    @Override
    public void applyStartEndModifier(FogData data, Camera camera, ClientWorld clientWorld, float tickDelta, RenderTickCounter renderTickCounter) {
        if (!com.korr.KorrConfig.data.enabled) return;

        long dayTime = clientWorld.getTimeOfDay() % 24000L;
        boolean isNight = dayTime >= 13000L && dayTime <= 23000L;

        com.korr.KorrConfig.ConfigData cfg = com.korr.KorrConfig.data;
        float targetEnd = isNight ? cfg.nightFogEnd : cfg.dayFogEnd;
        float targetStart = isNight ? cfg.nightFogStart : cfg.dayFogStart;

        long now = System.currentTimeMillis();

        if (now >= nextSpikeCheckMs) {
            long delayMs = ThreadLocalRandom.current().nextLong(cfg.spikeMinDelayMs, cfg.spikeMaxDelayMs);
            nextSpikeCheckMs = now + delayMs;
            if (ThreadLocalRandom.current().nextFloat() < cfg.spikeChance) {
                long durationMs = ThreadLocalRandom.current().nextLong(cfg.spikeMinDurationMs, cfg.spikeMaxDurationMs);
                spikeUntilMs = now + durationMs;
            }
        }

        if (now < spikeUntilMs) {
            targetEnd = cfg.spikeFogEnd;
            targetStart = cfg.spikeFogEnd * 0.5f;
        }

        if (!initialized) {
            currentStart = targetStart;
            currentEnd = targetEnd;
            initialized = true;
        }

        if (cfg.smoothTransitions) {
            float speed = cfg.transitionSpeed;
            currentStart += (targetStart - currentStart) * speed;
            currentEnd += (targetEnd - currentEnd) * speed;
        } else {
            currentStart = targetStart;
            currentEnd = targetEnd;
        }

        data.renderDistanceStart = currentStart;
        data.renderDistanceEnd = currentEnd;
        data.environmentalStart = currentStart;
        data.environmentalEnd = currentEnd;
    }

    @Override
    public boolean shouldApply(CameraSubmersionType type, Entity entity) {
        return (type == null || type == CameraSubmersionType.NONE) && com.korr.KorrConfig.data.enabled;
    }
}
