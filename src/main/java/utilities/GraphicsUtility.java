package utilities;

import framework.Player;
import framework.Position;

import static constants.GameConstants.BLACK_PLAYER;
import static constants.GameConstants.WHITE_PLAYER;
import static view.GraphicsConstants.*;

public class GraphicsUtility {

    public static int getXFromColumn(int column) { return ((column * FIELD_TYPE_SIZE) + BOARD_X_OFFSET); }
    public static int getYFromRow(int row) {
        return ((row * FIELD_TYPE_SIZE) + BOARD_Y_OFFSET);
    }
    public static int getXFromCardIndex(int cardIndex, Player player) {

        int xPosition = 0;
        switch (cardIndex) {
            case (0) -> {
                if (player.equals( BLACK_PLAYER )) {
                    xPosition = BLACK_HAND_X;
                } else if (player.equals( WHITE_PLAYER )) {
                    xPosition = WHITE_HAND_X;
                }
            }
            case (1), (2), (3), (4), (5), (6), (7), (8), (9) -> {
                if (player.equals( BLACK_PLAYER )) {
                    xPosition = BLACK_HAND_X + (cardIndex * CARD_SIZE);
                } else if (player.equals( WHITE_PLAYER )) {
                    xPosition = WHITE_HAND_X + (cardIndex * CARD_SIZE);
                }

            }
            default -> System.out.println("Do you have an illegal card up your sleeve?");
        }

        return xPosition;
    }

    public static java.awt.Color getColorForPlayer(Player player) {
        String playerName = player.toString();
        return switch (playerName) {
            case ("Player.BLACK") -> java.awt.Color.BLACK;
            case ("Player.WHITE") -> java.awt.Color.WHITE;
            default -> java.awt.Color.YELLOW;
        };
    }

    public static Position getPositionFromCoordinates(int xCoordinate, int yCoordinate) {
        return new Position((yCoordinate - BOARD_Y_OFFSET) / FIELD_TYPE_SIZE,
                            (xCoordinate - BOARD_X_OFFSET) / FIELD_TYPE_SIZE);
    }


}
