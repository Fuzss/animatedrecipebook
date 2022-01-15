package fuzs.completionistsindex.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ClientProxy extends ServerProxy {
    @Override
    public boolean isSurvival(Player player) {
        if (player instanceof ServerPlayer) {
            return super.isSurvival(player);
        }
        return Minecraft.getInstance().gameMode.getPlayerMode().isSurvival();
    }
}
