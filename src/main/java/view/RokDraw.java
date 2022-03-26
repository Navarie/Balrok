package view;

import constants.GameConstants;
import framework.*;
import view.figure.BalrokFigure;
import view.figure.CardFigure;
import view.figure.InformationFigure;
import view.figure.UnitFigure;

import java.awt.*;
import java.util.*;
import java.util.List;
import minidraw.framework.*;
import minidraw.standard.ImageFigure;
import minidraw.standard.StandardFigureCollection;
import minidraw.standard.handlers.ForwardingFigureChangeHandler;
import minidraw.standard.handlers.StandardDrawingChangeListenerHandler;
import minidraw.standard.handlers.StandardSelectionHandler;

import static constants.CardConstants.*;
import static view.GraphicsConstants.*;
import static utilities.GraphicsUtility.*;
import static constants.GameConstants.*;

public class RokDraw implements Drawing, GameObserver {

    private final SelectionHandler selectionHandler;
    private final DrawingChangeListenerHandler listenerHandler;
    private final ForwardingFigureChangeHandler figureChangeListener;
    private final FigureCollection figureCollection;

    private final Map<Position, UnitFigure> positionToUnitFigureMap;
    private final List<CardFigure> blackHandCollection;
    private final List<CardFigure> whiteHandCollection;
    private final List<CardFigure> throwawayCollection;

    protected Game game;

    private static final Point UNIT_INFO_POS = new Point(UNIT_INFO_X, UNIT_INFO_Y);
    private static final Point UNIT_SECOND_ROW_POS = new Point(UNIT_INFO_X, UNIT_INFO_Y + LINE_SEPARATION);
    private static final Point UNIT_THIRD_ROW_POS = new Point(UNIT_INFO_X, UNIT_INFO_Y + (2 * LINE_SEPARATION));
    private static final Point UNIT_FOURTH_ROW_POS = new Point(UNIT_INFO_X, UNIT_INFO_Y + (3 * LINE_SEPARATION));
    private static final Point UNIT_BUFFS_POS = new Point(UNIT_INFO_X, UNIT_INFO_Y + (4 * LINE_SEPARATION));
    private static final Point UNIT_DEBUFFS_POS = new Point(UNIT_INFO_X, UNIT_INFO_Y + (5 * LINE_SEPARATION));
    private static final Point CARD_INFO_POS = new Point(CARD_INFO_X, CARD_INFO_Y);
    private static final Point CARD_SECOND_ROW_POS = new Point(CARD_INFO_X, CARD_INFO_Y + LINE_SEPARATION);
    private static final Point CARD_THIRD_ROW_POS = new Point(CARD_INFO_X, CARD_INFO_Y + (2 * LINE_SEPARATION));
    private static final Point CARD_FOURTH_ROW_POS = new Point(CARD_INFO_X, CARD_INFO_Y + (3 * LINE_SEPARATION));
    private static final Point END_TURN_BUTTON_POS = new Point(TURN_BUTTON_X, TURN_BUTTON_Y);
    private static final Point REFRESH_BUTTON_POS = new Point(REFRESH_BUTTON_X, REFRESH_BUTTON_Y);
    private static final Point DECK_IMAGE_POS = new Point(DECK_IMAGE_X, DECK_IMAGE_Y);
    private static final Point DECK_INFO_POS = new Point(DECK_IMAGE_X + DECK_IMAGE_WIDTH + SEPARATION, DECK_IMAGE_Y);
    private static final Point DECK_SECOND_ROW_POS = new Point(DECK_IMAGE_X + DECK_IMAGE_WIDTH + SEPARATION, DECK_IMAGE_Y + LINE_SEPARATION);
    private static final Point PLAYER_INFO_POS = new Point(PLAYER_IMAGE_X + PLAYER_IMAGE_WIDTH + SEPARATION, PLAYER_IMAGE_Y);
    private static final Point PLAYER_SECOND_ROW_POS = new Point(PLAYER_IMAGE_X + PLAYER_IMAGE_WIDTH + SEPARATION, PLAYER_IMAGE_Y + LINE_SEPARATION);
    private static final Point AGE_INFO_POS = new Point(AGE_INFO_X + SEPARATION, AGE_INFO_Y);
    private static final Point AGE_ROW_2_POS = new Point(AGE_INFO_X + SEPARATION, AGE_INFO_Y + LINE_SEPARATION);
//    private static final Point HAND_POS = new Point(HAND_X, HAND_Y);

    public RokDraw(Game game) {

        selectionHandler = new StandardSelectionHandler();
        listenerHandler = new StandardDrawingChangeListenerHandler();
        figureChangeListener = new ForwardingFigureChangeHandler(this,
                (StandardDrawingChangeListenerHandler) listenerHandler);
        figureCollection = new StandardFigureCollection(figureChangeListener);
        positionToUnitFigureMap = new HashMap<>();
        blackHandCollection = new ArrayList<>();
        whiteHandCollection = new ArrayList<>();
        throwawayCollection = new ArrayList<>();

        this.game = game;
        game.addObserver(this);

        synchronizeUnitFigureCollectionWithGameUnits();
        synchronizeIconsWithGameState();
        addInitialCardsToHandCollection();
        updateDeckInfo();
        updatePlayerInfo();
        updateAgeInfo();
    }

    protected void synchronizeUnitFigureCollectionWithGameUnits() {
        Position p;
        for (int row = 0; row < GameConstants.EPSILON_BOARD_HEIGHT; row++) {
            for (int column = 0; column < GameConstants.EPSILON_BOARD_WIDTH; column++) {
                p = new Position(row, column);
                Unit unit = game.getUnitAt(p);
                UnitFigure unitFigure = positionToUnitFigureMap.get(p);

                    // Synchronize each field position with figure collection
                if (unit != null) {
                    if (unitFigure == null) {
                        unitFigure = createUnitFigureFor(p, unit);

                        positionToUnitFigureMap.put(p, unitFigure);
                        figureCollection.add(unitFigure); }
                } else {
                    if (unitFigure != null) {
                        positionToUnitFigureMap.remove(p);
                        figureCollection.remove(unitFigure); }
                }
            }
        }
    }

    protected void addInitialCardsToHandCollection() {
        for (Player player : PLAYER_LIST) {
            List<Card> hand = game.getHand(player);

            if (hand != null) {
                int handSize = hand.size();

                for (int i=0; i < handSize; i++) {
                    Card card = hand.get(i);
                    CardFigure cardFigure = createCardFigureFor(player, card, i);

                    if (player.equals( BLACK_PLAYER )) {
                        blackHandCollection.add(cardFigure);
                    } else if (player.equals( WHITE_PLAYER )) {
                        whiteHandCollection.add(cardFigure);
                    }
                    figureCollection.add(cardFigure); }
            }
        }
    }

    private CardFigure createCardFigureFor(Player player, Card card, int cardIndex) {

        String cardName = card.getName();
        Point coordinate = null;
        if (player.equals( BLACK_PLAYER )) {
            coordinate = new Point(getXFromCardIndex(cardIndex, player), BLACK_HAND_Y);
        } else if (player.equals( WHITE_PLAYER )) {
            coordinate = new Point(getXFromCardIndex(cardIndex, player), WHITE_HAND_Y);
        }
        return new CardFigure(cardName, coordinate, card);
    }

    private UnitFigure createUnitFigureFor(Position p, Unit unit) {
        String unitAttributes = getAttributesFor(unit);
        Point coordinate = new Point(getXFromColumn(p.getColumn()), getYFromRow(p.getRow()));

        return new UnitFigure(unitAttributes, coordinate, unit);
    }

    private String getAttributesFor(Unit unit) {
        Player playerOwner = unit.getOwner();
        String owner = null;
        if (playerOwner.equals( BLACK_PLAYER) ) {
            owner = "black";
        } else if (playerOwner.equals( WHITE_PLAYER )) {
            owner = "white";
        } else if (playerOwner.equals( YELLOW_PLAYER )) {
            owner = "yellow";
        }

        String name = unit.getName();
        String race = unit.getUnitRace();
        StringBuilder placeholderString = new StringBuilder();
        if (name != null) {
                // Append string so card in hand transforms into unit-size by look-up
            return name + "-field-scale";
        } else {
            placeholderString.append(owner);
            placeholderString.append("-");
            placeholderString.append(race);
        }
        String affinity = unit.getAffinity();
        boolean affinityNullCheck = (affinity != null);
        if (affinityNullCheck) {
            if (!affinity.equals( NO_AFFINITY )) {
                placeholderString.append("-");
                placeholderString.append(affinity);
            }
        }
        return placeholderString.toString();
    }

    private InformationFigure unitInformation;
    private InformationFigure unitInformationRow2;
    private InformationFigure unitInformationRow3;
    private InformationFigure unitInformationRow4;
    private InformationFigure unitInformationBuffs;
    private InformationFigure unitInformationDebuffs;
    private InformationFigure cardInformation;
    private InformationFigure cardInformationRow2;
    private InformationFigure cardInformationRow3;
    private InformationFigure cardInformationRow4;
    private InformationFigure deckInformation;
    private InformationFigure deckInformationRow2;
    private InformationFigure playerInformation;
    private InformationFigure playerInformationRow2;
    private InformationFigure ageInformation;
    private InformationFigure ageInformationRow2;
    private ImageFigure endTurnButton;
    private ImageFigure refreshButton;
    private ImageFigure deckImage;

    protected void synchronizeIconsWithGameState() {
            // Guard creating figures due to call on requestUpdate().
        if (endTurnButton == null) {
            endTurnButton = new BalrokFigure(N_TURN_BUTTON, END_TURN_BUTTON_POS, TURN_BUTTON_STRING);
            figureCollection.add(endTurnButton); }

        if (unitInformation == null) {
            unitInformation = new InformationFigure("", UNIT_INFO_POS);
            unitInformationRow2 = new InformationFigure("", UNIT_SECOND_ROW_POS);
            unitInformationRow3 = new InformationFigure("", UNIT_THIRD_ROW_POS);
            unitInformationRow4 = new InformationFigure("", UNIT_FOURTH_ROW_POS);
            unitInformationBuffs = new InformationFigure("", UNIT_BUFFS_POS);
            unitInformationDebuffs = new InformationFigure("", UNIT_DEBUFFS_POS);
            figureCollection.add(unitInformation);
            figureCollection.add(unitInformationRow2);
            figureCollection.add(unitInformationRow3);
            figureCollection.add(unitInformationRow4);
            figureCollection.add(unitInformationBuffs);
            figureCollection.add(unitInformationDebuffs); }

        if (cardInformation == null) {
            cardInformation = new InformationFigure("", CARD_INFO_POS);
            cardInformationRow2 = new InformationFigure("", CARD_SECOND_ROW_POS);
            cardInformationRow3 = new InformationFigure("", CARD_THIRD_ROW_POS);
            cardInformationRow4 = new InformationFigure("", CARD_FOURTH_ROW_POS);
            figureCollection.add(cardInformation);
            figureCollection.add(cardInformationRow2);
            figureCollection.add(cardInformationRow3);
            figureCollection.add(cardInformationRow4); }

        if (ageInformation == null) {
            ageInformation = new InformationFigure("", AGE_INFO_POS);
            ageInformationRow2 = new InformationFigure("", AGE_ROW_2_POS);
            figureCollection.add(ageInformation);
            figureCollection.add(ageInformationRow2);
        }

        if (deckInformation == null) {
            deckInformation = new InformationFigure("", DECK_INFO_POS);
            deckInformationRow2 = new InformationFigure("", DECK_SECOND_ROW_POS);
            figureCollection.add(deckInformation);
            figureCollection.add(deckInformationRow2);
        }

        if (playerInformation == null) {
            playerInformation = new InformationFigure("", PLAYER_INFO_POS);
            playerInformationRow2 = new InformationFigure("", PLAYER_SECOND_ROW_POS);
            figureCollection.add(playerInformation);
            figureCollection.add(playerInformationRow2);
        }

        if (refreshButton == null) {
            refreshButton = new BalrokFigure(N_REFRESH_BUTTON, REFRESH_BUTTON_POS, REFRESH_BUTTON_STRING);
            figureCollection.add(refreshButton); }

        if (deckImage == null) {
            deckImage = new BalrokFigure(N_DECK, DECK_IMAGE_POS, DECK_IMAGE_STRING);
            figureCollection.add(deckImage);
        }

        updateTurnButton(game.getPlayerInTurn());
        removeTrailSwordFigures();
    }

    private void removeTrailSwordFigures() {
        for (Figure nextFigure : figureCollection) {
            if (nextFigure instanceof BalrokFigure) {
                if (((BalrokFigure) nextFigure).getTypeString().equals(B_F_SWORD_STRING)) {
                    figureCollection.remove(nextFigure);
                }
            }
        }
    }

    public void turnEnds(Player nextPlayer, int age) {

        String playerName = nextPlayer.toString();
        System.out.println("-- RokDraw: " + playerName + " draws a card. Hand size is now: " + game.getHand(nextPlayer).size());
        updateTurnButton(nextPlayer);
        updateHands();
        updateHandFor(nextPlayer);
        updateDeckInfo();
        updatePlayerInfo();
        updateAgeInfo();
    }

    private void updateAgeInfo() {
        String[] info = buildAgeInfoString();

        ageInformation.setInformation(info[0]);
        ageInformationRow2.setInformation(info[1]);
    }

    private String[] buildAgeInfoString() {
        String[] ageInfo = new String[2];
        int currentYear = game.getCurrentYear();
        String timePeriod = fetchTimePeriod(currentYear);

        ageInfo[0] = "Age: " + currentYear;
        ageInfo[1] = "Period: " + timePeriod;
        return ageInfo;
    }

    private String fetchTimePeriod(int currentYear) {
        String timePeriod;
        if (currentYear <= 1445) {
            timePeriod = ANTIQUE;
        } else if (currentYear <= 1750) {
            timePeriod = BAROQUE;
        } else if (currentYear <= 1820) {
            timePeriod = CLASSICISM;
        } else if (currentYear <= 1900) {
            timePeriod = ROMANTICISM;
        } else if (currentYear <= 1960) {
            timePeriod = MODERNISM;
        } else {
            timePeriod = POST_MODERNISM;
        }
        return timePeriod;
    }

    // Supports only turn draw
    private void updateHandFor(Player nextPlayer) {
        int nextCardIndex = game.getHand(nextPlayer).size() - 1;
        Card nextCard = game.getHand(nextPlayer).get(nextCardIndex);
        CardFigure cardFigure = createCardFigureFor(nextPlayer, nextCard, nextCardIndex);

        if (nextPlayer.equals( BLACK_PLAYER )) {
            blackHandCollection.add(cardFigure);
        } else if (nextPlayer.equals( WHITE_PLAYER )) {
            whiteHandCollection.add(cardFigure);
        }
        figureCollection.add(cardFigure);
    }

    private void updateTurnButton(Player nextPlayer) {

        if (nextPlayer == WHITE_PLAYER) {
            endTurnButton.set(N_TURN_IN_PROGRESS, END_TURN_BUTTON_POS);
        } else if (nextPlayer == BLACK_PLAYER) {
            endTurnButton.set(N_TURN_BUTTON, END_TURN_BUTTON_POS);
        }
    }

    public void fieldInspectedAt(Position p) {
        Unit unit = game.getUnitAt(p);
        String unitName = "N/A";
        if (unit != null) unitName = unit.getName();

        updateUnitInfo(unit);
        System.out.println("-- RokDraw.fieldInspectedAt() called. Showing info for: " + p + ". Unit: " + unitName);
    }

    public void cardInspectedAt(Card card) { updateCardInfo(card); }

    private void updateCardInfo(Card card) {

            // Remove, if any, overlapping unit information
        for (Figure nextFigure : figureCollection) {
            if (nextFigure.equals( unitInformation )) {
                figureCollection.remove(nextFigure);
                figureCollection.add(cardInformation);
            } else if (nextFigure.equals( unitInformationRow2 )) {
                figureCollection.remove(nextFigure);
                figureCollection.add(cardInformationRow2);
            } else if (nextFigure.equals( unitInformationRow3 )) {
                figureCollection.remove(nextFigure);
                figureCollection.add(cardInformationRow3);
            } else if (nextFigure.equals( unitInformationRow4 )) {
                figureCollection.remove(nextFigure);
                figureCollection.add(cardInformationRow4);
            } else if (nextFigure.equals( unitInformationBuffs )) {
                figureCollection.remove(nextFigure);
            } else if (nextFigure.equals( unitInformationDebuffs )) {
                figureCollection.remove(nextFigure);
            }
        }

        if (card == null) return;
        String[] info = buildCardInfoFor(card);

        cardInformation.setInformation(info[0]);
        cardInformationRow2.setInformation(info[1]);
        cardInformationRow3.setInformation(info[2]);
        cardInformationRow4.setInformation(info[3]);
    }

    private String[] buildCardInfoFor(Card card) {
        String[] cardInfo = new String[4];

        cardInfo[0] = card.getName();
        cardInfo[1] = card.getQuote();
        cardInfo[2] = card.getDescription();
        cardInfo[3] = "Type: " + getHRCardType(card.getType()) +
                      " - Cost: " + card.getCost();

        return cardInfo;
    }

    private String getHRCardType(String type) {
        switch (type) {
            case (UNIT_CARD) -> { return "Unit"; }
            case (MAGIC_CARD) -> { return "Magic"; }
            case (FIELD_CARD) -> { return "Field"; }
            case (TRAP_CARD) -> { return "Trap"; }
            case (DEMONIC_TRAP_CARD) -> { return "Demonic trap"; }
            case (DEMONIC_CARD) -> { return "Demonic"; }
            case (STRUCTURE_CARD) -> { return "Structure"; }

            default -> { return "Card type not found."; }
        }
    }

    private void updateUnitInfo(Unit unit) {

            // Remove, if any, overlapping card information
        for (Figure nextFigure : figureCollection) {
            if (nextFigure.equals( cardInformation )) {
                figureCollection.remove(nextFigure);
                figureCollection.add(unitInformation);
            } else if (nextFigure.equals( cardInformationRow2 )) {
                figureCollection.remove(nextFigure);
                figureCollection.add(unitInformationRow2);
            } else if (nextFigure.equals( cardInformationRow3 )) {
                figureCollection.remove(nextFigure);
                figureCollection.add(unitInformationRow3);
            } else if (nextFigure.equals( cardInformationRow4 )) {
                figureCollection.remove(nextFigure);
                figureCollection.add(unitInformationRow4);
                figureCollection.add(unitInformationBuffs);
                figureCollection.add(unitInformationDebuffs);
            }
        }

        if (unit == null) return;
        String[] info = buildInformationStringFor(unit);

        unitInformation.setInformation(info[0]);
        unitInformationRow2.setInformation(info[1]);
        unitInformationRow3.setInformation(info[2]);
        unitInformationRow4.setInformation(info[3]);
        unitInformationBuffs.setInformation(info[4]);
        unitInformationDebuffs.setInformation(info[5]);
    }

    private String[] buildInformationStringFor(Unit unit) {
        String[] unitInfo = new String[6];
        String unitName = "--";
        if (unit.getName() != null) {
            unitName = unit.getName() + " -";
        }

        unitInfo[0] = "HP " + unit.getHealth() +
                " - MS " + unit.getCurrentMovementSpeed() +
                " - FER " + unit.getMaxFerocity() +
                " - STR " + unit.getStrength() +
                " - AP " + unit.getAbilityPower();
        unitInfo[1] = "Affinity: " + unit.getAffinity();
        unitInfo[2] = "Affiliation: " + unit.getAffiliation();
        unitInfo[3] = "Name: " + unitName +
                " Race: " + unit.getUnitRace();
        unitInfo[4] = "Buffs: " + unit.getBuffs();
        unitInfo[5] = "Debuffs: " + unit.getDebuffs();
        return unitInfo;
    }

    private void updateDeckInfo() {
        if (game.getPlayers() == null) return;
        String[] info = buildDeckString();

        deckInformation.setInformation(info[0]);
        deckInformationRow2.setInformation(info[1]);
    }

    private String[] buildDeckString() {
        List<Player> players = game.getPlayers();
        String[] deckInfo = new String[2];

        deckInfo[0] = "B: " + game.getDeck(players.get(0)).size();
        deckInfo[1] = "W: " + game.getDeck(players.get(1)).size();
        return deckInfo;
    }

    private void updatePlayerInfo() {
        if (game.getPlayers() == null) return;
        String[] info = buildPlayerString();

        playerInformation.setInformation(info[0]);
        playerInformationRow2.setInformation(info[1]);
    }

    private String[] buildPlayerString() {
        List<Player> players = game.getPlayers();
        String[] playerInfo = new String[2];

        playerInfo[0] = players.get(0).toString() + ": "
                + players.get(0).getCurrentHealth() + " HP/"
                + players.get(0).getCurrentMana() + " Mana";
        playerInfo[1] = players.get(1).toString() + ": "
                + players.get(1).getCurrentHealth() + " HP/"
                + players.get(1).getCurrentMana() + " Mana";
        return playerInfo;
    }

    public void requestUpdate() {
            // Redraw from scratch, to synchronize with game state
        synchronizeUnitFigureCollectionWithGameUnits();
        synchronizeIconsWithGameState();
        updateHands();
        updatePlayerInfo();
    }

    public void boardChangeAt(Position p) {
        System.out.println("RokDraw: board change at " + p);
        Unit unit = game.getUnitAt(p);
        if (unit == null) {
                // Unit has been removed
            UnitFigure unitFigure = positionToUnitFigureMap.remove(p);
            figureCollection.remove(unitFigure);
        } else {
                // Unit has appeared
            UnitFigure unitFigure = createUnitFigureFor(p, unit);
            positionToUnitFigureMap.put(p, unitFigure);
            figureCollection.add(unitFigure);
        }
    }

    public void removeCardFromView(Card card) {
        String cardName = card.getName();
        Player playerInTurn = game.getPlayerInTurn();

        if (playerInTurn.equals( BLACK_PLAYER )) {
            blackHandCollection.removeIf(cf -> cf.getName().equals(cardName));
        } else if (playerInTurn.equals( WHITE_PLAYER )) {
            whiteHandCollection.removeIf(cf -> cf.getName().equals(cardName));
        }
    }

    public void updateHands() {

        for (Player player : game.getPlayers()) {

            List<Card> hand = game.getHand(player);
            if (hand != null) {

                throwawayCollection.clear();
                int handSize = hand.size();
                for (int i = handSize - 1; i >= 0; i--) {
                    Card card = hand.get(i);
                    CardFigure cardFigure = createCardFigureFor(player, card, i);
                    throwawayCollection.add(cardFigure);
                    figureCollection.remove(cardFigure);
                }
                if (player.equals( BLACK_PLAYER )) {
//                    blackHandCollection.removeAll(throwawayCollection);
                    blackHandCollection.addAll(throwawayCollection);
                    for (CardFigure cf : throwawayCollection) {
                        figureCollection.add(cf);
                    }
                } else if (player.equals( WHITE_PLAYER )) {
//                    whiteHandCollection.removeAll(throwawayCollection);
                    whiteHandCollection.addAll(throwawayCollection);
                    for (CardFigure cf : throwawayCollection) {
                        figureCollection.add(cf);
                    }
                }
            }
        }
    }

    public void addToSelection(Figure figure) {
        selectionHandler.addToSelection(figure);
        if (figure instanceof BalrokFigure) {
            if (((BalrokFigure) figure).getTypeString().equals( B_F_SWORD_STRING) ) {
                figureCollection.add(figure);
            }
        }
    }
    public void clearSelection() {
        selectionHandler.clearSelection();
    }
    public void removeFromSelection(Figure figure) {
        selectionHandler.removeFromSelection(figure);
        if (figure instanceof BalrokFigure) {
            if (((BalrokFigure) figure).getTypeString().equals( B_F_SWORD_STRING) ) {
                figureCollection.remove(figure);
            } else if (((CardFigure) figure).getTypeString().equals( GENERIC_CARD_STRING )) {
                figureCollection.remove(figure);
            }
        }
    }
    public List<Figure> selection() {
        return selectionHandler.selection();
    }
    public void toggleSelection(Figure arg0) {
        selectionHandler.toggleSelection(arg0);
    }
    public void figureChanged(FigureChangeEvent arg0) {
        figureChangeListener.figureChanged(arg0);
    }
    public void figureInvalidated(FigureChangeEvent arg0) {
        figureChangeListener.figureInvalidated(arg0);
    }
    public void addDrawingChangeListener(DrawingChangeListener arg0) { listenerHandler.addDrawingChangeListener(arg0); }
    public void removeDrawingChangeListener(DrawingChangeListener arg0) { listenerHandler.removeDrawingChangeListener(arg0); }
    public Figure findFigure(int arg0, int arg1) {
        return figureCollection.findFigure(arg0, arg1);
    }
    public Figure zOrder(Figure figure, ZOrder order) {
        return figureCollection.zOrder(figure, order);
    }
    public Iterator<Figure> iterator() { return figureCollection.iterator(); }

    @Override
    @Deprecated
    public void lock() {}
    @Override
    @Deprecated
    public void unlock() {}

    /** Disallow client side units to add and manipulate figures. */
    public Figure add(Figure arg0) {
        throw new RuntimeException("Should not be used, handled by Observing Game");
    }
    public Figure remove(Figure arg0) { throw new RuntimeException("Should not be used, handled by Observing Game"); }
}
