package variants;

import common.GameImpl;
import framework.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import variants.configuration.GammaFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static constants.CardConstants.*;
import static constants.GameConstants.*;
import static constants.SpellConstants.*;
import static constants.UnitConstants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GammaBal {

    private static final Position FIRST_WHITE_SPAWN_POSITION = new Position(1, 0);
    public static final Position FIRST_BLACK_SPAWN_POSITION = new Position(11, 10);
    public static final Position SECOND_BLACK_SPAWN_POSITION = new Position(10, 10);
    public static final Position DEMON_POSITION = new Position(11, 9);
    public static final Position MONSTER_POSITION = new Position(10, 9);
    public static final Position KANYE_SPAWN_POSITION = new Position(0, 10);
    public static final Position MR_GARRISON_POSITION = new Position(3, 9);
    public static final Position STUDENT_ONE_POSITION = new Position(4, 7);
    public static final Position STUDENT_TWO_POSITION = new Position(0, 7);
    public static final Position STUDENT_THREE_POSITION = new Position(3, 8);
    public static final Position STUDENT_FOUR_POSITION = new Position(1, 9);
    public static final Position FEMALE_ONE_POSITION = new Position(1, 3);
    public static final Position FEMALE_TWO_POSITION = new Position(1, 5);
    public static final Position FEMALE_THREE_POSITION = new Position(1, 7);
    public static final Position LEFT_TOP_CORNER_POSITION = new Position(6, 2);
    public static final Position SECOND_ROW_POSITION = new Position(7, 3);
    public static final Position THIRD_ROW_POSITION = new Position(8, 4);
    public static final Position LEFT_BOTTOM_CORNER_POSITION = new Position(9, 2);
    public static final Position RIGHT_BOTTOM_CORNER_POSITION = new Position(6, 5);
    public static final Position RIGHT_TOP_CORNER_POSITION = new Position(9, 5);

    private Game game;

    @BeforeEach
    void setup() {
            // Given a default Epsilon game setting,
        game = new GameImpl(new GammaFactoryBuilder().build());
    }

    private void endRound() {
        game.endTurn();
        game.endTurn();
    }

    @Test
    void shouldMakeAndersLethargicAfterSetAmountOfTurns() {
            // Given that black player has played Anders,
        game.addCardToHand(ANDERS);
        game.playCard(ANDERS, null, null);
            // He should be initialised properly.
        assertThat(game.getUnitAt(FIRST_BLACK_SPAWN_POSITION).getStrength(), is (ST_ANDERS[4]));
        assertThat(game.getUnitAt(FIRST_BLACK_SPAWN_POSITION).getMaxFerocity(), is (ST_ANDERS[1]));
        assertThat(game.getUnitAt(FIRST_BLACK_SPAWN_POSITION).getCurrentMovementSpeed(), is (ST_ANDERS[3]));
            // But when 5 rounds have passed,
        endRound(); endRound(); endRound(); endRound(); endRound();
            // He should become lethargic, reducing MS to 0, FER to 1, STR to half and HP to 50.
        assertThat(game.getUnitAt(FIRST_BLACK_SPAWN_POSITION).getStrength(), is (ST_ANDERS[4] / 2));
        assertThat(game.getUnitAt(FIRST_BLACK_SPAWN_POSITION).getMaxFerocity(), is (1));
        assertThat(game.getUnitAt(FIRST_BLACK_SPAWN_POSITION).getCurrentMovementSpeed(), is (0));
    }

    @Test
    void shouldIncreaseIsaacStatsAgainstDemonsAndMonsters() {
            // Given that black player has played Isaac,
        game.addCardToHand(ISAAC);
        game.playCard(ISAAC, null, null);
        assertThat(game.getUnitAt(MONSTER_POSITION).getUnitType(), is (MONSTER));
        assertThat(game.getUnitAt(DEMON_POSITION).getUnitRace(), is (DEMON));
        game.getUnitAt(MONSTER_POSITION).setUnitStat("HP", 300);
        assertThat(game.getUnitAt(MONSTER_POSITION).getHealth(), is (300));
        game.getUnitAt(DEMON_POSITION).setUnitStat("HP", 300);
        assertThat(game.getUnitAt(DEMON_POSITION).getHealth(), is (300));
            // When Isaac attacks the two creatures,
        game.attackOpponent(FIRST_BLACK_SPAWN_POSITION, MONSTER_POSITION);
        endRound();
        game.attackOpponent(FIRST_BLACK_SPAWN_POSITION, DEMON_POSITION);
            // They have taken increased damage.
        assertThat(game.getUnitAt(MONSTER_POSITION).getHealth(), is (300 - ST_ISAAC[2] - 100));
        assertThat(game.getUnitAt(DEMON_POSITION).getHealth(), is (300 - ST_ISAAC[2] - 100));
    }

    @Test
    void shouldSpawnKanyeEastOnLeftMostSpawnPosition() {
            // Given that black player has played Kanye East,
        game.addCardToHand(KANYE_EAST);
        game.playCard(KANYE_EAST, null, null);
            // He should spawn on the left-most position possible.
        assertThat(game.getUnitAt(KANYE_SPAWN_POSITION).getName(), is (N_KANYE_EAST));
    }

    @Test
    void shouldDenyKanyeToMoveWest() {
            // Given that black player has played Kanye East,
        game.addCardToHand(KANYE_EAST);
        game.playCard(KANYE_EAST, null, null);
        assertThat(game.moveUnit(KANYE_SPAWN_POSITION, new Position(1, 10)), is (true));
        endRound();
            // When Kanye attempts to move west,
            // Then it is rejected.
        assertThat(game.moveUnit(new Position(1, 10), KANYE_SPAWN_POSITION), is (false));
    }

    @Test
    void shouldBanEveryStudentIn5x5SurroundingGridAsMr_Garrison() {
            // Given that black player plays Mr. Garrison,
        game.addCardToHand(MR_GARRISON);
        game.playCard(MR_GARRISON, null, MR_GARRISON_POSITION);
            // He is initialised properly.
        assertThat(game.getUnitAt(MR_GARRISON_POSITION).getUnitGender(), is (TRANSGENDER));
            // And when he moves to p(2, 9),
        game.getUnitAt(STUDENT_ONE_POSITION).setAffiliation(STATE_SCHOOL);
        game.getUnitAt(STUDENT_TWO_POSITION).setAffiliation(ALSSUND);
        game.getUnitAt(STUDENT_THREE_POSITION).setAffiliation(STATE_SCHOOL);
        game.getUnitAt(STUDENT_FOUR_POSITION).setAffiliation(ALSSUND);
        game.moveUnit(MR_GARRISON_POSITION, new Position(2, 9));
            // He bans every student for 3 rounds.
        assertThat(game.getUnitAt(STUDENT_ONE_POSITION).getDebuffs().get(Debuff.BAN), is (6));
        assertThat(game.getUnitAt(STUDENT_TWO_POSITION).getDebuffs().get(Debuff.BAN), is (6));
        assertThat(game.getUnitAt(STUDENT_THREE_POSITION).getDebuffs().get(Debuff.BAN), is (6));
        assertThat(game.getUnitAt(STUDENT_FOUR_POSITION).getDebuffs().get(Debuff.BAN), is (6));
    }

    @Test
    void shouldGrantUnitsWindfuryUponActivatingLienack() {
            // Given that black player has two units in play,
        game.playCard(THOMAS, null, null);
        game.addCardToHand(EMILY_COON);
        game.playCard(EMILY_COON, null, null);

            // When black player plays Lienack,
        game.addCardToHand(LIENACK);
        game.playCard(LIENACK, null, null);
            // Then black's unit gain windfury for 5 turns.
        assertThat(game.getUnitAt(FIRST_BLACK_SPAWN_POSITION).getMaxFerocity(), is (ST_THOMAS[1] + 1));
        assertThat(game.getUnitAt(SECOND_BLACK_SPAWN_POSITION).getMaxFerocity(), is (ST_EMILY_COON[1] + 1));
    }

    @Test
    void shouldEmpowerFemaleUnitsUponPlayingFiftyShades() {
            // Given that white player has female units in play,
        game.getUnitAt(FEMALE_ONE_POSITION).setGender(FEMALE);
        game.getUnitAt(FEMALE_TWO_POSITION).setGender(FEMALE);
        game.getUnitAt(FEMALE_THREE_POSITION).setGender(FEMALE);
        assertThat(game.getUnitAt(FEMALE_ONE_POSITION).getStrength(), is (40));
        assertThat(game.getUnitAt(FEMALE_ONE_POSITION).getAbilityPower(), is (0));
        assertThat(game.getUnitAt(FEMALE_TWO_POSITION).getStrength(), is (40));
        assertThat(game.getUnitAt(FEMALE_TWO_POSITION).getAbilityPower(), is (0));
        assertThat(game.getUnitAt(FEMALE_THREE_POSITION).getStrength(), is (40));
        assertThat(game.getUnitAt(FEMALE_THREE_POSITION).getAbilityPower(), is (0));
            // When white player plays Fifty Shades of Empowerment,
        game.endTurn();
        game.addCardToHand(FIFTY_SHADES);
        game.playCard(FIFTY_SHADES, null, null);
            // Then all female units gain increased stats.
        assertThat(game.getUnitAt(FEMALE_ONE_POSITION).getStrength(), is (40 + 40));
        assertThat(game.getUnitAt(FEMALE_ONE_POSITION).getAbilityPower(), is (40));
        assertThat(game.getUnitAt(FEMALE_TWO_POSITION).getStrength(), is (40 + 40));
        assertThat(game.getUnitAt(FEMALE_TWO_POSITION).getAbilityPower(), is (40));
        assertThat(game.getUnitAt(FEMALE_THREE_POSITION).getStrength(), is (40 + 40));
        assertThat(game.getUnitAt(FEMALE_THREE_POSITION).getAbilityPower(), is (40));
    }

    @Test
    void shouldEndOpponentsTurnWhenActivatingObjection() {
            // Given black player sets trap Objection,
        game.addCardToHand(OBJECTION);
        game.playCard(OBJECTION, null, null);
            // When black player activates the trap a turn and a half later,
        endRound();
        game.endTurn();
        game.activateCard(OBJECTION, null, null, null);
            // Then white player's turn is skipped, and it is black player again.
        assertThat(game.getPlayerInTurn(), is (BLACK_PLAYER));
            // But he has taken 50 damage.
        assertThat(game.getPlayerInTurn().getCurrentHealth(), is (100 - 50));
    }

    @Test
    void shouldCleanseFriendlyUnitUponActivatingTrapSwitcheroo() {
            // Given that white player has a suppressed unit in play,
        game.endTurn();
        game.playCard(THOMAS, null, null);
        game.getUnitAt(FIRST_WHITE_SPAWN_POSITION).debuffUnit("SUPP", 9);
        assertThat(game.getUnitAt(FIRST_WHITE_SPAWN_POSITION).getDebuffs().get(Debuff.SUPPRESS), is (9));
            // When white player sets Switcheroo and later activates it,
        game.addCardToHand(OL_SWITCHEROO);
        game.playCard(OL_SWITCHEROO, null, null);
        endRound();
        game.activateCard(OL_SWITCHEROO, null, FIRST_WHITE_SPAWN_POSITION, null);
            // Then Thomas is freed from CC.
        assertThat(game.getUnitAt(FIRST_WHITE_SPAWN_POSITION).getDebuffs().get(Debuff.SUPPRESS), is (0));
    }

    @Test
    void shouldGainControlOfEnemyUnitUponActivatingTrapSwitcheroo() {
            // Given that black player has a unit in play,
        assertThat(game.getHand(BLACK_PLAYER).get(1).getName(), is (N_THOMAS));
        assertThat(game.playCard(THOMAS, null, null), is (true));
        assertThat(game.getUnitAt(FIRST_BLACK_SPAWN_POSITION).getOwner(), is (BLACK_PLAYER));
            // When white player sets Switcheroo and later activates it,
        game.endTurn();
        game.addCardToHand(OL_SWITCHEROO);
        assertThat(game.playCard(OL_SWITCHEROO, null, null), is (true));
        endRound();
        assertThat(game.activateCard(OL_SWITCHEROO, null, FIRST_BLACK_SPAWN_POSITION, null), is (true));
            // Then Thomas is now white.
        assertThat(game.getUnitAt(FIRST_BLACK_SPAWN_POSITION).getOwner(), is (WHITE_PLAYER));
    }

    @Test
    void shouldDrawCardsUponPlayingTitForTat() {
            // Given each player has drawn their starting hand,
        assertThat(game.getHand(BLACK_PLAYER).size(), is (3));
        assertThat(game.getHand(WHITE_PLAYER).size(), is (3));
            // When black player plays Tit for Tat,
        game.addCardToHand(TIT_FOR_TAT);
        game.playCard(TIT_FOR_TAT, null, null);
            // Then black player shall have 5 cards, while white player shall have 4.
        assertThat(game.getHand(BLACK_PLAYER).size(), is (5));
        assertThat(game.getHand(WHITE_PLAYER).size(), is (4));
    }

    @Test
    void shouldRecycleHandUponPlayingSaveEnvironment() {
            // Given that black player initially has Thomas, Geralt and Ciri in hand,
        assertThat(game.getHand(BLACK_PLAYER).size(), is (3));
        assertThat(game.getHand(BLACK_PLAYER).get(0).getName(), is (N_GERALT_OF_RIVIA));
        assertThat(game.getHand(BLACK_PLAYER).get(1).getName(), is (N_THOMAS));
        assertThat(game.getHand(BLACK_PLAYER).get(2).getName(), is (N_CIRI));
            // When black player plays Save the Environment,
        game.addCardToHand(SAVE_ENVIRONMENT);
        game.playCard(SAVE_ENVIRONMENT, null, null);
            // Then his hand is recycled.
        assertThat(game.getHand(BLACK_PLAYER).size(), is (4));
        assertThat(game.getHand(BLACK_PLAYER).get(0).getName(), is (N_WHITE_FLAME));
        assertThat(game.getHand(BLACK_PLAYER).get(1).getName(), is (N_HEISENBERG));
        assertThat(game.getHand(BLACK_PLAYER).get(2).getName(), is (N_DAGFINNUR));
        assertThat(game.getHand(BLACK_PLAYER).get(3).getName(), is (N_THORSTEN));
            // But white player gains 30 HP.
        assertThat(WHITE_PLAYER.getCurrentHealth(), is (130));
    }

    @Test
    void shouldRandomiseAllUnitPositionsUponPlayingHowlingAbyssingeCoos() {
            // Given that black player plays Howling Abyss,
        Set<Position> unitsPositionBefore = game.getUnits().keySet();
        assertThat(unitsPositionBefore.contains(STUDENT_ONE_POSITION), is (true));
        assertThat(unitsPositionBefore.contains(STUDENT_THREE_POSITION), is (true));
        assertThat(unitsPositionBefore.contains(FEMALE_ONE_POSITION), is (true));
        assertThat(unitsPositionBefore.contains(FEMALE_TWO_POSITION), is (true));
        assertThat(unitsPositionBefore.contains(DEMON_POSITION), is (true));
        game.addCardToHand(HOWLING_ABYSS);
        game.playCard(HOWLING_ABYSS, null, null);
        List<Position> newPositions = new ArrayList<>( game.getUnits().keySet() );

            // Then all the unit positions are randomised
        assertNotEquals(unitsPositionBefore, is (newPositions));
    }

    @Test
    void shouldGrantUnitsHPWhenNearFountain() {
            // When black player plays Fountain of Youth,
        assertThat(game.getUnitAt(LEFT_TOP_CORNER_POSITION).getRegeneration(), is (0));
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getRegeneration(), is (0));
        assertThat(game.getUnitAt(SECOND_ROW_POSITION).getRegeneration(), is (0));
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getRegeneration(), is (0));
        assertThat(game.getUnitAt(RIGHT_BOTTOM_CORNER_POSITION).getRegeneration(), is (0));
        assertThat(game.getUnitAt(LEFT_BOTTOM_CORNER_POSITION).getRegeneration(), is (0));
        game.addCardToHand(FOUNTAIN);
        game.playCard(FOUNTAIN, null, LEFT_TOP_CORNER_POSITION);
        assertThat(game.getUnitAt(LEFT_TOP_CORNER_POSITION).getRegeneration(), is (0));
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getRegeneration(), is (0));
        assertThat(game.getUnitAt(SECOND_ROW_POSITION).getRegeneration(), is (0));
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getRegeneration(), is (0));
        assertThat(game.getUnitAt(RIGHT_BOTTOM_CORNER_POSITION).getRegeneration(), is (0));
        assertThat(game.getUnitAt(LEFT_BOTTOM_CORNER_POSITION).getRegeneration(), is (0));
            // Then all units restore HP while near fountain.
        endRound();
        assertThat(game.getUnitAt(LEFT_TOP_CORNER_POSITION).getHealth(), is (110));
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getHealth(), is (110));
        assertThat(game.getUnitAt(SECOND_ROW_POSITION).getHealth(), is (110));
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getHealth(), is (110));
        assertThat(game.getUnitAt(RIGHT_BOTTOM_CORNER_POSITION).getHealth(), is (110));
        assertThat(game.getUnitAt(LEFT_BOTTOM_CORNER_POSITION).getHealth(), is (110));
    }

    @Test
    void shouldFearUnitsWhenNearCampingVan() {
            // When black player plays Ancient Camping Van,
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getRegeneration(), is (0));
        assertThat(game.getUnitAt(RIGHT_BOTTOM_CORNER_POSITION).getRegeneration(), is (0));
        assertThat(game.getUnitAt(LEFT_BOTTOM_CORNER_POSITION).getRegeneration(), is (0));
        assertThat(game.getUnitAt(LEFT_TOP_CORNER_POSITION).getStrength(), is (40));
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getStrength(), is (40));
        game.getUnitAt(SECOND_ROW_POSITION).buffUnit("FER", 1);
        assertThat(game.getUnitAt(SECOND_ROW_POSITION).getMaxFerocity(), is (2));
        game.getUnitAt(THIRD_ROW_POSITION).buffUnit("MS", 1);
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getCurrentMovementSpeed(), is (2));
        game.addCardToHand(CAMPING_VAN);
        game.playCard(CAMPING_VAN, null, LEFT_TOP_CORNER_POSITION);
            // Then all units are feared.
        assertThat(game.getUnitAt(LEFT_TOP_CORNER_POSITION).getDebuffs().get(Debuff.ROOT), is (2));
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getDebuffs().get(Debuff.ROOT), is (2));
        assertThat(game.getUnitAt(SECOND_ROW_POSITION).getDebuffs().get(Debuff.ROOT), is (2));
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getDebuffs().get(Debuff.ROOT), is (2));
        assertThat(game.getUnitAt(RIGHT_BOTTOM_CORNER_POSITION).getDebuffs().get(Debuff.ROOT), is (2));
        assertThat(game.getUnitAt(LEFT_BOTTOM_CORNER_POSITION).getDebuffs().get(Debuff.ROOT), is (2));
        assertThat(game.getUnitAt(LEFT_TOP_CORNER_POSITION).getStrength(), is (40 / 2));
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getStrength(), is (40 / 2));
        assertThat(game.getUnitAt(SECOND_ROW_POSITION).getMaxFerocity(), is (1));
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getCurrentMovementSpeed(), is (1));
        endRound();
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getRegeneration(), is (-5));
        assertThat(game.getUnitAt(RIGHT_BOTTOM_CORNER_POSITION).getRegeneration(), is (-5));
        assertThat(game.getUnitAt(LEFT_BOTTOM_CORNER_POSITION).getRegeneration(), is (-5));
    }

    @Test
    void shouldRootUnitsWhenInsideTrumpWall() {
            // When black player plays Build a Wall,
        assertThat(game.getUnitAt(LEFT_TOP_CORNER_POSITION).getDebuffs().get(Debuff.ROOT), is ( nullValue() ));
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getDebuffs().get(Debuff.ROOT), is ( nullValue() ));
        assertThat(game.getUnitAt(SECOND_ROW_POSITION).getDebuffs().get(Debuff.ROOT), is ( nullValue() ));
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getDebuffs().get(Debuff.ROOT), is ( nullValue() ));
        assertThat(game.getUnitAt(RIGHT_BOTTOM_CORNER_POSITION).getDebuffs().get(Debuff.ROOT), is ( nullValue() ));
        assertThat(game.getUnitAt(LEFT_BOTTOM_CORNER_POSITION).getDebuffs().get(Debuff.ROOT), is ( nullValue() ));
        game.addCardToHand(BUILD_A_WALL);
        game.playCard(BUILD_A_WALL, null, LEFT_TOP_CORNER_POSITION);
            // Then all units are rooted inside.
        assertThat(game.getUnitAt(LEFT_TOP_CORNER_POSITION).getDebuffs().get(Debuff.ROOT), is (10));
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getDebuffs().get(Debuff.ROOT), is (10));
        assertThat(game.getUnitAt(SECOND_ROW_POSITION).getDebuffs().get(Debuff.ROOT), is (10));
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getDebuffs().get(Debuff.ROOT), is (10));
        assertThat(game.getUnitAt(RIGHT_BOTTOM_CORNER_POSITION).getDebuffs().get(Debuff.ROOT), is (10));
        assertThat(game.getUnitAt(LEFT_BOTTOM_CORNER_POSITION).getDebuffs().get(Debuff.ROOT), is (10));
    }

    @Test
    void shouldIntoxicateUnitsNearRingriderPalace() {
            // When black player plays Ringrider Palace,
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getCurrentMovementSpeed(), is (1));
        assertThat(game.getUnitAt(RIGHT_BOTTOM_CORNER_POSITION).getAbilityPower(), is (0));
        assertThat(game.getUnitAt(LEFT_BOTTOM_CORNER_POSITION).getAbilityPower(), is (0));
        assertThat(game.getUnitAt(LEFT_TOP_CORNER_POSITION).getStrength(), is (40));
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getStrength(), is (40));
        game.getUnitAt(SECOND_ROW_POSITION).buffUnit("FER", 1);
        assertThat(game.getUnitAt(SECOND_ROW_POSITION).getMaxFerocity(), is (2));
        game.getUnitAt(THIRD_ROW_POSITION).buffUnit("MS", 1);
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getCurrentMovementSpeed(), is (2));
        assertThat(game.getUnitAt(RIGHT_BOTTOM_CORNER_POSITION).getDebuffs().get(Debuff.DRUNK), is ( nullValue() ));
        assertThat(game.getUnitAt(LEFT_BOTTOM_CORNER_POSITION).getDebuffs().get(Debuff.DRUNK), is ( nullValue() ));
        game.addCardToHand(RINGRIDER_PALACE);
        game.playCard(RINGRIDER_PALACE, null, LEFT_TOP_CORNER_POSITION);
            // Then all affected units are intoxicated.
        assertThat(game.getUnitAt(LEFT_TOP_CORNER_POSITION).getDebuffs().get(Debuff.DRUNK), is (2));
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getDebuffs().get(Debuff.DRUNK), is (2));
        assertThat(game.getUnitAt(SECOND_ROW_POSITION).getDebuffs().get(Debuff.DRUNK), is (2));
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getDebuffs().get(Debuff.DRUNK), is (2));
        assertThat(game.getUnitAt(RIGHT_BOTTOM_CORNER_POSITION).getDebuffs().get(Debuff.DRUNK), is (2));
        assertThat(game.getUnitAt(LEFT_BOTTOM_CORNER_POSITION).getDebuffs().get(Debuff.DRUNK), is (2));
        assertThat(game.getUnitAt(LEFT_TOP_CORNER_POSITION).getStrength(), is (40 - 20));
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getStrength(), is (40 - 20));
        assertThat(game.getUnitAt(THIRD_ROW_POSITION).getCurrentMovementSpeed(), is (1));
        assertThat(game.getUnitAt(RIGHT_BOTTOM_CORNER_POSITION).getAbilityPower(), is (-20));
        assertThat(game.getUnitAt(LEFT_BOTTOM_CORNER_POSITION).getAbilityPower(), is (-20));
        assertThat(game.getUnitAt(SECOND_ROW_POSITION).getMaxFerocity(), is (1));
    }

    @Test
    void shouldRejectAllAttacksInHouseOfGod() {
            // When black player plays House of God,
        assertThat(game.attackOpponent(SECOND_ROW_POSITION, THIRD_ROW_POSITION), is (true));
        game.endTurn();
        assertThat(game.attackOpponent(LEFT_TOP_CORNER_POSITION, SECOND_ROW_POSITION), is (true));
        assertThat(game.attackOpponent(THIRD_ROW_POSITION, RIGHT_TOP_CORNER_POSITION), is (true));
        game.endTurn();
        game.addCardToHand(HOUSE_OF_GOD);
        game.playCard(HOUSE_OF_GOD, null, LEFT_TOP_CORNER_POSITION);
            // Then all units may not attack inside the structure.
        assertThat(game.getUnitAt(SECOND_ROW_POSITION).getMaxFerocity(), is (1));
        assertThat(game.attackOpponent(SECOND_ROW_POSITION, THIRD_ROW_POSITION), is (false));
        game.endTurn();
        assertThat(game.attackOpponent(LEFT_TOP_CORNER_POSITION, SECOND_ROW_POSITION), is (false));
        assertThat(game.attackOpponent(THIRD_ROW_POSITION, RIGHT_TOP_CORNER_POSITION), is (false));
    }

    @Test
    void shouldRejectUnitsToSetAffinityAfterMoving() {
            // Given black player has a neutral unit in play,
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getOwner(), is (BLACK_PLAYER));
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getAffinity(), is (NO_AFFINITY));
            // When black player moves the unit,
        game.moveUnit(RIGHT_TOP_CORNER_POSITION, new Position(9 ,6));
            // He may not choose affinities this turn.
        assertThat(game.getUnitAt(new Position(9 ,6)).setAffinity(BERSERKER), is (false));
            // But when another turn passes,
        endRound();
            // He may choose an affinity.
        assertThat(game.getUnitAt(new Position(9 ,6)).setAffinity(BERSERKER), is (true));
    }

    @Test
    void shouldRejectUnitsToSetAffinityAfterAttacking() {
            // Given black player has a neutral unit in play,
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getOwner(), is (BLACK_PLAYER));
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).getAffinity(), is (NO_AFFINITY));
            // When black player attacks with the unit,
        game.attackOpponent(RIGHT_TOP_CORNER_POSITION, THIRD_ROW_POSITION);
            // He may not choose affinities this turn.
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).setAffinity(SORCERER), is (false));
            // But when another turn passes,
        endRound();
            // He may choose an affinity.
        assertThat(game.getUnitAt(RIGHT_TOP_CORNER_POSITION).setAffinity(SORCERER), is (true));
    }
}