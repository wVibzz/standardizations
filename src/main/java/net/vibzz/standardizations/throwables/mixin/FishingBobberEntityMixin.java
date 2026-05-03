package net.vibzz.standardizations.throwables.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.world.World;
import net.vibzz.standardizations.throwables.ThrowableStandardization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {

    private static final long SALT_FISHING = 0xF1L;

    @Redirect(method = "<init>(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;II)V",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D", ordinal = 0))
    private double standardizations$castX(Random r, PlayerEntity thrower, World world, int lure, int luck) {
        if (!ThrowableStandardization.isActive(world)) return r.nextGaussian();
        if (!(thrower instanceof ServerPlayerEntity)) return r.nextGaussian();
        int attempt = ((ServerPlayerEntity) thrower).getStatHandler()
                .getStat(Stats.USED.getOrCreateStat(Items.FISHING_ROD));
        return ThrowableStandardization.gaussianForAttempt(
                ThrowableStandardization.seedOf(world), attempt, SALT_FISHING, 0);
    }

    @Redirect(method = "<init>(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;II)V",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D", ordinal = 1))
    private double standardizations$castY(Random r, PlayerEntity thrower, World world, int lure, int luck) {
        if (!ThrowableStandardization.isActive(world)) return r.nextGaussian();
        if (!(thrower instanceof ServerPlayerEntity)) return r.nextGaussian();
        int attempt = ((ServerPlayerEntity) thrower).getStatHandler()
                .getStat(Stats.USED.getOrCreateStat(Items.FISHING_ROD));
        return ThrowableStandardization.gaussianForAttempt(
                ThrowableStandardization.seedOf(world), attempt, SALT_FISHING, 1);
    }

    @Redirect(method = "<init>(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/World;II)V",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D", ordinal = 2))
    private double standardizations$castZ(Random r, PlayerEntity thrower, World world, int lure, int luck) {
        if (!ThrowableStandardization.isActive(world)) return r.nextGaussian();
        if (!(thrower instanceof ServerPlayerEntity)) return r.nextGaussian();
        int attempt = ((ServerPlayerEntity) thrower).getStatHandler()
                .getStat(Stats.USED.getOrCreateStat(Items.FISHING_ROD));
        return ThrowableStandardization.gaussianForAttempt(
                ThrowableStandardization.seedOf(world), attempt, SALT_FISHING, 2);
    }
}
