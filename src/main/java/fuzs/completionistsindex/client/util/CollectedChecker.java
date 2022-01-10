package fuzs.completionistsindex.client.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class CollectedChecker {
    private static final List<StatType<Block>> BLOCK_STAT_TYPES = ImmutableList.of(Stats.BLOCK_MINED);
    private static final List<StatType<Item>> ITEM_STAT_TYPES = ImmutableList.of(Stats.ITEM_BROKEN, Stats.ITEM_CRAFTED, Stats.ITEM_USED, Stats.ITEM_PICKED_UP, Stats.ITEM_DROPPED);
    private static final List<StatType<EntityType<?>>> ENTITY_STAT_TYPES = ImmutableList.of(Stats.ENTITY_KILLED);

    public static boolean hasCollectedItem(Item item) {
        if (item instanceof BlockItem blockItem) {
            if (contains(BLOCK_STAT_TYPES, blockItem.getBlock())) return true;
        }
        return contains(ITEM_STAT_TYPES, item);
    }

    public static boolean hasKilledEntity(EntityType<?> entityType) {
        return contains(ENTITY_STAT_TYPES, entityType);
    }

    private static <T> boolean contains(List<StatType<T>> statTypes, T item) {
        for (StatType<T> statType : statTypes) {
            if (isContainedInStat(statType, item)) return true;
        }
        return false;
    }

    private static <T> boolean isContainedInStat(StatType<T> stattype, T item) {
        final Minecraft minecraft = Minecraft.getInstance();
        return stattype.contains(item) && minecraft.player.getStats().getValue(stattype.get(item)) > 0;
    }
}
