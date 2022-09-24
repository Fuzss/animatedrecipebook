package fuzs.completionistsindex.registry;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.world.item.PortableHoleItem;
import fuzs.completionistsindex.world.item.WingsItem;
import fuzs.completionistsindex.world.item.enchantment.AirWorkerEnchantment;
import fuzs.completionistsindex.world.item.enchantment.EnduranceEnchantment;
import fuzs.completionistsindex.world.level.block.TemporaryHoleBlock;
import fuzs.completionistsindex.world.level.block.entity.TemporaryHoleBlockEntity;
import fuzs.puzzleslib.registry.RegistryManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.RegistryObject;

public class ModRegistry {
    private static final RegistryManager REGISTRY = RegistryManager.of(CompletionistsIndex.MOD_ID);
    public static final RegistryObject<Item> BUTTERFlY_WINGS_ITEM = REGISTRY.registerItem("butterfly_wings", () -> new WingsItem(WingsItem.WingsType.BUTTERFLY, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> CHICKEN_WINGS_ITEM = REGISTRY.registerItem("chicken_wings", () -> new WingsItem(WingsItem.WingsType.CHICKEN, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> DRAGON_WINGS_ITEM = REGISTRY.registerItem("dragon_wings", () -> new WingsItem(WingsItem.WingsType.DRAGON, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> FAIRY_WINGS_ITEM = REGISTRY.registerItem("fairy_wings", () -> new WingsItem(WingsItem.WingsType.FAIRY, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> ANGEL_WINGS_ITEM = REGISTRY.registerItem("angel_wings", () -> new WingsItem(WingsItem.WingsType.ANGEL, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> INVISIBLE_WINGS_ITEM = REGISTRY.registerItem("invisible_wings", () -> new WingsItem(WingsItem.WingsType.INVISIBLE, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Enchantment> AERIAL_AFFINITY_ENCHANTMENT = REGISTRY.registerEnchantment("aerial_affinity", () -> new AirWorkerEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.OFFHAND));
    public static final RegistryObject<Enchantment> ENDURANCE_ENCHANTMENT = REGISTRY.registerEnchantment("endurance", () -> new EnduranceEnchantment(Enchantment.Rarity.RARE, EquipmentSlot.OFFHAND));

    public static final RegistryObject<Block> TEMPORARY_HOLE_BLOCK = REGISTRY.registerBlock("temporary_hole", () -> new TemporaryHoleBlock(BlockBehaviour.Properties.of(Material.PORTAL, MaterialColor.COLOR_BLACK).noCollission().lightLevel((p_50854_) -> {
        return 15;
    }).strength(-1.0F, 3600000.0F).noDrops()));
    public static final RegistryObject<Item> PORTABLE_HOLE_ITEM = REGISTRY.registerItem("portable_hole", () -> new PortableHoleItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TOOLS).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<BlockEntityType<TemporaryHoleBlockEntity>> TEMPORARY_HOLE_BLOCK_ENTITY_TYPE = REGISTRY.registerRawBlockEntityType("temporary_hole", () -> BlockEntityType.Builder.of(TemporaryHoleBlockEntity::new, TEMPORARY_HOLE_BLOCK.get()));

    public static final TagKey<Item> WINGS_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(CompletionistsIndex.MOD_ID, "wings"));
    public static final TagKey<Block> PORTABLE_HOLE_IMMUNE_TAG = TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(CompletionistsIndex.MOD_ID, "portable_hole_immune"));

    public static void touch() {

    }
}
