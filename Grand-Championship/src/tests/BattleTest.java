package tests;

import java.util.Collection;
import java.util.LinkedList;

import actor.Actor;
import actor.characteristics.status.EachTurnStatus;
import actor.characteristics.status.IStatus;
import actor.characteristics.status.OneTimeStatus;
import actor.characteristics.status.traitModifier.BasicTraitModifier;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.ITrait;
import gameEngine.DefaultBattle;
import gameEngine.IBattleControler;
import hmi.LogScreen;
import objects.equipables.ObjectEmplacement;
import objects.equipables.ObjectEmplacement.PlaceType;
import objects.equipables.weapons.IWeapon;
import objects.equipables.weapons.meleWeapons.MeleWeapon;
import objects.equipables.wearables.armors.IArmor;
import objects.equipables.wearables.armors.Armor;

/**
 * Test the battle system
 * @author Thomas MEDARD
 *
 */
public class BattleTest {
	
	/**
	 * The main function
	 * @param args Arguments passed to the main function (unused)
	 */
	public static void main(String[] args) {
		try {
			// Creating the log's screen.
			final LogScreen log = new LogScreen();
						
			// The champion !
			final Actor bob = new Actor("Bob");
			
			// Preparation of his main weapon
			Collection<ITraitModifier> weaponModifiedTraits = new LinkedList<ITraitModifier>();
			weaponModifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.STRENGTH, +1));
			OneTimeStatus weaponStatus = new OneTimeStatus("Better strength", "Equiping this makes you feel stronger", 
					weaponModifiedTraits, true, 100, null);

			// On equip
			Collection<IStatus> weaponStatuss = new LinkedList<IStatus>();
			
			Collection<ITraitModifier> modifiedTraits = new LinkedList<ITraitModifier>();
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.WILL, -2));
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.STRENGTH, -1));
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.CONSTITUTION, -1));
			weaponStatuss.add(weaponStatus);
			
			// On hit
			Collection<IStatus> attackStatus = new LinkedList<IStatus>();
			attackStatus.add(new EachTurnStatus("Spoon curse", "You have been cursed by a spoon, seriously?", modifiedTraits,
					4, true, 70, null, IStatus.StatusType.TEMPORARY));
			
			Collection <PlaceType> objEmps = new LinkedList<PlaceType>();
			objEmps.add(ObjectEmplacement.PlaceType.ONE_HAND);
			
			// Creating his main weapon
			MeleWeapon spoon = new MeleWeapon(
					null, 
					"cursed spoon",
					"It's a spoon but with some dark spirits trapped inside",
					1,
					1, 
					IWeapon.DamageType.SLASH,
					3, 
					weaponStatuss,
					attackStatus,
					objEmps);
			
			modifiedTraits = new LinkedList<ITraitModifier>();
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.WILL, -2));
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.DEXTERITY, -2));
			
			// On hit
			attackStatus = new LinkedList<IStatus>();
			attackStatus.add(new EachTurnStatus("DETERMINATION", "His determination to kill frightnes you !", modifiedTraits,
					5, true, 85, null, IStatus.StatusType.TEMPORARY));
			
			modifiedTraits = new LinkedList<ITraitModifier>();
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.VITALITY, -5));
			
			attackStatus.add(new OneTimeStatus("DETERMINATION", "His determination to kill frightnes you !", modifiedTraits,
					false, 6, null));
			
			objEmps = new LinkedList<PlaceType>();
			objEmps.add(ObjectEmplacement.PlaceType.ONE_HAND);
			
			MeleWeapon OyaWeapon = new MeleWeapon(
					null, 
					"True Knife",
					"Now We're Talking =D",
					1,
					1, 
					IWeapon.DamageType.SLASH,
					21, 
					null,
					attackStatus,
					objEmps);
			
			// Picking and equipping his main weapon
			log.displayLog(bob.equip(spoon));
			
			objEmps = new LinkedList<PlaceType>();
			objEmps.add(ObjectEmplacement.PlaceType.TORSO);
			
			// Creation of some Armor (yeah, bob is overpowered)
			Armor metalPlates = new Armor(
					null,
					"Metal plates", 
					"Some good old metal plates",
					30,
					60,
					IArmor.ArmorType.PHYSICAL, 
					20, 
					null, 
					objEmps);
			
			// Picking then equipping the Armor
			log.displayLog(bob.pick(metalPlates));
			log.displayLog(bob.equip(metalPlates));
			
			// Creating his challenger (poor guy)
			final Actor pop = new Actor("Pop");
			
			log.displayLog(pop.equip(OyaWeapon));
			
			// Bob is cheating! He attacks before the actual fight!;
			log.displayLog(bob.weaponAtack(pop));
			// So now bob will desequip
			log.displayLog(bob.desequip(spoon));
			
			// Testing some flaming sword		
			
			// On hit
			modifiedTraits = new LinkedList<ITraitModifier>();
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.VITALITY, -10));
			
			attackStatus = new LinkedList<IStatus>();
			attackStatus.add(new EachTurnStatus("Burning", "This guy is on fiiiire!", 
					modifiedTraits, 3, true, 100, null, IStatus.StatusType.EACH_TURN));
			
			objEmps = new LinkedList<PlaceType>();
			objEmps.add(ObjectEmplacement.PlaceType.ONE_HAND);
			
			// Creating his main weapon
			MeleWeapon flammingsword = new MeleWeapon(
					null, 
					"Flamming sword",
					"And you get light in dark!",
					10,
					100, 
					IWeapon.DamageType.SLASH,
					20, 
					null,
					attackStatus,
					objEmps);
			
			log.displayLog(bob.equip(flammingsword));

			// Preparation of the battle
			final IBattleControler battleControler = new DefaultBattle();
			battleControler.addActor(bob);
			battleControler.addActor(pop);
			
			// Now let them fight for 10 turns
			for (int i = 0; i < 10; ++i) {
				log.displayLog(battleControler.nextActor());
				// log.displayLog(bob.toString());
				// log.displayLog(pop.toString());
			}
			
			// And now fight to the death
			while(!battleControler.isBattleOver()) {
				log.displayLog(battleControler.nextActor());
			}
			
			// Who won? Who's next? You decide! Epic Battle of the History!!!!!!!!!
			log.displayLog(bob.toString());
			log.displayLog(pop.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
