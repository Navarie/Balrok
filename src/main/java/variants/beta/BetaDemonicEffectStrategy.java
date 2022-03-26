package variants.beta;

import constants.GameConstants;
import strategies.DemonicEffectStrategy;
import common.GameImpl;
import framework.*;
import utilities.BoardUtility;

import java.util.Iterator;

import static constants.CardConstants.*;
import static constants.SpellConstants.*;

public class BetaDemonicEffectStrategy implements DemonicEffectStrategy {

    @Override
    public void demonicDisplacement(Game game, Position target, Card c) {

        String cardName = c.getName();
        switch (cardName) {
            case (N_CIRI) -> {
                Unit targetUnit = game.getUnitAt(target);
                Player targetUnitOwner = targetUnit.getOwner();
                ((GameImpl) game).removeUnitAt(target);

                if (targetUnitOwner.equals(GameConstants.BLACK_PLAYER)) {
                    game.getUnits().put(new Position(target.getRow() + 2, target.getColumn()), targetUnit);
                } else {
                    game.getUnits().put(new Position(target.getRow() - 2, target.getColumn()), targetUnit);
            }   }
            case (N_INGER) -> {
                Unit targetUnit = game.getUnitAt(target);
                Player targetUnitOwner = targetUnit.getOwner();
                ((GameImpl) game).removeUnitAt(target);

                if (targetUnitOwner.equals ( GameConstants.BLACK_PLAYER )) {
                    game.getUnits().put(new Position(6, target.getColumn()), targetUnit);
                } else {
                    game.getUnits().put(new Position(0, target.getColumn()), targetUnit);
            }   }
            case (N_CHOPIN) -> {
                Iterator<Position> Grid3x3 = BoardUtility.get3x3GridWithoutCenter(target);
                while (Grid3x3.hasNext()) {
                    Position p = Grid3x3.next();
                    Unit unitAtTo = game.getUnitAt(p);

                    if (unitAtTo != null) {
                        unitAtTo.getDebuffs().put(GameConstants.Debuff.ROOT, 6); }
            }   }
            case (N_TWO_SMOKES) -> {
                Iterator<Position> Grid3x3 = BoardUtility.get3x3GridWithoutCenter(target);
                while (Grid3x3.hasNext()) {
                    Position p = Grid3x3.next();
                    Unit unitAtTo = game.getUnitAt(p);

                    if (unitAtTo != null) {
                        unitAtTo.getDebuffs().put(GameConstants.Debuff.SUPPRESS, 4); }
            }   }
            case (N_BLACK_HOLE) -> {
                Iterator<Position> boardCenter = BoardUtility.getBoardCenter();
                while (boardCenter.hasNext()) {
                    Position p = boardCenter.next();
                    Unit unitAtTo = game.getUnitAt(p);

                    if (unitAtTo != null) {
                        game.sendToCemetery(unitAtTo, unitAtTo.getOwner()); }
            }   }
            default -> System.out.println("Error in retrieving demonic card.");
        }
    }
}
