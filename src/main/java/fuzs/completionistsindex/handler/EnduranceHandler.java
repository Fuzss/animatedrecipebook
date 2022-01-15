package fuzs.completionistsindex.handler;

import fuzs.completionistsindex.proxy.Proxy;
import fuzs.completionistsindex.world.entity.EnduranceEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EnduranceHandler {
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent evt) {
        if (evt.phase != TickEvent.Phase.END) return;
        final Player player = evt.player;
        if (player.isAlive()) {
            EnduranceEntity endurance = (EnduranceEntity) player;
            if (Proxy.INSTANCE.isSurvival(player) && player.getAbilities().flying) {
                endurance.setEndurance(endurance.decreaseEndurance(endurance.getEndurance()));
                if (endurance.isOutOfEndurance()) player.getAbilities().flying = false;
            } else if (player.isOnGround() && endurance.getEndurance() < endurance.getMaxEndurance()) {
                endurance.setEndurance(endurance.increaseEndurance(endurance.getEndurance()));
            }
        }
    }
}
