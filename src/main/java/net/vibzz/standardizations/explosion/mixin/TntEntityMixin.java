package net.vibzz.standardizations.explosion.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.vibzz.standardizations.blockdrops.BlockDropRng;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin {

    @Redirect(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/entity/LivingEntity;)V",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextDouble()D"))
    private double standardizations$deterministicPrimedAngle(Random random, World world, double x, double y, double z, LivingEntity igniter) {
        if (!BlockDropRng.isExplosionActive(world)) return random.nextDouble();
        BlockPos pos = new BlockPos(x, y, z);
        return BlockDropRng.uniform01(BlockDropRng.seedOf(world), pos, BlockDropRng.SALT_TNT_ANGLE);
    }
}
