package fuzs.completionistsindex.handler;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.world.entity.EnduranceEntity;
import fuzs.completionistsindex.world.item.WingsItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RingFlightHandler {

    public static void onPlayerTick$End(Player player) {
        if (player instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.isSurvival()) {
            final ItemStack itemStack = serverPlayer.getOffhandItem();
            final Abilities abilities = serverPlayer.getAbilities();
            if (itemStack.getItem() instanceof WingsItem) {
                if (!abilities.mayfly && (!((EnduranceEntity) serverPlayer).isOutOfEndurance() || CompletionistsIndex.CONFIG.server().unlimitedFlight)) {
                    abilities.mayfly = true;
                    serverPlayer.onUpdateAbilities();
                }
            } else {
                if (abilities.mayfly || abilities.flying) {
                    abilities.mayfly = abilities.flying = false;
                    serverPlayer.onUpdateAbilities();
                }
            }
        }
    }
}
