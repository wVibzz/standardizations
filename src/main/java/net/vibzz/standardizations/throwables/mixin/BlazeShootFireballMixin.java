package net.vibzz.standardizations.throwables.mixin;

import net.minecraft.entity.mob.BlazeEntity;
import net.vibzz.standardizations.Standardizations;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(targets = "net/minecraft/entity/mob/BlazeEntity$ShootFireballGoal")
public abstract class BlazeShootFireballMixin {

    @Shadow @Final private BlazeEntity blaze;

    @Redirect(method = "tick",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D", ordinal = 0))
    private double standardizations$blazeFireballX(Random random) {
        if (!this.blaze.world.getGameRules().getBoolean(Standardizations.STANDARDIZE_THROWABLES)) return random.nextGaussian();
        return 0.0;
    }

    @Redirect(method = "tick",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D", ordinal = 1))
    private double standardizations$blazeFireballZ(Random random) {
        if (!this.blaze.world.getGameRules().getBoolean(Standardizations.STANDARDIZE_THROWABLES)) return random.nextGaussian();
        return 0.0;
    }
}
