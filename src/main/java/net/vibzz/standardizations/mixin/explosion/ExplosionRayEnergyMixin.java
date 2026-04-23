package net.vibzz.standardizations.mixin.explosion;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.vibzz.standardizations.util.BlockDropRng;
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
public abstract class ExplosionRayEnergyMixin {

    @Shadow @Final private World world;
    @Shadow @Final private double x;
    @Shadow @Final private double y;
    @Shadow @Final private double z;

    @Unique
    private int standardizations$rayIndex;

    @Inject(method = "collectBlocksAndDamageEntities", at = @At("HEAD"))
    private void standardizations$resetRayCounter(CallbackInfo ci) {
        this.standardizations$rayIndex = 0;
    }

    @Redirect(method = "collectBlocksAndDamageEntities",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F"))
    private float standardizations$deterministicRayEnergy(Random random) {
        if (!BlockDropRng.isExplosionActive(this.world)) return random.nextFloat();
        BlockPos origin = new BlockPos(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
        int idx = this.standardizations$rayIndex++;
        return (float) BlockDropRng.uniform01(
                BlockDropRng.seedOf(this.world),
                origin,
                BlockDropRng.SALT_EXPLOSION_RAY,
                idx);
    }
}
