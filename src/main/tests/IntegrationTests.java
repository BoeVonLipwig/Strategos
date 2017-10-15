import mapcreation.mapgeneration.TerrainGeneration;
import org.junit.Test;
import strategos.*;
import strategos.behaviour.BehaviourFactoryImpl;
import strategos.gamelogic.GameRunner;
import strategos.model.*;
import strategos.networking.NetworkingHandler;
import strategos.networking.external_testing.ExternalTestGameState;
import strategos.networking.external_testing.ExternalTestMap;
import strategos.networking.external_testing.ExternalTestUnit;
import strategos.networking.external_testing.ExternalTestWorld;
import strategos.networking.handlers.NetworkingHandlerImpl;
import strategos.units.Unit;

import java.util.ArrayList;
import java.util.List;

import static strategos.Config.*;
import static strategos.Direction.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests verifying that the individual modules work in tandem with each other as expected.
 */
public class IntegrationTests {

	/**
	 * Tests that barbarians begins spawning after a number of turns
	 */
	@Test
	public void modelAndBehaviourTest_1() {
		StateCreator stateCreator = new StateCreator(new BehaviourFactoryImpl(), new TerrainGeneration());

		GameState gameState = stateCreator.createNewState();

		for (int i = 0 ; i < 13; i++) {
			gameState.nextTurn();
		}
		assertTrue(gameState.getPlayers().get(2).getUnits().size() > 9);
	}

	/**
	 * Tests that calling move on a unit works
	 */
	@Test
	public void modelAndBehaviourTest_2() {
		StateCreator stateCreator = new StateCreator(new BehaviourFactoryImpl(), new TerrainGeneration());

		GameState gameState = stateCreator.createNewState();

		Unit unit = gameState.getPlayers().get(0).getUnits().get(5);
		unit.turnTick();
		MapLocation startLocation = unit.getPosition();
		int ap = unit.getActionPoints();

		gameState.move(unit, startLocation.getNeighbour(NORTH_WEST));

		assertTrue(unit.getPosition().equals(startLocation.getNeighbour(NORTH_WEST)));
		assertTrue(unit.getActionPoints() < ap);
	}

	/**
	 * Tests that calling attack on a unit works
	 */
	@Test
	public void modelAndBehaviourTest_3() {
		StateCreator stateCreator = new StateCreator(new BehaviourFactoryImpl(), new TerrainGeneration());

		GameState gameState = stateCreator.createNewState();

		Unit unit = gameState.getPlayers().get(0).getUnits().get(0);

		Unit enemyUnit = gameState.getPlayers().get(1).getUnits().get(5);

		unit.setPosition(enemyUnit.getPosition().getNeighbour(SOUTH_WEST));
		unit.turnTick();
		int ap = unit.getActionPoints();

		gameState.attack(unit, enemyUnit.getPosition());

		assertTrue(unit.getActionPoints() == ap - 1);
		assertTrue(unit.getHitpoints() < 100);
		assertTrue(enemyUnit.getHitpoints() < 100);
	}

	/**
	 * Tests that calling attack on a unit can kill it
	 */
	@Test
	public void modelAndBehaviourTest_4() {
		StateCreator stateCreator = new StateCreator(new BehaviourFactoryImpl(), new TerrainGeneration());

		GameState gameState = stateCreator.createNewState();

		Unit unit = gameState.getPlayers().get(0).getUnits().get(0);

		Unit enemyUnit = gameState.getPlayers().get(1).getUnits().get(5);

		unit.setPosition(enemyUnit.getPosition().getNeighbour(SOUTH_WEST));

		gameState.attack(unit, enemyUnit.getPosition());
		unit.turnTick();
		gameState.attack(unit, enemyUnit.getPosition());
		unit.turnTick();
		gameState.attack(unit, enemyUnit.getPosition());
		unit.turnTick();
		gameState.attack(unit, enemyUnit.getPosition());
		unit.turnTick();
		gameState.attack(unit, enemyUnit.getPosition());
		unit.turnTick();
		gameState.attack(unit, enemyUnit.getPosition());
		unit.turnTick();

		assertFalse(enemyUnit.isAlive());
	}

	/**
	 * Tests that calling entrench for a unit works correctly
	 */
	@Test
	public void modelAndBehaviourTest_5() {
		StateCreator stateCreator = new StateCreator(new BehaviourFactoryImpl(), new TerrainGeneration());

		GameState gameState = stateCreator.createNewState();

		Unit unit = gameState.getPlayers().get(0).getUnits().get(0);
		unit.turnTick();
		int ap = unit.getActionPoints();

		gameState.entrench(unit);

		assertTrue(unit.getEntrench());
		assertTrue(unit.getActionPoints() == ap - ENTRENCH_COST);
	}

	/**
	 * Tests that calling wary for a unit works correctly
	 */
	@Test
	public void modelAndBehaviourTest_6() {
		StateCreator stateCreator = new StateCreator(new BehaviourFactoryImpl(), new TerrainGeneration());

		GameState gameState = stateCreator.createNewState();

		Unit unit = gameState.getPlayers().get(0).getUnits().get(0);
		unit.turnTick();
		int ap = unit.getActionPoints();

		gameState.wary(unit);

		assertTrue(unit.getWary());
		assertTrue(unit.getActionPoints() == ap - WARY_COST);
	}

	/**
	 * Tests that moving a unit without action points fails
	 */
	@Test
	public void modelAndBehaviourTest_7() {
		StateCreator stateCreator = new StateCreator(new BehaviourFactoryImpl(), new TerrainGeneration());

		GameState gameState = stateCreator.createNewState();

		Unit unit = gameState.getPlayers().get(0).getUnits().get(0);
		unit.turnTick();

		gameState.move(unit, NORTH_WEST);
		gameState.move(unit, NORTH_WEST);
		gameState.move(unit, NORTH_WEST);

		MapLocation location = unit.getPosition();

		gameState.move(unit, unit.getPosition().getNeighbour(NORTH_WEST));

		assertTrue(unit.getPosition().equals(location));
	}

	/**
	 * Tests that barbarians will wander the map
	 */
	@Test
	public void modelAndBehaviourTest_8() {
		StateCreator stateCreator = new StateCreator(new BehaviourFactoryImpl(), new TerrainGeneration());

		GameState gameState = stateCreator.createNewState();

		Unit barbarian = gameState.getPlayers().get(2).getUnits().get(4);

		MapLocation location = barbarian.getPosition();

		for (int i = 0; i < 20; i++) {
			gameState.nextTurn();
		}

		assertFalse(barbarian.getPosition().equals(location));
	}

	/**
	 * Tests that barbarians will attack enemies nearby
	 */
	@Test
	public void modelAndBehaviourTest_9() {
		StateCreator stateCreator = new StateCreator(new BehaviourFactoryImpl(), new TerrainGeneration());

		GameState gameState = stateCreator.createNewState();

		Unit barbarian = gameState.getPlayers().get(2).getUnits().get(3);

		Unit unit = gameState.getPlayers().get(0).getUnits().get(0);
		unit.setPosition(barbarian.getPosition().getNeighbour(EAST));

		for (int i = 0; i < 5; i++) {
			gameState.nextTurn();
		}

		assertTrue(unit.getHitpoints() < 100);
	}

	/**
	 * Tests that barbarians will capture enemy bridges
	 */
	@Test
	public void modelAndBehaviourTest_10() {
		StateCreator stateCreator = new StateCreator(new BehaviourFactoryImpl(), new TerrainGeneration());

		GameState gameState = stateCreator.createNewState();

		Unit bridge = gameState.getPlayers().get(2).getUnits().get(0);

		Unit unit = gameState.getPlayers().get(0).getUnits().get(0);
		MapLocation location = unit.getPosition();

		unit.setPosition(bridge.getPosition().getNeighbour(SOUTH_WEST));
		unit.turnTick();
		gameState.attack(unit, bridge.getPosition());
		unit.setPosition(location);

		Unit barbarian = gameState.getPlayers().get(2).getUnits().get(4);
		barbarian.setPosition(bridge.getPosition().getNeighbour(SOUTH_WEST));

		for (int i = 0; i < 5; i++) {
			gameState.nextTurn();
		}

		assertTrue(bridge.getOwner().isNPC());
	}

	/**
	 * Tests that modifying a unit then sending the gamestate updates the client accordingly
	 * @throws InterruptedException
	 */
	@Test
	public void modelAndNetworkingTest_1() throws InterruptedException {
		StateCreator stateCreator = new StateCreator(new BehaviourFactoryImpl(), new TerrainGeneration());
		GameState serverState = stateCreator.createNewState();
		serverState.setThisInstancePlayer(serverState.getPlayers().get(0));
		GameState clientState = stateCreator.createNewState();
		clientState.setThisInstancePlayer(clientState.getPlayers().get(1));

		NetworkingHandler server = setupHandler(false, serverState);
		NetworkingHandler client = setupHandler(true, clientState);

		Unit unit = serverState.getPlayers().get(0).getUnits().get(0);
		MapLocation location = unit.getPosition();
		unit.turnTick();
		serverState.move(unit, location.getNeighbour(NORTH_WEST));
		location = unit.getPosition();

		SaveInstance saveInstance = serverState.export();
		server.send(saveInstance);

		Unit unitClient = clientState.getPlayers().get(0).getUnits().get(0);

		assertTrue(unitClient.getPosition().equals(location));

		server.stop();
		client.stop();
	}

	/**
	 * Tests that loading from a save in the host updates the client as expected
	 * @throws InterruptedException
	 */
	@Test
	public void modelAndNetworkingTest_2() throws InterruptedException {
		StateCreator stateCreator = new StateCreator(new BehaviourFactoryImpl(), new TerrainGeneration());
		GameState serverState = stateCreator.createNewState();
		serverState.setThisInstancePlayer(serverState.getPlayers().get(0));
		GameState clientState = stateCreator.createNewState();
		clientState.setThisInstancePlayer(clientState.getPlayers().get(1));

		NetworkingHandler server = setupHandler(false, serverState);
		NetworkingHandler client = setupHandler(true, clientState);

		Unit unit = serverState.getPlayers().get(0).getUnits().get(0);
		MapLocation location = unit.getPosition();
		unit.turnTick();
		serverState.move(unit, location.getNeighbour(NORTH_WEST));
		location = unit.getPosition();

		serverState.save();

		SaveInstance saveInstance = serverState.export();
		server.send(saveInstance);

		serverState.load(serverState.getSaves().get(0));

		Unit unitClient = clientState.getPlayers().get(0).getUnits().get(0);
		assertTrue(unitClient.getPosition().equals(location));

		server.stop();
		client.stop();
	}

	/**
	 * Tests that the client can send back to the server
	 * @throws InterruptedException
	 */
	@Test
	public void modelAndNetworkingTest_3() throws InterruptedException {
		StateCreator stateCreator = new StateCreator(new BehaviourFactoryImpl(), new TerrainGeneration());
		GameState serverState = stateCreator.createNewState();
		serverState.setThisInstancePlayer(serverState.getPlayers().get(0));
		GameState clientState = stateCreator.createNewState();
		clientState.setThisInstancePlayer(clientState.getPlayers().get(1));

		NetworkingHandler server = setupHandler(false, serverState);
		NetworkingHandler client = setupHandler(true, clientState);

		Unit unitClient = clientState.getPlayers().get(0).getUnits().get(0);
		MapLocation location = unitClient.getPosition();
		unitClient.turnTick();
		clientState.move(unitClient, location.getNeighbour(NORTH_WEST));
		location = unitClient.getPosition();

		SaveInstance saveInstance = clientState.export();
		client.send(saveInstance);

		assertTrue(serverState.getPlayers().get(0).getUnits().get(0).getPosition().equals(location));

		server.stop();
		client.stop();
	}

	NetworkingHandler setupHandler(boolean client, GameState state) throws InterruptedException {
		NetworkingHandler handler = new NetworkingHandlerImpl();
		if (client) {
			handler.initialise(state, 8080);
		} else {
			handler.initialise(state, "127.0.0.1", 8080);
		}
		handler.run();
		return handler;
	}
}
