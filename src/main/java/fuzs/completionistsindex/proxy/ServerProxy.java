package fuzs.completionistsindex.proxy;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ServerProxy implements Proxy {
    @Override
    public boolean isSurvival(Player player) {
        return ((ServerPlayer) player).gameMode.isSurvival();
    }
}
