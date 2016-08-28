package gameEngine;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import actor.Actor;

public class DefaultBattle implements IBattleControler {

	private final ITurnControler turnControler;
	private final Collection<Actor> actors;
	private Iterator<Actor> actorIter;
	private Actor currentActor;
	private Boolean over;
	
	public DefaultBattle() {
		turnControler = new DefaultTurn();
		actors = new LinkedList<Actor>();
		actorIter = actors.iterator();
		over = false;
	}
	
	public void addActor(final Actor actor) {
		actors.add(actor);
		
		actorIter = actors.iterator();
		
		while (actorIter.hasNext()) {
			if (actorIter.next() == currentActor) {
				break;
			}
		}
	}
	
	public String nextActor() throws Exception {
		String log = "";
		if (actorIter.hasNext()) {
			currentActor = actorIter.next();
			if (currentActor.isDead()) {
				return finishBattle();
			}
			log += currentActor.getName() + " to move" + System.lineSeparator();
			log += currentActor.getAi().play(this) + System.lineSeparator();
			return log;
		}
		else {
			actorIter = actors.iterator();
			if (actorIter.hasNext()) {
				log += turnControler.nextTurn(actors) + System.lineSeparator();
				currentActor = actorIter.next();
				if (currentActor.isDead()) {
					return finishBattle();
				}
				log += currentActor.getName() + " to move" + System.lineSeparator();
				log += currentActor.getAi().play(this) + System.lineSeparator();
				return log;
			}
			else {
				return finishBattle();
			}
		}
	}
	
	private String finishBattle() {
		over = true;
		return "Battle over";
	}

	@Override
	public Collection<Actor> getActors() {
		return actors;
	}

	@Override
	public Boolean isBattleOver() {
		return over;
	}
}