package common;

import constants.CardConstants;
import constants.GameConstants;
import framework.*;
import strategies.*;
import utilities.GameFactory;

import java.util.*;

import static constants.GameConstants.BLACK_PLAYER;

public class GameImpl implements Game {

    private static final int STARTING_YEAR = 1415;
    private static final int BEFORE_GAME = 1;
    private final Player STARTING_PLAYER = BLACK_PLAYER;

    private Player playerInTurn;
    private int currentYear;
    private int ageCounter;
    private int roundCounter;

    private final List<GameObserver> gameObservers;

    // === STRATEGIES ========================
    // === VARIABILITY POINTS ================
    private BoardLayoutStrategy boardLayoutStrategy;
    private PlayerStatStrategy playerStatStrategy;
    private FieldValidatingStrategy fieldValidatingStrategy;
    private CombatStrategy combatStrategy;
    private CardManagementStrategy cardManagementStrategy;
    private MagicEffectStrategy magicEffectStrategy;
    private DemonicEffectStrategy demonicEffectStrategy;
    private TrapEffectStrategy trapEffectStrategy;
    private UnitStatStrategy unitStatStrategy;
    private StructureStrategy structureStrategy;

    public GameImpl(GameFactory factory) {
        boardLayoutStrategy = factory.getBoardLayoutStrategy();
        playerStatStrategy = factory.getPlayerStatStrategy();
        fieldValidatingStrategy = factory.getFieldValidatingStrategy();
        combatStrategy = factory.getCombatStrategy();
        cardManagementStrategy = factory.getCardManagementStrategy();
        magicEffectStrategy = factory.getMagicEffectStrategy();
        demonicEffectStrategy = factory.getDemonicEffectStrategy();
        trapEffectStrategy = factory.getTrapEffectStrategy();
        unitStatStrategy = factory.getUnitStatStrategy();
        structureStrategy = factory.getStructureStrategy();

        gameObservers = new ArrayList<>();

        startGame();
    }

    public FieldType getFieldTypeAt(Position p) { return boardLayoutStrategy.getFieldTypes().get(p); }
    public Unit getUnitAt(Position p) { return boardLayoutStrategy.getUnits().get(p); }
    public Map<Position, Unit> getUnits() {
        return boardLayoutStrategy.getUnits();
    }
    public Player getPlayerInTurn() { return playerInTurn; }
    public int getRoundCounter() {return roundCounter;}
    public Map<Position, List<Unit>> getCemetery(Position p) { return boardLayoutStrategy.getCemetery(p); }
    public List<Unit> getDeadUnitsAtCemetery(Position p) { return boardLayoutStrategy.getCemetery(p).get(p); }
    public List<Player> getPlayers() { return playerStatStrategy.getPlayers(); }
    public Card getCard(String name) { return cardManagementStrategy.getCard(name); }
    public List<Card> getDeck(Player owner) { return cardManagementStrategy.getDeckList(owner); }
    public List<Card> getHand(Player owner) { return cardManagementStrategy.getHand(owner); }
    public int getCurrentYear() { return currentYear; }
    public List<Player> getOpponents() {
        List<Player> allPlayers = getPlayers();
        List<Player> opponents = new ArrayList<>();
        for (Player p : allPlayers) {
            if (!p.equals(playerInTurn)) {
                opponents.add(p); }
        }
        return opponents;
    }

    public Card getTrapCardAt(Position p) {
        return boardLayoutStrategy.getTrapCardAt(p);
    }
    public void addCardToHand(Card c, Player owner) {
        cardManagementStrategy.addCardToHand(c, owner);
    }

    @Override
    public void drawCardFor(Player p) {
        cardManagementStrategy.drawCardFor(p);
    }

    @Override
    public void removeFromHand(Card c, Player player) {
        cardManagementStrategy.removeFromHand(c, playerInTurn);
    }

    @Override
    public void sendToCemetery(Unit unit, Player owner) {
        boardLayoutStrategy.sendToCemetery(owner, unit);
        boardLayoutStrategy.getUnits().values().remove(unit);
    }

    @Override
    public void sendToPrisonKeep(Unit unit, Player owner) {
        boardLayoutStrategy.sendToPrisonKeep(unit, owner);
        boardLayoutStrategy.getUnits().values().remove(unit);
    }

    @Override
    public void startGame() {
        playerStatStrategy.initialisePlayers();
        cardManagementStrategy.initialiseDecks();
        cardManagementStrategy.drawStartingHands();

        playerInTurn = STARTING_PLAYER;
        currentYear = STARTING_YEAR;
        roundCounter = BEFORE_GAME;
        roundCounter++;
    }

    @Override
    public boolean playCard(Card c, Unit target, Position to) {

        boolean hasCardInHand = cardManagementStrategy.validateCard(c, getPlayerInTurn());
        if (!hasCardInHand) { return false; }

        String cardType = c.getType();
        switch (cardType) {
            case (CardConstants.UNIT_CARD) -> {
                if (target != null) {
                    boolean isValidSacrifice = cardManagementStrategy.validateSacrifice(c, target);
                    if (!isValidSacrifice) return false; }

                Unit unit = unitStatStrategy.initialiseUnitFrom(c, this);
                boardLayoutStrategy.setUnit(unit, to, this);
            }
            case (CardConstants.FIELD_CARD) -> {
                playerStatStrategy.setManaFor(getPlayerInTurn(), CardConstants.DELTA_FIELD_CARD_COST);
                magicEffectStrategy.playFieldCard(c, this);
            }
            case (CardConstants.MAGIC_CARD) -> {
                playerStatStrategy.setManaFor(getPlayerInTurn(), c.getCost());
                if (target != null) {
                    magicEffectStrategy.playTargetedMagicCard(c, target, this);
                }
                magicEffectStrategy.playMagicCard(c, this);
            }     // Fallthrough
            case (CardConstants.DEMONIC_TRAP_CARD), (CardConstants.TRAP_CARD) -> boardLayoutStrategy.setTrapCard(c, getPlayerInTurn(), getRoundCounter());
            case (CardConstants.DEMONIC_CARD) -> demonicEffectStrategy.demonicDisplacement(this, to, c);
            case (CardConstants.STRUCTURE_CARD) -> structureStrategy.playStructureCard(c, to, this);
            default -> {
                System.out.println("Invalid card type. Please contact customer support.");
                return false; }
        }
        cardManagementStrategy.removeFromHand(c, getPlayerInTurn());
        gameObservers.forEach(gameObserver -> gameObserver.removeCardFromView(c));

        return true;
    }

    @Override
    public boolean activateCard(Card c, Unit target, Position to, Player player) {
        String cardType = c.getType();

        switch (cardType) {
            case (CardConstants.UNIT_CARD):
            case (CardConstants.MAGIC_CARD):
                break;
            case (CardConstants.DEMONIC_TRAP_CARD):
                demonicEffectStrategy.demonicDisplacement(this, to, c);
                break;
            case (CardConstants.TRAP_CARD):
                boolean trapIsActivatable = (c.getTurnPlayed() <= getRoundCounter() - 1);
                if (trapIsActivatable) {
                    if (to != null) {
                        trapEffectStrategy.activateTargetedTrap(this, c, to, player);
                    } else {
                        trapEffectStrategy.activateTrap(this, c, player); }

                    boardLayoutStrategy.removeFromGame(c, getPlayerInTurn()); }
                break;
            case (CardConstants.DEMONIC_CARD):
        }

        return true;
    }

    @Override
    public boolean attackOpponent(Position from, Position to) {
        Unit attacker = getUnitAt(from);
        Unit defender = getUnitAt(to);

        boolean unitsNullCheck = (attacker != null & defender != null);
        if (!unitsNullCheck) return false;

        boolean isInRange = (combatStrategy.validateAttackRange( attacker, from, to ));
        boolean hasRemainingAttacks = (combatStrategy.validateAttacksNo( attacker ));
        boolean isSuppressed = (combatStrategy.validateSuppression( attacker ));

        if (!isInRange || !hasRemainingAttacks) return false;
        if (isSuppressed) return false;
        for (Position p : structureStrategy.getPositions()) {
            if (structureStrategy.getPositions().contains(from)) {
                return structureStrategy.validateAttack(p); }
        }

        Card activeField = magicEffectStrategy.getActiveFieldCard();
        combatStrategy.damageOpponent(activeField, attacker, defender);
        combatStrategy.validateSpecialEffects(attacker, defender);

        boolean fatalBlow = (defender.getHealth() == 0);
        if (fatalBlow) {
            ((UnitImpl) attacker).refreshMovementSpeed();
            sendToCemetery(defender, defender.getOwner() );
            combatStrategy.activateBloodline(defender.getOwner());
            combatStrategy.validateKillingSpree(attacker, to, this); }

        attacker.setHasAttacked(true);
        return true;
    }

    @Override
    public boolean moveUnit(Position from, Position to) {
        if (!isLegalMove(from, to)) return false;

        placeUnit(from, to);
        getUnitAt(to).setHasMoved(true);

        return true;
    }

    public void addObserver(GameObserver newObserver) { gameObservers.add(newObserver); }
    public void setFieldInspectionAt(Position p) {
        gameObservers.forEach(gameObserver -> gameObserver.fieldInspectedAt(p));
    }
    public void setInspectionOn(Card card) {
        gameObservers.forEach(gameObserver -> gameObserver.cardInspectedAt(card));
    }

    private void placeUnit(Position from, Position to) {
        Unit unit = getUnitAt(from);

        removeUnitAt(from);
        boardLayoutStrategy.getUnits().put(to, unit);

        fieldValidatingStrategy.validateMovementSpeedFor(this, unit, to);
        boardLayoutStrategy.validateSpecialEffectsFor(unit, to, this);
    }

    public void removeUnitAt(Position unitFrom) {
        boardLayoutStrategy.getUnits().remove(unitFrom);
    }

    private boolean isLegalMove(Position from, Position to) {
        Unit unit = getUnitAt(from);

            // Guards against GUI asynchronicity
        boolean isUnitAtFrom = (unit != null);
        if (!isUnitAtFrom) return false;

        boolean isOwnerOfUnit = (getPlayerInTurn().equals( unit.getOwner() ));
        boolean unitIsFatigued = (0 == unit.getCurrentMovementSpeed());
        boolean isEnsnared = ( combatStrategy.validateEnsnare(unit) );
        boolean isSuppressed = ( combatStrategy.validateSuppression(unit) );
        boolean isValidMoveDirection = ( boardLayoutStrategy.validateMoveDirection(unit, from, to) );
        boolean isValidDistance = (Position.distance(from, to) == 1);

        if (!isOwnerOfUnit) return false;
        if (unitIsFatigued) return false;
        if (isEnsnared) return false;
        if (isSuppressed) return false;
        if (!isValidMoveDirection) return false;
        if (!isLegalFieldType(to, unit)) return false;
        if (getUnitAt(to) != null) {
            boolean isOwnUnit = (getUnitAt(to).getOwner().equals( unit.getOwner() ));
            return !isOwnUnit;
        }
        return isValidDistance;
    }

    private boolean isLegalFieldType(Position to, Unit unit) {
        return fieldValidatingStrategy.isLegalFieldType(this, to, unit); }

    @Override
    public void endTurn() {
        if (getPlayerInTurn().equals( BLACK_PLAYER )) {
            playerInTurn = GameConstants.WHITE_PLAYER;
        } else if (getPlayerInTurn().equals( GameConstants.WHITE_PLAYER )) {
            playerInTurn = BLACK_PLAYER;
            endOfRoundEffects();
        }

        endOfTurnEffects();
    }

    @Override
    public void addCardToHand(Card c) {
        cardManagementStrategy.addCardToHand(c, getPlayerInTurn());
    }

    private void endOfTurnEffects() {
        ageWorld();
        tickDebuffs();
        tickBuffs();
        validateDebuffTriggers();

        boolean fromRound2Onwards = (getRoundCounter() > getPlayers().size());
        if (fromRound2Onwards) {
            drawCardFor(playerInTurn);
        }

        List<Position> structuralPositions = structureStrategy.getPositions();
        for (Position p : structuralPositions) {
            structureStrategy.validateStructureTurnEffects(p, this);
        }

        regenerateHealthToPlayer();
        regenerateHealthToUnits();
        resetBooleanValues();
        playerStatStrategy.validatePlayerDominance(this);

        gameObservers.forEach(gameObserver -> gameObserver.turnEnds(playerInTurn, 1445));
    }

    private void resetBooleanValues() {
        boardLayoutStrategy.getUnits().values().stream()
                .filter(unit -> unit.getOwner().equals( playerInTurn ))
                .forEach( (unit) -> {
                    unit.setHasMoved(false);
                    unit.setHasAttacked(false); } );
    }

    private void validateDebuffTriggers() {
        boardLayoutStrategy.getUnits().forEach( (position, unit) -> unitStatStrategy.validateDebuffTriggers(unit) );
    }

    private void regenerateHealthToUnits() {
        Map<Position, Unit> units = boardLayoutStrategy.getUnits();
        units.values().stream()
                  .filter(unit -> unit.getOwner().equals( playerInTurn ))
                  .filter(unit -> unit.getBuffs().get(GameConstants.Buff.SECOND_WIND) != null)
                  .filter(unit -> unit.getBuffs().get(GameConstants.Buff.SECOND_WIND).get(0) > 0)
                  .forEach(Unit::secondWind);

        units.values().stream()
                  .filter(unit -> unit.getOwner().equals( playerInTurn ))
                  .forEach(Unit::regenerateHealth);
    }

    private void regenerateHealthToPlayer() {
        if (getRoundCounter() % 6 == 0 || getRoundCounter() % 6 == 5) {
            playerStatStrategy.restoreHealthTo(getPlayerInTurn()); }
    }

    private void tickBuffs() {
        boardLayoutStrategy.getUnits().forEach( (position, unit) -> unit.tickBuffs() );
    }

    private void endOfRoundEffects() {
        roundCounter++;

        refreshUnitMovementSpeed();
        refreshUnitFerocity();
    }

    private void tickDebuffs() {
        boardLayoutStrategy.getUnits().forEach( (position, unit) -> unit.tickDebuffs() );
        playerStatStrategy.getPlayers().forEach( (player) -> boardLayoutStrategy.getKeep(player)
                                       .forEach( (unit) -> {
                                           unit.tickDebuffs();
                                           unitStatStrategy.validateUnban(unit, this); } ));
    }

    private void refreshUnitFerocity() {
        boardLayoutStrategy.getUnits().forEach( (position, unit) -> unitStatStrategy.validateFerocity(unit) ); }

    private void refreshUnitMovementSpeed() {
        boardLayoutStrategy.getUnits().forEach((position, unit) -> ((UnitImpl) unit).refreshMovementSpeed()); }

    private void ageWorld() {
        currentYear = currentYear + 10;
    }

    public void setBoardLayoutStrategy(BoardLayoutStrategy strategy) { boardLayoutStrategy = strategy; }
    public void setPlayerStatStrategy(PlayerStatStrategy strategy) { playerStatStrategy = strategy; }
    public void setFieldValidatingStrategy(FieldValidatingStrategy strategy) { fieldValidatingStrategy = strategy; }
    public void setCombatStrategy(CombatStrategy strategy) { combatStrategy = strategy; }
    public void setCardManagementStrategy(CardManagementStrategy strategy) { cardManagementStrategy = strategy; }
    public void setMagicEffectStrategy(MagicEffectStrategy strategy) { magicEffectStrategy = strategy; }
    public void setDemonicEffectStrategy(DemonicEffectStrategy strategy) { demonicEffectStrategy = strategy; }
    public void setTrapEffectStrategy(TrapEffectStrategy strategy) { trapEffectStrategy = strategy; }
    public void setUnitStatStrategy(UnitStatStrategy strategy) { unitStatStrategy = strategy; }
    public void setStructureStrategy(StructureStrategy strategy) { structureStrategy = strategy; }
}