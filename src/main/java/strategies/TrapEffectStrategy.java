package strategies;

import framework.*;

public interface TrapEffectStrategy {

    void activateTargetedTrap(Game game, Card c, Position target, Player player);
    void activateTrap(Game game, Card c, Player player);
}
