package strategies;

import framework.Card;
import framework.Game;
import framework.Position;

public interface DemonicEffectStrategy {

    void demonicDisplacement(Game game, Position target, Card c);
}
