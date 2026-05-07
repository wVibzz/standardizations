package net.vibzz.standardizations.mobSpawns.mixin;

import net.minecraft.entity.mob.MobEntity;
import net.vibzz.standardizations.mobSpawns.MobSpawnStandardization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityTickMixin {

    @Inject(method = "tickNewAi", at = @At("HEAD"))
    private void standardizations$setMobContext(CallbackInfo ci) {
        MobSpawnStandardization.setCurrentMob((MobEntity) (Object) this);
    }

    @Inject(method = "tickNewAi", at = @At("RETURN"))
    private void standardizations$clearMobContext(CallbackInfo ci) {
        MobSpawnStandardization.clearCurrentMob();
    }
}
