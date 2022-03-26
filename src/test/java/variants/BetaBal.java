package variants;

import common.GameImpl;

import constants.CardConstants;
import constants.GameConstants;
import framework.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import variants.configuration.BetaFactoryBuilder;

import static constants.CardConstants.*;
import static constants.GameConstants.BLACK_PLAYER;
import static constants.GameConstants.WHITE_PLAYER;
import static constants.SpellConstants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class BetaBal {

    private Game game;

    private static final Position WHITE_CEMETERY_POS = new Position(1,0);
    private static final Position SECOND_WHITE_DWARF_POS = new Position(1, 1);
    private static final Position WHITE_ELF_POS = new Position(6, 7);
    private static final Position BLACK_ELF_POS = new Position(4, 7);
    private static final Position SECOND_BLACK_ELF_POS = new Position(4, 1);
    private static final Position WHITE_DWARF_POS = new Position(3, 6);
    private static final Position WHITE_GNOME_POS = new Position(3, 7);
    private static final Position BLACK_DRAGON_POS = new Position(6, 6);
    private static final Position BLACK_HUMAN_POS = new Position(5, 5);
    private static final Position WHITE_HUMAN_POS = new Position(2, 2);
    private static final Position DESERT_POS = new Position(4, 4);
    private static final Position SWAMP_POS = new Position(4, 5);
    private static final Position SECOND_SWAMP_POS = new Position(4, 2);
    private static final Position MOUNTAIN_POS = new Position(4, 6);
    private static final Position BLACK_SIREN_POS = new Position(5, 3);
    private static final Position LAKE_POS = new Position(4, 3);

    @BeforeEach
    void setup() {
        game = new GameImpl(new BetaFactoryBuilder().build());
    }

    private void endRound() {
        game.endTurn();
        game.endTurn();
    }

    @Test
    void shouldHaveDefaultUnitHPOf100() {
            // Given a default BetaBal game setting,
            // When any unit has been placed/spawned on the map,
            // Then it's HP should default to 100.
        assertThat(game.getUnitAt(WHITE_ELF_POS).getHealth(), is (100));
    }

    @Test
    void shouldDeal40DamageToOpponentWhenAttacking() {
            // Given a default BetaBal game setting,
            // When the black elf moves to attack the white elf,
        game.moveUnit(BLACK_ELF_POS, new Position(5,7));
        game.attackOpponent(new Position(5,7), WHITE_ELF_POS);
            // Then white elf takes 40 damage.
        assertThat(game.getUnitAt(WHITE_ELF_POS).getHealth(), is (100 - 40));
    }

    @Test
    void shouldBeIllegalToAttackOutsideMeleeRangeAsMelee() {
            // Given a default BetaBal game setting,
            // When the black elf attempts to attack the white elf from range 2,
        game.attackOpponent(BLACK_ELF_POS, WHITE_ELF_POS);
            // Then the attack is rejected.
        assertThat(game.attackOpponent(BLACK_ELF_POS, WHITE_ELF_POS), is (false));
    }

    @Test
    void shouldRestore1MSAfterKillingEnemy() {
            // Given a default BetaBal game setting,
            // When the black elf moves to attack the white elf 3 times,
        game.moveUnit(BLACK_ELF_POS, new Position(5, 7));
        assertThat(game.getUnitAt(new Position(5, 7)).getCurrentMovementSpeed(), is (0));
        game.attackOpponent(new Position(5,7), WHITE_ELF_POS);
        endRound();
        game.attackOpponent(new Position(5,7), WHITE_ELF_POS);
        endRound();
        game.attackOpponent(new Position(5,7), WHITE_ELF_POS);
            // Then white elf dies (HP reaches 0).
        assertThat(game.getCemetery(WHITE_CEMETERY_POS).size(), is (1));
            // And the black elf gains 1 MS.
        assertThat(game.getUnitAt(new Position(5, 7)).getCurrentMovementSpeed(), is (1));
    }

    @Test
    void shouldKillGnomeAndDwarfOnFirstAttack() {
            // Given a default BetaBal game setting,
            // When the black elf attacks the white dwarf,
        game.attackOpponent(BLACK_ELF_POS, WHITE_DWARF_POS);
            // The dwarf dies on the first hit.
        assertThat(game.getCemetery(WHITE_CEMETERY_POS).size(), is (1));
    }

    @Test
    void shouldKillOpponentAttackingAsMonster() {
            // Given a default BetaBal game setting,
            // When the black dragon attacks the white dwarf,
        game.attackOpponent(BLACK_DRAGON_POS, WHITE_ELF_POS);
            // Then the white elf dies.
        assertThat(game.getCemetery(WHITE_CEMETERY_POS).size(), is (1));
    }

    @Test
    void shouldAllowHuntersToAttackFromRange1To4() {
            // Given a default BetaBal game setting,
            // When the black human with affinity hunter attacks from range 2,
        game.getUnitAt(BLACK_HUMAN_POS).setAffinity(GameConstants.HUNTER);
            // Then it is allowed,
        assertThat(game.getUnitAt(WHITE_ELF_POS).getHealth(), is (100));
        assertThat(game.attackOpponent(BLACK_HUMAN_POS, WHITE_ELF_POS), is (true));
            // And the white elf has taken damage.
        assertThat(game.getUnitAt(WHITE_ELF_POS).getHealth(), is (50));
    }

    @Test
    void shouldAllowDragonsToAttackFromRange1To4() {
            // Given a default BetaBal game setting,
            // When the black dragon attacks from range 4,
            // Then it is allowed,
        assertThat(game.attackOpponent(BLACK_DRAGON_POS, WHITE_HUMAN_POS), is (true));
            // And the white human has died.
        assertThat(game.getCemetery(WHITE_CEMETERY_POS).size(), is (1));
    }

    @Test
    void shouldRejectDragonsToAttackFromRange5() {
            // Given a default BetaBal game setting,
            // When the black dragon attacks from range 5,
            // Then it is rejected,
        assertThat(game.attackOpponent(BLACK_DRAGON_POS, SECOND_WHITE_DWARF_POS), is (false));
            // And the white dwarf lives.
        assertThat(game.getUnitAt(WHITE_HUMAN_POS).getHealth(), is (100));
    }

    @Test
    void shouldSendUnitsToCemeteryUponDeath() {
            // Given a default BetaBal game setting,
            // When the black elf attacks the white dwarf,
        game.attackOpponent(BLACK_ELF_POS, WHITE_DWARF_POS);
            // Then the dwarf is sent to the cemetery.
        assertThat(game.getCemetery(WHITE_CEMETERY_POS).size(), is (1));
            // And when the black elf attacks the white gnome,
        endRound();
        game.attackOpponent(BLACK_ELF_POS, WHITE_GNOME_POS);
            // Then it is also sent to the cemetery.
        assertThat(game.getDeadUnitsAtCemetery(GameConstants.WHITE_CEMETERY_POS).size(), is (2));
    }

    @Test
    void shouldRejectDruidsToMoveAcrossDesert() {
            // Given a default BetaBal game setting,
            // When the black human gets the affinity druid,
        game.getUnitAt(BLACK_HUMAN_POS).setAffinity( GameConstants.DRUID );
            // And it attempts to move through desert,
            // Then it is rejected.
        assertThat(game.moveUnit(BLACK_HUMAN_POS, DESERT_POS), is (false));
    }

    @Test
    void shouldAllowDruidsToMoveAcrossSwamp() {
            // Given a default BetaBal game setting,
            // When the black human gets the affinity druid,
        game.getUnitAt(BLACK_HUMAN_POS).setAffinity( GameConstants.DRUID );
            // And it attempts to move through swamp,
            // Then it is allowed.
        assertThat(game.moveUnit(BLACK_HUMAN_POS, SWAMP_POS), is (true));
    }

    @Test
    void shouldNotPenaliseSirensForMovingThroughLake() {
            // Given a default BetaBal game setting,
            // When the black siren moves through lake field,
        assertThat(game.getUnitAt(BLACK_SIREN_POS).getCurrentMovementSpeed(), is (1));
        assertThat(game.moveUnit(BLACK_SIREN_POS, LAKE_POS), is (true));
            // Then its MS remains intact.
        assertThat(game.getUnitAt(LAKE_POS).getCurrentMovementSpeed(), is (1));
    }

    @Test
    void shouldAllowSirensToMoveThroughSwamp() {
            // Given a default BetaBal game setting,
            // When the black siren moves through swamp field,
            // Then it is allowed.
        assertThat(game.moveUnit(BLACK_SIREN_POS, SECOND_SWAMP_POS), is (true));
    }

    @Test
    void shouldRejectUnitsToAttackMoreThanOncePerTurn() {
            // Given a default BetaBal game setting,
            // When the black elf moves to attack the white elf 2 times,
        game.moveUnit(BLACK_ELF_POS, new Position(5,7));
        assertThat(game.attackOpponent(new Position(5,7), WHITE_ELF_POS), is (true));
        // Then it is rejected.
        assertThat(game.attackOpponent(new Position(5,7), WHITE_ELF_POS), is (false));
            // But when a new round begins,
        endRound();
            // Then it is allowed to attack once more.
        assertThat(game.getUnitAt(new Position(5, 7)).getMaxFerocity(), is (1));
        assertThat(game.attackOpponent(new Position(5,7), WHITE_ELF_POS), is (true));
    }

    @Test
    void shouldAllowAddingCardsToPlayerHand() {
            // Given a default BetaBal game setting,
            // When adding a card to a player hand,
            // It is allowed.
        assertThat(game.getHand(WHITE_PLAYER), is (notNullValue()));
        ((GameImpl) game).addCardToHand(CardConstants.GERALT_OF_RIVIA, WHITE_PLAYER);
        assertThat(game.getHand(WHITE_PLAYER).size(), is (4));
    }

    @Test
    void shouldAllowPlayersToDrawACardEachTurn() {
            // Given a default BetaBal game setting,
        assertThat(game.getHand(WHITE_PLAYER).size(), is (3));
            // When black player starts his turn 2,
        game.endTurn();
        endRound();
        assertThat(game.getHand(WHITE_PLAYER).size(), is (4));
        // Then black player draws a card from the top of his deck into his hand.
        endRound();
        assertThat(game.getHand(WHITE_PLAYER).size(), is (5));
        endRound();
        assertThat(game.getHand(WHITE_PLAYER).size(), is (6));
        endRound();
        assertThat(game.getHand(WHITE_PLAYER).size(), is (7));
    }

    @Test
    void shouldStartTheGameWith3CardsInHands() {
            // Given a default BetaBal game setting,
            // When the game starts,
            // Then black player should have 3 cards in hand.
        assertThat(game.getHand(BLACK_PLAYER).size(), is (3));
            // And when it's white player's turn,
            // Then he also has 3 cards in hand.
        assertThat(game.getHand(WHITE_PLAYER).size(), is (3));
    }

    @Test
    void shouldSpend10ManaUponPlayingAMagicCard() {
            // Given a default BetaBal game setting,
            // When the game starts,
            // Then black player should have 200 mana.
        assertThat(BLACK_PLAYER.getCurrentMana(), is (200));
            // But when the magic card Vivi is cast,
        assertThat(game.playCard(VIVI, game.getUnitAt(BLACK_DRAGON_POS), BLACK_HUMAN_POS), is (true));
            // Then black player has 190 mana afterwards.
        assertThat(game.getPlayers().get(0).getCurrentMana(), is (200 - BETA_MAGIC_CARD_COST));
    }

    @Test
    void shouldHastenUnitUponPlayingMCVivi() {
            // Given a default BetaBal game setting,
            // When the MC Vivi is cast on a unit,
        assertThat(game.getUnitAt(BLACK_DRAGON_POS).getCurrentMovementSpeed(), is (1));
        game.playCard(VIVI, game.getUnitAt(BLACK_DRAGON_POS), BLACK_HUMAN_POS);
            // Then black dragon has 2 MS afterwards.
        assertThat(game.getUnitAt(BLACK_DRAGON_POS).getCurrentMovementSpeed(), is (2));
    }

    @Test
    void shouldRootUnitUponPlayingMCKardey() {
            // Given a default BetaBal game setting,
            // When the MC Vivi is cast on a unit,
        game.playCard(KARDEY, game.getUnitAt(BLACK_DRAGON_POS), BLACK_HUMAN_POS);
            // Then black dragon cannot move for three turns.
        assertThat(game.moveUnit(BLACK_DRAGON_POS, new Position(6, 5)), is (false));
        endRound();
        assertThat(game.moveUnit(BLACK_DRAGON_POS, new Position(6, 5)), is (false));
        endRound();
        assertThat(game.moveUnit(BLACK_DRAGON_POS, new Position(6, 5)), is (false));
        endRound();
        assertThat(game.moveUnit(BLACK_DRAGON_POS, new Position(6, 5)), is (true));
    }

    @Test
    void shouldDisplaceUnitTowardsBaseWhenDCCiriIsPlayed() {
            // Given a default BetaBal game setting,
            // When the DC Ciri is cast on an enemy unit,
        assertThat(game.getUnitAt(SECOND_BLACK_ELF_POS).getUnitRace(), is (GameConstants.ELF));
        assertThat(game.getUnitAt(SECOND_BLACK_ELF_POS).getOwner(), is (BLACK_PLAYER));
        game.playCard(CIRI, game.getUnitAt(SECOND_BLACK_ELF_POS), SECOND_BLACK_ELF_POS);
            // Then the unit is feared and moves two columns back to base.
        assertThat(game.getUnitAt(new Position(6,1)).getUnitRace(), is (GameConstants.ELF));
        assertThat(game.getUnitAt(new Position(6,1)).getOwner(), is (BLACK_PLAYER));
    }

    @Test
    void shouldRemoveMCFromGameAfterUse() {
            // Given a default BetaBal game setting,
            // When the DC Ciri is cast on an enemy unit,
        assertThat(game.getHand(BLACK_PLAYER).size(), is (3));
        game.playCard(CIRI, game.getUnitAt(SECOND_BLACK_ELF_POS), SECOND_BLACK_ELF_POS);
        assertThat(game.getHand(BLACK_PLAYER).size(), is (2));
    }
}
