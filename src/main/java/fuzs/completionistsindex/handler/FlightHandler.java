package fuzs.completionistsindex.handler;

import fuzs.completionistsindex.world.item.WingsItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FlightHandler {
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent evt) {
        if (evt.phase != TickEvent.Phase.END) return;
        if (evt.player instanceof ServerPlayer player && player.gameMode.isSurvival()) {
            final ItemStack itemStack = player.getOffhandItem();
            final Abilities abilities = player.getAbilities();
            if (itemStack.getItem() instanceof WingsItem) {
                if (!abilities.mayfly) {
                    abilities.mayfly = true;
                    player.onUpdateAbilities();
                }
            } else {
                if (abilities.mayfly || abilities.flying) {
                    abilities.mayfly = abilities.flying = false;
                    player.onUpdateAbilities();
                }
            }
        }
    }
}
