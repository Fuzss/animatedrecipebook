package fuzs.completionistsindex.handler;

import fuzs.completionistsindex.CompletionistsIndex;
import fuzs.completionistsindex.init.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.state.BlockState;

import java.util.OptionalDouble;

public class WingsEnchantmentHandler {

    public static OptionalDouble onBreakSpeed(Player player, BlockState state, float originalSpeed, float newSpeed, BlockPos pos) {
        if (!player.isOnGround()) {
            if (player.getAbilities().flying && player.getAbilities().mayfly && EnchantmentHelper.getEnchantmentLevel(ModRegistry.AERIAL_AFFINITY_ENCHANTMENT.get(), player) > 0) {
                return OptionalDouble.of(originalSpeed * 5.0F);
            }
        }
        return OptionalDouble.empty();
    }

    public static void onPlayerTick$End(Player player) {
        if (player.getAbilities().flying) {
//            CompletionistsIndex.LOGGER.info(player.getAbilities().getFlyingSpeed());
        }
    }
}
