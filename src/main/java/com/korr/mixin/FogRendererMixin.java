package com.korr.mixin;

import com.korr.Korr;
import com.korr.fog.KorrFogModifier;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.render.fog.FogRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(FogRenderer.class)
public class FogRendererMixin {

    @Mutable
    @Shadow
    @Final
    private static List<FogModifier> FOG_MODIFIERS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void korr$registerFogModifier(CallbackInfo ci) {
        List<FogModifier> modified = new ArrayList<>();
        modified.add(new KorrFogModifier());
        modified.addAll(FOG_MODIFIERS);
        FOG_MODIFIERS = modified;
        Korr.LOGGER.info("[Korr] Fog modifier registered (priority).");
    }
}
