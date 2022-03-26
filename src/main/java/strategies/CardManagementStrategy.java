package strategies;

import framework.Card;
import framework.Player;
import framework.Unit;

import java.util.List;

public interface CardManagementStrategy {

    Card getCard(String name);
    List<Card> getDeckList(Player owner);
    List<Card> getHand(Player owner);

    void addCardToDeck(Card c, Player owner);
    void drawCardFor(Player player);
    void removeFromHand(Card c, Player owner);
    void drawStartingHands();
    void initialiseDecks();

    boolean validateCard(Card c, Player player);
    boolean addCardToHand(Card c, Player owner);
    boolean validateSacrifice(Card c, Unit target);
}
