package fuzs.completionistsindex.world.item.enchantment;

import fuzs.completionistsindex.init.ModRegistry;
import fuzs.completionistsindex.world.item.WingsItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnduranceEnchantment extends Enchantment {
   public EnduranceEnchantment(Rarity p_45117_, EquipmentSlot... p_45118_) {
      super(p_45117_, ModRegistry.WINGS_ENCHANTMENT_CATEGORY, p_45118_);
   }

   @Override
   public int getMinCost(int p_45121_) {
      return 10 * p_45121_;
   }

   @Override
   public int getMaxCost(int p_45123_) {
      return this.getMinCost(p_45123_) + 30;
   }

   @Override
   public int getMaxLevel() {
      return 3;
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