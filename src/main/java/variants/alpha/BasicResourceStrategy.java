package variants.alpha;

import framework.Game;
import strategies.PlayerStatStrategy;
import framework.Player;

import java.util.ArrayList;
import java.util.List;

import static common.PlayerImpl.STARTING_HEALTH;
import static common.PlayerImpl.STARTING_MANA;
import static constants.GameConstants.BLACK_PLAYER;
import static constants.GameConstants.WHITE_PLAYER;

public class BasicResourceStrategy implements PlayerStatStrategy {

    private final List<Player> players;

    public BasicResourceStrategy() {
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
        blackPlayer.setMana(STARTING_MANA);
        whitePlayer.setHealth(STARTING_HEALTH);
        whitePlayer.setMana(STARTING_MANA);

        players.add( blackPlayer );
        players.add( whitePlayer );
    }

    @Override
    public void restoreHealthTo(Player player) {
        player.restoreHealth();
    }

    @Override
    public void setManaFor(Player player, int amount) {
        player.spendMana(amount);
    }

    @Override
    public void validatePlayerDominance(Game game) {

    }
}
