package net.vibzz.standardizations.debug;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.text.LiteralText;
import net.vibzz.standardizations.Standardizations;
import net.vibzz.standardizations.mobSpawns.InitialSpawnMarker;

import java.util.stream.Collectors;

public final class DebugNametag {

    private DebugNametag() {}

    public static boolean isEnabled(LivingEntity entity) {
        return entity.world.getGameRules().getBoolean(Standardizations.STANDARDIZE_DEBUG_NAMETAGS);
    }

    public static void update(MobEntity mob, GoalSelector goalSelector) {
        if (!isEnabled(mob)) return;

        boolean isInitialSpawn = mob instanceof InitialSpawnMarker
                && ((InitialSpawnMarker) mob).standardizations$isInitialSpawn();
        if (!isInitialSpawn) return;

        long tick = mob.world.getTime();
        if (((tick * 7) % 20) >= 7) return;

        String uuid = mob.getUuid().toString().substring(0, 8);
        String running = describeRunningGoals(goalSelector);

        StringBuilder sb = new StringBuilder();
        sb.append("t").append(tick);
        if (!running.isEmpty()) sb.append(" [").append(running).append("]");
        sb.append(" ").append(uuid);

        mob.setCustomName(new LiteralText(sb.toString()));
        mob.setCustomNameVisible(true);
    }

    private static String describeRunningGoals(GoalSelector goalSelector) {
        return goalSelector.getRunningGoals()
                .map(PrioritizedGoal::getGoal)
                .map(g -> g.getClass().getSimpleName())
                .collect(Collectors.joining(","));
    }
}
