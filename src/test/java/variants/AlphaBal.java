package variants;

import common.GameImpl;

import constants.GameConstants;
import framework.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import variants.configuration.AlphaFactoryBuilder;

import static constants.GameConstants.BLACK_PLAYER;
import static constants.GameConstants.WHITE_PLAYER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AlphaBal {

    private Game game;

    private static final Position BLACK_ORC_POSITION = new Position(3, 4);
    private static final Position WHITE_HUMAN_POSITION = new Position(3, 5);

    @BeforeEach
    void setup() {
        game = new GameImpl(new AlphaFactoryBuilder().build());
    }

    private void endRound() {
        game.endTurn();
        game.endTurn();
    }

    @Test
    void shouldStartWithBlackPlayer() {
            // Given a default game setting,
            // When the game starts,
            // Then starting player is black.
        assertThat(game.getPlayerInTurn(), is (BLACK_PLAYER));
    }

    @Test
    void shouldBeWhitePlayerAfterBlackThenBlackAgain() {
            // Given a default game setting,
            // When a turn has passed,
        game.endTurn();
            // Then it is white player's turn.
        assertThat(game.getPlayerInTurn(), is (WHITE_PLAYER));

            // And when another turn passes,
        game.endTurn();
            // Then it is black player's turn again.
        assertThat(game.getPlayerInTurn(),  is (BLACK_PLAYER));
    }

    @Test
    void shouldBeFieldTypeKeepInBottomRightAndTopLeft() {
            // Given a default game setting,
            // When the game starts,
            // Then the field type at Position(5, 8) and Position(0, 0) is keep.
        assertThat(game.getFieldTypeAt(new Position(5, 8)).getFieldType(), is (GameConstants.KEEP));
        assertThat(game.getFieldTypeAt(new Position(0, 0)).getFieldType(), is (GameConstants.KEEP));
    }

    @Test
    void shouldBeFieldTypeCemeteryAt8_1And1_0() {
            // Given a default game setting,
            // When the game starts,
            // Then the field type at Position(4, 8) and Position(1, 0) is cemetery.
        assertThat(game.getFieldTypeAt(new Position(4, 8)).getFieldType(), is (GameConstants.CEMETERY));
        assertThat(game.getFieldTypeAt(new Position(1, 0)).getFieldType(), is (GameConstants.CEMETERY));
    }

    @Test
    void shouldHaveSpecificFieldTypesAtRequiredPositions() {
            // Given a default game setting,
            // When the game starts,
            // Then the field types are as specified in the AlphaBal test list.
        assertThat(game.getFieldTypeAt(new Position(2, 6)).getFieldType(), is (GameConstants.SWAMP));
        assertThat(game.getFieldTypeAt(new Position(2, 7)).getFieldType(), is (GameConstants.MOUNTAIN));
        assertThat(game.getFieldTypeAt(new Position(2, 8)).getFieldType(), is (GameConstants.DESERT));
        assertThat(game.getFieldTypeAt(new Position(3, 1)).getFieldType(), is (GameConstants.BARRENS));
    }

    @Test
    void shouldHaveBlackDwarfAt3_2() {
            // Given a default game setting,
            // When the game starts,
            // Then there is a black Dwarf at Position (3, 2).
        assertThat(game.getUnitAt(new Position(3, 2)).getOwner(), is (BLACK_PLAYER));
        assertThat(game.getUnitAt(new Position(3, 2)).getUnitRace(), is (GameConstants.DWARF));
    }

    @Test
    void shouldHaveWhiteElfAt3_3() {
            // Given a default game setting,
            // When the game starts,
            // Then there is a white Elf at Position (3, 3).
        assertThat(game.getUnitAt(new Position(3, 3)).getOwner(), is (WHITE_PLAYER));
        assertThat(game.getUnitAt(new Position(3, 3)).getUnitRace(), is (GameConstants.ELF));
    }

    @Test
    void shouldHaveSelectedUnitsInInitialFormation() {
            // Given a default game setting,
            // When the game starts,
            // Then units are found as specified in the AlphaBal test list.
        assertThat(game.getUnitAt(BLACK_ORC_POSITION).getOwner(), is (BLACK_PLAYER));
        assertThat(game.getUnitAt(BLACK_ORC_POSITION).getUnitRace(), is (GameConstants.ORC));
        assertThat(game.getUnitAt(new Position(3, 5)).getOwner(), is (WHITE_PLAYER));
        assertThat(game.getUnitAt(new Position(3, 5)).getUnitRace(), is (GameConstants.HUMAN));
    }

    @Test
    void shouldStartGameInYear1415() {
            // Given a default game setting,
            // When the game starts,
            // Then the starting year is 1415.
        assertThat(game.getCurrentYear(),  is (1415));
    }

    @Test
    void shouldAdvanceYearOfGameBy1Every3Rounds() {
            // Given a default game setting,
            // When the game is advanced nine rounds,
        for (int i=0; i < 9; i++) {
            endRound();
        }
            // Then currentYear should be (1415 + 3).
        assertThat(game.getCurrentYear(), is (1415 + 180));
    }

    @Test
    void shouldStartWith100PlayerHealth() {
            // Given a default game setting,
            // When the game starts,
            // Black and white player have 100 health.
        assertThat(BLACK_PLAYER.getCurrentHealth(), is (100));
        assertThat(WHITE_PLAYER.getCurrentHealth(), is (100));
    }

    @Test
    void shouldStartWith200PlayerMana() {
            // Given a default game setting,
            // When the game starts,
            // Black and white player have 200 mana.
        assertThat(BLACK_PLAYER.getCurrentMana(), is (200));
        assertThat(WHITE_PLAYER.getCurrentMana(), is (200));
    }

    @Test
    void shouldRestore5HealthEveryThirdRound() {
            // Given a default game setting,
            // When the game is advanced 12 rounds,
        for (int i=0; i < 12; i++) {
            endRound();
        }
            // Then black and white player shall have (100 + (4 * 5)) health.
        assertThat(BLACK_PLAYER.getCurrentHealth(), is (100 + (4 * 5)));
        assertThat(WHITE_PLAYER.getCurrentHealth(), is (100 + (4 * 5)));
    }

    @Test
    void shouldBeIllegalToRestoreBeyond150Health() {
            // Given a default game setting,
            // When the game is advanced 60 rounds,
        for (int i=0; i < 35; i++) {
            endRound();
        }
            // Then black and white player shall have only 150 health.
        assertThat(BLACK_PLAYER.getCurrentHealth(), is (150));
        assertThat(WHITE_PLAYER.getCurrentHealth(), is (150));
    }

    @Test
    void shouldBeLegalToMoveOwnedUnits() {
            // Given a default game setting with black Orc at p(3,4),
            // When black player is in turn and attempts to move the Orc,
            // Then it is allowed.
        assertThat(game.moveUnit(BLACK_ORC_POSITION, new Position(4,4)), is (true));
    }

    @Test
    void shouldBeIllegalToMoveOpponentUnits() {
            // Given a default game setting with black Orc at p(3,4),
            // When white player is in turn and attempts to move the Orc,
        game.endTurn();
            // Then it is rejected.
        assertThat(game.moveUnit(BLACK_ORC_POSITION, new Position(4,4)), is (false));
    }

    @Test
    void shouldBeIllegalToMoveMoreThanOncePerUnitPerTurn() {
            // Given a default game setting with black Orc at p(3,4),
            // When black player is in turn and attempts to move the Orc,
            // Then it is allowed.
        assertThat(game.moveUnit(BLACK_ORC_POSITION, new Position(4, 4)), is (true));
        assertThat(game.getUnitAt(new Position(4, 4)).getCurrentMovementSpeed(), is (0));
            // And when black player attempts to move the same Orc again,
            // Then it is rejected.
        assertThat(game.moveUnit(new Position(4, 4), new Position(5,4)), is (false));
    }

    @Test
    void shouldBeLegalToMoveConsecutivelyAfterNewRound() {
            // Given a default game setting with black Orc at p(3,4),
            // When black player is in turn and attempts to move the Orc,
            // Then it is allowed.
        assertThat(game.moveUnit(BLACK_ORC_POSITION, new Position(4,4)), is (true));
            // And when black player is in turn again and attempts to move the same Orc,
        endRound();
            // Then it is allowed.
        assertThat(game.moveUnit(new Position(4,4), new Position(5,4)), is (true));
    }

    @Test
    void shouldBeLegalForOnlyDwarvesToMoveAcrossMountains() {
            // Given a default game setting with white Elf at p(3,3),
            // When white player is in turn and attempts to move onto the mountain field,
        game.endTurn();
            // Then it is rejected.
        assertThat(game.moveUnit(new Position(3,3), new Position(4,2)), is (false));
    }

    @Test
    void shouldBeIllegalToMoveAcrossSwamp() {
            // Given a default game setting with white Human at p(3,5),
            // When white player is in turn and attempts to move onto the swamp field,
            // Then it is rejected.
        assertThat(game.moveUnit(WHITE_HUMAN_POSITION, new Position(2,6)), is (false));
    }

    @Test
    void shouldBeIllegalToStackFriendlyUnits() {
            // Given a default game setting with white Human at p(3,5) and p(3,6),
            // When white player attempts to move them onto the same field,
        game.endTurn();
            // Then it is rejected.
        assertThat(game.moveUnit(WHITE_HUMAN_POSITION, new Position(3, 6)), is (false));
    }
}
