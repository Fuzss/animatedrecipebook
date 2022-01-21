package fuzs.completionistsindex.network.client;

import fuzs.completionistsindex.client.handler.SweepingHandler;
import fuzs.puzzleslib.network.message.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class C2SSweepAttackMessage implements Message {
    @Override
    public void write(FriendlyByteBuf buf) {

    }

    @Override
    public void read(FriendlyByteBuf buf) {

    }

    @Override
    public SweepAttackHandler makeHandler() {
        return new SweepAttackHandler();
    }

    private static class SweepAttackHandler extends PacketHandler<C2SSweepAttackMessage> {
        @Override
        public void handle(C2SSweepAttackMessage packet, Player player, Object gameInstance) {
            ItemStack itemstack = player.getItemInHand(InteractionHand.MAIN_HAND);
            boolean flag3 = itemstack.canPerformAction(net.minecraftforge.common.ToolActions.SWORD_SWEEP);
            if (flag3) {
                SweepingHandler.performSweepAttack(player);
            }
        }
    }
}
