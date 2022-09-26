package fuzs.completionistsindex.mixin;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.init.ModRegistry;
import fuzs.completionistsindex.world.entity.EnduranceEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements EnduranceEntity {
    private static final EntityDataAccessor<Integer> DATA_ENDURANCE_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    protected void defineSynchedData(CallbackInfo callbackInfo) {
        this.entityData.define(DATA_ENDURANCE_ID, this.getMaxEndurance());
    }

    @Override
    public int getMaxEndurance() {
        return CompletionistsIndex.CONFIG.server().flightSeconds * 20;
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
        int level = EnchantmentHelper.getEnchantmentLevel(ModRegistry.ENDURANCE_ENCHANTMENT.get(), this);
        return level > 0 && this.random.nextInt(level + 1) > 0 ? amount : Math.max(0, amount - 1);
    }

    @Override
    public int increaseEndurance(int amount) {
        return Math.min(amount + this.getEndurancePerTick(), this.getMaxEndurance());
    }
}
