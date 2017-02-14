package am2.spell.component;

import am2.api.affinity.Affinity;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellModifiers;
import am2.extensions.EntityExtension;
import am2.utils.BlockProtections;
import am2.utils.SpellUtils;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

public class Dig extends SpellComponent {

	@Override
	public Object[] getRecipe() {
		return new Object[] {
			new ItemStack(Items.IRON_SHOVEL),
			new ItemStack(Items.IRON_AXE),
			new ItemStack(Items.IRON_PICKAXE)
		};
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos blockPos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		if (!(caster instanceof EntityPlayer))
			return false;
		stack.getTagCompound().setBoolean("ArsMagica2.harvestByProjectile", true);
		if (world.isRemote)
			return true;
		if (!BlockProtections.hasBreakPermission((EntityPlayerMP) caster, blockPos)) return false;
        if (SpellUtils.modifierIsPresent(SpellModifiers.SILKTOUCH_LEVEL, stack)) {
			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) <= 0) {
				stack.addEnchantment(Enchantments.SILK_TOUCH, 1);
			}
		}else if (SpellUtils.modifierIsPresent(SpellModifiers.FORTUNE_LEVEL, stack)){

			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack) <= 0){
				stack.addEnchantment(Enchantments.FORTUNE, SpellUtils.countModifiers(SpellModifiers.FORTUNE_LEVEL, stack));
			}
		}

		IBlockState state = world.getBlockState(blockPos);
		float hardness = state.getBlockHardness(world, blockPos);
		if (hardness != -1 && state.getBlock().getHarvestLevel(state) <= SpellUtils.getModifiedInt_Add(2, stack, caster, null, world, SpellModifiers.MINING_POWER)) {
			state.getBlock().harvestBlock(world, (EntityPlayer)caster, blockPos, state, null, stack);
			world.destroyBlock(blockPos, false);
			EntityExtension.For(caster).deductMana(hardness * 1.28f);
		}
		return true;
	}

	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.FORTUNE_LEVEL, SpellModifiers.MINING_POWER);
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world,
			EntityLivingBase caster, Entity target) {
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		return 10;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z,
			EntityLivingBase caster, Entity target, Random rand,
			int colorModifier) {
	}

	@Override
	public Set<Affinity> getAffinity() {
		return Sets.newHashSet(Affinity.EARTH);
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0.001F;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
	}

}
