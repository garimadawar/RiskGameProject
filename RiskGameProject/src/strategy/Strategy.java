package strategy;

import model.GameMap;
import model.Player;
import model.PlayersList;
/**
 * This is the strategy interface which contains execute method
 * @author Ashish Chaudhary
 */
public interface Strategy {
    /**
     * this method is used to execute strategy
     * @param gm this contains the game map
     * @param pl this contains the list of players
     * @param player this contains player data
     * @return String result from a particular strategy pattern
     * @author garimadawar
     * @throws InterruptedException this throws interrupted exception on thread sleep
     */
	String executeStrategy(GameMap gm , PlayersList pl , Player player) throws InterruptedException;
		
}
