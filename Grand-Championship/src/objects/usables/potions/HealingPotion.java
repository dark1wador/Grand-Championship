package objects.usables.potions;

import actor.Actor;
import actor.characteristics.traits.ITrait.TraitType;
import gameExceptions.GameException;
import objects.RandomObject;

/**
 * Standard healing potion.
 * @author Thomas MEDARD
 *
 */
public class HealingPotion implements IPotion {
	/**
	 * The name of the {@link HealingPotion}
	 */
	private final String name;
	/**
	 * The description of the {@link HealingPotion}
	 */
	private final String description;
	/**
	 * The weight of the {@link HealingPotion}
	 */
	private final int weight;
	/**
	 * The value (in money) of the {@link HealingPotion}
	 */
	private final int value;
	/**
	 * The healing value of teh {@link HealingPotion}
	 */
	private final int healing;
	/**
	 * The number of time you can use the {@link HealingPotion}.<br>
	 * For a {@link HealingPotion} it's {@value 1}.
	 */
	private int useTime;
	
	/**
	 * The constructor
	 * @param name {@link HealingPotion#name} of the {@link HealingPotion}
	 * @param description {@link HealingPotion#description} of the {@link HealingPotion}
	 * @param weight {@link HealingPotion#weight} of the {@link HealingPotion}
	 * @param value {@link HealingPotion#value} of the {@link HealingPotion}
	 * @param healing {@link HealingPotion#healing} of the {@link HealingPotion}
	 */
	public HealingPotion(final String name, final String description, final int weight, final int value, final int healing) {
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.value = value;
		this.healing = healing;
		useTime = 1;
	}

	/**
	 * @see objects.usables.IUsable#use(actor.Actor, actor.Actor)
	 */
	@Override
	public String use(Actor user, Actor target) throws GameException {

		final int max = target.getBasicTrait(TraitType.VITALITY).getValue();
		final int vitality = target.getCurrentTrait(TraitType.VITALITY).getValue();
		int realHealing = healing;
		if (max - vitality < healing) {
			realHealing -= healing - (max - vitality);
			target.getCurrentTrait(TraitType.VITALITY).setValue(target.getCurrentTrait(TraitType.VITALITY).getValue() + 
					realHealing);
		}
		else {
			target.getCurrentTrait(TraitType.VITALITY).setValue(target.getCurrentTrait(TraitType.VITALITY).getValue() +
					realHealing);
		}
		user.drop(this);
		try {
			user.pick(new RandomObject(
					"Empty bottle",
					"It's just waiting to be filled",
					getWeight(),
					2));
		} 
		catch (GameException e) {
			e.printStackTrace();
		}
		return target.getName() + " has been healed for " + realHealing + " PV";
	}

	/**
	 * @see objects.IObject#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see objects.IObject#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @see objects.IObject#getWeight()
	 */
	@Override
	public int getWeight() {
		return weight;
	}

	/**
	 * @see objects.IObject#getValue()
	 */
	@Override
	public int getValue() {
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String potionStr = getName() + System.lineSeparator() +
				healing + " PV" + System.lineSeparator() +
				getDescription() + System.lineSeparator() +
				getWeight() + " Kg, " + getValue() + " $";
		return potionStr;
	}

	/**
	 * @see objects.usables.IUsable#getUseTime()
	 */
	@Override
	public int getUseTime() {
		return useTime;
	}
}
