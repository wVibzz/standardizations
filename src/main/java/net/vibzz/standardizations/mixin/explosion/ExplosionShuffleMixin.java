package net.vibzz.standardizations.mixin.explosion;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.vibzz.standardizations.util.BlockDropRng;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Mixin(Explosion.class)
public abstract class ExplosionShuffleMixin {

    @Shadow @Final private World world;

    @Redirect(method = "affectWorld",
              at = @At(value = "INVOKE", target = "Ljava/util/Collections;shuffle(Ljava/util/List;Ljava/util/Random;)V"))
    private void standardizations$deterministicOrder(List<?> list, Random random) {
        if (!BlockDropRng.isExplosionActive(this.world)) {
            Collections.shuffle(list, random);
            return;
        }
        @SuppressWarnings("unchecked")
        List<BlockPos> posList = (List<BlockPos>) list;
        posList.sort(Comparator.comparingLong(BlockPos::asLong));
    }
}
