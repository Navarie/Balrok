package constants;

import common.CardImpl;
import framework.Card;

import static constants.SpellConstants.*;
import static constants.UnitConstants.*;

@SuppressWarnings("SpellCheckingInspection")
public class CardConstants {

    public static final int BETA_MAGIC_CARD_COST = 10;
    public static final int EPSILON_MAGIC_CARD_COST = 70;
    public static final int DELTA_FIELD_CARD_COST = 30;

    // Valid card types
    public static final String UNIT_CARD = "unit-card";
    public static final String MAGIC_CARD = "magic-card";
    public static final String FIELD_CARD = "field-card";
    public static final String TRAP_CARD = "trap-card";
    public static final String DEMONIC_CARD = "demonic-card";
    public static final String DEMONIC_TRAP_CARD = "demonic-trap-card";
    public static final String STRUCTURE_CARD = "structure-card";

    // === Unit ===================
    public static final Card GERALT_OF_RIVIA = new CardImpl(N_GERALT_OF_RIVIA, "Det dejligt mandt", UNIT_CARD, 0, D_THE_WITCHER);
    public static final Card THOMAS = new CardImpl(N_THOMAS, "Jeg smadre dig", UNIT_CARD, 0, D_THOMAS);
    public static final Card WHITE_FLAME = new CardImpl(N_WHITE_FLAME, "Flækker dig", UNIT_CARD, 0, D_WHITE_FLAME);
    public static final Card HEISENBERG = new CardImpl(N_HEISENBERG, "Quod erat demonstrandum.", UNIT_CARD, 0, D_HEISENBERG);
    public static final Card DAGFINNUR = new CardImpl(N_DAGFINNUR, "Wallah habibi", UNIT_CARD, 0, D_DAGFINNUR);
    public static final Card THORSTEN = new CardImpl(N_THORSTEN, "Forstår du selv at det være sådan?", UNIT_CARD, 0,D_THORSTEN);
    public static final Card BLUE_EYES = new CardImpl(N_BLUE_EYES, "WHITE LIGHT", UNIT_CARD, 0, D_BLUE_EYES);
    public static final Card OZITY = new CardImpl(N_OZITY, "Fuld på orale", UNIT_CARD, 0, D_OZITY);
    public static final Card LEGOLAS = new CardImpl(N_LEGOLAS, "They're taking the horbits!", UNIT_CARD, 0, D_LEGOLAS);
    public static final Card HJOERDIS = new CardImpl(N_HJOERDIS, "I take ze sausage and put it down ze throat.", UNIT_CARD, 0,D_HJOERDIS);
    public static final Card STATHAM = new CardImpl(N_STATHAM, "Et eller andet sejt", UNIT_CARD, 0, D_STATHAM);
    public static final Card MATHIAS = new CardImpl(N_MATHIAS, "Eeeeeeeeeeeejj.", UNIT_CARD, 0, D_MATHIAS);
    public static final Card GORDON = new CardImpl(N_GORDON, "Look at this, it's fookin raw mate!", UNIT_CARD, 0, D_GORDON);
    public static final Card BENSE = new CardImpl(N_BENSE, "Helt sikkert!", UNIT_CARD, 0, D_BENSE);
    public static final Card AHRI = new CardImpl(N_AHRI, "Imagine if I had a REAL weapon.", UNIT_CARD, 0, D_AHRI);
    public static final Card VAYNE = new CardImpl(N_VAYNE, "The dawn has arrived.", UNIT_CARD, 0, D_VAYNE);
    public static final Card TYRONE = new CardImpl(N_TYRONE, "...", UNIT_CARD, 0, D_TYRONE);
    public static final Card GERMAN_KID = new CardImpl(N_GERMAN_KID, "Verdammt noch mal!", UNIT_CARD, 0, D_GERMAN_KID);
    public static final Card EMILY_COON = new CardImpl(N_EMILY_COON, "...", UNIT_CARD, 0, D_EMILY_COON);
    public static final Card ANDERS = new CardImpl(N_ANDERS, "...", UNIT_CARD, 0, D_ANDERS);
    public static final Card ISAAC = new CardImpl(N_ISAAC, "...", UNIT_CARD, 0, D_ISAAC);
    public static final Card MR_GARRISON = new CardImpl(N_MR_GARRISON, "...", UNIT_CARD, 0, D_MR_GARRISON);
    public static final Card KANYE_EAST = new CardImpl(N_KANYE_EAST, "...", UNIT_CARD, 0, D_KANYE_EAST);

    // === Names of ===============
    public static final String N_RENE_DESCARTES = "Rene Descartes";
    public static final String N_JONAS = "Farmer Jones";
    public static final String N_RICKE = "The Curse of Ricke Peterson";
    public static final String N_OBJECTION = "Objection!";
    public static final String N_SWITCHEROO = "The Ol' Switcheroo";
    // === Trap ===================
    public static final Card RENE_DESCARTES = new CardImpl(N_RENE_DESCARTES, "Cogito ergo slum.", TRAP_CARD, 0, "...");
    public static final Card JONAS = new CardImpl(N_JONAS, "Boys, I have stopped.", TRAP_CARD, 0, "...");
    public static final Card RICKE = new CardImpl(N_RICKE, "Please give me a higher grade, teacher... *sobs*", TRAP_CARD, 0, "...");
    public static final Card OBJECTION = new CardImpl(N_OBJECTION, "Formal protest, your honor; my opponent is wack.", TRAP_CARD, 0, "...");
    public static final Card OL_SWITCHEROO = new CardImpl(N_SWITCHEROO, "...", TRAP_CARD, 0, "...");

    // === Names of ===============
    public static final String N_INGER = "Inger Hamburgerryg";
    // === Demonic trap ===========
    public static final Card INGER = new CardImpl(N_INGER, "Hvornår lærer I at opføre jer ordentligt?", DEMONIC_TRAP_CARD, 0, "...");

    public static final Card[] DECK_LIST = {
            GERALT_OF_RIVIA,
            THOMAS,
            CIRI,
            WHITE_FLAME,
            HEISENBERG,
            DAGFINNUR,
            THORSTEN,
            VIVI,
            KARDEY,
            RENE_DESCARTES,
            INGER,
            JONAS,
            BATTLEGROUNDS,
            MAGISTRATE,
            STONEBURG,
            BLUE_EYES,
            OZITY,
            LEGOLAS,
            HJOERDIS,
            STATHAM,
            MATHIAS,
            GORDON,
            CHOPIN,
            FOX_CHARM,
            TWO_SMOKES,
            POT_OF_GREED,
            SUSPENSION,
            RITO_CLIENT,
            REPORT,
            F_STATE_SCHOOL,
            F_ALSSUND,
            SHOTGUN,
            RICKE,
            BLACK_HOLE,
            WAFFLEBOAT,
            LIENACK,
            AVERAGE_SWORD,
            AHRI,
            VAYNE,
            TYRONE,
            BENSE,
            GERMAN_KID,
            EMILY_COON,
            ANDERS,
            ISAAC,
            MR_GARRISON,
            KANYE_EAST,
            FIFTY_SHADES,
            OBJECTION,
            OL_SWITCHEROO,
            TIT_FOR_TAT,
            SAVE_ENVIRONMENT,
            HOWLING_ABYSS,
            FOUNTAIN,
            CAMPING_VAN,
            BUILD_A_WALL,
            WALL_OF_CHINA,
            HOUSE_OF_GOD,
            RINGRIDER_PALACE
    };

    // == Null Object-pattern ================
    public static final Card FREDERIK = new CardImpl("Nawoori", "DO NOT IMPLEMENT!", UNIT_CARD, 0, "...");
}
