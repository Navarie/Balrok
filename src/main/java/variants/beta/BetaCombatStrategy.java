package variants.beta;

import framework.*;
import strategies.CombatStrategy;
import common.UnitImpl;
import utilities.BoardUtility;

import java.util.Iterator;

import static common.PlayerImpl.DEFAULT_BLOODLINE;
import static constants.GameConstants.*;
import static constants.SpellConstants.*;
import static constants.UnitConstants.*;

public class BetaCombatStrategy implements CombatStrategy {

    @Override
    public void damageOpponent(Card activeField, Unit attacker, Unit defender) {
        boolean isStoneburgPark = (activeField.getName().equals( STONEBURG.getName() ));
        boolean isFemaleDefender = (defender.getUnitGender().equals( FEMALE ));
        boolean isEnemyGnome = (defender.getUnitRace().equals( GNOME ));
        boolean isEnemyDwarf = (defender.getUnitRace().equals( DWARF ));
        boolean isUnholyCreature = (defender.getUnitRace().equals( DEMON ) || defender.getUnitType().equals( MONSTER ));
        boolean isMonster = (attacker.getUnitType().equals( MONSTER ));

        if (isEnemyDwarf || isEnemyGnome) {
            defender.takeDamage(FATAL);
        } else if (isMonster) {
            defender.takeDamage(FATAL);
        } else {
            defender.takeDamage( attacker.getStrength() );
        }

        boolean attackerNameNullCheck = (attacker.getName() != null);
        if (attackerNameNullCheck) {
            boolean isIsaac = (attacker.getName().equals( N_ISAAC ));
            boolean isMaleAttacker = (attacker.getUnitGender().equals( MALE ));

            if (isStoneburgPark && isMaleAttacker && isFemaleDefender) {
                defender.takeDamage(attacker.getStrength() + 50);
            } else if (isIsaac && isUnholyCreature) {
                defender.takeDamage(Math.max(attacker.getStrength(), attacker.getAbilityPower()) + 100);
            }
        }
    }

    @Override
    public void activateBloodline(Player player) {
        player.takeDamage(DEFAULT_BLOODLINE);
    }

    @Override
    public boolean validateAttackRange(Unit attacker, Position from, Position to) {
        boolean isRangedCharacter =
                (attacker.getAffinity().equals( HUNTER )
                || attacker.getUnitRace().equals( PRIMORDIAL_DRAGON )
                || attacker.getAffinity().equals( SORCERER) );
        boolean isInShootingRange =
                (Position.distance(from, to) >= 1
                && Position.distance(from, to) <= 4);
        boolean isInMeleeRange = (Position.distance(from, to) == 1);

        if (isRangedCharacter && isInShootingRange) {
            return true;
        } else if (!isRangedCharacter && isInMeleeRange) {
            return true;
        }

        return false;
    }

    @Override
    public boolean validateAttacksNo(Unit attacker) {
        if (attacker.getMaxFerocity() > 0) {
            ((UnitImpl) attacker).setMaxFerocity( attacker.getMaxFerocity() - 1 );
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean validateEnsnare(Unit target) {
        boolean rootNullCheck = (target.getDebuffs().get(Debuff.ROOT) != null);
        if (rootNullCheck) {
            boolean isEnsnared = ( target.getDebuffs().get(Debuff.ROOT) > 0 );
            return isEnsnared; }

        String targetName = target.getName();
        boolean targetNameNullCheck = (targetName != null);
        if (targetNameNullCheck) {
            switch (targetName) {
                case (N_MATHIAS) -> target.getDebuffs().put(Debuff.ROOT, 6);
            }
        }

        return false;
    }

    @Override
    public boolean validateSuppression(Unit target) {
        boolean suppressNullCheck = (target.getDebuffs().get(Debuff.SUPPRESS) != null);
        if (suppressNullCheck) {
            boolean isSuppressed = ( target.getDebuffs().get(Debuff.SUPPRESS) > 0 );
            return isSuppressed;
        }

        return false;
    }

    @Override
    public void validateSpecialEffects(Unit attacker, Unit defender) {
        boolean defenderNameNullCheck = (defender.getName() != null);
        boolean attackerNameNullCheck = (attacker.getName() != null);

        if (defenderNameNullCheck && attackerNameNullCheck) {
            boolean isGermanKidFurious = (defender.getName().equals( N_GERMAN_KID ) && defender.getHealth() <= 60);
            boolean isBense = (attacker.getName().equals( N_BENSE ));

            if (isBense) {
                defender.controlUnit("SUPP", 3);
            } else if (isGermanKidFurious) {
                defender.buffUnit("STR", 10);
                defender.buffUnit("FER", 15);
            }
        }
    }

    @Override
    public void validateKillingSpree(Unit attacker, Position to, Game game) {
        boolean isTyrone = (attacker.getName().equals( N_TYRONE ));
        if (isTyrone) {
            Iterator<Position> collateralPositions = BoardUtility.get3x3GridWithoutCenter(to);
            while (collateralPositions.hasNext()) {
                Position p = collateralPositions.next();
                Unit unitAtTo = game.getUnitAt(p);

                if (unitAtTo != null) {
                    unitAtTo.takeDamage(FATAL);
                    game.sendToCemetery(unitAtTo, unitAtTo.getOwner() );
                }
            }
        }
    }
}
