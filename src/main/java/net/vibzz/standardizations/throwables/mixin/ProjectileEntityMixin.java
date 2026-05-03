package net.vibzz.standardizations.throwables.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.vibzz.standardizations.silverfish.mixin.EntityUuidAccessor;
import net.vibzz.standardizations.throwables.ThrowableStandardization;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.UUID;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin {

    @Inject(method = "setProperties", at = @At("HEAD"))
    private void standardizations$assignThrowableUuid(Entity user, float pitch, float yaw, float roll, float speed, float divergence, CallbackInfo ci) {
        ProjectileEntity self = (ProjectileEntity) (Object) this;
        if (!ThrowableStandardization.isActive(self.world)) return;
        if (!(user instanceof ServerPlayerEntity)) return;
        Item statItem = ThrowableStandardization.statItemFor(self);
        if (statItem == null) return;

        int attempt = ((ServerPlayerEntity) user).getStatHandler()
                .getStat(Stats.USED.getOrCreateStat(statItem));
        UUID uuid = ThrowableStandardization.uuidForAttempt(
                ThrowableStandardization.seedOf(self.world), attempt, statItem);
        ((EntityUuidAccessor) self).setUuidDirect(uuid);
        ((EntityUuidAccessor) self).setUuidStringDirect(uuid.toString());
    }

    @Redirect(method = "setVelocity(DDDFF)V",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D", ordinal = 0))
    private double standardizations$divergenceX(Random random) {
        ProjectileEntity self = (ProjectileEntity) (Object) this;
        if (!ThrowableStandardization.isActive(self.world)) return random.nextGaussian();
        if (ThrowableStandardization.statItemFor(self) == null) return random.nextGaussian();
        return ThrowableStandardization.gaussianFromUuid(self.getUuid(), 0);
    }

    @Redirect(method = "setVelocity(DDDFF)V",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D", ordinal = 1))
    private double standardizations$divergenceY(Random random) {
        ProjectileEntity self = (ProjectileEntity) (Object) this;
        if (!ThrowableStandardization.isActive(self.world)) return random.nextGaussian();
        if (ThrowableStandardization.statItemFor(self) == null) return random.nextGaussian();
        return ThrowableStandardization.gaussianFromUuid(self.getUuid(), 1);
    }

    @Redirect(method = "setVelocity(DDDFF)V",
              at = @At(value = "INVOKE", target = "Ljava/util/Random;nextGaussian()D", ordinal = 2))
    private double standardizations$divergenceZ(Random random) {
        ProjectileEntity self = (ProjectileEntity) (Object) this;
        if (!ThrowableStandardization.isActive(self.world)) return random.nextGaussian();
        if (ThrowableStandardization.statItemFor(self) == null) return random.nextGaussian();
        return ThrowableStandardization.gaussianFromUuid(self.getUuid(), 2);
    }
}
