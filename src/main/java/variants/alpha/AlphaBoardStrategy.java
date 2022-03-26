package variants.alpha;

import common.GameImpl;
import constants.GameConstants;
import strategies.BoardLayoutStrategy;
import common.UnitImpl;
import framework.*;
import utilities.BoardUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static constants.GameConstants.*;
import static constants.UnitConstants.NAWOORI;
import static utilities.BoardUtility.generateSpawnPosition;

public class AlphaBoardStrategy implements BoardLayoutStrategy {

    private Map<Position, FieldType> fieldTypes;
    private final Map<Position, Unit> units;

    private final List<Unit> deadWhiteUnits;
    private final List<Unit> deadBlackUnits;
    private final Map<Position, List<Unit>> whiteCemetery;
    private final Map<Position, List<Unit>> blackCemetery;
    private final List<Unit> whiteKeep;
    private final List<Unit> blackKeep;
    private final Map<Position, Card> setWhiteTrapCards;
    private final Map<Position, Card> setBlackTrapCards;

    public AlphaBoardStrategy() {
        units = new HashMap<>();

        deadWhiteUnits = new ArrayList<>();
        deadBlackUnits = new ArrayList<>();
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
        setUnitAt(new Position(3, 2), new UnitImpl(BLACK_PLAYER, NEUTRAL, DWARF, NAWOORI));
        setUnitAt(new Position(3, 3), new UnitImpl(WHITE_PLAYER, NEUTRAL, ELF, NAWOORI));
        setUnitAt(new Position(3, 4), new UnitImpl(BLACK_PLAYER, NEUTRAL, ORC, NAWOORI));
        setUnitAt(new Position(3, 5), new UnitImpl(WHITE_PLAYER, NEUTRAL, HUMAN, NAWOORI));
        setUnitAt(new Position(3, 6), new UnitImpl(WHITE_PLAYER, NEUTRAL, HUMAN, NAWOORI));
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
        if (p.equals( GameConstants.WHITE_CEMETERY_POS) ) {
            return whiteCemetery;
        } else {
            return blackCemetery;
        }
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
            return setBlackTrapCards.get(p);
        }

        return null;
    }

    @Override
    public void sendToCemetery(Player owner, Unit corpse) {
        if (owner.equals( GameConstants.WHITE_PLAYER)) {
            deadWhiteUnits.add(corpse);
            whiteCemetery.put( GameConstants.WHITE_CEMETERY_POS, deadWhiteUnits);
        } else {
            deadBlackUnits.add(corpse);
            blackCemetery.put( GameConstants.BLACK_CEMETERY_POS, deadBlackUnits);
        }
    }

    @Override
    public void setTrapCard(Card c, Player owner, int turn) {
        int whiteTrapCardsNo = setWhiteTrapCards.size();
        int blackTrapCardsNo = setBlackTrapCards.size();

        if (owner.equals( GameConstants.WHITE_PLAYER)) {
            setWhiteTrapCards.put(new Position(0, 8 - whiteTrapCardsNo), c);
        } else {
            setBlackTrapCards.put(new Position(6, 0 + blackTrapCardsNo), c);
        }

        c.registerTurnPlayed(turn);
    }

    @Override
    public void setUnit(Unit unit, Position position, Game game) {
        if (position != null) {
            units.put(position, unit);
        } else {
            Position spawnPosition = generateSpawnPosition(unit, game);
            units.put(spawnPosition, unit);
        }
    }

    @Override
    public void sendToPrisonKeep(Unit unit, Player owner) {

    }

    @Override
    public void removeFromGame(Card c, Player owner) {
        if (owner.equals( GameConstants.WHITE_PLAYER)) {
            setWhiteTrapCards.values().remove(c);
        } else {
            setBlackTrapCards.values().remove(c);
        }
    }

    @Override
    public boolean validateMoveDirection(Unit unit, Position from, Position to) {
        return true;
    }

    @Override
    public void validateSpecialEffectsFor(Unit unit, Position to, Game game) {

    }

    public void setUnitAt(Position p, Unit unit) {
        units.put(p, unit);
    }

    private void initialiseBoard() {
        String[] boardLayout = new String[] {
                "Koooooooo",
                "Xoooooooo",
                "ooooooSM.",
                "o_ooooooo",
                "ooMoooooX",
                "ooooooooK"
        };

        fieldTypes = BoardUtility.createInitialBoard(boardLayout, "ALPHA");
    }
}
