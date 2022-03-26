package view;

import constants.GameConstants;
import utilities.BoardUtility;

@SuppressWarnings("SpellCheckingInspection")
public class GraphicsConstants {

    public static final int FIELD_TYPE_SIZE = 60;
    public static final int CARD_SIZE = 92;
    public static final int BOARD_X_OFFSET = 21;
    public static final int BOARD_Y_OFFSET = 150;
    public static final int FRAME_WIDTH = 1550;
    public static final int FRAME_HEIGHT = 994;
    public static final int SEPARATION = 3;
    public static final int LINE_SEPARATION = 45;


    // === File names ==========================
    public static final String NOTHING = "black";
    public static final String N_TURN_BUTTON = "end-turn-button";
    public static final String N_REFRESH_BUTTON = "refresh-button";
    public static final String N_TURN_IN_PROGRESS = "turn-in-progress";
    public static final String N_DECK = "deck";
    public static final String N_GENERIC_CARD = "card";
    public static final String N_B_F_SWORD = "b-f-sword";

    // === Figure positions ===================
    public static final int AGE_INFO_X = FRAME_WIDTH - 500;
    public static final int AGE_INFO_Y = 280;
    public static final int UNIT_OFFSET_Y = 0;
    public static final int UNIT_INFO_X = 820;
    public static final int UNIT_INFO_Y = 550;
    public static final int CARD_INFO_X = 820;
    public static final int CARD_INFO_Y = 550;
    public static final int TURN_BUTTON_X = 800;
    public static final int TURN_BUTTON_Y = 350;
    public static final int REFRESH_BUTTON_X = 820;
    public static final int REFRESH_BUTTON_Y = 475;
    public static final int DECK_IMAGE_X = FRAME_WIDTH - 300;
    public static final int DECK_IMAGE_Y = 120;
    public static final int DECK_IMAGE_WIDTH = 120;
    public static final int PLAYER_IMAGE_X = FRAME_WIDTH - 500;
    public static final int PLAYER_IMAGE_Y = 10;
    public static final int PLAYER_IMAGE_WIDTH = 0;
    public static final int WHITE_HAND_X = BOARD_X_OFFSET;
    public static final int WHITE_HAND_Y = 10;
    public static final int BLACK_HAND_X = BOARD_X_OFFSET;
    public static final int BLACK_HAND_Y = (GameConstants.EPSILON_BOARD_HEIGHT * FIELD_TYPE_SIZE) + 153;

    // === Valid figure strings ================
    public static final String UNIT_TYPE_STRING = "unit-type";
    public static final String TURN_BUTTON_STRING = "turn-button";
    public static final String REFRESH_BUTTON_STRING = "refresh-button";
    public static final String DECK_IMAGE_STRING = "deck-image";
    public static final String GENERIC_CARD_STRING = "generic-card";
    public static final String B_F_SWORD_STRING = "big-effing-sword";
}
