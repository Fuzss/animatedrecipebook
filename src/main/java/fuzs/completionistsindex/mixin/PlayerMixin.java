package fuzs.completionistsindex.mixin;

import fuzs.completionistsindex.world.entity.EnduranceEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements EnduranceEntity {
    private static final EntityDataAccessor<Integer> DATA_ENDURANCE_ID = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.INT);

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ENDURANCE_ID, this.getMaxAirSupply());
    }

    @Override
    public int getMaxEndurance() {
        return 1200;
    }

    @Override
    public int getEndurance() {
        return this.entityData.get(DATA_ENDURANCE_ID);
    }

    @Override
    public void setEndurance(int p_20302_) {
        this.entityData.set(DATA_ENDURANCE_ID, p_20302_);
    }

    @Override
    public int decreaseEndurance(int amount) {
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.RESPIRATION, this);
        return level > 0 && this.random.nextInt(level + 1) > 0 ? amount : Math.max(0, amount - 1);
    }

    @Override
    public int increaseEndurance(int amount) {
        return Math.min(amount + 16, this.getMaxEndurance());
    }
}
