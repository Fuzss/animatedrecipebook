package fuzs.completionistsindex.world.item.enchantment;

import fuzs.completionistsindex.init.ModRegistry;
import fuzs.completionistsindex.world.item.WingsItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class AirWorkerEnchantment extends Enchantment {
   public AirWorkerEnchantment(Rarity p_45290_, EquipmentSlot... p_45291_) {
      super(p_45290_, ModRegistry.WINGS_ENCHANTMENT_CATEGORY, p_45291_);
   }

   @Override
   public int getMinCost(int p_45294_) {
      return 1;
   }

   @Override
   public int getMaxCost(int p_45296_) {
      return this.getMinCost(p_45296_) + 40;
   }

   @Override
   public int getMaxLevel() {
      return 1;
   }

   @Override
   public boolean canEnchant(ItemStack p_44689_) {
      return p_44689_.getItem() instanceof WingsItem;
   }

   @Override
   public boolean isTreasureOnly() {
      return true;
   }
}