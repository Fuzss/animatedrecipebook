package fuzs.completionistsindex;

import fuzs.completionistsindex.config.ClientConfig;
import fuzs.completionistsindex.config.ServerConfig;
import fuzs.completionistsindex.data.*;
import fuzs.completionistsindex.handler.FlightEnduranceHandler;
import fuzs.completionistsindex.handler.RingFlightHandler;
import fuzs.completionistsindex.handler.WingsEnchantmentHandler;
import fuzs.completionistsindex.init.ModRegistry;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.config.ConfigHolderImpl;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CompletionistsIndex.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CompletionistsIndex {
    public static final String MOD_ID = "completionistsindex";
    public static final String MOD_NAME = "Completionist's Index";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder<ClientConfig, ServerConfig> CONFIG = ConfigHolder.of(() -> new ClientConfig(), () -> new ServerConfig());

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ((ConfigHolderImpl<?, ?>) CONFIG).addConfigs(MOD_ID);
        ModRegistry.touch();
        registerHandlers();
    }

    private static void registerHandlers() {
        MinecraftForge.EVENT_BUS.addListener((final TickEvent.PlayerTickEvent evt) -> {
            if (evt.phase == TickEvent.Phase.END) {
                RingFlightHandler.onPlayerTick$End(evt.player);
            }
        });
        MinecraftForge.EVENT_BUS.addListener((final TickEvent.PlayerTickEvent evt) -> {
            if (evt.phase == TickEvent.Phase.END) {
                FlightEnduranceHandler.onPlayerTick$End(evt.player);
            }
        });
        MinecraftForge.EVENT_BUS.addListener((final PlayerEvent.BreakSpeed evt) -> {
            WingsEnchantmentHandler.onBreakSpeed(evt.getPlayer(), evt.getState(), evt.getOriginalSpeed(), evt.getNewSpeed(), evt.getPos())
                    .ifPresent(newSpeed -> evt.setNewSpeed((float) newSpeed));
        });
        MinecraftForge.EVENT_BUS.addListener((final TickEvent.PlayerTickEvent evt) -> {
            if (evt.phase == TickEvent.Phase.END) {
                WingsEnchantmentHandler.onPlayerTick$End(evt.player);
            }
        });
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(new ModAdvancementProvider(generator, existingFileHelper));
        generator.addProvider(new ModItemModelProvider(generator, MOD_ID, existingFileHelper));
        generator.addProvider(new ModItemTagsProvider(generator, existingFileHelper));
        generator.addProvider(new ModLanguageProvider(generator, MOD_ID));
        generator.addProvider(new ModRecipeProvider(generator));
    }
}
