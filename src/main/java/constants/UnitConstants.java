package constants;

import common.UnitImpl;
import framework.Unit;

import static constants.GameConstants.*;

@SuppressWarnings("SpellCheckingInspection")
public class UnitConstants {

    // === Unit names =======================
    public static final String N_BLUE_EYES = "Blue-Eyes White Dragon";
    public static final String N_OZITY = "Jeppe the Hammer";
    public static final String N_THOMAS = "Thomas the Goof";
    public static final String N_STATHAM = "Jason Statham the Pissice";
    public static final String N_MATHIAS = "Mathias the Long";
    public static final String N_LEGOLAS = "Legolas of Isengard";
    public static final String N_GORDON = "Bloody Gordon Ramsay";
    public static final String N_HJOERDIS = "Hjoerdis the Verdammt";
    public static final String N_DAGFINNUR = "Dagfinnur the Unintelligible";
    public static final String N_GERALT_OF_RIVIA = "Geralt of Rivia";
    public static final String N_HEISENBERG = "Holdsbjerg of Brauger";
    public static final String N_WHITE_FLAME = "Cahir of Xin'trea";
    public static final String N_THORSTEN = "Thorsten of Valwallah";
    public static final String N_BENSE = "Thomas the Supreme Converser";
    public static final String N_AHRI = "Foxy Ahri";
    public static final String N_VAYNE = "Vayne the Osoonly";
    public static final String N_TYRONE = "Sir Michael Tyrone";
    public static final String N_GERMAN_KID = "Angry German Kid";
    public static final String N_EMILY_COON = "Emily Coon";
    public static final String N_ANDERS = "Anders the Frozen Leet";
    public static final String N_ISAAC = "Isaac the Holy";
    public static final String N_MR_GARRISON = "Mr Garrison";
    public static final String N_KANYE_EAST = "Kanye East";

    // === Unit invariants ==================
    public static final Unit U_BLUE_EYES = new UnitImpl(null, MONSTER, PRIMORDIAL_DRAGON, N_BLUE_EYES);
    public static final Unit U_OZITY = new UnitImpl(null, NEUTRAL, HUMAN, N_OZITY);
    public static final Unit U_THOMAS = new UnitImpl(null, NEUTRAL, HUMAN, N_THOMAS);
    public static final Unit U_STATHAM = new UnitImpl(null, NEUTRAL, HUMAN, N_STATHAM);
    public static final Unit U_MATHIAS = new UnitImpl(null, NEUTRAL, HUMAN, N_MATHIAS);
    public static final Unit U_LEGOLAS = new UnitImpl(null, NEUTRAL, ELF, N_LEGOLAS);
    public static final Unit U_GORDON = new UnitImpl(null, MONSTER, HUMAN, N_GORDON);
    public static final Unit U_HJOERDIS = new UnitImpl(null, NEUTRAL, DEMON, N_HJOERDIS);
    public static final Unit U_DAGFINNUR = new UnitImpl(null, NEUTRAL, DWARF, N_DAGFINNUR);
    public static final Unit U_WITCHER = new UnitImpl(null, NEUTRAL, HUMAN, N_GERALT_OF_RIVIA);
    public static final Unit U_HEISENBERG = new UnitImpl(null, NEUTRAL, HUMAN, N_HEISENBERG);
    public static final Unit U_WHITE_FLAME = new UnitImpl(null, NEUTRAL, ELF, N_WHITE_FLAME);
    public static final Unit U_THORSTEN = new UnitImpl(null, NEUTRAL, HUMAN, N_THORSTEN);
    public static final Unit U_BENSE = new UnitImpl(null, NEUTRAL, HUMAN, N_BENSE);
    public static final Unit U_AHRI = new UnitImpl(null, NEUTRAL, FURRY, N_AHRI);
    public static final Unit U_VAYNE = new UnitImpl(null, NEUTRAL, HUMAN, N_VAYNE);
    public static final Unit U_TYRONE = new UnitImpl(null, NEUTRAL, HUMAN, N_TYRONE);
    public static final Unit U_GERMAN_KID = new UnitImpl(null, NEUTRAL, MONSTER, N_GERMAN_KID);
    public static final Unit U_EMILY_COON = new UnitImpl(null, NEUTRAL, HUMAN, N_EMILY_COON);
    public static final Unit U_ANDERS = new UnitImpl(null, NEUTRAL, HUMAN, N_ANDERS);
    public static final Unit U_ISAAC = new UnitImpl(null, NEUTRAL, HUMAN, N_ISAAC);
    public static final Unit U_MR_GARRISON = new UnitImpl(null, NEUTRAL, HUMAN, N_MR_GARRISON);
    public static final Unit U_KANYE_EAST = new UnitImpl(null, NEUTRAL, HUMAN, N_KANYE_EAST);

    // === Stats ============================
    // === Form: AP - FER - HP - MS - STR ===
    public static final int[] ST_BLUE_EYES = { 3000, 1, 200, 1, 2500 };
    public static final int[] ST_OZITY = { 0, 3, 123, 2, 2000 };
    public static final int[] ST_THOMAS = { 25, 2, 100, 1, 89 };
    public static final int[] ST_STATHAM = { 0, 2, 300, 1, 9999 };
    public static final int[] ST_MATHIAS = { 100, 1, 100, 1, 49 };
    public static final int[] ST_LEGOLAS = { 90, 1, 70, 1, 40 };
    public static final int[] ST_GORDON = { 0, 1, 200, 1, 80 };
    public static final int[] ST_HJOERDIS = { 30, 4, 500, 2, 9 };
    public static final int[] ST_DAGFINNUR = { 0, 1, 80, 1, 55 };
    public static final int[] ST_WITCHER = { 50, 3, 250, 2, 60 };
    public static final int[] ST_HEISENBERG = { 150, 1, 150, 1, 50 };
    public static final int[] ST_WHITE_FLAME = { 160, 2, 300, 1, 70 };
    public static final int[] ST_THORSTEN = { 25, 1, 100, 1, 13 };
    public static final int[] ST_BENSE = { 45, 1, 100, 1, 20 };
    public static final int[] ST_AHRI = { 130, 1, 125, 2, 35 };
    public static final int[] ST_VAYNE = { 25, 2, 130, 2, 60 };
    public static final int[] ST_TYRONE = { 150, 2, 150, 5, 150 };
    public static final int[] ST_GERMAN_KID = { 25, 4, 70, 1, 180 };
    public static final int[] ST_EMILY_COON = { 25, 1, 100, 1, 15 };
    public static final int[] ST_ANDERS = { 0, 5, 200, 3, 200 };
    public static final int[] ST_ISAAC = { 80, 2, 140, 2, 60 };
    public static final int[] ST_MR_GARRISON = { 0, 3, 120, 2, 40 };
    public static final int[] ST_KANYE_EAST = { 0, 1, 110, 1, 80 };

    // === Null Object-pattern ==============
    public static final String NAWOORI = "placeholder";

    // === Specifics ========================
    // === Form: AFF - GEN - AFFIL ==========
    public static final String[] SP_BLUE_EYES = { SORCERER, MALE, NAWOORI };
    public static final String[] SP_OZITY = { KNIGHT, MALE, ALSSUND };
    public static final String[] SP_THOMAS = { BERSERKER, MALE, STATE_SCHOOL };
    public static final String[] SP_STATHAM = { BERSERKER, MALE, NAWOORI };
    public static final String[] SP_MATHIAS = { PALADIN, MALE, STATE_SCHOOL };
    public static final String[] SP_LEGOLAS = { HUNTER, FEMALE, NAWOORI };
    public static final String[] SP_GORDON = { KNIGHT, MALE, NAWOORI };
    public static final String[] SP_HJOERDIS = { PALADIN, FEMALE, STATE_SCHOOL };
    public static final String[] SP_DAGFINNUR = { HUNTER, MALE, STATE_SCHOOL };
    public static final String[] SP_WITCHER = { KNIGHT, MALE, NAWOORI };
    public static final String[] SP_HEISENBERG = { SORCERER, MALE, STATE_SCHOOL };
    public static final String[] SP_WHITE_FLAME = { PALADIN, MALE, NAWOORI };
    public static final String[] SP_THORSTEN = { SORCERER, MALE, STATE_SCHOOL };
    public static final String[] SP_BENSE = { SORCERER, MALE, ALSSUND };
    public static final String[] SP_AHRI = { SORCERER, FEMALE, NAWOORI };
    public static final String[] SP_VAYNE = { HUNTER, FEMALE, NAWOORI };
    public static final String[] SP_TYRONE = { KNIGHT, MALE, ALSSUND };
    public static final String[] SP_GERMAN_KID = { BERSERKER, MALE, NAWOORI };
    public static final String[] SP_EMILY_COON = { SORCERER, FEMALE, STATE_SCHOOL };
    public static final String[] SP_ANDERS = { KNIGHT, MALE, ALSSUND };
    public static final String[] SP_ISAAC = { SORCERER, MALE, NAWOORI };
    public static final String[] SP_MR_GARRISON = { BERSERKER, TRANSGENDER, NAWOORI };
    public static final String[] SP_KANYE_EAST = { KNIGHT, MALE, NAWOORI };

    // === Descriptions ==========================
    public static final String D_THE_WITCHER = "Toss a coin to your witcher!";
    public static final String D_BLUE_EYES = "RAWR!";
    public static final String D_OZITY = "Pissestærk. May only attack 3 times.";
    public static final String D_THOMAS = "Jeg smadrer dig, din lille hund man.";
    public static final String D_STATHAM = "May be played only by sacrificing Thomas. 9999 STR.";
    public static final String D_MATHIAS = "Struggling with his bowels. Limited movement.";
    public static final String D_LEGOLAS = "Skal du DØ!?";
    public static final String D_GORDON = "...";
    public static final String D_HJOERDIS = "An insufferable demon from the depths of Hell itself.";
    public static final String D_DAGFINNUR = "*Says stuff*, but noone it listening.";
    public static final String D_HEISENBERG = "E = MC^2. Heraf fås, at du taber.";
    public static final String D_WHITE_FLAME = "Father of Ciri and lost ruler of the Elves.";
    public static final String D_THORSTEN = "Hvad sker der så, når der ikke sker noget?";
    public static final String D_BENSE = "May sometimes lag, causing him to be unable to do anything.";
    public static final String D_AHRI = "...";
    public static final String D_VAYNE = "Aaarhh, osoonly!";
    public static final String D_TYRONE = "When dealing a fatal blow, goes for the penta.";
    public static final String D_GERMAN_KID = "Enrages at low Health.";
    public static final String D_EMILY_COON = "...";
    public static final String D_ANDERS = "Draws from the power of snow. Pissestærk.";
    public static final String D_ISAAC = "...";
    public static final String D_MR_GARRISON = "Suspends all students in his nearby proximity.";
    public static final String D_KANYE_EAST = "Spawns on the west coast. May only move east.";
}
