package strategies;

import framework.Card;
import framework.Game;
import framework.Unit;

public interface MagicEffectStrategy {

    Card getActiveFieldCard();

    @SuppressWarnings("SpellCheckingInspection")
    void playMagicCard(Card c, Game game);

    @SuppressWarnings("SpellCheckingInspection")
    void playTargetedMagicCard(Card c, Unit target, Game game);

    void playFieldCard(Card c, Game game);
}
