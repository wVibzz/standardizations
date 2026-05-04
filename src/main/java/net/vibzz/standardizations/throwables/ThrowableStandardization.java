package net.vibzz.standardizations.throwables;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.vibzz.standardizations.Standardizations;

import java.util.UUID;

public final class ThrowableStandardization {

    private static final long MIX_C1 = 0xBF58476D1CE4E5B9L;
    private static final long MIX_C2 = 0x94D049BB133111EBL;

    private static final long SALT_ATTEMPT = 0xE0L;
    private static final long SALT_GAUSS_X = 0xE1L;
    private static final long SALT_GAUSS_Y = 0xE2L;
    private static final long SALT_GAUSS_Z = 0xE3L;

    private ThrowableStandardization() {}

    public static boolean isActive(World world) {
        return world instanceof ServerWorld
                && world.getGameRules().getBoolean(Standardizations.STANDARDIZE_THROWABLES);
    }

    public static long seedOf(World world) {
        return ((ServerWorld) world).getSeed();
    }

    public static Item statItemFor(ProjectileEntity entity) {
        if (entity instanceof EnderPearlEntity) return Items.ENDER_PEARL;
        if (entity instanceof EggEntity) return Items.EGG;
        if (entity instanceof SnowballEntity) return Items.SNOWBALL;
        if (entity instanceof ExperienceBottleEntity) return Items.EXPERIENCE_BOTTLE;
        if (entity instanceof PotionEntity) return ((PotionEntity) entity).getStack().getItem();
        return null;
    }

    public static UUID uuidForAttempt(long worldSeed, int attemptIndex, Item statItem) {
        long base = mix(worldSeed
                ^ ((long) attemptIndex * 0x9E3779B97F4A7C15L)
                ^ ((long) Item.getRawId(statItem) * 0x6C3671D916B4A139L)
                ^ SALT_ATTEMPT);
        return new UUID(base, mix(base ^ MIX_C1));
    }

    public static double gaussianFromUuid(UUID uuid, int axis) {
        long base = mix(uuid.getMostSignificantBits() ^ (uuid.getLeastSignificantBits() * 0x9E3779B97F4A7C15L));
        return gaussian(base, saltForAxis(axis));
    }

    public static double gaussianForAttempt(long worldSeed, int attemptIndex, long extraSalt, int axis) {
        long base = mix(worldSeed
                ^ ((long) attemptIndex * 0x9E3779B97F4A7C15L)
                ^ extraSalt);
        return gaussian(base, saltForAxis(axis));
    }

    private static double gaussian(long base, long axisSalt) {
        double u1 = Math.max(uniform(mix(base ^ axisSalt ^ 0xC1L)), 1.0E-300);
        double u2 = uniform(mix(base ^ axisSalt ^ 0xC2L));
        return Math.sqrt(-2.0 * Math.log(u1)) * Math.cos(2.0 * Math.PI * u2);
    }

    private static long saltForAxis(int axis) {
        switch (axis) {
            case 0: return SALT_GAUSS_X;
            case 1: return SALT_GAUSS_Y;
            default: return SALT_GAUSS_Z;
        }
    }

    private static long mix(long z) {
        z = (z ^ (z >>> 30)) * MIX_C1;
        z = (z ^ (z >>> 27)) * MIX_C2;
        return z ^ (z >>> 31);
    }

    private static double uniform(long bits) {
        return (bits >>> 11) * 0x1.0p-53;
    }
}
