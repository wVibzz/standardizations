package net.vibzz.standardizations.explosion.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.vibzz.standardizations.blockdrops.BlockDropRng;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(Explosion.class)
public abstract class ExplosionFireMixin {

    @Shadow @Final private World world;
    @Shadow @Final private double x;
    @Shadow @Final private double y;
    @Shadow @Final private double z;

    @Unique private int standardizations$fireIndex;

    @Inject(method = "affectWorld", at = @At("HEAD"))
    private void standardizations$resetFireIndex(boolean particles, CallbackInfo ci) {
        this.standardizations$fireIndex = 0;
    }

    @Redirect(method = "affectWorld",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"))
    private int standardizations$deterministicFire(Random random, int bound) {
        if (!BlockDropRng.isExplosionActive(this.world)) return random.nextInt(bound);
        BlockPos origin = new BlockPos(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
        int idx = this.standardizations$fireIndex++;
        double u = BlockDropRng.uniform01(BlockDropRng.seedOf(this.world), origin,
                BlockDropRng.SALT_EXPLOSION_FIRE, idx);
        return (int) (u * bound);
    }
}
