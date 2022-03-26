package strategies;

import common.GameImpl;
import framework.Card;
import framework.Game;
import framework.Position;

import java.util.List;

public interface StructureStrategy {

    List<Position> getPositions();

    void playStructureCard(Card c, Position to, Game game);

    void validateStructureTurnEffects(Position p, Game game);
    boolean validateAttack(Position from);
}
