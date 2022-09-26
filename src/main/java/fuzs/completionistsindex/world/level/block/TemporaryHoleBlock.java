package fuzs.completionistsindex.world.level.block;

import fuzs.completionistsindex.core.particles.SparkleParticleData;
import fuzs.completionistsindex.registry.ModRegistry;
import fuzs.completionistsindex.world.level.block.entity.TemporaryHoleBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class TemporaryHoleBlock extends BaseEntityBlock {

    public TemporaryHoleBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState p_60578_, BlockGetter p_60579_, BlockPos p_60580_) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState p_56707_, BlockGetter p_56708_, BlockPos p_56709_) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getVisualShape(BlockState p_56684_, BlockGetter p_56685_, BlockPos p_56686_, CollisionContext p_56687_) {
        return Shapes.block();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new TemporaryHoleBlockEntity(p_153215_, p_153216_);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153182_, BlockState p_153183_, BlockEntityType<T> p_153184_) {
        return !p_153182_.isClientSide ? createTickerHelper(p_153184_, ModRegistry.TEMPORARY_HOLE_BLOCK_ENTITY_TYPE.get(), TemporaryHoleBlockEntity::serverTick) : null;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter p_53021_, BlockPos p_53022_, BlockState p_53023_) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canBeReplaced(BlockState p_53035_, Fluid p_53036_) {
        return false;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (level.getBlockEntity(pos) instanceof TemporaryHoleBlockEntity blockEntity && blockEntity.sourceState != null) {
            int color = ChatFormatting.BLUE.getColor();
            SparkleParticleData sparkle = SparkleParticleData.noClip(1.0F, (color >> 16 & 0xFF) / 255.0F, (color >> 8 & 0xFF) / 255.0F, (color & 0xFF) / 255.0F, 20);
            VoxelShape occlusionShape = blockEntity.sourceState.getShape(level, pos);
            occlusionShape.forAllEdges((x0, y0, z0, x1, y1, z1) -> {
                Vec3 from = new Vec3(x0, y0, z0);
                Vec3 to = new Vec3(x1, y1, z1);
                if (random.nextDouble() < from.distanceTo(to)) {
                    Vec3 vec3 = from.lerp(to, random.nextDouble());
                    level.addParticle(sparkle, pos.getX() + vec3.x(), pos.getY() + vec3.y(), pos.getZ() + vec3.z(), 0.0, 0.0, 0.0);
                }
            });
        }
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return true;
    }
}
