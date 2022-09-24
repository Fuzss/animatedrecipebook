package fuzs.completionistsindex.handler;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.registry.ModRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnchantmentHandler {
//    public static final EnchantmentCategory WINGS = EnchantmentCategory.create("WINGS", (Item item) -> {
//        return item instanceof WingsItem;
//    });

    @SubscribeEvent
    public void onBreakSpeed(final PlayerEvent.BreakSpeed evt) {
        Player player = evt.getPlayer();
        if (!player.isOnGround()) {
            if (player.getAbilities().flying && player.getAbilities().mayfly && EnchantmentHelper.getEnchantmentLevel(ModRegistry.AERIAL_AFFINITY_ENCHANTMENT.get(), player) > 0) {
                evt.setNewSpeed(evt.getOriginalSpeed() * 5.0F);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent evt) {
        if (evt.phase != TickEvent.Phase.END) return;
        if (evt.player.getAbilities().flying) {
//            CompletionistsIndex.LOGGER.info(evt.player.getAbilities().getFlyingSpeed());
        }
    }
}
