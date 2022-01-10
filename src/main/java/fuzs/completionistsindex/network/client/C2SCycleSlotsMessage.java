package fuzs.completionistsindex.network.client;

import fuzs.completionistsindex.util.SlotUtil;
import fuzs.puzzleslib.network.message.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class C2SCycleSlotsMessage implements Message {
    private boolean cycleRight;

    public C2SCycleSlotsMessage() {
    }

    public C2SCycleSlotsMessage(boolean cycleRight) {
        this.cycleRight = cycleRight;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.cycleRight);
    }

    @Override
    public void read(FriendlyByteBuf buf) {
        this.cycleRight = buf.readBoolean();
    }

    @Override
    public CycleSlotsHandler makeHandler() {
        return new CycleSlotsHandler();
    }

    private static class CycleSlotsHandler extends PacketHandler<C2SCycleSlotsMessage> {
        @Override
        public void handle(C2SCycleSlotsMessage packet, Player player, Object gameInstance) {
            if (!player.isSpectator()) {
                if (packet.cycleRight) {
                    if (SlotUtil.cycleHotbarSlotRight(player) != -1) {
                        SlotUtil.cycleSlotsRight(player);
                        player.stopUsingItem();
                    }
                } else {
                    if (SlotUtil.cycleHotbarSlotLeft(player) != -1) {
                        SlotUtil.cycleSlotsLeft(player);
                        player.stopUsingItem();
                    }
                }
            }
        }
    }
}
