package net.vibzz.standardizations.explosion.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.vibzz.standardizations.blockdrops.BlockDropRng;
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
        if (!BlockDropRng.isExplosionActive(self.world)) return random.nextInt(bound);
        double u = BlockDropRng.uniform01(BlockDropRng.seedOf(self.world), self.getBlockPos(),
                BlockDropRng.SALT_TNT_MINECART_FUSE_A);
        return (int) (u * bound);
    }

    @Redirect(method = "dropItems",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 1))
    private int standardizations$minecartFuseB(Random random, int bound) {
        Entity self = (Entity) (Object) this;
        if (!BlockDropRng.isExplosionActive(self.world)) return random.nextInt(bound);
        double u = BlockDropRng.uniform01(BlockDropRng.seedOf(self.world), self.getBlockPos(),
                BlockDropRng.SALT_TNT_MINECART_FUSE_B);
        return (int) (u * bound);
    }

    @Redirect(method = "explode",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextDouble()D"))
    private double standardizations$minecartPower(Random random) {
        Entity self = (Entity) (Object) this;
        if (!BlockDropRng.isExplosionActive(self.world)) return random.nextDouble();
        return BlockDropRng.uniform01(BlockDropRng.seedOf(self.world), self.getBlockPos(),
                BlockDropRng.SALT_TNT_MINECART_POWER);
    }
}
