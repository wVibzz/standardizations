package net.vibzz.standardizations.glowstone.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.vibzz.standardizations.Standardizations;
import net.vibzz.standardizations.glowstone.GlowstoneCounter;
import net.vibzz.standardizations.glowstone.GlowstoneStandardization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Random;

@Mixin(Block.class)
public abstract class BlockDropMixin {

    @WrapOperation(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;",
                   at = @At(value = "INVOKE",
                            target = "Lnet/minecraft/loot/context/LootContext$Builder;random(Ljava/util/Random;)Lnet/minecraft/loot/context/LootContext$Builder;"))
    private static LootContext.Builder standardizations$wrapFull(LootContext.Builder builder, Random random, Operation<LootContext.Builder> original, BlockState state, ServerWorld world, BlockPos pos, BlockEntity be, Entity entity, ItemStack tool) {
        return original.call(builder, standardizations$pickRandom(world, state, pos, random));
    }

    @WrapOperation(method = "getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;)Ljava/util/List;",
                   at = @At(value = "INVOKE",
                            target = "Lnet/minecraft/loot/context/LootContext$Builder;random(Ljava/util/Random;)Lnet/minecraft/loot/context/LootContext$Builder;"))
    private static LootContext.Builder standardizations$wrapPartial(LootContext.Builder builder, Random random, Operation<LootContext.Builder> original, BlockState state, ServerWorld world, BlockPos pos, BlockEntity be) {
        return original.call(builder, standardizations$pickRandom(world, state, pos, random));
    }

    @Unique
    private static Random standardizations$pickRandom(ServerWorld world, BlockState state, BlockPos pos, Random fallback) {
        if (!state.isOf(Blocks.GLOWSTONE)) return fallback;
        if (!GlowstoneStandardization.isActive(world)) return fallback;
        if (world.getGameRules().getBoolean(Standardizations.STANDARDIZE_DROPS_BY_XYZ)) {
            return GlowstoneStandardization.dropRandomByPos(world.getSeed(), pos);
        }
        int idx = GlowstoneCounter.get(world).getAndIncrement();
        return GlowstoneStandardization.dropRandom(world.getSeed(), idx);
    }
}
