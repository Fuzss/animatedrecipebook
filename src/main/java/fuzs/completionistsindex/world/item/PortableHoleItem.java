package fuzs.completionistsindex.world.item;

import fuzs.completionistsindex.world.level.block.entity.TemporaryHoleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

public class PortableHoleItem extends Item {

    public PortableHoleItem(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos clickedPos = context.getClickedPos();
        Player player = context.getPlayer();
        Direction clickedFace = context.getClickedFace();
//        boolean success = false;

        if (TemporaryHoleBlockEntity.isValidHolePosition(level, clickedPos)) {

            if (!level.isClientSide) {
                List<BlockPos> positions = BlockPos.betweenClosedStream(-1, -1, -1, 1, 1, 1).filter((BlockPos pos) -> {
                    return clickedFace.getAxis().choose(pos.getX(), pos.getY(), pos.getZ()) == 0;
                }).map(BlockPos::immutable).toList();

                for (BlockPos pos : positions) {
                    if (TemporaryHoleBlockEntity.setTemporaryHoleBlock(level, pos.offset(clickedPos), clickedFace.getOpposite(), 12)) {
//                success = true;
                    }
                }
                level.playSound(null, clickedPos.getX(), clickedPos.getY(), clickedPos.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
                player.getCooldowns().addCooldown(this, 20);
                player.awardStat(Stats.ITEM_USED.get(this));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
//
//        if (success) {
//        }
        return InteractionResult.PASS;
    }
}
