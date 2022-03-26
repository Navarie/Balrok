package variants.delta;

import common.UnitImpl;
import framework.*;
import strategies.UnitStatStrategy;
import utilities.BoardUtility;

import static constants.GameConstants.*;
import static constants.UnitConstants.*;

public class DeltaUnitStatStrategy implements UnitStatStrategy {

    private Player owner;

    @Override
    public Unit initialiseUnitFrom(Card c, Game game) {
        String cardName = c.getName();
        this.owner = game.getPlayerInTurn();

        switch (cardName) {
            case (N_BLUE_EYES) -> { return buildUnit( U_BLUE_EYES, ST_BLUE_EYES, SP_BLUE_EYES ); }
            case (N_MATHIAS) -> { return buildUnit( U_MATHIAS, ST_MATHIAS, SP_MATHIAS ); }
            case (N_OZITY) -> { return buildUnit( U_OZITY, ST_OZITY, SP_OZITY ); }
            case (N_STATHAM) -> { return buildUnit( U_STATHAM, ST_STATHAM, SP_STATHAM ); }
            case (N_THOMAS) -> { return buildUnit( U_THOMAS, ST_THOMAS, SP_THOMAS ); }
            case (N_LEGOLAS) -> { return buildUnit( U_LEGOLAS, ST_LEGOLAS, SP_LEGOLAS ); }
            case (N_GORDON) -> { return buildUnit( U_GORDON, ST_GORDON, SP_GORDON ); }
            case (N_HJOERDIS) -> { return buildUnit( U_HJOERDIS, ST_HJOERDIS, SP_HJOERDIS ); }
            case (N_DAGFINNUR) -> { return buildUnit( U_DAGFINNUR, ST_DAGFINNUR, SP_DAGFINNUR ); }
            case (N_GERALT_OF_RIVIA) -> { return buildUnit( U_WITCHER, ST_WITCHER, SP_WITCHER ); }
            case (N_HEISENBERG) -> { return buildUnit( U_HEISENBERG, ST_HEISENBERG, SP_HEISENBERG ); }
            case (N_THORSTEN) -> { return buildUnit( U_THORSTEN, ST_THORSTEN, SP_THORSTEN ); }
            case (N_WHITE_FLAME) -> { return buildUnit( U_WHITE_FLAME, ST_WHITE_FLAME, SP_WHITE_FLAME ); }
            case (N_BENSE) -> { return buildUnit( U_BENSE, ST_BENSE, SP_BENSE ); }
            case (N_TYRONE) -> { return buildUnit( U_TYRONE, ST_TYRONE, SP_TYRONE ); }
            case (N_EMILY_COON) -> { return buildUnit( U_EMILY_COON, ST_EMILY_COON, SP_EMILY_COON ); }
            case (N_AHRI) -> { return buildUnit( U_AHRI, ST_AHRI, SP_AHRI ); }
            case (N_VAYNE) -> { return buildUnit( U_VAYNE, ST_VAYNE, SP_VAYNE ); }
            case (N_GERMAN_KID) -> { return buildUnit( U_GERMAN_KID, ST_GERMAN_KID, SP_GERMAN_KID ); }
            case (N_ANDERS) -> { return buildUnit( U_ANDERS, ST_ANDERS, SP_ANDERS ); }
            case (N_ISAAC) -> { return buildUnit( U_ISAAC, ST_ISAAC, SP_ISAAC ); }
            case (N_MR_GARRISON) -> { return buildUnit( U_MR_GARRISON, ST_MR_GARRISON, SP_MR_GARRISON ); }
            case (N_KANYE_EAST) -> { return buildUnit( U_KANYE_EAST, ST_KANYE_EAST, SP_KANYE_EAST ); }
            default -> {
                System.out.println("Unit not found. Look harder.");
                return null;
            }
        }
    }

    @Override
    public Unit buildUnit(Unit u, int[] stats, String[] specifics) {
        u.setAffinity(specifics[0]);
        u.setGender(specifics[1]);
        u.setAffiliation(specifics[2]);
        u.setOwner(getOwner());

        u.setUnitStat("AP", stats[0]);
        u.setUnitStat("FER", stats[1]);
        u.setUnitStat("HP", stats[2]);
        u.setUnitStat("MS", stats[3]);
        u.setUnitStat("STR", stats[4]);

        if (validateRush(u)) {
            u.buffUnitTemporarily("RUSH", 10, 0);
        }

        return u;
    }

    @Override
    public void validateFerocity(Unit unit) {
        String unitName = unit.getName();
        boolean unitNameNullCheck = (unitName != null);
        if (unitNameNullCheck) {
            switch (unitName) {
                case (N_OZITY) -> {}
                default -> ((UnitImpl) unit).refreshFerocity();
            }
        }
    }

    @Override
    public void validateDebuffTriggers(Unit unit) {
        boolean rushNullCheck = (unit.getBuffs().get(Buff.RUSH) != null);
        if (rushNullCheck) {
            boolean lethargicTrigger = (unit.getBuffs().get(Buff.RUSH).get(0) == 0);
            if (lethargicTrigger) {
                unit.debuffUnit("LETH", 10);
            }
        }
    }

    @Override
    public boolean validateUnban(Unit unit, Game game) {
        boolean banNullCheck = (unit.getDebuffs().get(Debuff.BAN) != null);
        if (banNullCheck) {

            boolean unitIsBanned = (unit.getDebuffs().get(Debuff.BAN) > 0);
            if (!unitIsBanned) {

                Position generatedPosition = BoardUtility.generateSpawnPosition(unit, game);
                if (generatedPosition != null) {
                    game.getUnits().put(generatedPosition, unit);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validateRush(Unit unit) {
        boolean isAnders = (unit.getName().equals( N_ANDERS ));
        return isAnders;
    }

    public Player getOwner() {
        return owner;
    }
}
