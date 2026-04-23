package net.vibzz.standardizations.mixin.drops;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.vibzz.standardizations.util.BlockDropRng;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;

@Mixin(Entity.class)
public abstract class EntityPushOutMixin {

    @Redirect(method = "pushOutOfBlocks",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F"))
    private float standardizations$deterministicPushMagnitude(Random random) {
        Entity self = (Entity) (Object) this;
        if (!(self instanceof ItemEntity)) return random.nextFloat();
        if (!BlockDropRng.isActive(self.world)) return random.nextFloat();
        return (float) BlockDropRng.uniform01(
                BlockDropRng.seedOf(self.world),
                self.getBlockPos(),
                BlockDropRng.SALT_PUSH_OUT);
    }
}
