package utilities;

import strategies.*;

public interface GameFactory {

    BoardLayoutStrategy getBoardLayoutStrategy();
    CardManagementStrategy getCardManagementStrategy();
    DemonicEffectStrategy getDemonicEffectStrategy();

    FieldValidatingStrategy getFieldValidatingStrategy();
    MagicEffectStrategy getMagicEffectStrategy();
    PlayerStatStrategy getPlayerStatStrategy();

    CombatStrategy getCombatStrategy();
    TrapEffectStrategy getTrapEffectStrategy();
    UnitStatStrategy getUnitStatStrategy();

    StructureStrategy getStructureStrategy();
}
