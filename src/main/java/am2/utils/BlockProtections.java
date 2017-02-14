package am2.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Part of ArsMagica2
 * <p>
 * From projectE, edit mostly made by Tencao
 *
 * @author Bluexin, Tencao
 */
public class BlockProtections {

    public static boolean hasBreakPermission(EntityPlayerMP player, BlockPos pos) {
        return hasEditPermission(player, pos)
                && !(ForgeHooks.onBlockBreakEvent(player.getEntityWorld(), player.interactionManager.getGameType(), player, pos) == -1);
    }

    public static boolean hasEditPermission(EntityPlayerMP player, BlockPos pos) {
        if (FMLCommonHandler.instance().getMinecraftServerInstance().isBlockProtected(player.getEntityWorld(), pos, player)) {
            return false;
        }

        for (EnumFacing e : EnumFacing.VALUES) {
            if (!player.canPlayerEdit(pos, e, null)) {
                return false;
            }
        }

        return true;
    }

    public static boolean checkedPlaceBlock(EntityPlayerMP player, BlockPos pos, IBlockState state) {
        if (!hasEditPermission(player, pos)) {
            return false;
        }
        World world = player.getEntityWorld();
        BlockSnapshot before = BlockSnapshot.getBlockSnapshot(world, pos);
        world.setBlockState(pos, state);
        BlockEvent.PlaceEvent evt = new BlockEvent.PlaceEvent(before, Blocks.AIR.getDefaultState(), player);
        MinecraftForge.EVENT_BUS.post(evt);
        if (evt.isCanceled()) {
            world.restoringBlockSnapshots = true;
            before.restore(true, false);
            world.restoringBlockSnapshots = false;
            return false;
        }
        return true;
    }

    public static boolean checkedReplaceBlock(EntityPlayerMP player, BlockPos pos, IBlockState state) {
        return hasBreakPermission(player, pos) && checkedPlaceBlock(player, pos, state);
    }
}
