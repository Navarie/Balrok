# Complete.
- [OK] Graphical:
  - [OK] Debuff graphic.
  - [OK] Player infographic.
  - [OK] Play magic/trap/demonic card functionality.
  - [OK] Shift-click a card for description infographic.
  - [OK] Realignment of hand on turn end (and on draw mechanics...).
  - [OK] Downscale unit image size.
  - [OK] Separate handCollection for players.
  - [OK] Card pictures.
  - [OK] Deck information.
  - [OK] Opponent hand.
  - [OK] Attacking opponents by shift-clicking attacker and dragging to defender.
  - [OK] A preliminary, minimal graphical user interface is introduced in KappaRok, using MiniDraw.
- [OK] Abstract Factory/Builder-pattern (Bloch?).
- [OK] Delegate BoardLayoutImpl
- [OK] Separate buffUnit() and setUnitStat().
- [OK] Constants files:
- [OK] Spell constants.
- [DC] Trap constants.
- [DC] Demonic constants.

# Fixed bugs
- [OK] Siren's MS is 3 instead of 1 (possibly due to DEFAULT_MOVEMENT_SPEED's static property).
- [OK] Mana depletes, and STR/AP retains their value, for all instances of GameImpl in test cases.
    - [OK] Tests are, however, passing individually.