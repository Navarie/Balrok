## Test list for AlphaBal
# Completed.
- [OK] Black player goes first.
- [OK] There are 2 players as a default setting: Player.BLACK and Player.WHITE. They take turns after one another.
- [OK] The bottom right cell (Position 5, 8) is reserved for the Black Deck. Field type is keep.
- [OK] Above it - Position 4, 8 - the Black Cemetery.
- [OK] White Deck - Position 0, 0.
- [OK] White Cemetery - Position 1, 0.
- [OK] The field is swamp at 2, 6.
- [OK] The field is mountain at 2, 7 and 4, 2.
- [OK] The field is desert at 2, 8.
- [OK] The field is barrens at 3, 1.
- [OK] A black Dwarf is positioned at 3, 2.
- [OK] A white Elf is positioned at 3, 3.
- [OK] A black Orc is positioned at 3, 4.
- [OK] A white Human is positioned at 3, 5 and p(3,6).
- [OK] The game starts in the year 1415.
- [OK] The game advances a year per three rounds.
- [OK] A player starts at 100 Health.
- [OK] A player replenishes 5 Health every third round. 
- [OK] A player starts with 200 Mana.
- [OK] Players may only move one field for one unit per one turn unless otherwise stated. They may not move in opponent's turn.
- [OK] Players may only move their own units.
- [OK] Only Dwarves may move over mountains.
- [OK] Units may not move through swamp.
- [OK] A cell may only be occupied by a single unit of the same owner.

# Test-incomplete
- [] A player replenishes 5 Health every third round. This value may not exceed 150 unless otherwise stated.