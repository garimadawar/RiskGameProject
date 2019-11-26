package controller;

import java.util.ArrayList;
import java.util.Collections;
import model.GameMap;
import model.Player;
import model.PlayersList;

/**
 * This Class is for Random Strategy for player Behavior
 * 
 * @author Ashish Chaudhary
 *
 */
public class RandomStrategy implements Strategy {

	CommonController cc = new CommonController();
	PlayerController pc = new PlayerController();

	ArrayList<Integer> attackerCountryList;
	ArrayList<Integer> neighbouringList;
	ArrayList<String> countriesOwned = new ArrayList<String>();

	/**
	 * Method overrides the Strategy Pattern Execute method
	 * 
	 * @return Success if all operation are performed
	 * @author Ashish Chaudhary
	 * @param gm     it is the gameMap
	 * @param pl     contains the hashmap of player object
	 * @param player contains the player object
	 */
	@Override
	public String executeStrategy(GameMap gm, PlayersList pl, Player player) {

		reinforce(gm, pl, player);
		player.notifyToObserver();
		pl.notifyToObserver(player);
		attack(gm, pl, player);
		player.notifyToObserver();
		pl.notifyToObserver(player);
		fortify(gm, pl, player);

		return "Success";
	}

	/**
	 * Method is used to perform reinforce of Random behavior player
	 * 
	 * @param gm     is the game map containing all info about game
	 * @param pl     contains all information about player
	 * @param player it is the player object
	 * @author Ashish Chaudhary
	 */
	private void reinforce(GameMap gm, PlayersList pl, Player player) {

		player.setCardReward(cc.exchangeCardForStrategy(pl, player));

		Player playerData = pl.getListOfPlayers().get(player.getCurrentPlayerTurn());

		countriesOwned = playerData.getOwnedCountriesList();

		Collections.shuffle(countriesOwned);
		String randomCountry = countriesOwned.get(0);
		int countryReward = pc.calculateOwnedCountryReward(pl.getListOfPlayers().get(player.getCurrentPlayerTurn()));
		int continetReward = pc.calculateContinentReward(pl.getListOfPlayers().get(player.getCurrentPlayerTurn()),
				gm.getContinents(), gm.getCountries(), randomCountry);

		player.setAvailableReinforceArmies(
				pc.calculateReinforceArmy(countryReward, continetReward, player.getCardReward()));

		while (player.getAvailableReinforceArmies() > 0) {

			int randomNumber = randomArmyToReinforceGenerator(player);

			pc.placeReinforceArmy(randomCountry, randomNumber, gm.getCountries(), pl.getListOfPlayers(),
					gm.getContinents(), player);

			// check return of place reinforcearmy method

			Collections.shuffle(countriesOwned);
			randomCountry = countriesOwned.get(0);

		}

		// check if it is a tournament

		player.setCardReward(0);
		player.setGameState("ATTACK");

	}

	/**
	 * method performs the attack of the Random Behavior player
	 * 
	 * @param gm     is the game map containing all info about game
	 * @param pl     contains all information about player
	 * @param player it is the player object
	 * @author Ashish Chaudhary
	 * 
	 */
	private void attack(GameMap gm, PlayersList pl, Player player) {

		neighbouringList = new ArrayList<Integer>();
		attackerCountryList = new ArrayList<Integer>();

		Collections.shuffle(countriesOwned);
		String randomCountry = countriesOwned.get(0);

		int attackerCountryNum = cc.getCountryNumberByName(gm.getCountries(), randomCountry);

		for (String country : pl.getListOfPlayers().get(player.getCurrentPlayerTurn()).getOwnedCountriesList()) {

			int countriesOwnedNumbers = cc.getCountryNumberByName(gm.getCountries(), country);
			attackerCountryList.add(countriesOwnedNumbers);
		}

		neighbouringList = gm.getBoundries().get(attackerCountryNum);

		for (int x = 0; x < neighbouringList.size(); x++) {
			if (attackerCountryList.contains(neighbouringList.get(x))) {
				neighbouringList.remove(x);
				continue;
			}
		}
		if (neighbouringList.size() > 0) {
			Collections.shuffle(neighbouringList);

			player.setAttackerCountry(randomCountry);
			player.setDefenderCountry(cc.getCountryNameByNum(gm.getCountries(), neighbouringList.get(0)));
			String defenderPlayer = cc.findPlayerNameFromCountry(gm.getCountries(), player.getDefenderCountry());

			player.setDefenderName(defenderPlayer);
			player.setAttackerName(player.getCurrentPlayerTurn());

			Player defenderPlayerData = pl.getListOfPlayers().get(defenderPlayer);
			Player attackerPlayerData = pl.getListOfPlayers().get(player.getAttackerName());

			int totalArmiesOwnedByDefender = defenderPlayerData.getOwnedCountriesArmiesList()
					.get(player.getDefenderCountry());

			int numberOfAttackToBeDone = randomNumberOfAttack(totalArmiesOwnedByDefender);

			while (numberOfAttackToBeDone != 0) {

				totalArmiesOwnedByDefender = defenderPlayerData.getOwnedCountriesArmiesList()
						.get(player.getDefenderCountry());

				int totalArmiesownedByAttacker = attackerPlayerData.getOwnedCountriesArmiesList()
						.get(player.getAttackerCountry());

				int attackerDiceToRoll = 0;

				if (totalArmiesownedByAttacker > 3) {
					attackerDiceToRoll = 3;
				} else if (totalArmiesownedByAttacker == 3) {
					attackerDiceToRoll = 2;
				} else if (totalArmiesownedByAttacker == 2) {
					attackerDiceToRoll = 1;
				} else
					break; // TODO: check at first loop ,if attacker army = 1 , then return

				player.setDiceRolled(attackerDiceToRoll);

				String attackSet = pc.attackPhase(player.getAttackerCountry(), player.getDefenderCountry(),
						attackerDiceToRoll, player);

				int defenderDiceToRoll = 0;

				if (player.getAttackerDice().size() == 1) {
					defenderDiceToRoll = 1;
				} else {
					if (totalArmiesOwnedByDefender > 1) {
						defenderDiceToRoll = 2;
					} else if (totalArmiesOwnedByDefender == 1) {
						defenderDiceToRoll = 1;
					} else
						break;
				}

				boolean defendSet = pc.defendPhaseDiceRoll(player.getDefenderCountry(), defenderDiceToRoll, player);

				String warStarted = pc.defendingTheBase(player, pl);

				if (warStarted.contains("Won")) {

					boolean checkAllCountriesOwned = pc.checkGameEnd(pl);

					if (checkAllCountriesOwned) {
						System.out.println("\n" + player.getAttackerName() + " won the Risk Game");
						System.out.println("\nThe game is ended");
						System.exit(0); // TODO : avoid system.exit if tournament mode
					} else {
						String movingArmyResult = pc.movingArmyToConqueredCountry(player.getDiceRolled(),
								pl.getListOfPlayers(), player, gm);
						break;
					}
				}

				numberOfAttackToBeDone--;
			}
		}

		player.setGameState("FORTIFY");
	}

	/**
	 * method performs the fortification of the random player
	 * 
	 * @param gm     is the game map containing all info about game
	 * @param pl     contains all information about player
	 * @param player it is the player object
	 * @author Ashish Chaudhary
	 * 
	 */
	private void fortify(GameMap gm, PlayersList pl, Player player) {

		Collections.shuffle(countriesOwned);
		String randomToCountry = countriesOwned.get(0);

		Player playerData = pl.getListOfPlayers().get(player.getCurrentPlayerTurn());

		String randomFromCountry = "";
		for (String fromCountry : playerData.getOwnedCountriesArmiesList().keySet()) {
			if (!fromCountry.equals(randomToCountry)) {
				pc.checkOwnPath(gm.getBoundries(), cc.getCountryNumberByName(gm.getCountries(), fromCountry),
						cc.getCountryNumberByName(gm.getCountries(), randomToCountry));
				if (pc.ownedPath) {
					randomFromCountry = fromCountry;
					break;
				}
			}
		}
		if (!randomFromCountry.equals("")) {

			int fromCountryArmy = playerData.getOwnedCountriesArmiesList().get(randomFromCountry);

			int randomArmyNumber = randomNumberToFortify(fromCountryArmy - 1);

			String fortifyResult = pc.fortify(pl.getListOfPlayers(), randomFromCountry, randomToCountry,
					randomArmyNumber, gm.getCountries(), gm.getBoundries());
			if (player.getConqueredCountries().size() > 0) {
				pc.addGameCardsToAttacker(
						pl.getListOfPlayers().get(player.getAttackerName()), player, gm);
			}
		}
	}

	/**
	 * Mehtod is used to generate random number to reinforce the country
	 * 
	 * @param player contains the player object
	 * @return random number generated
	 * @author Ashish Chaudhary
	 */
	public int randomArmyToReinforceGenerator(Player player) {
		double random = Math.random();
		random = random * player.getAvailableReinforceArmies() + 1;
		int randomNumber = (int) random;
		return randomNumber;

	}

	/**
	 * Method is used to generate random number to attack
	 * 
	 * @param totalArmiesOwnedByDefender it is the total armies owned by defender
	 * @return random number generated
	 * @author Ashish Chaudhary
	 */
	private int randomNumberOfAttack(int totalArmiesOwnedByDefender) {

		double random = Math.random();
		random = random * totalArmiesOwnedByDefender + 1;
		int randomNumber = (int) random;
		return randomNumber;

	}

	private int randomNumberToFortify(int fromCountryArmy) {

		double random = Math.random();
		random = random * fromCountryArmy + 1;
		int randomNumber = (int) random;
		return randomNumber;

	}

}
