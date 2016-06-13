package objects.equipables.wearables.armors;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import actor.Actor;
import actor.characteristics.status.IStatus;
import actor.characteristics.status.OneTimeStatus;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.status.traitModifier.StatModifier;
import actor.characteristics.traits.ITrait;
import objects.equipables.IEquipable;
import objects.equipables.weapons.IWeapon.DamageType;

public class MetalArmor implements IArmor {
	
	private Collection<ITrait> requiredTraits;
	private String name;
	private final String description;
	private final int weight;
	private final int value;
	private final ArmorType armorType;
	private final int armorValue;
	private Collection<IStatus> statusApllied;
	private final Collection<OccupiedPlace> occupiedPlace;

	public MetalArmor(final Collection<ITrait> requiredTraits, final String name, final String description, 
			final int weight, final int value, final ArmorType armorType, final int armorValue, 
			final Collection<IStatus> statusApllied, final Collection<OccupiedPlace> occupiedPlace) {
		super();
		this.requiredTraits = requiredTraits;
		if (requiredTraits == null) {
			this.requiredTraits = new LinkedList<>();
		}
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.value = value;
		this.armorType = armorType;
		this.armorValue = armorValue;
		this.statusApllied = statusApllied;
		if (statusApllied == null) {
			this.statusApllied = new LinkedList<>();
		}
		
		Collection<ITraitModifier> armorModifier = new LinkedList<ITraitModifier>();
		armorModifier.add(new StatModifier(ITrait.TraitType.ARMOR, armorValue));
		this.statusApllied.add(new OneTimeStatus(name, "Armor form " + name, armorModifier));
		
		this.occupiedPlace = occupiedPlace;
	}

	@Override
	public int getArmorReduction(DamageType damageType, int damageValue) throws Exception {
		switch (damageType) {
		case PIERCING :
			if (this.getArmorType() == ArmorType.PHYSICAL) {
				return (int) (damageValue - (this.getArmorValue() - this.getArmorValue() * 0.2));
			}
			else {
				return 0;
			}
		case SLASH :
			if (this.getArmorType() == ArmorType.PHYSICAL) {
				return (int) (damageValue - this.getArmorValue());
			}
			else {
				return 0;
			}
		case SMASH :
			if (this.getArmorType() == ArmorType.PHYSICAL) {
				return (int) (damageValue - this.getArmorValue());
			}
			else {
				return 0;
			}
		default :
			throw new Exception ("Unknown armor type");
		}
	}

	@Override
	public int getArmorValue() {
		return armorValue;
	}

	@Override
	public ArmorType getArmorType() {
		return armorType;
	}

	@Override
	public Collection<ITrait> getRequiredTraits() {
		return requiredTraits;
	}

	@Override
	public void applieOnEquipe(Actor target) throws Exception {
		Iterator<IStatus> statusIter = statusApllied.iterator();
		
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			target.addIStatus(currentStatus);
		}
		
		this.name += "(E)";
	}

	@Override
	public void removeApplieOnEquipe(Actor target) throws Exception {
		Iterator<IStatus> statusIter = statusApllied.iterator();
		
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			target.removeStatus(currentStatus);
		}
		
		this.name = this.name.substring(0, name.length() - 3);
	}

	@Override
	public Collection<OccupiedPlace> getOccupiedPlace() {
		return occupiedPlace;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public int weight() {
		return weight;
	}

	@Override
	public int value() {
		return value;
	}
	
	@Override
	public String toString() {
		try {
			String armorStr = name + " : " + description + System.lineSeparator();
			
			for (IEquipable.OccupiedPlace occupiedPlace : occupiedPlace) {
				armorStr += occupiedPlace + " ";
			}
			armorStr += System.lineSeparator() +
					 + armorValue + " " + IArmor.getArmorTypeString(armorType) + " armor" + System.lineSeparator() +
					weight + " Kg, " + value + " $" + System.lineSeparator();
			
			if (!requiredTraits.isEmpty()) {
				armorStr += "required : " + requiredTraits + System.lineSeparator();
			}
			if (!statusApllied.isEmpty()) {
				armorStr += "applies" + statusApllied + System.lineSeparator();
			}
			return  armorStr;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}