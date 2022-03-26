package variants.beta;

import framework.*;
import strategies.MagicEffectStrategy;
import utilities.BoardUtility;

import java.util.*;

import static constants.CardConstants.*;
import static constants.GameConstants.*;
import static constants.SpellConstants.*;
import static constants.UnitConstants.N_MATHIAS;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")

public class BetaMagicEffectStrategy implements MagicEffectStrategy {

    private Card activeFieldCard = FREDERIK;

    @Override
    public Card getActiveFieldCard() {
        return activeFieldCard;
    }

    @Override
    public void playMagicCard(Card c, Game game) {
        Player playerInTurn = game.getPlayerInTurn();
        String cardName = c.getName();
        switch (cardName) {
            case (N_POT_OF_GREED) -> {
                for (int i=0; i < 3; i++) {
                    game.drawCardFor(playerInTurn);
                }
                game.removeFromHand(c, playerInTurn);
                game.endTurn(); }
            case (N_WAFFLEBOAT) -> {
                game.getUnits().values()
                        .forEach(unit -> unit.buffUnitTemporarily("RGN", 9, 20)); }
            case (N_LIENACK) -> {
                game.getUnits().values()
                        .forEach(unit -> unit.buffUnitTemporarily("FER", 9, 1)); }
            case (N_TIT_FOR_TAT) -> {
                game.drawCardFor(playerInTurn);
                game.drawCardFor(playerInTurn);
                for (Player p : game.getOpponents()) {
                    game.drawCardFor(p); }
            }
            case (N_SAVE_THE_ENVIRONMENT) -> {
                int handSize = game.getHand(playerInTurn).size();

                game.getHand(playerInTurn).clear();
                for (int i=0; i < handSize; i++) {
                    game.drawCardFor(playerInTurn); }
                for (Player p : game.getOpponents()) {
                    p.setHealth( p.getCurrentHealth() + 30 ); }
            }
            case (N_HOWLING_ABYSS) -> {
                int numberOfUnits = game.getUnits().size();
                Iterator<Unit> unitsIterator = game.getUnits().values().iterator();
                List<Unit> randomisedUnits = new ArrayList<>();
                while (unitsIterator.hasNext()) {
                    Unit u = unitsIterator.next();
                    randomisedUnits.add(u);
                }

                game.getUnits().clear();
                Iterator<Position> randomPositions = BoardUtility.getRandomSpawnPositions(numberOfUnits);
                while (randomPositions.hasNext()) {
                    Position p = randomPositions.next();

                    for (int i=0; i < numberOfUnits; i++) {
                        game.getUnits().put(p, randomisedUnits.get(i) );
                    }
                }
            }
            default -> System.out.println("Invalid magic card name. Check database.");
        }
    }

    @Override
    public void playTargetedMagicCard(Card c, Unit target, Game game) {
        String cardName = c.getName();
        switch (cardName) {
            case (N_VIVI) -> target.hastenUnit();
            case (N_KARDEY) -> target.controlUnit("ROOT", 6);
            case (N_FOX_CHARM) -> target.controlUnit("SUPP", 8);
            case (N_SUSPENSION) -> {
                target.setAffiliation(NO_AFFILIATION);
                target.controlUnit("SUPP", 6); }
            case (N_REPORT) -> {
                target.controlUnit("BAN", 10);
                game.sendToPrisonKeep(target, target.getOwner() );
            }
            case (N_RITO_CLIENT) -> {
                target.takeDamage(FATAL);
                game.sendToCemetery(target, target.getOwner() );
            }
            case (N_SHOTGUN) -> {
                boolean combinedStatsUnder200 = ((target.getStrength() + target.getAbilityPower() + target.getHealth()) < 200);
                if (combinedStatsUnder200) {
                    target.takeDamage(FATAL);
                    game.sendToCemetery(target, target.getOwner() );
                }
            }
            case (N_AVERAGE_SWORD) -> target.buffUnit("STR", 80);
            default -> System.out.println("Invalid targeted magic card. Check database.");
        }
    }

    private boolean isFemaleCharacter(Unit unit) {
        return (unit.getUnitGender().equals( FEMALE ));
    }

    @Override
    public void playFieldCard(Card c, Game game) {
        deactivateCurrentFieldCard(game);
        activateFieldCard(c, game);
    }

    private void deactivateCurrentFieldCard(Game game) {
        String activeFieldCard = this.activeFieldCard.getName();
        switch (activeFieldCard) {
            case (N_BATTLEGROUNDS) -> {
                game.getUnits().values().stream()
                        .filter(unit -> !isRangedCharacter(unit))
                        .forEach(unit -> unit.buffUnit("STR", -10)); }
            case (N_MAGISTRATE) -> {
                game.getUnits().values().stream()
                        .filter(this::isCasterCharacter)
                        .forEach(unit -> unit.buffUnit("AP", -10)); }
            case (N_STONEBURG) -> {}
            case (N_STATE_SCHOOL) -> {
                game.getUnits().values().stream()
                        .filter(this::belongsToStateSchool)
                        .forEach(unit -> {
                            unit.buffUnit("STR", -30);
                            unit.buffUnit("AP", -20); }); }
            case (N_ALSSUND) -> {
                game.getUnits().values().stream()
                        .filter(this::belongsToAlssund)
                        .forEach(unit -> {
                            unit.buffUnit("STR", -20);
                            unit.buffUnit("AP", -30); }); }
        }
    }

    private void activateFieldCard(Card newCard, Game game) {
        String cardName = newCard.getName();
        switch (cardName) {
            case (N_BATTLEGROUNDS) -> {
                game.getUnits().values().stream()
                        .filter(unit -> !isRangedCharacter(unit))
                        .forEach(unit -> unit.buffUnit("STR", 10)); }
            case (N_MAGISTRATE) -> {
                game.getUnits().values().stream()
                        .filter(this::isCasterCharacter)
                        .forEach(unit -> unit.buffUnit("AP", 10)); }
            case (N_STONEBURG) -> {}
                    // Calculation in combat strategy.
            case (N_STATE_SCHOOL) -> {
                game.getUnits().values().stream()
                        .filter(unit -> unit.getName() != null)
                        .filter(this::belongsToStateSchool)
                        .forEach(unit -> {
                            unit.buffUnit("STR", 30);
                            unit.buffUnit("AP", 20);
                        }); }
            case (N_ALSSUND) -> {
                game.getUnits().values().stream()
                        .filter(unit -> unit.getName() != null)
                        .filter(this::belongsToAlssund)
                        .forEach(unit -> {
                            unit.buffUnit("STR", 20);
                            unit.buffUnit("AP", 30);
                        });
            }
            case (N_FIFTY_SHADES) -> {
                game.getUnits().values().stream()
                        .filter(this::isFemaleCharacter)
                        .forEach(unit -> {
                            unit.buffUnit("STR", 40);
                            unit.buffUnit("AP", 40);
                        });
            }
            default -> System.out.println("Invalid field card name. Contact the developers.");
        }

        this.activeFieldCard = newCard;
    }

    private boolean belongsToStateSchool(Unit unit) {
        boolean unitNullCheck = (unit != null);
        if (unitNullCheck) {
            boolean affiliatedWithStateSchool = (unit.getAffiliation().equals( STATE_SCHOOL ));
            boolean isMathias = (unit.getName().equals ( N_MATHIAS ));
            return affiliatedWithStateSchool && !isMathias;
        }

        return false;
    }

    private boolean belongsToAlssund(Unit unit) {
        return (unit.getAffiliation().equals( ALSSUND ));
    }

    private boolean isCasterCharacter(Unit unit) {
        boolean isSorcerer = (unit.getAffinity().equals( SORCERER ));
        boolean isLeshy = (unit.getUnitRace().equals( LESHY ));
        boolean isChernobog = (unit.getUnitRace().equals( CHERNOBOG ));

        return (isSorcerer || isLeshy || isChernobog);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isRangedCharacter(Unit unit) {
        boolean isHunter = (unit.getAffinity().equals( HUNTER ));
        boolean isDragon = (unit.getUnitRace().equals( PRIMORDIAL_DRAGON ));
        boolean isSorcerer = (unit.getAffinity().equals( SORCERER ));

        return (isHunter || isDragon || isSorcerer);
    }
}