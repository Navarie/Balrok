package constants;

import common.PlayerImpl;
import framework.Player;
import framework.Position;

public class GameConstants {

    public static final int MAX_DECK_SIZE = 60;
    public static final int MAX_HAND_SIZE = 10;
    public static final int FATAL = 9999;

    // Board dimensions
    public static final int ALPHA_BOARD_WIDTH = 9;
    public static final int ALPHA_BOARD_HEIGHT = 6;
    public static final int BETA_BOARD_HEIGHT = 7;
    public static final int EPSILON_BOARD_WIDTH = 13;
    public static final int EPSILON_BOARD_HEIGHT = 11;

    // Valid players
    public static final Player WHITE_PLAYER = new PlayerImpl("Player.WHITE");
    public static final Player BLACK_PLAYER = new PlayerImpl("Player.BLACK");
    public static final Player YELLOW_PLAYER = new PlayerImpl("Player.YELLOW");

    public static final Player[] PLAYER_LIST = {
            BLACK_PLAYER,
            WHITE_PLAYER,
    };

    // Important positions
    public static final Position WHITE_KEEP_POS = new Position(0, 0);
    public static final Position WHITE_CEMETERY_POS = new Position(1, 0);
    public static final Position BLACK_KEEP_POS = new Position(6, 8);
    public static final Position EPSILON_BLACK_KEEP_POS = new Position(12, 10);
    public static final Position BLACK_CEMETERY_POS = new Position(5, 8);
    public static final Position EPSILON_BLACK_CEMETERY_POS = new Position(9, 12);

    // Valid field types
    public static final String PLAINS = "plains";
    public static final String BARRENS = "barrens";
    public static final String DESERT = "desert";
    public static final String SWAMP = "swamp";
    public static final String LAKE = "lake";
    public static final String FOREST = "forest";
    public static final String HILL = "hill";
    public static final String MOUNTAIN = "mountain";
    public static final String CEMETERY = "cemetery";
    public static final String KEEP = "keep";

    // Time periods
    public static final String ANTIQUE = "Antique";
    public static final String BAROQUE = "Baroque";
    public static final String CLASSICISM = "Classicism";
    public static final String ROMANTICISM = "Romanticism";
    public static final String MODERNISM = "Modernism";
    public static final String POST_MODERNISM = "Post-modernism";

    // Valid unit races
    public static final String DWARF = "dwarf";
    public static final String ELF = "elf";
    public static final String ORC = "orc";
    public static final String HUMAN = "human";
    public static final String GNOME = "gnome";
    public static final String PRIMORDIAL_DRAGON = "primordial-dragon";
    public static final String CHERNOBOG = "chernobog";
    public static final String LESHY = "leshy";
    public static final String SIREN = "siren";
    public static final String FURRY = "furry";
    public static final String DEMON = "demon";

    // Valid unit types
    public static final String NEUTRAL = "neutral";
    public static final String MONSTER = "monster";

    // "Valid" unit genders
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    public static final String TRANSGENDER = "transgender";

    public static final String[] GENDER_LIST = {
            MALE,
            FEMALE,
            TRANSGENDER
    };

    // Valid unit affinities
    public static final String NO_AFFINITY = "--";
    public static final String SORCERER = "sorcerer";
    public static final String DRUID = "druid";
    public static final String PALADIN = "paladin";
    public static final String HUNTER = "hunter";
    public static final String KNIGHT = "knight";
    public static final String BERSERKER = "berserker";

    // Valid unit affiliations
    public static final String NO_AFFILIATION = "--";
    public static final String ALSSUND = "Alssund High";
    public static final String STATE_SCHOOL = "The State School";

    // Valid unit debuffs
    public enum Debuff {
        ROOT, SUPPRESS, BAN, LETHARGY, MIND_CONTROL, DRUNK
    }

    // Valid unit buffs
    public enum Buff {
        SECOND_WIND, WINDFURY, RUSH
    }
}
