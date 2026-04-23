package net.vibzz.standardizations.mixin.drops;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.vibzz.standardizations.util.BlockDropRng;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemScatterer.class)
public abstract class ItemScattererMixin {

    @Inject(method = "spawn(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V",
            at = @At("HEAD"),
            cancellable = true)
    private static void standardizations$deterministicScatter(World world, double x, double y, double z, ItemStack item, CallbackInfo ci) {
        if (world.isClient || item.isEmpty() || !BlockDropRng.isActive(world)) return;

        BlockPos pos = new BlockPos(Math.floor(x), Math.floor(y), Math.floor(z));
        long seed = BlockDropRng.seedOf(world);

        // per-call salt: distinguishes leaf spawn calls in the same container break
        // (inventory iteration calls this leaf once per slot; each slot has its own item / count)
        long callKey = ((long) Registry.ITEM.getRawId(item.getItem()) << 32) ^ (item.getCount() & 0xffffffffL);

        double width = EntityType.ITEM.getWidth();    // 0.25
        double span = 1.0 - width;                     // 0.75
        double half = width / 2.0;                     // 0.125
        double g = Math.floor(x) + BlockDropRng.uniform01(seed, pos, BlockDropRng.SALT_SCATTER_X, callKey) * span + half;
        double h = Math.floor(y) + BlockDropRng.uniform01(seed, pos, BlockDropRng.SALT_SCATTER_Y, callKey) * span;
        double i = Math.floor(z) + BlockDropRng.uniform01(seed, pos, BlockDropRng.SALT_SCATTER_Z, callKey) * span + half;

        int chunkIndex = 0;
        while (!item.isEmpty()) {
            long chunkKey = callKey ^ ((long) (chunkIndex + 1) * 0x9E3779B97F4A7C15L);
            int chunkSize = (int) (BlockDropRng.uniform01(seed, pos, BlockDropRng.SALT_CHUNK_SIZE, chunkKey) * 21) + 10;
            ItemEntity entity = new ItemEntity(world, g, h, i, item.split(chunkSize));
            entity.yaw = (float) (BlockDropRng.uniform01(seed, pos, BlockDropRng.SALT_YAW_ALT, chunkKey) * 360.0);
            double vx = BlockDropRng.gaussian(seed, pos, BlockDropRng.SALT_SCATTER_VX, chunkKey) * 0.05;
            double vy = BlockDropRng.gaussian(seed, pos, BlockDropRng.SALT_SCATTER_VY, chunkKey) * 0.05 + 0.2;
            double vz = BlockDropRng.gaussian(seed, pos, BlockDropRng.SALT_SCATTER_VZ, chunkKey) * 0.05;
            entity.setVelocity(vx, vy, vz);
            world.spawnEntity(entity);
            chunkIndex++;
        }
        ci.cancel();
    }
}
