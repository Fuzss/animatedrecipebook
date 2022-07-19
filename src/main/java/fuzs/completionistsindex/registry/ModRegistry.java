package fuzs.completionistsindex.registry;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.world.item.WingsItem;
import fuzs.completionistsindex.world.item.enchantment.AirWorkerEnchantment;
import fuzs.completionistsindex.world.item.enchantment.EnduranceEnchantment;
import fuzs.puzzleslib.registry.RegistryManager;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantment;
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

    public static final TagKey<Item> WINGS_TAG = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(CompletionistsIndex.MOD_ID, "wings"));

    public static void touch() {

    }
}
