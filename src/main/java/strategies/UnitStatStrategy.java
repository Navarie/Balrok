package strategies;

import common.GameImpl;
import framework.*;

public interface UnitStatStrategy {

    Unit initialiseUnitFrom(Card c, Game game);
    Unit buildUnit(Unit u, int[] stats, String[] specifics);

    void validateFerocity(Unit unit);
    void validateDebuffTriggers(Unit unit);
    boolean validateUnban(Unit unit, Game game);
    boolean validateRush(Unit unit);
}
