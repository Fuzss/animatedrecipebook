package fuzs.completionistsindex.client;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.client.handler.KeyBindingHandler;
import fuzs.completionistsindex.client.handler.MouseScrollHandler;
import fuzs.completionistsindex.client.handler.PauseMenuHandler;
import fuzs.completionistsindex.client.handler.SlotRendererHandler;
import fuzs.puzzleslib.PuzzlesLib;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = CompletionistsIndex.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CompletionistsIndexClient {
    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        PuzzlesLib.setSideOnly();
        registerHandlers();
    }
    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent evt) {
        SlotRendererHandler.init();
        final KeyBindingHandler keyBindingHandler = new KeyBindingHandler();
        keyBindingHandler.init();
        MinecraftForge.EVENT_BUS.addListener(keyBindingHandler::onClientTick);
        final MouseScrollHandler mouseScrollHandler = new MouseScrollHandler(Minecraft.getInstance());
        MinecraftForge.EVENT_BUS.addListener(mouseScrollHandler::onMouseScroll);
    }

    private static void registerHandlers() {
        final PauseMenuHandler pauseMenuHandler = new PauseMenuHandler();
        MinecraftForge.EVENT_BUS.addListener(pauseMenuHandler::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(pauseMenuHandler::onInitGui);
    }
}
