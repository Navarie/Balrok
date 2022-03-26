package strategies;

import common.GameImpl;
import framework.*;

import java.util.List;
import java.util.Map;

public interface BoardLayoutStrategy {

    Map<Position, FieldType> getFieldTypes();
    Map<Position, Unit> getUnits();
    Map<Position, List<Unit>> getCemetery(Position p);
    List<Unit> getKeep(Player owner);
    Card getTrapCardAt(Position p);

    void setTrapCard(Card c, Player owner, int turn);
    void setUnit(Unit u, Position p, Game game);

    void sendToCemetery(Player owner, Unit corpse);
    void sendToPrisonKeep(Unit unit, Player owner);
    void removeFromGame(Card c, Player owner);

    boolean validateMoveDirection(Unit unit, Position from, Position to);
    void validateSpecialEffectsFor(Unit unit, Position to, Game game);

}
