package strategies;

import framework.Game;
import framework.Player;

import java.util.List;
import java.util.Map;

public interface PlayerStatStrategy {

    List<Player> getPlayers();

    void initialisePlayers();
    void restoreHealthTo(Player color);
    void setManaFor(Player player, int amount);
    void validatePlayerDominance(Game game);
}
