package am2.items;

import am2.api.SkillPointRegistry;
import am2.api.skill.SkillPoint;
import am2.extensions.EntityExtension;
import am2.extensions.SkillData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class ItemInfinityOrb extends ItemArsMagica2 {
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		SkillPoint point = SkillPointRegistry.getPointForTier(itemStackIn.getItemDamage());
		if (point == null)
			playerIn.addChatMessage(new TextComponentString("Broken Item : Please use a trash bin."));
		itemStackIn = doGiveSkillPoints(playerIn, itemStackIn, point);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}
	
	private ItemStack doGiveSkillPoints(EntityPlayer player, ItemStack stack, SkillPoint type){
		if (EntityExtension.For(player).getCurrentLevel() > 0){
			SkillData.For(player).setSkillPoint(type, SkillData.For(player).getSkillPoint(type) + 1);
			if (player.worldObj.isRemote){
				player.addChatMessage(new TextComponentString(I18n.translateToLocal("am2.tooltip.infOrb" + type.toString())));
			}
			if (!player.capabilities.isCreativeMode)
			stack.stackSize--;
			if (stack.stackSize < 1){
				player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			}
		}else{
			if (player.worldObj.isRemote){
				int message = player.worldObj.rand.nextInt(10);
				player.addChatMessage(new TextComponentString(I18n.translateToLocal("am2.tooltip.infOrbFail" + message)));
			}
		}
		return stack;
	}
}