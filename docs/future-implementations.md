# Graphical
- [] Structures.
- [] Aesthetics.
  - [] 
- [] Hide opponent's cards.
- [] Text wrap on different rows.
# Cards
# Gameplay
- [] Winning strategy.
  - [] Player health mechanics.
- [] Handling of empty deck.
- [] If a player's deck is empty, he takes 10 damage every turn.
- [] Transcription decorator.
- [] Invisibility feature for units not implemented.
## Refactorings
- [] String/GameConstants files
- [] ConcurrentHashMap?
- [] Card database.
- [] Create xFieldEffectStrategy.
## Restructurings
- [] Replace System.out.println() with Exceptions/Errors.
# Complete.
- [OK] Graphical:
  - [OK] Force units to spawn on BoardUtility-given positions.
  - [OK] Field types implemented.
  - [OK] Age infographic.
    - [OK] Time period.
## Known bugs
- [] Red square covers trail sword figures and card selections.
- [] Hand is not correctly realigned: played cards visually stay in place.
# 3 player variant
- [] Yellow player.
- [] After Player.BLACK, Player.WHITE is in turn, then Player.YELLOW - rotating indefinitely.
- [] Triangular game mechanics (3 players).
# Fixed