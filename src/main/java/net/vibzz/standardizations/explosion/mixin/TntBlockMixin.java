package net.vibzz.standardizations.explosion.mixin;

import net.minecraft.block.TntBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.vibzz.standardizations.explosion.ExplosionRng;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin {

    @Redirect(method = "onDestroyedByExplosion",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"))
    private int standardizations$deterministicChainFuse(Random random, int bound, World world, BlockPos pos, Explosion explosion) {
        if (!ExplosionRng.isActive(world)) return random.nextInt(bound);
        return (int) (ExplosionRng.uniform01(ExplosionRng.seedOf(world), pos, ExplosionRng.SALT_TNT_FUSE) * bound);
    }
}
