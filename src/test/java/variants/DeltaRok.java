package variants;

import common.GameImpl;
import common.PlayerImpl;
import framework.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import variants.configuration.DeltaFactoryBuilder;

import static constants.CardConstants.*;
import static constants.GameConstants.*;
import static constants.UnitConstants.*;
import static constants.SpellConstants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class DeltaRok {

    public static final Position MATHIAS_POS = new Position(5, 0);
    public static final Position DAGFINNUR_POS = new Position(4, 0);
    private Game game;

    private static final Position TRAP_CARD_POS = new Position(6, 0);
    private static final Position WHITE_HUMAN_POS = new Position(6, 0);
    private static final Position SECOND_WHITE_HUMAN_POS = new Position(5, 1);
    private static final Position BLACK_DWARF_POS = new Position(6, 1);
    private static final Position BLACK_FURRY_POS = new Position(6, 2);
    private static final Position BLACK_DRAGON_POS = new Position(6, 3);
    private static final Position BLACK_LESHY_POS = new Position(6, 4);
    private static final Position BLUE_EYES_POS = new Position(4, 8);
    private static final Position WHITE_CEMETERY_POS = new Position(1, 0);
    private static final Position OZITY_POS = new Position(4, 7);
    private static final Position OZITY_TARGET_POS = new Position(5, 7);
    private static final Position SECOND_OZITY_TARGET_POS = new Position(5, 6);
    private static final Position THIRD_OZITY_TARGET_POS = new Position(5, 5);
    private static final Position FOURTH_OZITY_TARGET_POS = new Position(5, 4);
    private static final Position THOMAS_POS = new Position(4, 6);
    private static final Position LEGOLAS_POS = new Position(4, 5);
    private static final Position GORDON_POS = new Position(4, 4);
    private static final Position HJOERDIS_POS = new Position(4, 3);

    @BeforeEach
    void setup() {
        game = new GameImpl(new DeltaFactoryBuilder().build());
    }

    private void endRound() {
        game.endTurn();
        game.endTurn();
    }

    @Test
    void shouldPlaceTrapCardOnBackRow() {
            // Given a default DeltaRok game,
            // When black player plays a trap card,
        assertThat(game.playCard(RENE_DESCARTES, null, null), is (true));
            // Then it is placed on p(6, 0).
        assertThat(((GameImpl) game).getTrapCardAt(TRAP_CARD_POS), is (RENE_DESCARTES));
    }

    @Test
    void shouldAllowActivatingTrapCardARoundAfterBeingSet() {
            // When black player plays a trap card,
        assertThat(game.playCard(RENE_DESCARTES, null, null), is (true));
            // And a round passes,
        endRound();
            // Then black player can activate the trap card.
        assertThat(game.activateCard(RENE_DESCARTES, null, null, WHITE_PLAYER), is (true));
    }

    @Test
    void shouldDamagePlayerUponActivatingDescartes() {
            // Given that black player set a trap card,
        assertThat(game.playCard(RENE_DESCARTES, null, null), is (true));
        endRound();
            // When black activates the trap,
        assertThat(game.getPlayers().get(1).getCurrentHealth(), is (PlayerImpl.STARTING_HEALTH));
        assertThat(game.activateCard(RENE_DESCARTES, null, null, game.getPlayers().get(1)), is (true));
        // Then white player takes damage equal to ((10 + turnPlayed = 2) + 10% missing_HP) = 12.
        assertThat(game.getPlayers().get(1).getCurrentHealth(), is (88));
    }

    @Test
    void shouldRemoveTrapFromBoardAfterEffectFinishes() {
            // Given that black player sets a trap card,
        assertThat(game.playCard(RENE_DESCARTES, null, null), is (true));
        endRound();
            // When black activates the trap,
        assertThat(game.activateCard(RENE_DESCARTES, null, null, WHITE_PLAYER), is (true));
        assertThat(((GameImpl) game).getTrapCardAt(TRAP_CARD_POS), is ( nullValue() ));
    }

    @Test
    void shouldSendUnitBackToWhereTheyBelongWhenUsingInger() {
            // Given that black player sets a demonic trap card,
        assertThat(game.playCard(INGER, null, null), is (true));
            // When black activates the trap,
        assertThat(game.activateCard(INGER, null, WHITE_HUMAN_POS, null), is (true));
            // Then the affected unit is sent to the back row.
        assertThat(game.getUnitAt(new Position(0, 0)).getUnitRace(), is (HUMAN));
        assertThat(game.getUnitAt(new Position(0, 0)).getOwner(), is (WHITE_PLAYER));
    }

    @Test
    void shouldEnsnareUnitUponActivatingFarmerJones() {
            // Given that black player sets a trap card,
        assertThat(game.playCard(JONAS, null, null), is (true));
            // When black activates the trap,
        endRound();
        assertThat(game.activateCard(JONAS, null, WHITE_HUMAN_POS, null), is (true));
            // Then the affected unit rooted for five turns.
        game.endTurn();
        assertThat(game.moveUnit(WHITE_HUMAN_POS, new Position(5, 0)), is (false));
        endRound();
        assertThat(game.moveUnit(WHITE_HUMAN_POS, new Position(5, 0)), is (false));
        endRound();
        assertThat(game.moveUnit(WHITE_HUMAN_POS, new Position(5, 0)), is (true));
    }

    @Test
    void shouldBuffAllMeleeUnitsUponActivatingBattlegrounds() {
            // Given a default setting of melee units having 40 strength,
        assertThat(game.getUnitAt(BLACK_FURRY_POS).getStrength(), is (40));
        assertThat(game.getUnitAt(BLACK_LESHY_POS).getStrength(), is (40));
        assertThat(game.getUnitAt(BLACK_DWARF_POS).getStrength(), is (40));
        assertThat(game.getUnitAt(BLACK_DRAGON_POS).getStrength(), is (40));
        game.getUnitAt(BLACK_DWARF_POS).setAffinity(HUNTER);
            // When black player plays Battlegrounds,
        game.playCard(BATTLEGROUNDS, null, null);
            // Then his melee units shall have +10 strength.
        assertThat(game.getUnitAt(BLACK_DWARF_POS).getStrength(), is (50));
        assertThat(game.getUnitAt(BLACK_DRAGON_POS).getStrength(), is (40));
        assertThat(game.getUnitAt(BLACK_FURRY_POS).getStrength(), is (50));
        assertThat(game.getUnitAt(BLACK_LESHY_POS).getStrength(), is (50));
    }

    @Test
    void shouldHaveDefaultValueOf25APToCasters() {
            // Given a default game setting,
            // When a unit gets the affinity sorcerer,
        assertThat(game.getUnitAt(BLACK_FURRY_POS).getStrength(), is (40));
        assertThat(game.getUnitAt(BLACK_FURRY_POS).getAbilityPower(), is (0));
        assertThat(game.getUnitAt(BLACK_LESHY_POS).getAbilityPower(), is (25));
        game.getUnitAt(BLACK_FURRY_POS).setAffinity(SORCERER);
            // Then the unit shall have reduced strength and increased AP.
        assertThat(game.getUnitAt(BLACK_FURRY_POS).getStrength(), is (15));
        assertThat(game.getUnitAt(BLACK_FURRY_POS).getAbilityPower(), is (25));
    }

    @Test
    void shouldBuffCastersWith10APUponActivatingMagistrate() {
            // Given a default setting of casters units having 25 ability power,
        assertThat(game.getUnitAt(BLACK_LESHY_POS).getAbilityPower(), is (25));
        game.getUnitAt(BLACK_DWARF_POS).setAffinity(SORCERER);
        assertThat(game.getUnitAt(BLACK_DWARF_POS).getAbilityPower(), is (25));
            // When black player plays Magistrate,
        game.playCard(MAGISTRATE, null, null);
            // Then his caster units shall have +10 strength.
        assertThat(game.getUnitAt(BLACK_LESHY_POS).getAbilityPower(), is (35));
        assertThat(game.getUnitAt(BLACK_DWARF_POS).getAbilityPower(), is (35));
    }

    @Test
    void shouldAllowTransitioningFromDefaultOrientation() {
            // Given a default game setting,
            // Units shall default to male.
        assertThat(game.getUnitAt(WHITE_HUMAN_POS).getUnitGender(), is (MALE));
        assertThat(game.getUnitAt(BLACK_LESHY_POS).getUnitGender(), is (MALE));
        assertThat(game.getUnitAt(BLACK_DWARF_POS).getUnitGender(), is (MALE));
        assertThat(game.getUnitAt(BLACK_DRAGON_POS).getUnitGender(), is (MALE));
            // But when a unit elects to another orientation,
        game.getUnitAt(BLACK_DRAGON_POS).setGender(TRANSGENDER);
        game.getUnitAt(BLACK_LESHY_POS).setGender(FEMALE);
            // Then it is reflected in the unit.
        assertThat(game.getUnitAt(BLACK_DRAGON_POS).getUnitGender(), is (TRANSGENDER));
        assertThat(game.getUnitAt(BLACK_LESHY_POS).getUnitGender(), is (FEMALE));
    }

    @Test
    void shouldBuffMaleUnitsWhenAttackingFemaleUnitsUponActivatingStoneburg() {
            // Given white players has two female units and black has two male units,
        game.getUnitAt(WHITE_HUMAN_POS).setGender(FEMALE);
        game.getUnitAt(SECOND_WHITE_HUMAN_POS).setGender(FEMALE);
        assertThat(game.getUnitAt(WHITE_HUMAN_POS).getUnitGender(), is (FEMALE));
        assertThat(game.getUnitAt(SECOND_WHITE_HUMAN_POS).getUnitGender(), is (FEMALE));
        game.getUnitAt(WHITE_HUMAN_POS).setUnitStat("HP", 300);
        assertThat(game.getUnitAt(WHITE_HUMAN_POS).getHealth(), is (300));
        game.getUnitAt(SECOND_WHITE_HUMAN_POS).setUnitStat("HP", 300);
        assertThat(game.getUnitAt(SECOND_WHITE_HUMAN_POS).getHealth(), is (300));
        assertThat(game.getUnitAt(BLACK_FURRY_POS).getUnitGender(), is (MALE));
        assertThat(game.getUnitAt(BLACK_DWARF_POS).getUnitGender(), is (MALE));
        assertThat(game.getUnitAt(BLACK_FURRY_POS).getStrength(), is (40));
        assertThat(game.getUnitAt(BLACK_DWARF_POS).getStrength(), is (40));
            // When black player activates Stoneburg,
        game.playCard(STONEBURG, null, null);
            // And when black male units attack the female units,
        game.attackOpponent(BLACK_FURRY_POS, SECOND_WHITE_HUMAN_POS);
        game.attackOpponent(BLACK_DWARF_POS, WHITE_HUMAN_POS);
            // Then the female units take (40 STR + 50 STR) damage.
        assertThat(game.getUnitAt(WHITE_HUMAN_POS).getHealth(), is (170));
        assertThat(game.getUnitAt(SECOND_WHITE_HUMAN_POS).getHealth(), is (170));
    }

    @Test
    void shouldSpawnBlueEyesOnChosenPosition() {
            // Given a default game setting,
            // When black player plays Blue-Eyes,
        game.addCardToHand(BLUE_EYES);
        game.playCard(BLUE_EYES, null, BLUE_EYES_POS);
            // Then it is retrievable from its position.
        assertThat(game.getUnitAt(BLUE_EYES_POS).getUnitRace(), is (PRIMORDIAL_DRAGON));
        assertThat(game.getUnitAt(BLUE_EYES_POS).getUnitType(), is (MONSTER));
        assertThat(game.getUnitAt(BLUE_EYES_POS).getAbilityPower(), is (3000));
        assertThat(game.getUnitAt(BLUE_EYES_POS).getStrength(), is (2500));
        assertThat(game.getUnitAt(BLUE_EYES_POS).getCurrentMovementSpeed(), is (1));
        assertThat(game.getUnitAt(BLUE_EYES_POS).getMaxFerocity(), is (1));
        assertThat(game.getUnitAt(BLUE_EYES_POS).getHealth(), is (200));
    }

    @Test
    void shouldAllowOzityToAttackOnlyThreeTimes() {
            // Given black player plays Ozity,
        game.addCardToHand(OZITY);
        game.playCard(OZITY, null, OZITY_POS);
            // When he attacks white humans in succession,
        assertThat(game.getUnitAt(OZITY_TARGET_POS).getOwner(), is (WHITE_PLAYER));
        assertThat(game.getUnitAt(OZITY_POS).getMaxFerocity(), is (3));
        assertThat(game.attackOpponent(OZITY_POS, OZITY_TARGET_POS), is (true));
        assertThat(game.getUnitAt(OZITY_POS).getMaxFerocity(), is (2));
        assertThat(game.getDeadUnitsAtCemetery(WHITE_CEMETERY_POS).size(), is (1));
        assertThat(game.moveUnit(OZITY_POS, OZITY_TARGET_POS), is (true));
        endRound();
        assertThat(game.attackOpponent(OZITY_TARGET_POS, SECOND_OZITY_TARGET_POS), is (true));
        assertThat(game.getDeadUnitsAtCemetery(WHITE_CEMETERY_POS).size(), is (2));
        assertThat(game.moveUnit(OZITY_TARGET_POS, SECOND_OZITY_TARGET_POS), is (true));
        assertThat(game.attackOpponent(SECOND_OZITY_TARGET_POS, THIRD_OZITY_TARGET_POS), is (true));
        assertThat(game.getDeadUnitsAtCemetery(WHITE_CEMETERY_POS).size(), is (3));
        assertThat(game.moveUnit(SECOND_OZITY_TARGET_POS, THIRD_OZITY_TARGET_POS), is (true));
            // Then he is rejected on his fourth attack.
        assertThat(game.attackOpponent(THIRD_OZITY_TARGET_POS, FOURTH_OZITY_TARGET_POS), is (false));
            // And when a round passes,
        endRound();
            // His ferocity stays at 0.
        assertThat(game.getUnitAt(THIRD_OZITY_TARGET_POS).getMaxFerocity(), is (0));
    }

    @Test
    void shouldSummonJasonStathamUponSacrificingThomasTheGoof() {
            // Given black plays Thomas,
        game.playCard(THOMAS, null, THOMAS_POS);
        assertThat(game.getUnitAt(THOMAS_POS).getName(), is (N_THOMAS));
            // When black plays Statham,
        game.playCard(STATHAM, game.getUnitAt(THOMAS_POS), THOMAS_POS);
            // Then Statham takes Thomas' place.
        assertThat(game.getUnitAt(THOMAS_POS).getName(), is (N_STATHAM));
    }

    @Test
    void shouldRejectSummoningStathamWhenSacrificingNon_Thomas() {
            // Given black plays Ozity,
        game.playCard(OZITY, null, THOMAS_POS);
        assertThat(game.getUnitAt(THOMAS_POS).getName(), is (N_OZITY));
            // When black plays Statham,
        game.playCard(STATHAM, game.getUnitAt(THOMAS_POS), THOMAS_POS);
            // Then Ozity stays in place.
        assertThat(game.getUnitAt(THOMAS_POS).getName(), is (N_OZITY));
    }

    @Test
    void shouldRejectMathiasToMoveMoreThanEveryThirdRound() {
            // Given black plays Mathias,
        game.playCard(MATHIAS, null, THOMAS_POS);
            // When black moves Mathias the first time,
            // Then it is allowed.
        assertThat(game.moveUnit(THOMAS_POS, new Position(4, 5)), is (true));
            // But when black attempts to move Mathias in the next round,
            // It is rejected.
        endRound();
        assertThat(game.moveUnit(new Position(4, 5), new Position( 4, 4)), is (false));
        endRound();
        assertThat(game.moveUnit(new Position(4, 5), new Position( 4, 4)), is (false));
            // But when three rounds has passed,
            // It is allowed yet again.
        endRound();
        assertThat(game.moveUnit(new Position(4, 5), new Position( 4, 4)), is (true));
    }

    @Test
    void shouldSpawnUnitsOnSelectedPosition() {
            // Given black player plays a lot of units,
        game.playCard(LEGOLAS, null, LEGOLAS_POS);
        game.playCard(GORDON, null, GORDON_POS);
        game.playCard(HJOERDIS, null, HJOERDIS_POS);
            // Then they are retrievable from their positions.
        assertThat(game.getUnitAt(LEGOLAS_POS).getName(), is (N_LEGOLAS));
        assertThat(game.getUnitAt(GORDON_POS).getName(), is (N_GORDON));
        assertThat(game.getUnitAt(HJOERDIS_POS).getName(), is (N_HJOERDIS));
    }

    @Test
    void shouldEntranceUnitsOnA3x3GridUponActivatingChopin() {
            // Given that black plays Chopin on p(5, 1)
        game.playCard(CHOPIN, null, SECOND_WHITE_HUMAN_POS);
            // Then they shall all be entranced by the magical piano.
        assertThat(game.moveUnit(WHITE_HUMAN_POS, new Position(5, 0)), is (false));
        assertThat(game.moveUnit(BLACK_FURRY_POS, new Position(5, 2)), is (false));
            // But when three rounds pass,
        endRound();
        endRound();
        assertThat(game.moveUnit(WHITE_HUMAN_POS, new Position(5, 0)), is (false));
        assertThat(game.moveUnit(BLACK_FURRY_POS, new Position(5, 2)), is (false));
        endRound();
            // They are allowed to move yet again.
        assertThat(game.moveUnit(BLACK_FURRY_POS, new Position(5, 2)), is (true));
        game.endTurn();
        assertThat(game.moveUnit(WHITE_HUMAN_POS, new Position(5, 0)), is (true));
    }

    @Test
    void shouldSuppressChosenUnitUponPlayingFoxCharm() {
            // Given that Fox Charm is played,
        game.playCard(FOX_CHARM, game.getUnitAt(WHITE_HUMAN_POS), null);
            // Then the unit is unable to move or attack for 4 turns.
        assertThat(game.moveUnit(WHITE_HUMAN_POS, new Position(5, 0)), is (false));
        assertThat(game.attackOpponent(WHITE_HUMAN_POS, new Position(6, 1)), is (false));
        endRound();
        endRound();
        endRound();
        assertThat(game.attackOpponent(WHITE_HUMAN_POS, BLACK_DWARF_POS), is (false));
        assertThat(game.moveUnit(WHITE_HUMAN_POS, new Position(5, 0)), is (false));
            // But when the fourth round passes,
        endRound();
            // Then it can attack or move again.
        game.endTurn();
        assertThat(game.attackOpponent(WHITE_HUMAN_POS, BLACK_DWARF_POS), is (true));
        assertThat(game.moveUnit(WHITE_HUMAN_POS, new Position(5, 0)), is (true));
    }

    @Test
    void shouldSuppressUnitsIn3x3GridWithoutCenter() {
            // Given that Two-Smokes is played,
        game.playCard(TWO_SMOKES, null, SECOND_WHITE_HUMAN_POS);
            // Then surrounding black units shall be unable to move or attack for 4 turns.
        assertThat(game.moveUnit(BLACK_DWARF_POS, new Position(5, 2)), is (false));
        assertThat(game.attackOpponent(BLACK_DWARF_POS, SECOND_WHITE_HUMAN_POS), is (false));
        assertThat(game.moveUnit(BLACK_FURRY_POS, new Position(5, 2)), is (false));
        assertThat(game.attackOpponent(BLACK_FURRY_POS, SECOND_WHITE_HUMAN_POS), is (false));
        endRound();
        assertThat(game.moveUnit(BLACK_DWARF_POS, new Position(5, 2)), is (false));
        assertThat(game.attackOpponent(BLACK_DWARF_POS, SECOND_WHITE_HUMAN_POS), is (false));
        assertThat(game.moveUnit(BLACK_FURRY_POS, new Position(5, 2)), is (false));
        assertThat(game.attackOpponent(BLACK_FURRY_POS, SECOND_WHITE_HUMAN_POS), is (false));
        endRound();
        assertThat(game.attackOpponent(BLACK_DWARF_POS, SECOND_WHITE_HUMAN_POS), is (true));
        assertThat(game.moveUnit(BLACK_DWARF_POS, new Position(5, 0)), is (true));
        assertThat(game.attackOpponent(BLACK_FURRY_POS, SECOND_WHITE_HUMAN_POS), is (true));
        assertThat(game.moveUnit(BLACK_FURRY_POS, new Position(5, 2)), is (true));
    }

    @Test
    void shouldEnableToUnitToAttackTwiceInATurnWhenChoosingBerserker() {
            // Given Mathias is in play,
        game.playCard(MATHIAS, null, MATHIAS_POS);
            // He can only attack once a turn.
        assertThat(game.attackOpponent(MATHIAS_POS, WHITE_HUMAN_POS), is (true));
            // === Restore HP to human ==============
        game.getUnitAt(WHITE_HUMAN_POS).buffUnit("HP", 100);
        assertThat(game.attackOpponent(MATHIAS_POS, WHITE_HUMAN_POS), is (false));
        endRound();
            // But when black player opts to go Berserker for Mathias,
        game.getUnitAt(MATHIAS_POS).setAffinity(BERSERKER);
            // He can attack another time this turn.
        assertThat(game.attackOpponent(MATHIAS_POS, WHITE_HUMAN_POS), is (true));
            // And when another round begins, he can attack twice per turn again.
        endRound();
        game.getUnitAt(WHITE_HUMAN_POS).buffUnit("HP", 100);
        assertThat(game.attackOpponent(MATHIAS_POS, WHITE_HUMAN_POS), is (true));
        assertThat(game.attackOpponent(MATHIAS_POS, WHITE_HUMAN_POS), is (true));
    }

    @Test
    void shouldRejectDwarvesAndDemonsToChoosePaladinAffinity() {
            // Given Dagfinnur is in play,
        game.playCard(DAGFINNUR, null, DAGFINNUR_POS);
            // When black player attempts to choose aff. Paladin,
            // Then it is rejected.
        assertThat(game.getUnitAt(DAGFINNUR_POS).getUnitRace(), is (DWARF));
        assertThat(game.getUnitAt(DAGFINNUR_POS).setAffinity(PALADIN), is (false));
            // And when Hjørdis comes into play,
        game.playCard(HJOERDIS, null, HJOERDIS_POS);
            // It is likewise rejected.
        assertThat(game.getUnitAt(HJOERDIS_POS).getUnitRace(), is (DEMON));
        assertThat(game.getUnitAt(HJOERDIS_POS).setAffinity(PALADIN), is (false));

    }
}