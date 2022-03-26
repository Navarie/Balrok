package variants.delta;

import framework.*;
import strategies.TrapEffectStrategy;

import static constants.CardConstants.*;

public class DeltaTrapEffectStrategy implements TrapEffectStrategy {

    @Override
    public void activateTargetedTrap(Game game, Card c, Position target, Player player) {
        String cardName = c.getName();
        Unit targetUnit = game.getUnitAt(target);
        if (targetUnit == null) return;

        switch (cardName) {
            case (N_JONAS) -> targetUnit.controlUnit("ROOT", 5);
            case (N_RICKE) -> {
                targetUnit.controlUnit("SUPP", 4);
                targetUnit.buffUnit("STR", - targetUnit.getStrength());
                targetUnit.buffUnit("AP", - targetUnit.getAbilityPower());
                targetUnit.buffUnit("HP", - (targetUnit.getHealth() - 1) );
            }
            case (N_SWITCHEROO) -> {
                if (isFriendly(targetUnit, game)) {
                    targetUnit.cleanseUnit();
                } else {
                    targetUnit.setOwner(game.getPlayerInTurn());
                }
            }
            default -> System.out.println("Invalid trap card name. Check database.");
        }
    }

    @Override
    public void activateTrap(Game game, Card c, Player player) {
        String cardName = c.getName();

        switch (cardName) {
            case (N_RENE_DESCARTES) -> {
                int descartesDamage = ((10 + c.getTurnPlayed())
                        + (((100 - player.getCurrentHealth()) / 100) * 10));
                player.takeDamage(descartesDamage); }
            case (N_OBJECTION) -> {
                game.endTurn();
                game.getPlayerInTurn().takeDamage(50); }
            default -> System.out.println("Invalid targeted trap. Check database.");
        }
    }

    private boolean isFriendly(Unit targetUnit, Game game) {
        return (targetUnit.getOwner().equals( game.getPlayerInTurn() ));
    }
}
