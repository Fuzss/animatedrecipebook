package fuzs.completionistsindex.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class SlotUtil {
    public static boolean isLeftSlotValid(Player player) {
        return getLeftSlot(player) != -1;
    }

    public static boolean isRightSlotValid(Player player) {
        return getLeftSlot(player) < getRightSlot(player);
    }

    public static int getLeftSlot(Player player) {
        int leftSlot = -1;
        final Inventory inventory = player.getInventory();
        int selected = inventory.selected;
        for (int i = 1; i < 4; i++) {
            int slotIndex = i * 9 + selected;
            if (!inventory.items.get(slotIndex).isEmpty()) {
                leftSlot = slotIndex;
                break;
            }
        }
        return leftSlot;
    }

    public static int getRightSlot(Player player) {
        int rightSlot = -1;
        final Inventory inventory = player.getInventory();
        int selected = inventory.selected;
        for (int i = 3; i > 0; i--) {
            int slotIndex = i * 9 + selected;
            if (!inventory.items.get(slotIndex).isEmpty()) {
                rightSlot = slotIndex;
                break;
            }
        }
        return rightSlot;
    }
}
