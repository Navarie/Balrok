package variants.gamma;

import common.GameImpl;
import common.UnitImpl;
import constants.GameConstants;
import framework.*;
import strategies.BoardLayoutStrategy;
import utilities.BoardUtility;

import java.util.*;

import static constants.GameConstants.*;
import static constants.UnitConstants.*;
import static utilities.BoardUtility.generateLeftMostSpawnPosition;
import static utilities.BoardUtility.generateSpawnPosition;

public class GammaBoardStrategy implements BoardLayoutStrategy {

    private Map<Position, FieldType> fieldTypes;
    private final Map<Position, Unit> units;

    private final List<Unit> deadWhiteUnits;
    private final List<Unit> deadBlackUnits;
    private final List<Unit> bannedWhiteUnits;
    private final List<Unit> bannedBlackUnits;
    private final Map<Position, List<Unit>> whiteCemetery;
    private final Map<Position, List<Unit>> blackCemetery;
    private final List<Unit> whiteKeep;
    private final List<Unit> blackKeep;
    private final Map<Position, Card> setWhiteTrapCards;
    private final Map<Position, Card> setBlackTrapCards;

    public GammaBoardStrategy() {
        units = new HashMap<>();

        deadWhiteUnits = new ArrayList<>();
        deadBlackUnits = new ArrayList<>();
        bannedWhiteUnits = new ArrayList<>();
        bannedBlackUnits = new ArrayList<>();
        whiteCemetery = new HashMap<>();
        blackCemetery = new HashMap<>();
        whiteKeep = new ArrayList<>();
        blackKeep = new ArrayList<>();
        setWhiteTrapCards = new HashMap<>();
        setBlackTrapCards = new HashMap<>();

        initialiseBoard();
        initialiseUnitFormation();
    }

    private void initialiseUnitFormation() {
        setUnitAt(new Position(11, 9), new UnitImpl(WHITE_PLAYER, NEUTRAL, DEMON, NAWOORI));
        setUnitAt(new Position(10, 9), new UnitImpl(WHITE_PLAYER, MONSTER, HUMAN, NAWOORI));
        setUnitAt(new Position(4, 7), new UnitImpl(WHITE_PLAYER, NEUTRAL, HUMAN, NAWOORI));
        setUnitAt(new Position(0, 7), new UnitImpl(WHITE_PLAYER, NEUTRAL, HUMAN, NAWOORI));
        setUnitAt(new Position(3, 8), new UnitImpl(WHITE_PLAYER, NEUTRAL, HUMAN, NAWOORI));
        setUnitAt(new Position(1, 9), new UnitImpl(WHITE_PLAYER, NEUTRAL, HUMAN, NAWOORI));
        setUnitAt(new Position(1, 3), new UnitImpl(WHITE_PLAYER, NEUTRAL, HUMAN, NAWOORI));
        setUnitAt(new Position(1, 5), new UnitImpl(WHITE_PLAYER, NEUTRAL, HUMAN, NAWOORI));
        setUnitAt(new Position(1, 7), new UnitImpl(WHITE_PLAYER, NEUTRAL, HUMAN, NAWOORI));
        setUnitAt(new Position(6, 2), new UnitImpl(WHITE_PLAYER, NEUTRAL, HUMAN, NAWOORI));
        setUnitAt(new Position(7, 3), new UnitImpl(BLACK_PLAYER, NEUTRAL, HUMAN, NAWOORI));
        setUnitAt(new Position(8, 4), new UnitImpl(WHITE_PLAYER, NEUTRAL, HUMAN, NAWOORI));
        setUnitAt(new Position(9, 2), new UnitImpl(BLACK_PLAYER, NEUTRAL, HUMAN, NAWOORI));
        setUnitAt(new Position(6, 5), new UnitImpl(WHITE_PLAYER, NEUTRAL, HUMAN, NAWOORI));
        setUnitAt(new Position(9, 5), new UnitImpl(BLACK_PLAYER, NEUTRAL, HUMAN, NAWOORI));
    }

    @Override
    public Map<Position, FieldType> getFieldTypes() {
        return fieldTypes;
    }
    @Override
    public Map<Position, Unit> getUnits() {
        return units;
    }
    @Override
    public Map<Position, List<Unit>> getCemetery(Position p) {
        if (p.equals( WHITE_CEMETERY_POS) ) {
            return whiteCemetery;
        } else {
            return blackCemetery; }
    }
    @Override
    public List<Unit> getKeep(Player owner) {
        if (owner.equals( WHITE_PLAYER )) {
            return whiteKeep;
        } else {
            return blackKeep;
        }
    }
    @Override
    public Card getTrapCardAt(Position p) {
        if (p.getRow() == 0 || p.getRow() == 1) {
            return setWhiteTrapCards.get(p);
        } else if (p.getRow() == 6 || p.getRow() == 5) {
            return setBlackTrapCards.get(p); }

        return null;
    }

    @Override
    public void sendToCemetery(Player owner, Unit corpse) {
        if (owner.equals( WHITE_PLAYER)) {
            deadWhiteUnits.add(corpse);
            whiteCemetery.put( WHITE_CEMETERY_POS, deadWhiteUnits);
        } else {
            deadBlackUnits.add(corpse);
            blackCemetery.put( EPSILON_BLACK_CEMETERY_POS, deadBlackUnits);
        }
    }

    @Override
    public void setTrapCard(Card c, Player owner, int turn) {
        int whiteTrapCardsNo = setWhiteTrapCards.size();
        int blackTrapCardsNo = setBlackTrapCards.size();

        if (owner.equals( WHITE_PLAYER)) {
            setWhiteTrapCards.put(new Position(0, 8 - whiteTrapCardsNo), c);
        } else {
            setBlackTrapCards.put(new Position(6, blackTrapCardsNo), c); }

        c.registerTurnPlayed(turn);
    }

    @Override
    public void removeFromGame(Card c, Player owner) {
        if (owner.equals( WHITE_PLAYER)) {
            setWhiteTrapCards.values().remove(c);
        } else {
            setBlackTrapCards.values().remove(c); }
    }

    @Override
    public boolean validateMoveDirection(Unit unit, Position from, Position to) {
        boolean isKanyeEast = (unit.getName().equals( N_KANYE_EAST ));
        boolean isMovingLeft = ((from.getRow() - to.getRow()) == 1);
        if (isKanyeEast && isMovingLeft) {
            return false;
        }

        return true;
    }

    @Override
    public void validateSpecialEffectsFor(Unit unit, Position to, Game game) {
        boolean isMrGarrison = (unit.getName().equals( N_MR_GARRISON ));
        if (isMrGarrison) {

            Position upperLeftCorner = (new Position( (to.getRow() - 2), (to.getColumn() - 2) ));
            Iterator<Position> banPositions = BoardUtility.getRectangularGrid(5, 5, upperLeftCorner);
            while (banPositions.hasNext()) {
                Position p = banPositions.next();
                Unit unitAtTo = game.getUnitAt(p);

                if (unitAtTo != null) {
                    boolean isStudent = ((unitAtTo.getAffiliation().equals( STATE_SCHOOL ))
                                      || (unitAtTo.getAffiliation().equals( ALSSUND )));
                    if (isStudent) {
                        unitAtTo.debuffUnit("BAN", 6);
                    }
                }
            }
        }
    }


    public void setUnitAt(Position p, Unit unit) {
        units.put(p, unit);
    }
    @Override
    public void setUnit(Unit unit, Position position, Game game) {

        boolean isKanyeEast = (unit.getName().equals( N_KANYE_EAST ));
        if (isKanyeEast) {
            Position spawnPosition = generateLeftMostSpawnPosition(unit, game);
            units.put(spawnPosition, unit);
            return;
        }

        if (position != null) {
            units.put(position, unit);
        } else {
            Position spawnPosition = generateSpawnPosition(unit, game);
            units.put(spawnPosition, unit);
        }
    }

    @Override
    public void sendToPrisonKeep(Unit unit, Player owner) {
        if (owner.equals( WHITE_PLAYER)) {
            bannedWhiteUnits.add(unit);
            whiteKeep.add(unit);
        } else {
            bannedBlackUnits.add(unit);
            blackKeep.add(unit);
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private void initialiseBoard() {
        String[] boardLayout = new String[] {
                "Koooooooooooo",
                "Xoooooooooooo",
                "ooooooooooooo",
                "ooooooooooooo",
                "ooooooooooooo",
                "ooooooooooooo",
                "ooooooooooooo",
                "ooooooooooooo",
                "ooooooooooooo",
                "ooooooooooooX",
                "ooooooooooooK"
        };

        fieldTypes = BoardUtility.createInitialBoard(boardLayout, "GAMMA");
    }
}