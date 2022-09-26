package fuzs.completionistsindex.world.level.block.entity;

import com.google.common.math.DoubleMath;
import fuzs.completionistsindex.core.particles.SparkleParticleData;
import fuzs.completionistsindex.registry.ModRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class TemporaryHoleBlockEntity extends BlockEntity {
    private static final String TAG_BLOCK_STATE_SOURCE = "SourceState";
    private static final String TAG_LIFETIME_TICKS = "LifetimeTicks";
    private static final String TAG_GROWTH_DIRECTION = "GrowthDirection";
    private static final String TAG_GROWTH_DISTANCE = "GrowthDistance";

    public BlockState sourceState;
    private int lifetimeTicks;
    private Direction growthDirection;
    private int growthDistance;
    private List<Vec3[]> particleEdges;

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
        if (this.growthDistance > 0) {
            tag.putInt(TAG_GROWTH_DISTANCE, this.growthDistance);
        }
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

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        if (this.sourceState != null) {
            tag.put(TAG_BLOCK_STATE_SOURCE, NbtUtils.writeBlockState(this.sourceState));
        }
        return tag;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TemporaryHoleBlockEntity blockEntity) {
        if (blockEntity.sourceState == null) {
            level.removeBlock(pos, false);
        } else if (blockEntity.lifetimeTicks <= 0) {
            level.setBlock(pos, blockEntity.sourceState, 3);
            // plays the block breaking sound to provide some feedback
            level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
        } else {
            blockEntity.lifetimeTicks--;
            tryGrowInDirection(level, pos, blockEntity);
        }
    }

    private static void tryGrowInDirection(Level level, BlockPos pos, TemporaryHoleBlockEntity blockEntity) {
        if (blockEntity.growthDistance > 0 && blockEntity.growthDirection != null) {
            setTemporaryHoleBlock(level, pos.relative(blockEntity.growthDirection), blockEntity.growthDirection, blockEntity.growthDistance - 1);
            blockEntity.growthDistance = 0;
            blockEntity.growthDirection = null;
        }
    }

    public static boolean setTemporaryHoleBlock(Level level, BlockPos pos, Direction growthDirection, int growthDistance) {
        if (isValidHolePosition(level, pos)) {
            BlockState state = level.getBlockState(pos);
            boolean replaceBlock = !state.is(ModRegistry.TEMPORARY_HOLE_BLOCK.get());
            if (replaceBlock) {
                level.setBlock(pos, ModRegistry.TEMPORARY_HOLE_BLOCK.get().defaultBlockState(), 3);
            }
            if (level.getBlockEntity(pos) instanceof TemporaryHoleBlockEntity blockEntity) {
                if (replaceBlock) {
                    blockEntity.sourceState = state;
                }
                blockEntity.growthDirection = growthDirection;
                blockEntity.growthDistance = growthDistance;
                blockEntity.lifetimeTicks = 80;
            }
            return true;
        }
        return false;
    }

    public static boolean isValidHolePosition(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (level.hasChunkAt(pos) && level.isInWorldBounds(pos)) {
            if (state.is(ModRegistry.TEMPORARY_HOLE_BLOCK.get())) {
                return true;
            } else if (!state.isAir() && !state.hasBlockEntity() && !state.is(ModRegistry.PORTABLE_HOLE_IMMUNE_TAG)) {
                float destroySpeed = state.getDestroySpeed(level, pos);
                return destroySpeed != -1.0F && destroySpeed <= 20.0F;
            }
        }
        return false;
    }

    public void clearEdges() {
        this.particleEdges = null;
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (this.particleEdges == null) {
            this.particleEdges = getParticleEdges(level, pos);
        }
        for (Vec3[] edge : this.particleEdges) {
            Vec3 vec3 = edge[0].lerp(edge[1], random.nextDouble());
            int color = ChatFormatting.BLUE.getColor();
            SparkleParticleData sparkle = SparkleParticleData.noClip(1.0F, (color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, 15);
            level.addParticle(sparkle, pos.getX() + vec3.x(), pos.getY() + vec3.y(), pos.getZ() + vec3.z(), 0.0, 0.0, 0.0);
        }
    }

    public static List<Vec3[]> getParticleEdges(Level level, BlockPos pos) {
        List<Vec3[]> edges = Lists.newArrayList();
        for (Direction direction : Direction.values()) {
            if (true || !shouldRenderFace(level, pos, level.getBlockState(pos), direction)) {
                BlockPos neighborPos = pos.relative(direction);
                Direction neighborDirection = direction.getOpposite();
                BlockState neighborState = level.getBlockState(neighborPos);
                if (neighborState.is(ModRegistry.TEMPORARY_HOLE_BLOCK.get())) continue;
                VoxelShape occlusionShape = neighborState.getOcclusionShape(level, neighborPos);
                for (Direction otherNeighborDirection : Direction.values()) {
                    if (otherNeighborDirection.getAxis() != direction.getAxis()) {

                        List<Vec3[]> neighborEdges = filterEdges(toEdgesStream(occlusionShape), neighborDirection, otherNeighborDirection).toList();
                        BlockPos otherNeighborPos = neighborPos.relative(otherNeighborDirection);
                        BlockState otherNeighborState = level.getBlockState(otherNeighborPos);
                        if (otherNeighborState.is(ModRegistry.TEMPORARY_HOLE_BLOCK.get())) continue;
                        VoxelShape otherNeighborOcclusionShape = otherNeighborState.getOcclusionShape(level, otherNeighborPos);
                        List<Vec3[]> otherNeighborEdges = filterEdges(toEdgesStream(otherNeighborOcclusionShape), neighborDirection, otherNeighborDirection.getOpposite()).toList();
                        List<Vec3[]> uniqueEdges = findUniqueEdges(neighborEdges, otherNeighborEdges, findThirdAxis(neighborDirection, otherNeighborDirection));
                        edges.addAll(uniqueEdges);
                    }
                }


//                occlusionShape.forAllEdges((x0, y0, z0, x1, y1, z1) -> {
//                    double v = neighborDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0 : 0.0;
//                    double v0 = neighborDirection.getAxis().choose(x0, y0, z0);
//                    double v1 = neighborDirection.getAxis().choose(x1, y1, z1);
//                    if (DoubleMath.fuzzyEquals(v, v0, 1.0E-7) && DoubleMath.fuzzyEquals(v, v1, 1.0E-7)) {
//
//                    }
//
//
//                });
            }
        }
        return edges;
    }

    private static Direction.Axis findThirdAxis(Direction one, Direction two) {
        for (Direction.Axis axis : Direction.Axis.values()) {
            if (axis != one.getAxis() && axis != two.getAxis()) {
                return axis;
            }
        }
        throw new IllegalStateException("No third axis found");
    }

    private static List<Vec3[]> findUniqueEdges(List<Vec3[]> edgesWeWantToKeep, List<Vec3[]> edgesToCheckWith, Direction.Axis axis) {



        List<Vec3[]> uniqueEdges = Lists.newArrayList();
        main: for (Vec3[] edge1 : edgesWeWantToKeep) {
            double min1 = axis.choose(edge1[0].x(), edge1[0].y(), edge1[0].z());
            double max1 = axis.choose(edge1[1].x(), edge1[1].y(), edge1[1].z());
            if (min1 > max1) {
                double v = max1;
                max1 = min1;
                min1 = v;
            }
            for (Vec3[] edge2 : edgesToCheckWith) {
                double min2 = axis.choose(edge2[0].x(), edge2[0].y(), edge2[0].z());
                double max2 = axis.choose(edge2[1].x(), edge2[1].y(), edge2[1].z());
                if (min2 > max2) {
                    double v = max2;
                    max2 = min2;
                    min2 = v;
                }
                if (min1 > min2 && min1 < max2 || max1 > min2 && max1 < max2) {
                    continue main;
                }
            }
            uniqueEdges.add(edge1);
        }
        return uniqueEdges;
    }

    private static boolean belongsEdgeToFace(Vec3[] edge, Direction direction) {
        double step = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1.0 : 0.0;
        double start = direction.getAxis().choose(edge[0].x(), edge[0].y(), edge[0].z());
        if (DoubleMath.fuzzyEquals(step, start, 1.0E-7)) {
            double end = direction.getAxis().choose(edge[1].x(), edge[1].y(), edge[1].z());
            return DoubleMath.fuzzyEquals(step, end, 1.0E-7);
        } else {
            return false;
        }
    }

    private static List<Vec3[]> toEdges(VoxelShape shape) {
        List<Vec3[]> edges = Lists.newArrayList();
        shape.forAllEdges((x0, y0, z0, x1, y1, z1) -> {
            edges.add(new Vec3[]{new Vec3(x0, y0, z0), new Vec3(x1, y1, z1)});
        });
        return edges;
    }

    private static Stream<Vec3[]> filterEdges(Stream<Vec3[]> edges, Direction... directions) {
        return edges.filter(edge -> {
            for (Direction direction : directions) {
                if (!belongsEdgeToFace(edge, direction)) {
                    return false;
                }
            }
            return true;
        });
    }

    private static Stream<Vec3[]> toEdgesStream(VoxelShape shape) {
        Stream.Builder<Vec3[]> edges = Stream.builder();
        shape.forAllEdges((x0, y0, z0, x1, y1, z1) -> {
            edges.add(new Vec3[]{new Vec3(x0, y0, z0), new Vec3(x1, y1, z1)});
        });
        return edges.build();
    }

    public static boolean shouldRenderFace(TemporaryHoleBlockEntity blockEntity, Direction direction) {
        return shouldRenderFace(blockEntity.getLevel(), blockEntity.getBlockPos(), blockEntity.getBlockState(), direction);
    }

    public static boolean shouldRenderFace(Level level, BlockPos pos, BlockState state, Direction direction) {
        return Block.shouldRenderFace(state, level, pos, direction, pos.relative(direction));
    }
}
