package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This model contains the properties of the players
 */
public class Player extends Observable {

	private ArrayList<String> ownedCountriesList;
	private ArrayList<Integer> ownedArmiesList;
	private HashMap<String, Integer> ownedCountriesArmiesList;
	private String gameState;
	private String currentPlayerTurn;
	private String actionsPerformed;
	private String attackerName;
	private String defenderName;
	private String attackerCountry;
	private String defenderCountry;
	private ArrayList<Integer> attackerDice;
	private ArrayList<Integer> defenderDice;
	private String diceRolled;
	private int cardBonusArmy;
	private int cardReward;
	private int availableArmies;
	private ArrayList<String> currentCardList;
	/**
	 * @Default Constructor This method initiates the variables
	 */
	public Player() {
		ownedCountriesList = new ArrayList<String>();
		ownedArmiesList = new ArrayList<Integer>();
		ownedCountriesArmiesList = new HashMap<String, Integer>();
		gameState="STARTUP";
		attackerDice = new ArrayList<Integer>();
		defenderDice = new ArrayList<Integer>();
		currentPlayerTurn="";
		cardBonusArmy=0;
		cardReward=0;
		availableArmies=0;
		currentCardList=new ArrayList<String>();
	}

	public HashMap<String, Integer> getOwnedCountriesArmiesList() {
		return ownedCountriesArmiesList;
	}

	public void setOwnedCountriesArmiesList(HashMap<String, Integer> ownedCountriesArmiesList) {
		this.ownedCountriesArmiesList = ownedCountriesArmiesList;
	}

	public ArrayList<String> getOwnedCountriesList() {
		return ownedCountriesList;
	}

	public void setOwnedCountriesList(ArrayList<String> ownedCountriesList) {
		this.ownedCountriesList = ownedCountriesList;
	}

	public ArrayList<Integer> getOwnedArmiesList() {
		return ownedArmiesList;
	}

	public void setOwnedArmiesList(ArrayList<Integer> ownedArmiesList) {
		this.ownedArmiesList = ownedArmiesList;
	}
	
	public String getGameState() {
		return gameState;
	}

	public void setGameState(String gameState) {
		this.gameState = gameState;
		notifyToObserver();
	}

	public String getCurrentPlayerTurn() {
		return currentPlayerTurn;
	}

	public void setCurrentPlayerTurn(String currentPlayerTurn) {
		this.currentPlayerTurn = currentPlayerTurn;
		notifyToObserver();
	}
	
	public String getActionsPerformed() {
		return actionsPerformed;
	}

	public void setActionsPerformed(String actionsPerformed) {
		this.actionsPerformed = actionsPerformed;
		notifyToObserver();
	}

	public String getAttackerName() {
		return attackerName;
	}

	public void setAttackerName(String attackerName) {
		this.attackerName = attackerName;
	}

	public String getDefenderName() {
		return defenderName;
	}

	public void setDefenderName(String defenderName) {
		this.defenderName = defenderName;
	}

	public String getAttackerCountry() {
		return attackerCountry;
	}

	public void setAttackerCountry(String attackerCountry) {
		this.attackerCountry = attackerCountry;
	}

	public String getDefenderCountry() {
		return defenderCountry;
	}

	public void setDefenderCountry(String defenderCountry) {
		this.defenderCountry = defenderCountry;
	}

	public ArrayList<Integer> getAttackerDice() {
		return attackerDice;
	}

	public void setAttackerDice(ArrayList<Integer> attackerDice) {
		this.attackerDice = attackerDice;
	}

	public ArrayList<Integer> getDefenderDice() {
		return defenderDice;
	}

	public void setDefenderDice(ArrayList<Integer> defenderDice) {
		this.defenderDice = defenderDice;
	}

	public String getDiceRolled() {
		return diceRolled;
	}

	public void setDiceRolled(String diceRolled) {
		this.diceRolled = diceRolled;
	}
	
	public void notifyToObserver() {
		notifyObservers(this);
	}
}
