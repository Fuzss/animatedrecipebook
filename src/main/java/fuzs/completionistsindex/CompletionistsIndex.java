package fuzs.completionistsindex;

import fuzs.completionistsindex.config.ClientConfig;
import fuzs.completionistsindex.network.client.C2SCycleSlotMessage;
import fuzs.puzzleslib.config.AbstractConfig;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.config.ConfigHolderImpl;
import fuzs.puzzleslib.network.MessageDirection;
import fuzs.puzzleslib.network.NetworkHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CompletionistsIndex.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CompletionistsIndex {
    public static final String MOD_ID = "completionistsindex";
    public static final String MOD_NAME = "Completionist's Index";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    public static final NetworkHandler NETWORK = NetworkHandler.of(MOD_ID);
    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder<ClientConfig, AbstractConfig> CONFIG = ConfigHolder.client(() -> new ClientConfig());

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ((ConfigHolderImpl<?, ?>) CONFIG).addConfigs(MOD_ID);
        registerMessages();
    }

    private static void registerMessages() {
        NETWORK.register(C2SCycleSlotMessage.class, C2SCycleSlotMessage::new, MessageDirection.TO_SERVER);
    }
}
