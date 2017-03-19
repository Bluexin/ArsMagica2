package am2.affinity.abilities;

import am2.affinity.AffinityAbilityModifiers;
import am2.api.affinity.AbstractAffinityAbility;
import am2.api.affinity.Affinity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class AbilitySunlightWeakness extends AbstractAffinityAbility {

	public AbilitySunlightWeakness() {
		super(new ResourceLocation("arsmagica2", "sunlightweakness"));
	}

	@Override
	public float getMinimumDepth() {
		return 0.65f;
	}
	
	@Override
	public float getMaximumDepth() {
		return 0.95f;
	}

	@Override
	public Affinity getAffinity() {
		return Affinity.ENDER;
	}
	
	@Override
	public void applyTick(EntityPlayer player) {
		IAttributeInstance attribute = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		AffinityAbilityModifiers.instance.applyOrRemoveModifier(attribute, AffinityAbilityModifiers.sunlightWeakness, player.world.canBlockSeeSky(player.getPosition()) && player.world.isDaytime());
	}
	
	@Override
	public void removeEffects(EntityPlayer player) {
		IAttributeInstance attribute = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		AffinityAbilityModifiers.instance.applyOrRemoveModifier(attribute, AffinityAbilityModifiers.sunlightWeakness, false);
	}

}
