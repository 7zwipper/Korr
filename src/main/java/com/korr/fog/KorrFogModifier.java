package com.korr.fog;

import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

public class KorrFogModifier extends FogModifier {

    // 3 чанка = 48 блоков
    private static final float FOG_START = 24.0f;
    private static final float FOG_END = 48.0f;

    @Override
    public void applyStartEndModifier(FogData data, Camera camera, ClientWorld clientWorld, float tickDelta, RenderTickCounter renderTickCounter) {
        data.renderDistanceStart = FOG_START;
        data.renderDistanceEnd = FOG_END;
        data.environmentalStart = FOG_START;
        data.environmentalEnd = FOG_END;
    }

    @Override
    public boolean shouldApply(CameraSubmersionType submersionType, Entity cameraEntity) {
        return submersionType == null || submersionType == CameraSubmersionType.NONE;
     }
}
