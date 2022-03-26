package variants.beta;

import framework.Player;
import framework.Unit;
import strategies.CardManagementStrategy;
import framework.Card;
import constants.CardConstants;
import constants.GameConstants;

import java.util.*;

import static constants.GameConstants.MAX_DECK_SIZE;
import static constants.UnitConstants.*;

public class BetaCardManagementStrategy implements CardManagementStrategy {

    public static final int STARTING_HAND_NO = 3;

    private List<Card> whiteHand;
    private List<Card> blackHand;
    private List<Card> blackDeck;
    private List<Card> whiteDeck;

    public BetaCardManagementStrategy() {
        whiteHand = new ArrayList<>();
        blackHand = new ArrayList<>();
        whiteDeck = new ArrayList<>();
        blackDeck = new ArrayList<>();
    }

    @Override
    public Card getCard(String name) {
        return null;
    }

    @Override
    public List<Card> getDeckList(Player owner) {
        if (owner.equals ( GameConstants.BLACK_PLAYER )) {
            return blackDeck;
        } else {
            return whiteDeck;
        }
    }

    public List<Card> getHand(Player owner) {
        if (owner.equals ( GameConstants.BLACK_PLAYER )) {
            return blackHand;
        } else {
            return whiteHand;
        }
    }

    @Override
    public void addCardToDeck(Card c, Player owner) {
        getDeckList(owner).add(c);
    }

    @Override
    public boolean addCardToHand(Card c, Player owner) {
        getHand(owner).add(c);

        return true;
    }

    @Override
    public void drawCardFor(Player player) {
        Card nextCard = getDeckList(player).get(0);
        getDeckList(player).remove(nextCard);

        addCardToHand(nextCard, player);
    }

    @Override
    public void removeFromHand(Card c, Player owner) {
        if (owner.equals ( GameConstants.BLACK_PLAYER )) {
            blackHand.remove(c);
        } else {
            whiteHand.remove(c);
        }
    }

    @Override
    public void initialiseDecks() {
        List<Card> deckList = Arrays.asList( CardConstants.DECK_LIST);

        if (deckList.size() <= MAX_DECK_SIZE) {
            for (Player owner : GameConstants.PLAYER_LIST) {
                getDeckList(owner).addAll( deckList );
            }
        } else {
            System.out.println("Deck-size was over " + MAX_DECK_SIZE + " and was not initialised. Please reduce number of cards by: " + (deckList.size() - MAX_DECK_SIZE));
        }
    }

    @Override
    public void drawStartingHands() {
        for (Player player : GameConstants.PLAYER_LIST) {
            for (int i=0; i < STARTING_HAND_NO; i++) {
                drawCardFor(player);
            }
        }
    }

    @Override
    public boolean validateCard(Card c, Player player) {
        return true;
    }

    @Override
    public boolean validateSacrifice(Card c, Unit target) {
        String cardName = c.getName();
        String targetName = target.getName();

        return switch (cardName) {
            case (N_STATHAM) -> targetName.equals(N_THOMAS);
            default -> true;
        };

    }
}
