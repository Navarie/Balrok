package variants.beta;

import strategies.FieldValidatingStrategy;
import framework.Game;
import framework.Position;
import framework.Unit;
import java.util.Arrays;

import static constants.GameConstants.*;

public class BetaFieldStrategy implements FieldValidatingStrategy {

    public static final String[] RACE_LIST = {
            DWARF,
            ELF,
            HUMAN,
            ORC,
            GNOME,
            PRIMORDIAL_DRAGON,
            LESHY,
            CHERNOBOG,
            SIREN,
            FURRY,
            DEMON,
            MONSTER
    };

    @Override
    public boolean isLegalFieldType(Game game, Position p, Unit unit) {
        boolean isMatchingUnit = Arrays.asList(RACE_LIST).contains( unit.getUnitRace() );
        if (!isMatchingUnit) return false;


        boolean isDesert = (game.getFieldTypeAt(p).getFieldType().equals( DESERT ));
        boolean isDruid = (unit.getAffinity().equals( DRUID ));
        if (isDruid && isDesert) {
            return false;
        } else if (isDruid) {
            return true;
        }

        boolean isSwampField = (game.getFieldTypeAt(p).getFieldType().equals( SWAMP ));
        boolean isSiren = (unit.getUnitRace().equals( SIREN ));
        if (isSwampField && !isSiren) return false;

        boolean isMountainField = (game.getFieldTypeAt(p).getFieldType().equals( MOUNTAIN ));
        boolean isDwarf = (unit.getUnitRace().equals( DWARF ));
        if (!isDwarf && isMountainField) return false;

        return true;
    }

    @Override
    public void validateMovementSpeedFor(Game game, Unit unit, Position p) {
        boolean isSiren = (unit.getUnitRace().equals( SIREN ));
        boolean isLakeField = (game.getFieldTypeAt(p).getFieldType().equals( LAKE ));
        if (isSiren && isLakeField) {
            return;
        }

        unit.fatigueUnit();
    }
}
