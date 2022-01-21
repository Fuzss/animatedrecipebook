package fuzs.completionistsindex.client.handler;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.client.CompletionistsIndexClient;
import fuzs.completionistsindex.network.client.C2SSweepAttackMessage;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SweepingHandler {
    @SubscribeEvent
    public void onLeftClickEmpty(final PlayerInteractEvent.LeftClickEmpty evt) {
        if (evt.getHand() == InteractionHand.MAIN_HAND && evt.getItemStack().canPerformAction(net.minecraftforge.common.ToolActions.SWORD_SWEEP)) {
            CompletionistsIndex.NETWORK.sendToServer(new C2SSweepAttackMessage());
            final Player player = evt.getPlayer();
            performSweepAttack(player);
        }
    }

    public static void performSweepAttack(Player player) {
        final float f2 = player.getAttackStrengthScale(0.5F);
        boolean flag = f2 > 0.9F;
        double d0 = player.walkDist - player.walkDistO;
        if (flag && !player.isSprinting() && player.isOnGround() && d0 < player.getSpeed()) {
            float f = (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE);
            f *= 0.2F + f2 * f2 * 0.8F;
            for (LivingEntity livingentity : player.level.getEntitiesOfClass(LivingEntity.class, getAttackBoundingBox(player))) {
                if (livingentity != player && !player.isAlliedTo(livingentity) && (!(livingentity instanceof ArmorStand) || !((ArmorStand)livingentity).isMarker()) && player.distanceToSqr(livingentity) < 9.0D) {
                    livingentity.knockback((double)0.4F, (double) Mth.sin(player.getYRot() * ((float)Math.PI / 180F)), (double)(-Mth.cos(player.getYRot() * ((float)Math.PI / 180F))));
                    float f1 = getDamageBonus(player, livingentity);
                    f1 *= f2;
                    float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * (f + f1);
                    livingentity.hurt(DamageSource.playerAttack(player), f3);
                }
            }
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
            player.sweepAttack();
        }
    }

    private static AABB getAttackBoundingBox(Player player) {
        AABB sweepHitBox = player.getItemInHand(InteractionHand.MAIN_HAND).getSweepHitBox(player, player);
        sweepHitBox = sweepHitBox.move(player.getLookAngle().normalize());
        return sweepHitBox;
    }

    private static float getDamageBonus(Player player, Entity entity) {
        if (entity instanceof LivingEntity) {
            return EnchantmentHelper.getDamageBonus(player.getMainHandItem(), ((LivingEntity) entity).getMobType());
        } else {
            return EnchantmentHelper.getDamageBonus(player.getMainHandItem(), MobType.UNDEFINED);
        }
    }
}
