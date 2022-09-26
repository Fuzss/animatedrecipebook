package fuzs.completionistsindex;

import fuzs.completionistsindex.config.ClientConfig;
import fuzs.completionistsindex.config.ServerConfig;
import fuzs.completionistsindex.data.*;
import fuzs.completionistsindex.handler.EnchantmentHandler;
import fuzs.completionistsindex.handler.EnduranceHandler;
import fuzs.completionistsindex.handler.RingFlightHandler;
import fuzs.completionistsindex.network.client.C2SCycleSlotsMessage;
import fuzs.completionistsindex.network.client.C2SSweepAttackMessage;
import fuzs.completionistsindex.registry.ModRegistry;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.config.ConfigHolderImpl;
import fuzs.puzzleslib.network.MessageDirection;
import fuzs.puzzleslib.network.NetworkHandler;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.LootTableLoadEvent;
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

    public static final NetworkHandler NETWORK = NetworkHandler.of(MOD_ID);
    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder<ClientConfig, ServerConfig> CONFIG = ConfigHolder.of(() -> new ClientConfig(), () -> new ServerConfig());

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ((ConfigHolderImpl<?, ?>) CONFIG).addConfigs(MOD_ID);
        ModRegistry.touch();
        registerHandlers();
        registerMessages();
    }

    private static void registerHandlers() {
        final RingFlightHandler ringFlightHandler = new RingFlightHandler();
        MinecraftForge.EVENT_BUS.addListener(ringFlightHandler::onPlayerTick);
        final EnduranceHandler enduranceHandler = new EnduranceHandler();
        MinecraftForge.EVENT_BUS.addListener(enduranceHandler::onPlayerTick);
        final EnchantmentHandler enchantmentHandler = new EnchantmentHandler();
        MinecraftForge.EVENT_BUS.addListener(enchantmentHandler::onBreakSpeed);
        MinecraftForge.EVENT_BUS.addListener(enchantmentHandler::onPlayerTick);
        MinecraftForge.EVENT_BUS.addListener((final LootTableLoadEvent evt) -> {
            if (evt.getName().equals(BuiltInLootTables.STRONGHOLD_CORRIDOR)) {
                evt.getTable().addPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootTableReference.lootTableReference(ModRegistry.STRONGHOLD_CORRIDOR_INJECT_LOOT_TABLE)).build());
            }
        });
    }

    private static void registerMessages() {
        NETWORK.register(C2SCycleSlotsMessage.class, C2SCycleSlotsMessage::new, MessageDirection.TO_SERVER);
        NETWORK.register(C2SSweepAttackMessage.class, C2SSweepAttackMessage::new, MessageDirection.TO_SERVER);
    }

    @SubscribeEvent
    public static void onGatherData(final GatherDataEvent evt) {
        DataGenerator generator = evt.getGenerator();
        final ExistingFileHelper existingFileHelper = evt.getExistingFileHelper();
        generator.addProvider(new ModRecipeProvider(generator));
        generator.addProvider(new ModAdvancementProvider(generator, existingFileHelper));
        generator.addProvider(new ModBlockTagsProvider(generator, existingFileHelper));
        generator.addProvider(new ModItemTagsProvider(generator, existingFileHelper));
        generator.addProvider(new ModBlockStateProvider(generator, MOD_ID, existingFileHelper));
        generator.addProvider(new ModLanguageProvider(generator, MOD_ID));
        generator.addProvider(new ModItemModelProvider(generator, MOD_ID, existingFileHelper));
        generator.addProvider(new ModLootTableProvider(generator, MOD_ID));
    }
}
