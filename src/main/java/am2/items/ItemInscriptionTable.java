package am2.items;

import am2.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemInscriptionTable extends ItemBlock {

	public ItemInscriptionTable(Block block) {
		super(block);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(facing);
        }

		EnumFacing nf = playerIn.getHorizontalFacing().rotateY();
		LogHelper.info("Testing " + pos + " with facing " + nf);

		BlockPos placePos = pos.offset(playerIn.getHorizontalFacing().rotateY());
		LogHelper.info("Testing second at " + placePos);

		if (worldIn.getBlockState(placePos).getBlock().isReplaceable(worldIn, placePos))
			return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		return EnumActionResult.FAIL;
	}

}
