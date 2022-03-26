package strategies;

import framework.Game;
import framework.Position;
import framework.Unit;

public interface FieldValidatingStrategy {

    boolean isLegalFieldType(Game game, Position p, Unit unit);

    void validateMovementSpeedFor(Game game, Unit unit, Position p);
}
