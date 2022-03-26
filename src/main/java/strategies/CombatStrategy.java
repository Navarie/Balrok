package strategies;

import common.GameImpl;
import framework.*;

public interface CombatStrategy {

    void damageOpponent(Card activeField, Unit attacker, Unit defender);
    void activateBloodline(Player player);

    boolean validateAttackRange(Unit attacker, Position from, Position to);
    boolean validateAttacksNo(Unit attacker);
    boolean validateEnsnare(Unit target);
    boolean validateSuppression(Unit target);
    void validateSpecialEffects(Unit attacker, Unit defender);
    void validateKillingSpree(Unit attacker, Position to, Game game);
}
