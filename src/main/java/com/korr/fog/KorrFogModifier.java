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

    private static final float DAY_START = 48f;
    private static final float DAY_END = 96f;
    private static final float NIGHT_START = 24f;
    private static final float NIGHT_END = 48f;
    private static final float SPIKE_END = 24f;

    private static long spikeUntilMs = 0L;
    private static long nextSpikeCheckMs = 0L;

    @Override
    public void applyStartEndModifier(FogData data, Camera camera, ClientWorld clientWorld, float tickDelta, RenderTickCounter renderTickCounter) {
        long dayTime = clientWorld.getTimeOfDay() % 24000L;
        boolean isNight = dayTime >= 13000L && dayTime <= 23000L;

        float end = isNight ? NIGHT_END : DAY_END;
        float start = isNight ? NIGHT_START : DAY_START;

        long now = System.currentTimeMillis();

        if (now >= nextSpikeCheckMs) {
            long delayMs = ThreadLocalRandom.current().nextLong(180000L, 1500000L);
            nextSpikeCheckMs = now + delayMs;
            if (ThreadLocalRandom.current().nextFloat() < 0.6f) {
                long durationMs = ThreadLocalRandom.current().nextLong(8000L, 20000L);
                spikeUntilMs = now + durationMs;
            }
        }

        if (now < spikeUntilMs) {
            end = SPIKE_END;
            start = SPIKE_END * 0.5f;
        }

        data.renderDistanceStart = start;
        data.renderDistanceEnd = end;
        data.environmentalStart = start;
        data.environmentalEnd = end;
    }

    @Override
    public boolean shouldApply(CameraSubmersionType submersionType, Entity cameraEntity) {
        boolean alwaysApply = true;
        return alwaysAp ply;
    }
}
