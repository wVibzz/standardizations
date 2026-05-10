package net.vibzz.standardizations.debug.mixin;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import net.vibzz.standardizations.debug.DebugNametag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public abstract class MobEntityDebugTickMixin {

    @Shadow @Final protected GoalSelector goalSelector;

    @Inject(method = "tickNewAi", at = @At("RETURN"))
    private void standardizations$updateDebugNametag(CallbackInfo ci) {
        MobEntity self = (MobEntity) (Object) this;
        DebugNametag.update(self, this.goalSelector);
    }
}
