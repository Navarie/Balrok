package variants;

import common.GameImpl;
import common.UnitImpl;
import framework.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import variants.configuration.EpsilonFactoryBuilder;


import static constants.CardConstants.*;
import static constants.GameConstants.*;
import static constants.UnitConstants.*;
import static constants.SpellConstants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class EpsilonRok {

    private Game game;

    private static final Position SECOND_BLACK_HUMAN_POS = new Position(1, 2);
    private static final Position BOTTOM_RIGHT_CORNER = new Position(11, 10);
    private static final Position SECOND_BLACK_UNIT_POS = new Position(9, 10);
    private static final Position THIRD_BLACK_UNIT_POS = new Position(8, 10);
    private static final Position FOURTH_BLACK_UNIT_POS = new Position(7, 10);
    private static final Position UPPER_LEFT_CORNER = new Position(1, 0);

    private static final Position UPPER_LEFT_BOARD_CENTER = new Position(0, 2);
    private static final Position UPPER_RIGHT_BOARD_CENTER = new Position(12, 2);
    private static final Position LOWER_RIGHT_BOARD_CENTER = new Position(12, 8);
    private static final Position LOWER_LEFT_BOARD_CENTER = new Position(0, 8);
    private static final Position WHITE_ELF_POS = new Position(1, 3);
    private static final Position WHITE_DRAGON_POS = new Position(5, 5);
    private static final Position WHITE_FURRY_POS = new Position(6, 7);
    private static final Position WHITE_HUMAN_POS = new Position(2, 0);
    private static final Position BLACK_HUMAN_POS = new Position(10, 10);
    private static final Position BLACK_DWARF_POS = new Position(10, 4);
    private static final Position BLACK_SIREN_POS = new Position(7, 6);
    private static final Position BLACK_LESHY_POS = new Position(9, 8);
    private static final Position WHITE_DWARF_POS = new Position(2, 1);
    private static final Position THIRD_BLACK_HUMAN_POS = new Position(2, 2);


    @BeforeEach
    void setup() {
            // Given a default Epsilon game setting,
        game = new GameImpl(new EpsilonFactoryBuilder().build());
    }

    private void endRound() {
        game.endTurn();
        game.endTurn();
    }

    @Test
    void shouldKillEnemyUnitUponActivatingRitoClient() {
            // Given black player has initialised Thomas,
        game.addCardToHand(THOMAS);
        game.playCard(THOMAS, null, null);
            // When white player activates Report on Thomas,
        game.endTurn();
        game.addCardToHand(RITO_CLIENT);
        int whitePlayerManaBefore = game.getPlayers().get(1).getCurrentMana();
        game.playCard(RITO_CLIENT, game.getUnitAt(BOTTOM_RIGHT_CORNER), null);
            // Then Thomas dies.
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER), is ( nullValue() ));
            // And 70 mana has been depleted by white player.
        assertThat(game.getPlayers().get(1).getCurrentMana(), is (whitePlayerManaBefore - EPSILON_MAGIC_CARD_COST));
    }

    @Test
    void shouldPlaceBlackUnitOn12_0UponSummoning() {
            // When black player spawns a unit,
        game.playCard(GERALT_OF_RIVIA, null, null);

            // Then it shall appear, first, in the bottom right corner.
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getName(), is (N_GERALT_OF_RIVIA));
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getOwner(), is (BLACK_PLAYER));
    }

    @Test
    void shouldPlaceUnitsOnExpectedPositions() {
            // Black player summons a unit,
        game.playCard(GERALT_OF_RIVIA, null, null);
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getName(), is (N_GERALT_OF_RIVIA));
            // White player summons a unit at p(1, 0).
        game.endTurn();
        game.playCard(THOMAS, null, null);
        assertThat(game.getUnitAt(UPPER_LEFT_CORNER).getName(), is (N_THOMAS));
            // Black player summons a unit at p(9, 10), since p(10, 10) is occupied.
        game.endTurn();
        game.addCardToHand(STATHAM);
        game.playCard(STATHAM, null, null);
        assertThat(game.getUnitAt(new Position(9, 10)).getName(), is (N_STATHAM));
            // White player summons a unit at p(3, 0), since p(2, 0) is occupied.
        game.endTurn();
        game.addCardToHand(DAGFINNUR);
        game.playCard(DAGFINNUR, null, null);
        assertThat(game.getUnitAt(new Position(3, 0)).getName(), is (N_DAGFINNUR));
    }

    @Test
    void shouldDisallowPlayingCardsNotInHand() {
            // Given black player draws 3 cards at the start of game,
        assertThat(game.getHand(BLACK_PLAYER).size(), is (3));
            // And that the three following always appear due to the non-randomised deck-list,
        assertThat(game.getHand(BLACK_PLAYER).get(0).getName(), is (N_GERALT_OF_RIVIA));
        assertThat(game.getHand(BLACK_PLAYER).get(1).getName(), is (N_THOMAS));
        assertThat(game.getHand(BLACK_PLAYER).get(2).getName(), is (N_CIRI));
            // When black player attempts to play Dagfinnur,
            // Then it is rejected.
        assertThat(game.playCard(DAGFINNUR, null, null), is (false));
        assertThat(game.playCard(GERALT_OF_RIVIA, null, null), is (true));
    }

    @Test
    void shouldAllowUpTo10CardsInHand() {
            // Given white player is in turn,
        game.endTurn();
            // They should have 3 cards in hand.
        assertThat(game.getHand(WHITE_PLAYER).size(), is (3));
            // And when 7 turns pass,
        for (int i=0; i < 7; i++) {
            endRound();
        }
            // They should have 10.
        assertThat(game.getHand(WHITE_PLAYER).size(), is (10));
            // But when another turn passes,
        endRound();
            // The card to be drawn is burned (removed from game) instead.
        assertThat(game.getHand(WHITE_PLAYER).size(), is (10));
    }

    @Test
    void shouldDraw3CardsThenSkipTurnUponActivatingPotOfGreed() {
            // Given white player is in turn,
        game.endTurn();
            // They should have 3 cards in hand.
        assertThat(game.getHand(WHITE_PLAYER).size(), is (3));
            // And when they activate Pot of Greed,
        game.addCardToHand(POT_OF_GREED);
        game.playCard(POT_OF_GREED, null, null);
            // They shall have 6 cards in hand, but black player is now in turn.
        assertThat(game.getHand(WHITE_PLAYER).size(), is (6));
        assertThat(game.getPlayerInTurn(), is (BLACK_PLAYER));
    }

    @Test
    void shouldGrantSelectedUnitAffiliationsUponInitialisation() {
            // Given Benseboy and Thomas are initialised,
        game.addCardToHand(BENSE);
        game.playCard(THOMAS, null, null);
        game.playCard(BENSE, null, null);
            // They should have Alssund and State School as affiliations, respectively.
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getAffiliation(), is (STATE_SCHOOL));
        assertThat(game.getUnitAt(SECOND_BLACK_UNIT_POS).getAffiliation(), is (ALSSUND));
    }

    @Test
    void shouldRemoveAffiliationAndOppressUnitFor6TurnsUponPlayingSuspension() {
            // Given black player has initialised Thomas from The State School,
        game.playCard(THOMAS, null, null);
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getAffiliation(), is (STATE_SCHOOL));
            // When white player activates Suspension on Thomas,
        game.endTurn();
        game.addCardToHand(SUSPENSION);
        game.playCard(SUSPENSION, game.getUnitAt(BOTTOM_RIGHT_CORNER), null);
            // Then Thomas is suspended and suppressed for 6 turns.
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getAffiliation(), is (NO_AFFILIATION));
        assertThat(((UnitImpl) game.getUnitAt(BOTTOM_RIGHT_CORNER)).getDebuffs().get(Debuff.SUPPRESS), is (6));
    }

    @Test
    void shouldBanTargetUnitFor10TurnsUponActivatingReport() {
            // Given black player has initialised Thomas,
        game.playCard(THOMAS, null, null);
            // When white player activates Report on Thomas,
        game.endTurn();
        game.addCardToHand(REPORT);
        game.playCard(REPORT, game.getUnitAt(BOTTOM_RIGHT_CORNER), null);
            // Then Thomas is banned for 5 turns, whereafter he shall respawn.
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER), is ( nullValue() ));
        for (int i=0; i < 5; i++) {
            endRound(); }
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getName(), is ( N_THOMAS ));
    }

    @Test
    void shouldBuffUnitsBelongingToStateSchoolUponPlayingTheStateSchool() {
            // Given a setup with 3 units from State School
        game.addCardToHand(HEISENBERG);
        game.addCardToHand(HJOERDIS);
        game.addCardToHand(F_STATE_SCHOOL);
        game.addCardToHand(MATHIAS);
        game.playCard(THOMAS, null, null);
        game.playCard(HEISENBERG, null, null);
        game.playCard(HJOERDIS, null, null);
        game.playCard(MATHIAS, null, null);
            // When black player activates The State School,
        int ThomasSTRBefore = game.getUnitAt(BOTTOM_RIGHT_CORNER).getStrength();
        int ThomasAPBefore = game.getUnitAt(BOTTOM_RIGHT_CORNER).getAbilityPower();
        int HeisenbergSTRBefore = game.getUnitAt(SECOND_BLACK_UNIT_POS).getStrength();
        int HeisenbergAPBefore = game.getUnitAt(SECOND_BLACK_UNIT_POS).getAbilityPower();
        int HjoerdisSTRBefore = game.getUnitAt(THIRD_BLACK_UNIT_POS).getStrength();
        int HjoerdisAPBefore = game.getUnitAt(THIRD_BLACK_UNIT_POS).getAbilityPower();
        int MathiasSTRBefore = game.getUnitAt(FOURTH_BLACK_UNIT_POS).getStrength();
        int MathiasAPBefore = game.getUnitAt(FOURTH_BLACK_UNIT_POS).getAbilityPower();
        game.playCard(F_STATE_SCHOOL, null, null);
            // Then all units have increased stats.
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getName(), is (N_THOMAS));
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getAffiliation(), is (STATE_SCHOOL));
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getStrength(), is (ThomasSTRBefore + 30));
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getAbilityPower(), is (ThomasAPBefore + 20));
        assertThat(game.getUnitAt(SECOND_BLACK_UNIT_POS).getName(), is (N_HEISENBERG));
        assertThat(game.getUnitAt(SECOND_BLACK_UNIT_POS).getAffiliation(), is (STATE_SCHOOL));
        assertThat(game.getUnitAt(SECOND_BLACK_UNIT_POS).getStrength(), is (HeisenbergSTRBefore + 30));
        assertThat(game.getUnitAt(SECOND_BLACK_UNIT_POS).getAbilityPower(), is (HeisenbergAPBefore + 20));
        assertThat(game.getUnitAt(THIRD_BLACK_UNIT_POS).getName(), is (N_HJOERDIS));
        assertThat(game.getUnitAt(THIRD_BLACK_UNIT_POS).getAffiliation(), is (STATE_SCHOOL));
        assertThat(game.getUnitAt(THIRD_BLACK_UNIT_POS).getStrength(), is (HjoerdisSTRBefore + 30));
        assertThat(game.getUnitAt(THIRD_BLACK_UNIT_POS).getAbilityPower(), is (HjoerdisAPBefore + 20));
            // Except Mathias.
        assertThat(game.getUnitAt(FOURTH_BLACK_UNIT_POS).getName(), is (N_MATHIAS));
        assertThat(game.getUnitAt(FOURTH_BLACK_UNIT_POS).getAffiliation(), is (STATE_SCHOOL));
        assertThat(game.getUnitAt(FOURTH_BLACK_UNIT_POS).getStrength(), is (MathiasSTRBefore));
        assertThat(game.getUnitAt(FOURTH_BLACK_UNIT_POS).getAbilityPower(), is (MathiasAPBefore));
    }

    @Test
    void shouldBuffUnitsBelongingToAlssundUponPlayingAlssundHigh() {
            // Given that black player has two units from Alssund,
        game.addCardToHand(BENSE);
        game.addCardToHand(TYRONE);
        game.playCard(BENSE, null, null);
        game.playCard(TYRONE, null, null);
            // When black player activates Alssund,
        int BenseSTRBefore = game.getUnitAt(BOTTOM_RIGHT_CORNER).getStrength();
        int BenseAPBefore = game.getUnitAt(BOTTOM_RIGHT_CORNER).getAbilityPower();
        int TyroneSTRBefore = game.getUnitAt(SECOND_BLACK_UNIT_POS).getStrength();
        int TyroneAPBefore = game.getUnitAt(SECOND_BLACK_UNIT_POS).getAbilityPower();
        game.addCardToHand(F_ALSSUND);
        game.playCard(F_ALSSUND, null, null);
            // Then all units have increased stats.
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getName(), is (N_BENSE));
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getAffiliation(), is (ALSSUND));
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getStrength(), is (BenseSTRBefore + 20));
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getAbilityPower(), is (BenseAPBefore + 30));
        assertThat(game.getUnitAt(SECOND_BLACK_UNIT_POS).getName(), is (N_TYRONE));
        assertThat(game.getUnitAt(SECOND_BLACK_UNIT_POS).getAffiliation(), is (ALSSUND));
        assertThat(game.getUnitAt(SECOND_BLACK_UNIT_POS).getStrength(), is (TyroneSTRBefore + 20));
        assertThat(game.getUnitAt(SECOND_BLACK_UNIT_POS).getAbilityPower(), is (TyroneAPBefore + 30));
    }

    @Test
    void shouldKillWeakUnitsUponActivatingShotgun() {
            // Given that black player has two weak units,
        game.addCardToHand(THORSTEN);
        game.addCardToHand(EMILY_COON);
        game.playCard(THORSTEN, null, null);
        game.playCard(EMILY_COON, null, null);
            // When white player activates Shotgun on Thorsten,
        game.endTurn();
        game.addCardToHand(SHOTGUN);
        game.playCard(SHOTGUN, game.getUnitAt(BOTTOM_RIGHT_CORNER), null);
            // Then Thorsten dies.
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER), is ( nullValue() ));
        assertThat(game.getDeadUnitsAtCemetery(EPSILON_BLACK_CEMETERY_POS).size(), is (1));
            // And when white player activates Shotgun on Emily,
        game.addCardToHand(SHOTGUN);
        game.playCard(SHOTGUN, game.getUnitAt(SECOND_BLACK_UNIT_POS), null);
            // She dies.
        assertThat(game.getUnitAt(SECOND_BLACK_UNIT_POS), is ( nullValue() ));
        assertThat(game.getDeadUnitsAtCemetery(EPSILON_BLACK_CEMETERY_POS).size(), is (2));
    }

    @Test
    void shouldSuppressAndWeakenUnitUponActivatingRicke() {
            // Given black player has Blue-Eyes in play,
        game.addCardToHand(BLUE_EYES);
        game.playCard(BLUE_EYES, null, null);
            // When white player sets Ricke, and later activates it,
        game.endTurn();
        game.addCardToHand(RICKE);
        game.playCard(RICKE, null, null);
        endRound();
        int BlueEyesHPBefore = game.getUnitAt(BOTTOM_RIGHT_CORNER).getHealth();
        game.activateCard(RICKE, null, BOTTOM_RIGHT_CORNER, null);
            // Then Blue-Eyes is suppressed for 4 turns.
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getDebuffs().get(Debuff.SUPPRESS), is (4));
        endRound();
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getDebuffs().get(Debuff.SUPPRESS), is (2));
        endRound();
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getDebuffs().get(Debuff.SUPPRESS), is (0));
            // And its stats are weakened.
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getHealth(), is (BlueEyesHPBefore - (BlueEyesHPBefore - 1)));
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getStrength(), is (0));
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getAbilityPower(), is (0));
    }

    @Test
    void shouldKillAllUnitsInBoardCenterUponPlayingBlackHole() {
            // Given that black and white player have units in assorted positions in the board center,
        assertThat(game.getUnitAt(UPPER_LEFT_BOARD_CENTER), is ( notNullValue() ));
        assertThat(game.getUnitAt(UPPER_RIGHT_BOARD_CENTER), is ( notNullValue() ));
        assertThat(game.getUnitAt(LOWER_LEFT_BOARD_CENTER), is ( notNullValue() ));
        assertThat(game.getUnitAt(LOWER_RIGHT_BOARD_CENTER), is ( notNullValue() ));
        assertThat(game.getUnitAt(WHITE_ELF_POS), is ( notNullValue() ));
        assertThat(game.getUnitAt(WHITE_DRAGON_POS), is ( notNullValue() ));
        assertThat(game.getUnitAt(WHITE_FURRY_POS), is ( notNullValue() ));
        assertThat(game.getUnitAt(BLACK_DWARF_POS), is ( notNullValue() ));
        assertThat(game.getUnitAt(BLACK_SIREN_POS), is ( notNullValue() ));
        assertThat(game.getUnitAt(BLACK_LESHY_POS), is ( notNullValue() ));
            // When white player plays Black Hole,
        game.endTurn();
        game.addCardToHand(BLACK_HOLE);
        game.playCard(BLACK_HOLE, null, null);
            // Then all units have been killed and sent to their cemetery.
        assertThat(game.getUnitAt(UPPER_LEFT_BOARD_CENTER), is ( nullValue() ));
        assertThat(game.getUnitAt(UPPER_RIGHT_BOARD_CENTER), is ( nullValue() ));
        assertThat(game.getUnitAt(LOWER_LEFT_BOARD_CENTER), is ( nullValue() ));
        assertThat(game.getUnitAt(LOWER_RIGHT_BOARD_CENTER), is ( nullValue() ));
        assertThat(game.getUnitAt(WHITE_ELF_POS), is ( nullValue() ));
        assertThat(game.getUnitAt(WHITE_DRAGON_POS), is ( nullValue() ));
        assertThat(game.getUnitAt(WHITE_FURRY_POS), is ( nullValue() ));
        assertThat(game.getUnitAt(BLACK_DWARF_POS), is ( nullValue() ));
        assertThat(game.getUnitAt(BLACK_SIREN_POS), is ( nullValue() ));
        assertThat(game.getUnitAt(BLACK_LESHY_POS), is ( nullValue() ));
    }

    @Test
    void shouldRegenerateHealthPerTurnUponPlayingWaffleboat() {
            // Given that white player has two units in play,
        game.endTurn();
        game.playCard(THOMAS, null, null);
        game.addCardToHand(DAGFINNUR);
        game.playCard(DAGFINNUR, null, null);
            // When white player plays Waffleboat,
        game.addCardToHand(WAFFLEBOAT);
        game.playCard(WAFFLEBOAT, null, null);
            // Then the units shall regen 20 HP per turn.
        assertThat(game.getUnitAt(UPPER_LEFT_CORNER).getHealth(), is (ST_THOMAS[2]));
        assertThat(game.getUnitAt(new Position(3, 0)).getHealth(), is (ST_DAGFINNUR[2]));
        endRound();
        assertThat(game.getUnitAt(UPPER_LEFT_CORNER).getHealth(), is (ST_THOMAS[2] + 20));
        assertThat(game.getUnitAt(new Position(3, 0)).getHealth(), is (ST_DAGFINNUR[2] + 20));
            // Until 4 turns have passed.
        endRound();
        endRound();
        endRound();
        assertThat(game.getUnitAt(UPPER_LEFT_CORNER).getHealth(), is (ST_THOMAS[2] + 80));
        assertThat(game.getUnitAt(new Position(3, 0)).getHealth(), is (ST_DAGFINNUR[2] + 80));
        endRound();
        assertThat(game.getUnitAt(UPPER_LEFT_CORNER).getHealth(), is (ST_THOMAS[2] + 80));
        assertThat(game.getUnitAt(new Position(3, 0)).getHealth(), is (ST_DAGFINNUR[2] + 80));
    }

    @Test
    void shouldGrantStrengthUponPlayingAverageSword() {
            // Given that black player has Thomas in play
        game.playCard(THOMAS, null, null);
            // When black player plays Average Sword on Thomas,
        game.addCardToHand(AVERAGE_SWORD);
        int ThomasSTRBefore = game.getUnitAt(BOTTOM_RIGHT_CORNER).getStrength();
        game.playCard(AVERAGE_SWORD, game.getUnitAt(BOTTOM_RIGHT_CORNER), null);
            // His strength is increased.
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getStrength(), is (ThomasSTRBefore + 80));
    }

    @Test
    void shouldRegenerateUnitHealthUponIncreasingRegeneration() {
            // Given that black player has two units in play,
        game.playCard(THOMAS, null, null);
        game.addCardToHand(EMILY_COON);
        game.playCard(EMILY_COON, null, null);
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getRegeneration(), is (0));
        assertThat(game.getUnitAt(SECOND_BLACK_UNIT_POS).getRegeneration(), is (0));
        int ThomasHPBefore = game.getUnitAt(BOTTOM_RIGHT_CORNER).getHealth();
        int EmilyHPBefore = game.getUnitAt(SECOND_BLACK_UNIT_POS).getHealth();
            // When the units' regeneration is buffed,
        game.getUnitAt(BOTTOM_RIGHT_CORNER).buffUnit("RGN", 10);
        game.getUnitAt(SECOND_BLACK_UNIT_POS).buffUnit("RGN", 10);
            // Then they gain health every turn.
        endRound();
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getHealth(), is (ThomasHPBefore + 10));
        assertThat(game.getUnitAt(SECOND_BLACK_UNIT_POS).getHealth(), is (EmilyHPBefore + 10));
        endRound();
        endRound();
        assertThat(game.getUnitAt(BOTTOM_RIGHT_CORNER).getHealth(), is (ThomasHPBefore + 30));
        assertThat(game.getUnitAt(SECOND_BLACK_UNIT_POS).getHealth(), is (EmilyHPBefore + 30));
    }

    @Test
    void shouldSuppressUnitDefenderWhenAttackingAsBense() {
            // Given that black player has a human in play,
        assertThat(game.getUnitAt(SECOND_BLACK_HUMAN_POS).getUnitRace(), is (HUMAN));
        assertThat(game.getUnitAt(SECOND_BLACK_HUMAN_POS).getOwner(), is (BLACK_PLAYER));
            // When white player attacks Thomas with Bense,
        game.endTurn();
        game.addCardToHand(BENSE);
        game.playCard(BENSE, null, null);
        assertThat(game.getUnitAt(UPPER_LEFT_CORNER).getName(), is (N_BENSE));
        assertThat(game.moveUnit(UPPER_LEFT_CORNER, new Position(1, 1)), is (true));
        assertThat(game.attackOpponent(new Position(1, 1), SECOND_BLACK_HUMAN_POS), is (true));
        assertThat(game.getUnitAt(SECOND_BLACK_HUMAN_POS).getDebuffs().get(Debuff.SUPPRESS), is (3));
    }

    @Test
    void shouldEnrageGermanKidWhenEnteringLowHP() {
            // Given that white player has German Kid in play,
        game.endTurn();
        game.addCardToHand(GERMAN_KID);
        game.playCard(GERMAN_KID, null, null);
            // When white player attacks German Kid, causing him to fall below 60 HP,
        assertThat(game.getUnitAt(UPPER_LEFT_CORNER).getName(), is (N_GERMAN_KID));
        assertThat(game.getUnitAt(UPPER_LEFT_CORNER).getCurrentMovementSpeed(), is (1));
        assertThat(game.getUnitAt(UPPER_LEFT_CORNER).getOwner(), is (WHITE_PLAYER));
        assertThat(game.moveUnit(UPPER_LEFT_CORNER, new Position(1, 1)), is (true));
        int germanKidHPBefore = game.getUnitAt(new Position(1, 1)).getHealth();
        int germanKidFERBefore = game.getUnitAt(new Position(1, 1)).getMaxFerocity();
        int germanKidSTRBefore = game.getUnitAt(new Position(1, 1)).getStrength();
        assertThat(game.attackOpponent(SECOND_BLACK_HUMAN_POS, new Position(1, 1)), is (true));
        assertThat(game.getUnitAt(new Position(1, 1)).getHealth(), is (germanKidHPBefore - 40));
        assertThat(game.getUnitAt(new Position(1, 1)).getMaxFerocity(), is (germanKidFERBefore + 15));
        assertThat(game.getUnitAt(new Position(1, 1)).getStrength(), is (germanKidSTRBefore + 10));
    }

    @Test
    void shouldKillAllUnitsIn3x3GridWhenDealingAFatalBlowAsTyrone() {
            // Given that white player has Tyrone in play,
        game.endTurn();
        game.addCardToHand(TYRONE);
        game.playCard(TYRONE, null, null);
            // When white player deals a fatal blow to the black human at p(1, 2),
        assertThat(game.moveUnit(UPPER_LEFT_CORNER, new Position(1, 1)), is (true));
        assertThat(game.attackOpponent(new Position(1, 1), SECOND_BLACK_HUMAN_POS), is (true));
        assertThat(game.getUnitAt(WHITE_ELF_POS), is ( nullValue() ));
        assertThat(game.getUnitAt(SECOND_BLACK_HUMAN_POS), is ( nullValue() ));
        assertThat(game.getUnitAt(UPPER_LEFT_BOARD_CENTER), is ( nullValue() ));
        assertThat(game.getUnitAt(WHITE_DWARF_POS), is ( nullValue() ));
        assertThat(game.getUnitAt(THIRD_BLACK_HUMAN_POS), is ( nullValue() ));
    }
}