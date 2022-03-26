package variants.alpha;

import strategies.FieldValidatingStrategy;
import framework.Game;
import constants.GameConstants;
import framework.Position;
import framework.Unit;

import java.util.Arrays;

public class AlphaFieldStrategy implements FieldValidatingStrategy {

    public static final String[] RACE_LIST = {
            GameConstants.DWARF,
            GameConstants.ELF,
            GameConstants.HUMAN,
            GameConstants.ORC
    };

    @Override
    public boolean isLegalFieldType(Game game, Position p, Unit u) {
        boolean isMatchingUnit = Arrays.asList(RACE_LIST).contains( u.getUnitRace() );
        if (!isMatchingUnit) return false;

        boolean isSwampField = (game.getFieldTypeAt(p).getFieldType().equals( GameConstants.SWAMP ));
        if (isSwampField) return false;

        boolean isMountainField = (game.getFieldTypeAt(p).getFieldType().equals( GameConstants.MOUNTAIN ));
        boolean isDwarf = ( u.getUnitRace().equals( GameConstants.DWARF ));
        if (!isDwarf && isMountainField) return false;

        return true;
    }

    @Override
    public void validateMovementSpeedFor(Game game, Unit unit, Position p) {
        unit.fatigueUnit();
    }
}
