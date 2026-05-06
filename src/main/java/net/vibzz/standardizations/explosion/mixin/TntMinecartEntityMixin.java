package net.vibzz.standardizations.explosion.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.vibzz.standardizations.explosion.ExplosionRng;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(TntMinecartEntity.class)
public abstract class TntMinecartEntityMixin {

    @Redirect(method = "dropItems",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 0))
    private int standardizations$minecartFuseA(Random random, int bound) {
        Entity self = (Entity) (Object) this;
        if (!ExplosionRng.isActive(self.world)) return random.nextInt(bound);
        double u = ExplosionRng.uniform01(ExplosionRng.seedOf(self.world), self.getBlockPos(),
                ExplosionRng.SALT_MINECART_FUSE_A);
        return (int) (u * bound);
    }

    @Redirect(method = "dropItems",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 1))
    private int standardizations$minecartFuseB(Random random, int bound) {
        Entity self = (Entity) (Object) this;
        if (!ExplosionRng.isActive(self.world)) return random.nextInt(bound);
        double u = ExplosionRng.uniform01(ExplosionRng.seedOf(self.world), self.getBlockPos(),
                ExplosionRng.SALT_MINECART_FUSE_B);
        return (int) (u * bound);
    }

    @Redirect(method = "explode",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextDouble()D"))
    private double standardizations$minecartPower(Random random) {
        Entity self = (Entity) (Object) this;
        if (!ExplosionRng.isActive(self.world)) return random.nextDouble();
        return ExplosionRng.uniform01(ExplosionRng.seedOf(self.world), self.getBlockPos(),
                ExplosionRng.SALT_MINECART_POWER);
    }
}
