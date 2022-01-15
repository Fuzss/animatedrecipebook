package fuzs.completionistsindex.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class ClientProxy extends ServerProxy {
    @Override
    public boolean isSurvival(Player player) {
        return Minecraft.getInstance().gameMode.getPlayerMode().isSurvival();
    }
}
