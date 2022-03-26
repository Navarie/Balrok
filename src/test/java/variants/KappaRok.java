package variants;

import common.GameImpl;
import constants.CardConstants;
import framework.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import variants.configuration.KappaFactoryBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static common.PlayerImpl.DEFAULT_BLOODLINE;
import static constants.CardConstants.*;
import static constants.GameConstants.*;
import static constants.SpellConstants.*;
import static constants.UnitConstants.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class KappaRok {


    private static final Position FIRST_BLACK_SPAWN_POSITION = new Position(10, 11);
    private static final Position WHITE_HUMAN_POSITION = new Position(4, 2);
    private static final Position THOMAS_POSITION = new Position(5, 2);
    private static final Position BLACK_HUMAN_POSITION = new Position(2, 2);

    private Game game;

    @BeforeEach
    void setup() {
            // Given a default Kappa game setting,
        game = new GameImpl(new KappaFactoryBuilder().build());
    }

    private void endRound() {
        game.endTurn();
        game.endTurn();
    }

    @Test
    void shouldRandomiseDeckAtStartOfGame() {
            // Given that black player is in turn and both players have initialised decks and starting hands,
        assertThat(game.getHand(BLACK_PLAYER).size(), is (5));
        assertThat(game.getHand(WHITE_PLAYER).size(), is (5));
        assertThat(game.getDeck(BLACK_PLAYER).size(), is (DECK_LIST.length - 5));
        assertThat(game.getDeck(WHITE_PLAYER).size(), is (DECK_LIST.length - 5));
            // Then their decks shall be randomised.
        assertNotEquals(game.getDeck(BLACK_PLAYER), game.getDeck(WHITE_PLAYER));
    }

    @Test
    void shouldDisallowUnitToMoveMoreThanOneFieldAtOnce() {
            // Given that black player has Thomas in play,
        game.addCardToHand(THOMAS);
        game.playCard(THOMAS, null, null);
        assertThat(game.getFieldTypeAt(FIRST_BLACK_SPAWN_POSITION).getFieldType(), is (PLAINS));
        assertThat(game.moveUnit(FIRST_BLACK_SPAWN_POSITION, new Position(10, 9)), is (false));
    }

    @Test
    void shouldDealDamageToUnitOwnerUponUnitDeath() {
            // Given that two opposing units are in play,
        assertThat(game.getUnitAt(WHITE_HUMAN_POSITION).getUnitRace(), is (HUMAN));
        assertThat(game.getUnitAt(BLACK_HUMAN_POSITION).getOwner(), is (BLACK_PLAYER));
        assertThat(game.getPlayers().get(0).getCurrentHealth(), is (100));
        assertThat(game.getPlayers().get(1).getCurrentHealth(), is (100));
            // When white player kills the black human,
        game.endTurn();
        game.getUnitAt(BLACK_HUMAN_POSITION).setUnitStat("HP", 40);
        assertThat(game.moveUnit(WHITE_HUMAN_POSITION, new Position(3, 2)), is (true));
        assertThat(game.attackOpponent(new Position(3, 2), BLACK_HUMAN_POSITION), is (true));
        assertThat(game.getUnitAt(BLACK_HUMAN_POSITION), is ( nullValue() ));
            // Then black player has taken damage.
        assertThat(game.getPlayers().get(0).getCurrentHealth(), is (100 - DEFAULT_BLOODLINE - 1));
    }

    @Test
    void shouldDealDamageToPlayersWithLessUnits() {
            // Given that black player has 1 unit, and white player has 2 units,
        assertThat(game.getUnits().size(), is (3));
        assertThat(game.getUnitAt(WHITE_HUMAN_POSITION).getOwner(), is (WHITE_PLAYER));
        assertThat(game.getUnitAt(THOMAS_POSITION).getOwner(), is (WHITE_PLAYER));
        assertThat(game.getUnitAt(BLACK_HUMAN_POSITION).getOwner(), is (BLACK_PLAYER));
        assertThat(game.getPlayers().get(0).getCurrentHealth(), is (100));
        assertThat(game.getPlayers().get(1).getCurrentHealth(), is (100));
            // Black player shall take damage every end of turn.
        game.endTurn();
        assertThat(game.getPlayers().get(0).getCurrentHealth(), is (99));
        assertThat(game.getPlayers().get(1).getCurrentHealth(), is (100));
        game.endTurn();
        assertThat(game.getPlayers().get(0).getCurrentHealth(), is (98));
        assertThat(game.getPlayers().get(1).getCurrentHealth(), is (100));
        game.endTurn();
        assertThat(game.getPlayers().get(0).getCurrentHealth(), is (97));
        assertThat(game.getPlayers().get(1).getCurrentHealth(), is (100));
    }
}