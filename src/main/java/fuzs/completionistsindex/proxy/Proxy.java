package fuzs.completionistsindex.proxy;

import fuzs.puzzleslib.core.EnvTypeExecutor;
import net.minecraft.world.entity.player.Player;

public interface Proxy {
    @SuppressWarnings("Convert2MethodRef")
    Proxy INSTANCE = EnvTypeExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    boolean isSurvival(Player player);
}
