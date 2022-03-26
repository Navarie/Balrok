package framework;

import java.util.List;
import java.util.Map;

public interface Game {

    FieldType getFieldTypeAt( Position p );
    List<Player> getPlayers();
    Player getPlayerInTurn();
    List<Player> getOpponents();

    Unit getUnitAt( Position p );
    Map<Position, Unit> getUnits();
    List<Unit> getDeadUnitsAtCemetery(Position p);
    Map<Position, List<Unit>> getCemetery(Position p);

    Card getCard(String name);
    List<Card> getDeck(Player owner);
    List<Card> getHand(Player owner);

    int getRoundCounter();
    int getCurrentYear();

    void startGame();
    void endTurn();

    void addCardToHand(Card c);
    void drawCardFor(Player p);
    void removeFromHand(Card c, Player player);

    void sendToCemetery(Unit unit, Player owner);
    void sendToPrisonKeep(Unit unit, Player owner);

    boolean activateCard(Card c, Unit target, Position to, Player p);
    boolean playCard(Card c, Unit target, Position to);
    boolean attackOpponent(Position from, Position to);
    boolean moveUnit(Position from, Position to);

    void addObserver(GameObserver newObserver);
    void setFieldInspectionAt(Position p);
    void setInspectionOn(Card card);
}
