package utilities;

import common.FieldTypeImpl;
import framework.*;
import java.util.*;

import static constants.GameConstants.*;

public class BoardUtility {

    private static String gameVariant;
    private static List<Position> allPositions;

    public static Map<Position, FieldType> createInitialBoard(String[] boardLayout, String gameVariant) {

        setGameVariant(gameVariant);

        int[] boardDimensions = retrieveBoardDimensions(gameVariant);
        boolean correctDimensions = validateBoardDimensions(boardLayout, boardDimensions);
        if (!correctDimensions) {
            System.out.println("Board height: " + boardLayout.length + " _ Board width: " + boardLayout[0].length());
            throw new RuntimeException("Please verify the board's dimensions."); }

        Map<Position, FieldType> boardState = new HashMap<>();
        allPositions = new ArrayList<>();
        String newRow;
        for (int row = 0; row < boardDimensions[1]; row++) {
            newRow = boardLayout[row];

            for (int column = 0; column < boardDimensions[0]; column++) {
                Position p = new Position(row, column);
                char fieldTypeChar = newRow.charAt(column);
                String type = getFieldTypeMapping(fieldTypeChar);

                boardState.put(p, new FieldTypeImpl(type)); }
        }
        return boardState;
    }

    public static int[] retrieveBoardDimensions(String gameVariant) {
        int[] boardDimensions = new int[2];
        switch (gameVariant) {
            case ("ALPHA") -> {
                boardDimensions[0] = ALPHA_BOARD_WIDTH;
                boardDimensions[1] = ALPHA_BOARD_HEIGHT; }
            case ("BETA"),
                 ("DELTA") -> {
                boardDimensions[0] = ALPHA_BOARD_WIDTH;
                boardDimensions[1] = BETA_BOARD_HEIGHT; }
            case ("EPSILON"),
                 ("GAMMA"),
                 ("KAPPA") -> {
                boardDimensions[0] = EPSILON_BOARD_WIDTH;
                boardDimensions[1] = EPSILON_BOARD_HEIGHT; }
        }
        return boardDimensions;
    }

    public static String getFieldTypeMapping(char c) {
        return fieldTypesMap.get(c);
    }

    public static void mapFieldTypes(char key, String fieldType){
        fieldTypesMap.put(key, fieldType);
    }

    private static final Map<Character, String> fieldTypesMap = new HashMap<>();

    static {
        mapFieldTypes('o', PLAINS);
        mapFieldTypes('f', FOREST);
        mapFieldTypes('K', KEEP);
        mapFieldTypes('X', CEMETERY);
        mapFieldTypes('S', SWAMP);
        mapFieldTypes('M', MOUNTAIN);
        mapFieldTypes('L', LAKE);
        mapFieldTypes('.', DESERT);
        mapFieldTypes('_', BARRENS);
    }

    private static boolean validateBoardDimensions(String[] boardLayout, int[] boardDimensions) {
        if (boardLayout.length != boardDimensions[1]) {
            return false;
        }

        for (String row : boardLayout) {
            if (row.length() != boardDimensions[0]) {
                return false;
            }
        }
        return true;
    }

    public static Iterator<Position> get3x3GridWithoutCenter(Position center) {
        int[] rowDisplacement = new int[] { -1, -1, 0, +1, +1, +1, 0, -1 };
        int[] columnDisplacement = new int[] { 0, +1, +1, +1, 0, -1, -1, -1 };
        int[] boardDimensions = retrieveBoardDimensions( getGameVariant() );
        List<Position> possiblePositions = new ArrayList<>();

        for (int i=0; i < rowDisplacement.length; i++) {
            int row = center.getRow() + rowDisplacement[i];
            int column = center.getColumn() + columnDisplacement[i];
            boolean withinBoardDimensions = (row > -1
                                            && column > -1
                                            && row < boardDimensions[0]
                                            && column < boardDimensions[1]);
            if (withinBoardDimensions) {
                possiblePositions.add( new Position(row, column)); }
        }

        return possiblePositions.iterator();
    }

    public static Iterator<Position> getRectangularGrid(int xDimension, int yDimension, Position upperLeftCorner) {
        int[] boardDimensions = retrieveBoardDimensions( getGameVariant() );
        int startRow = upperLeftCorner.getRow();
        int startColumn = upperLeftCorner.getColumn();
        List<Position> possiblePositions = new ArrayList<>();

        for (int i=0; i < xDimension; i++) {
            for (int j=0; j < yDimension; j++) {

                boolean withinBoardDimensions = ((startRow + i) > -1
                        && (startColumn + j) > -1
                        && (startRow + i) < boardDimensions[0]
                        && (startColumn + j) < boardDimensions[1]);
                if (withinBoardDimensions) {
                    possiblePositions.add( new Position( (startRow + i), (startColumn + j) )); }
            }
        }

        return possiblePositions.iterator();
    }

    public static Iterator<Position> getSpawnPositionsFor(Player player) {
        int[] boardDimensions = retrieveBoardDimensions( getGameVariant() );
        List<Position> possiblePositions = new ArrayList<>();

        if (player.equals( BLACK_PLAYER )) {
            for (int i=0; i < (boardDimensions[1] - 1); i++) {
                possiblePositions.add( new Position( (11 - i), 10) );
            }
        } else if (player.equals( WHITE_PLAYER )) {
            for (int i=0; i < (boardDimensions[1] - 1); i++) {
                possiblePositions.add( new Position( (i + 1), 0) );
            }
        }

        return possiblePositions.iterator();
    }

    public static Iterator<Position> getReverseSpawnPositionsFor(Player player) {
        int[] boardDimensions = retrieveBoardDimensions( getGameVariant() );
        List<Position> possiblePositions = new ArrayList<>();

        if (player.equals( BLACK_PLAYER )) {
            for (int i=0; i < (boardDimensions[0] - 1); i++) {
                possiblePositions.add( new Position( 10, 11 - i) );
            }
        } else if (player.equals( WHITE_PLAYER )) {
            for (int i=0; i < (boardDimensions[0] - 1); i++) {
                possiblePositions.add( new Position( 0, (i + 1)) );
            }
        }

        return possiblePositions.iterator();
    }

    public static Position generateSpawnPosition(Unit unit, Game game) {
        Player unitOwner = unit.getOwner();
        Iterator<Position> spawnPositions = BoardUtility.getSpawnPositionsFor(unitOwner);

        if (getGameVariant().equals("KAPPA")) {
            spawnPositions = BoardUtility.getReverseSpawnPositionsFor(unitOwner);
        }

        while (spawnPositions.hasNext()) {
            Position p = spawnPositions.next();
            Unit unitAtTo = game.getUnitAt(p);

            if (unitAtTo == null) { return p; }
        }
        return null;
    }

    public static Position generateLeftMostSpawnPosition(Unit unit, Game game) {
        Player unitOwner = unit.getOwner();
        Iterator<Position> spawnPositions = BoardUtility.getLeftMostSpawnPositionsFor(unitOwner);
        if (getGameVariant().equals("KAPPA")) {
            spawnPositions = BoardUtility.getReverseLeftMostSpawnPositionsFor(unitOwner);
        }

        while (spawnPositions.hasNext()) {
            Position p = spawnPositions.next();
            Unit unitAtTo = game.getUnitAt(p);

            if (unitAtTo == null) { return p; }
        }
        return null;
    }

    private static Iterator<Position> getReverseLeftMostSpawnPositionsFor(Player unitOwner) {
        int[] boardDimensions = retrieveBoardDimensions( getGameVariant() );
        List<Position> possiblePositions = new ArrayList<>();

        if (unitOwner.equals( BLACK_PLAYER )) {
            for (int i=0; i < boardDimensions[0]; i++) {
                possiblePositions.add( new Position( 0, 1 + i) );
            }
        } else if (unitOwner.equals( WHITE_PLAYER )) {
            for (int i=0; i < boardDimensions[0]; i++) {
                possiblePositions.add( new Position( 10, i) );
            }
        }

        return possiblePositions.iterator();
    }

    public static Iterator<Position> getLeftMostSpawnPositionsFor(Player player) {
        int[] boardDimensions = retrieveBoardDimensions( getGameVariant() );
        List<Position> possiblePositions = new ArrayList<>();

        if (player.equals( BLACK_PLAYER )) {
            for (int i=0; i < boardDimensions[1]; i++) {
                possiblePositions.add( new Position( i, boardDimensions[1] - 1) );
            }
        } else if (player.equals( WHITE_PLAYER )) {
            for (int i=0; i < boardDimensions[1]; i++) {
                possiblePositions.add( new Position( i, 0) );
            }
        }

        return possiblePositions.iterator();
    }

    public static Iterator<Position> getBoardCenter() {
        int[] boardDimensions = retrieveBoardDimensions( getGameVariant() );
        List<Position> boardCenterPositions = new ArrayList<>();

        for (int i=0; i < boardDimensions[0]; i++) {
                // Exclude iteration for the top two and bottom two rows.
            for (int j=2; j < (boardDimensions[1] - 2); j++) {

                boardCenterPositions.add( new Position( i, j )); }
        }
        return boardCenterPositions.iterator();
    }

    public static Iterator<Position> getRandomSpawnPositions(int amount) {
        int[] boardDimensions = retrieveBoardDimensions( getGameVariant() );
        List<Position> allPositions = new ArrayList<>();

        for (int i=0; i < boardDimensions[0]; i++) {
            for (int j=0; j < boardDimensions[1]; j++) {
                allPositions.add(new Position( i, j ));
        }   }
        allPositions.remove(EPSILON_BLACK_CEMETERY_POS);
        allPositions.remove(EPSILON_BLACK_KEEP_POS);
        allPositions.remove(WHITE_KEEP_POS);
        allPositions.remove(WHITE_CEMETERY_POS);
        Collections.shuffle(allPositions);

        List<Position> possiblePositions = new ArrayList<>();
        for (int i=0; i < amount; i++) {
            possiblePositions.add( allPositions.get(i) );
        }

        return possiblePositions.iterator();
    }

    private static String getGameVariant() {
        return gameVariant;
    }
    private static void setGameVariant(String gameVariant) {
        BoardUtility.gameVariant = gameVariant;
    }
}
