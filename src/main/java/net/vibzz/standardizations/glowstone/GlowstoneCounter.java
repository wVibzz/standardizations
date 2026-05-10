package net.vibzz.standardizations.glowstone;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

public class GlowstoneCounter extends PersistentState {

    private static final String KEY = "standardizations_glowstone_drops";

    private int counter = 0;

    public GlowstoneCounter() {
        super(KEY);
    }

    public static GlowstoneCounter get(ServerWorld world) {
        return world.getServer().getOverworld().getPersistentStateManager()
                .getOrCreate(GlowstoneCounter::new, KEY);
    }

    public int getAndIncrement() {
        int v = counter;
        counter = v + 1;
        markDirty();
        return v;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        counter = tag.getInt("Count");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("Count", counter);
        return tag;
    }
}
