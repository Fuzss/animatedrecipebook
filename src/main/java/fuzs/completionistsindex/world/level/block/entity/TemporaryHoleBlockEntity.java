package fuzs.completionistsindex.world.level.block.entity;

import fuzs.completionistsindex.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TemporaryHoleBlockEntity extends BlockEntity {
    private static final String TAG_BLOCK_STATE_SOURCE = "SourceState";
    private static final String TAG_LIFETIME_TICKS = "LifetimeTicks";
    private static final String TAG_GROWTH_DIRECTION = "GrowthDirection";
    private static final String TAG_GROWTH_DISTANCE = "GrowthDistance";

    private BlockState sourceState;
    private int lifetimeTicks = 80;
    private Direction growthDirection;
    private int growthDistance;

    public TemporaryHoleBlockEntity(BlockPos pos, BlockState state) {
        super(ModRegistry.TEMPORARY_HOLE_BLOCK_ENTITY_TYPE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.sourceState != null) {
            tag.put(TAG_BLOCK_STATE_SOURCE, NbtUtils.writeBlockState(this.sourceState));
        }
        tag.putInt(TAG_LIFETIME_TICKS, this.lifetimeTicks);
        if (this.growthDirection != null) {
            tag.putByte(TAG_GROWTH_DIRECTION, (byte) this.growthDirection.ordinal());
        }
        tag.putInt(TAG_GROWTH_DISTANCE, this.growthDistance);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(TAG_BLOCK_STATE_SOURCE, Tag.TAG_COMPOUND)) {
            this.sourceState = NbtUtils.readBlockState(tag.getCompound(TAG_BLOCK_STATE_SOURCE));
            if (this.sourceState.isAir()) {
                this.sourceState = null;
            }
        }
        this.lifetimeTicks = tag.getInt(TAG_LIFETIME_TICKS);
        if (tag.contains(TAG_GROWTH_DIRECTION, Tag.TAG_BYTE)) {
            this.growthDirection = Direction.values()[tag.getByte(TAG_GROWTH_DIRECTION)];
        }
        this.growthDistance = tag.getInt(TAG_GROWTH_DISTANCE);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TemporaryHoleBlockEntity blockEntity) {
        if (blockEntity.sourceState == null) {
            level.removeBlock(pos, false);
        } else if (blockEntity.lifetimeTicks <= 0) {
            level.setBlock(pos, blockEntity.sourceState, 3);
            level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
        } else {
            blockEntity.lifetimeTicks--;
            tryGrowInDirection(level, pos, blockEntity);
        }
    }

    private static void tryGrowInDirection(Level level, BlockPos pos, TemporaryHoleBlockEntity blockEntity) {
        if (blockEntity.growthDistance > 0) {
            setTemporaryHoleBlock(level, pos.relative(blockEntity.growthDirection), blockEntity.growthDirection, blockEntity.growthDistance - 1);
            blockEntity.growthDistance = 0;
        }
    }

    public static boolean setTemporaryHoleBlock(Level level, BlockPos pos, Direction growthDirection, int growthDistance) {
        BlockState state = level.getBlockState(pos);
        if (!state.isAir() && !state.hasBlockEntity() && !state.is(ModRegistry.PORTABLE_HOLE_IMMUNE_TAG)) {
            float destroySpeed = state.getDestroySpeed(level, pos);
            if (destroySpeed != -1.0F && destroySpeed <= 20.0F) {
                level.setBlock(pos, ModRegistry.TEMPORARY_HOLE_BLOCK.get().defaultBlockState(), 3);
                if (level.getBlockEntity(pos) instanceof TemporaryHoleBlockEntity blockEntity) {
                    blockEntity.sourceState = state;
                    blockEntity.growthDirection = growthDirection;
                    blockEntity.growthDistance = growthDistance;
                }
                return true;
            }
        }
        return false;
    }
}
