package fuzs.completionistsindex.world.item;

import fuzs.completionistsindex.CompletionistsIndex;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Wearable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class WingsItem extends Item implements Wearable {
    private final WingsType wingsType;

    public WingsItem(WingsType wingsType, Properties p_41383_) {
        super(p_41383_);
        this.wingsType = wingsType;
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_41137_, Player p_41138_, InteractionHand p_41139_) {
        ItemStack itemstack = p_41138_.getItemInHand(p_41139_);
        EquipmentSlot equipmentslot = Mob.getEquipmentSlotForItem(itemstack);
        ItemStack itemstack1 = p_41138_.getItemBySlot(equipmentslot);
        if (itemstack1.isEmpty()) {
            p_41138_.setItemSlot(equipmentslot, itemstack.copy());
            if (!p_41137_.isClientSide()) {
                p_41138_.awardStat(Stats.ITEM_USED.get(this));
            }

            itemstack.setCount(0);
            return InteractionResultHolder.sidedSuccess(itemstack, p_41137_.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_ELYTRA;
    }

    @Override
    public void onArmorTick(ItemStack stack, Level world, Player player) {
        super.onArmorTick(stack, world, player);
    }

    @Nullable
    @Override
    public EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EquipmentSlot.OFFHAND;
    }

    public WingsType getWingsType() {
        return this.wingsType;
    }

    public enum WingsType {
        BUTTERFLY, CHICKEN, DRAGON, FAIRY, ANGEL, INVISIBLE;

        public boolean canRenderWings() {
            return this != INVISIBLE;
        }

        public ResourceLocation getTextureLocation() {
            return new ResourceLocation(CompletionistsIndex.MOD_ID, "textures/models/wings/" + this.name().toLowerCase(Locale.ROOT) + ".png");
        }
    }
}
