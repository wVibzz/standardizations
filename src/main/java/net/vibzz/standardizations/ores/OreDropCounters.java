package net.vibzz.standardizations.ores;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;

public class OreDropCounters extends PersistentState {

    private static final String KEY = "standardizations_ore_drops";

    private final Map<String, Integer> counters = new HashMap<>();

    public OreDropCounters() {
        super(KEY);
    }

    public static OreDropCounters get(ServerWorld world) {
        return world.getServer().getOverworld().getPersistentStateManager()
                .getOrCreate(OreDropCounters::new, KEY);
    }

    public int getAndIncrement(Identifier blockId) {
        String k = blockId.toString();
        int v = counters.getOrDefault(k, 0);
        counters.put(k, v + 1);
        markDirty();
        return v;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        counters.clear();
        for (String k : tag.getKeys()) {
            counters.put(k, tag.getInt(k));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        for (Map.Entry<String, Integer> e : counters.entrySet()) {
            tag.putInt(e.getKey(), e.getValue());
        }
        return tag;
    }
}
