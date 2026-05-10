package net.vibzz.standardizations.mobSpawns.mixin;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.MobEntity;
import net.vibzz.standardizations.mobSpawns.MobSpawnStandardization;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PrioritizedGoal.class)
public abstract class PrioritizedGoalMixin {

    @Shadow @Final private Goal goal;

    @Inject(method = "canStart", at = @At("HEAD"))
    private void standardizations$seedCanStart(CallbackInfoReturnable<Boolean> cir) {
        standardizations$reseed();
    }

    @Inject(method = "shouldContinue", at = @At("HEAD"))
    private void standardizations$seedShouldContinue(CallbackInfoReturnable<Boolean> cir) {
        standardizations$reseed();
    }

    @Inject(method = "start", at = @At("HEAD"))
    private void standardizations$seedStart(CallbackInfo ci) {
        standardizations$reseed();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void standardizations$seedTick(CallbackInfo ci) {
        standardizations$reseed();
    }

    private void standardizations$reseed() {
        MobEntity mob = MobSpawnStandardization.getCurrentMob();
        if (mob == null) return;
        if (MobSpawnStandardization.isActiveForMob(mob)) {
            MobSpawnStandardization.seedForGoalCall(mob, this.goal.getClass());
        }
    }
}
