package net.vibzz.standardizations.mobSpawns.mixin;

import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.vibzz.standardizations.Standardizations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WanderAroundGoal.class)
public abstract class WanderAroundGoalMixin {

    @Redirect(
            method = "canStart",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/PathAwareEntity;getDespawnCounter()I"
            )
    )
    private int standardizations$bypassDespawnGate(PathAwareEntity self) {
        if (self.world instanceof ServerWorld
                && self.world.getGameRules().getBoolean(Standardizations.STANDARDIZE_INITIAL_SPAWNS)) {
            return 0;
        }
        return self.getDespawnCounter();
    }
}
