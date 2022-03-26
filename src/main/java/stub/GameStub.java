package stub;

import framework.*;
import strategies.CardManagementStrategy;
import utilities.BoardUtility;
import variants.kappa.KappaCardManagementStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static constants.GameConstants.*;

@SuppressWarnings("unused")
public class GameStub implements Game {

    private final Map<Position, Unit> units;
    private final CardManagementStrategy cardManagementStrategy;

    protected Map<Position, FieldType> board;
    protected final List<GameObserver> gameObservers = new ArrayList<>();

    private Player playerInTurn;

    public GameStub() {
        board = initialiseBoard();
        units = new HashMap<>();

        units.put(new Position(2, 2), new UnitStub(BLACK_PLAYER, NEUTRAL, HUMAN, null));
        units.put(new Position(4, 2), new UnitStub(WHITE_PLAYER, NEUTRAL, HUMAN, "nawoori"));
        units.put(new Position(5, 2), new UnitStub(WHITE_PLAYER, NEUTRAL, HUMAN, "Thomas the Goof"));

        playerInTurn = BLACK_PLAYER;
        cardManagementStrategy = new KappaCardManagementStrategy();

        startGame();
    }

    public List<Player> getPlayers() { return null; }
    public Player getPlayerInTurn() { return playerInTurn; }
    public List<Player> getOpponents() { return null; }
    public Unit getUnitAt(Position p) { return units.get(p); }
    public Map<Position, Unit> getUnits() { return null; }
    public List<Unit> getDeadUnitsAtCemetery(Position p) { return null; }
    public Map<Position, List<Unit>> getCemetery(Position p) { return null; }
    public Card getCard(String name) { return null; }
    public List<Card> getDeck(Player owner) { return null; }
    public List<Card> getHand(Player owner) { return cardManagementStrategy.getHand(owner); }
    public int getRoundCounter() { return 0; }
    public int getCurrentYear() { return 0; }

    public void startGame() {
        cardManagementStrategy.initialiseDecks();
        cardManagementStrategy.drawStartingHands();
    }

    public void endTurn() {
        System.out.println("-- GameStub.endOfTurn() called. Next player: ");
        if (playerInTurn.equals( BLACK_PLAYER )) {
            playerInTurn = WHITE_PLAYER;
        } else {
            playerInTurn = BLACK_PLAYER;
        }

        cardManagementStrategy.drawCardFor(playerInTurn);
        gameObservers.forEach(gameObserver -> gameObserver.turnEnds(playerInTurn, 1445));
    }

    public void addCardToHand(Card c) {}
    public void drawCardFor(Player p) {
        cardManagementStrategy.drawCardFor(p);
    }
    public void removeFromHand(Card c, Player player) {}
    public void sendToCemetery(Unit unit, Player owner) {}
    public void sendToPrisonKeep(Unit unit, Player owner) {}
    public boolean activateCard(Card c, Unit target, Position to, Player p) { return false; }
    public boolean playCard(Card c, Unit target, Position to) { return false; }
    public boolean attackOpponent(Position from, Position to) { return false; }
    public boolean moveUnit(Position from, Position to) {

        System.out.println("-- GameStub.moveUnit() called: " + from + "-->" + to);
        Unit unit = getUnitAt(from);
        if (unit == null) return false;
        System.out.println("... found unit at: " + from);

        units.put(from, null);
        gameObservers.forEach(gameObserver -> gameObserver.boardChangeAt(from));
        units.put(to, unit);
        gameObservers.forEach(gameObserver -> gameObserver.boardChangeAt(to));
        return true;
    }
    public void addObserver(GameObserver newObserver) { gameObservers.add(newObserver); }
    public void setFieldInspectionAt(Position p) {
        System.out.println("-- GameStub.setFieldInspectionAt() called. Position: " + p);
        gameObservers.forEach(gameObserver -> gameObserver.fieldInspectedAt(p));
    }

    @Override
    public void setInspectionOn(Card card) {

    }

    public FieldType getFieldTypeAt(Position p) {
        return board.get(p);
    }

    private Map<Position, FieldType> initialiseBoard() {
        String[] boardLayout = new String[] {
                "Koooooooooooo",
                "Xoooooooooooo",
                "ooooooooooooo",
                "ooooooooooooo",
                "ooooooooooooo",
                "ooooooooooooo",
                "ooooooooooooo",
                "ooooooooooooo",
                "ooooooooooooo",
                "ooooooooooooX",
                "ooooooooooooK"
        };
        return BoardUtility.createInitialBoard(boardLayout, "KAPPA");
    }
}
