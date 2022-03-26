package variants.gamma;

import framework.*;
import strategies.StructureStrategy;
import utilities.BoardUtility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static constants.SpellConstants.*;

public class GammaStructureStrategy implements StructureStrategy {

    private List<Position> structuralPositions = new ArrayList<>();

    @Override
    public void playStructureCard(Card c, Position to, Game game) {
        Player playerInTurn = game.getPlayerInTurn();
        String cardName = c.getName();
        switch (cardName) {
            case (N_FOUNTAIN),  // Fallthrough
                 (N_CAMPING_VAN) -> { iterateThroughArea(c, to, 4, 4, game); }
            case (N_BUILD_A_WALL) -> { iterateThroughArea(c, to, 4, 5, game); }
            case (N_RINGRIDER_PALACE) -> { iterateThroughArea(c, to, 6, 6, game); }
            case (N_HOUSE_OF_GOD) -> { iterateThroughArea(c, to, 5, 5, game); }
            default -> System.out.println("Invalid structure card name. Check log report for clues.");
        }
    }

    private void iterateThroughArea(Card c, Position to, int xDimension, int yDimension, Game game) {
        Iterator<Position> areaOfEffect = BoardUtility.getRectangularGrid(xDimension, yDimension, to);
        while (areaOfEffect.hasNext()) {

            Position p = areaOfEffect.next();
            p.setActiveStructureCard(c);
            structuralPositions.add(p);

            String cardName = c.getName();
            switch (cardName) {
                case (N_FOUNTAIN), (N_HOUSE_OF_GOD) -> {}
                case (N_CAMPING_VAN), (N_RINGRIDER_PALACE),
                    (N_BUILD_A_WALL) -> {
                        iterateUnitInArea(game, p, cardName);
                }
            }
        }
    }

    private void iterateUnitInArea(Game game, Position p, String cardName) {
        boolean unitNullCheck = (game.getUnits().get(p) != null);
        if (unitNullCheck) {
            Unit unit = game.getUnits().get(p);
            switch (cardName) {
                case (N_CAMPING_VAN) -> { unit.debuffUnit("FEAR", 2); }
                case (N_BUILD_A_WALL) -> { unit.debuffUnit("ROOT", 10); }
                case (N_RINGRIDER_PALACE) -> { unit.debuffUnit("DRUNK", 2);}
            }
        }
    }

    @Override
    public void validateStructureTurnEffects(Position p, Game game) {

        boolean unitNullCheck = (game.getUnits().get(p) != null);
        if (unitNullCheck) {
            Unit unit = game.getUnits().get(p);
            boolean unitOwner = (unit.getOwner().equals( game.getPlayerInTurn() ));
            boolean isFountain = (p.getActiveStructureCard().getName().equals( N_FOUNTAIN ));
            boolean isCampingVan = (p.getActiveStructureCard().getName().equals( N_CAMPING_VAN ));

            if (unitOwner && isFountain) {
                unit.buffUnit("RGN", 10);
            } else if (unitOwner && isCampingVan) {
                unit.buffUnit("RGN", -5);
            }
        }
    }

    @Override
    public boolean validateAttack(Position from) {
        boolean structureCardNullCheck = (from.getActiveStructureCard() != null);

        if (structureCardNullCheck) {
            boolean inSacredHouseOfGod = (from.getActiveStructureCard().getName().equals( (N_HOUSE_OF_GOD) ));

            return !inSacredHouseOfGod;
        }
        return true;
    }

    @Override
    public List<Position> getPositions() {
        return structuralPositions;
    }
}