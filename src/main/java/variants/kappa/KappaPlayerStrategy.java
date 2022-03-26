package variants.kappa;

import framework.Game;
import framework.Player;
import framework.Unit;
import strategies.PlayerStatStrategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static common.PlayerImpl.STARTING_HEALTH;
import static common.PlayerImpl.STARTING_MANA;
import static constants.GameConstants.BLACK_PLAYER;
import static constants.GameConstants.WHITE_PLAYER;

public class KappaPlayerStrategy implements PlayerStatStrategy {

    private final List<Player> players;

    public KappaPlayerStrategy() {
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void validatePlayerDominance(Game game) {
        int blackUnits = 0;
        int whiteUnits = 0;
        Collection<Unit> units = game.getUnits().values();

        for (Unit unit : units) {
            if (unit.getOwner().equals( BLACK_PLAYER )) {
                blackUnits++;
            } else if (unit.getOwner().equals( WHITE_PLAYER )) {
                whiteUnits++;
            }
        }
        int biggestUnitForce = Math.max(blackUnits, whiteUnits);
        boolean isDominantUnitForce = (biggestUnitForce > blackUnits || biggestUnitForce > whiteUnits);

        if (isDominantUnitForce) {

            Player weakerPlayer = null;
            if (biggestUnitForce == blackUnits) {
                weakerPlayer = WHITE_PLAYER;
                weakerPlayer.takeDamage(blackUnits - whiteUnits);
            } else if (biggestUnitForce == whiteUnits) {
                weakerPlayer = BLACK_PLAYER;
                weakerPlayer.takeDamage(whiteUnits - blackUnits);
            }

        }
    }
}
