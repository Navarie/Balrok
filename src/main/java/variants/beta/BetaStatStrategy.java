package variants.beta;

import common.PlayerImpl;
import framework.Game;
import strategies.PlayerStatStrategy;
import constants.CardConstants;
import framework.Player;

import java.util.ArrayList;
import java.util.List;

import static common.PlayerImpl.STARTING_HEALTH;
import static common.PlayerImpl.STARTING_MANA;
import static constants.GameConstants.*;

public class BetaStatStrategy implements PlayerStatStrategy {

    private List<Player> players;

    public BetaStatStrategy() {
        players = new ArrayList<>();
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void initialisePlayers() {
        Player blackPlayer = BLACK_PLAYER;
        Player whitePlayer = WHITE_PLAYER;
        blackPlayer.setHealth(STARTING_HEALTH);
        whitePlayer.setHealth(STARTING_HEALTH);
        blackPlayer.setMana(STARTING_MANA);
        whitePlayer.setMana(STARTING_MANA);

        players.add( blackPlayer );
        players.add( whitePlayer );
    }

    public Player getPlayer(Player color) {
        if (color.equals ( BLACK_PLAYER )) {
            return players.get(0);
        } else {
            return players.get(1);
        }
    }

    @Override
    public void restoreHealthTo(Player color) {
        getPlayer(color).restoreHealth();
    }

    @Override
    public void setManaFor(Player player, int amount) {
        player.spendMana(amount);
    }

    @Override
    public void validatePlayerDominance(Game game) {

    }
}
