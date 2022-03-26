package common;

import strategies.*;
import utilities.GameFactory;
import variants.alpha.AlphaFieldStrategy;
import variants.alpha.BasicResourceStrategy;
import variants.beta.*;
import variants.delta.DeltaBoardStrategy;
import variants.delta.DeltaTrapEffectStrategy;
import variants.delta.DeltaUnitStatStrategy;
import variants.gamma.GammaStructureStrategy;
import variants.kappa.KappaBoardStrategy;

import java.sql.Struct;

public class GameFactoryImpl implements GameFactory {

    private final BoardLayoutStrategy boardLayoutStrategy;
    private final PlayerStatStrategy playerStatStrategy;
    private final FieldValidatingStrategy fieldValidatingStrategy;
    private final CombatStrategy combatStrategy;
    private final CardManagementStrategy cardManagementStrategy;
    private final MagicEffectStrategy magicEffectStrategy;
    private final DemonicEffectStrategy demonicEffectStrategy;
    private final TrapEffectStrategy trapEffectStrategy;
    private final UnitStatStrategy unitStatStrategy;
    private final StructureStrategy structureStrategy;

    private GameFactoryImpl(Builder builder) {
        boardLayoutStrategy = builder.boardLayoutStrategy;
        playerStatStrategy = builder.playerStatStrategy;
        fieldValidatingStrategy = builder.fieldValidatingStrategy;
        combatStrategy = builder.combatStrategy;
        cardManagementStrategy = builder.cardManagementStrategy;
        magicEffectStrategy = builder.magicEffectStrategy;
        demonicEffectStrategy = builder.demonicEffectStrategy;
        trapEffectStrategy = builder.trapEffectStrategy;
        unitStatStrategy = builder.unitStatStrategy;
        structureStrategy = builder.structureStrategy;
    }

    // === BLOCH BUILDER-PATTERN ==================

    public static class Builder {

        private BoardLayoutStrategy boardLayoutStrategy = new KappaBoardStrategy();
        private PlayerStatStrategy playerStatStrategy = new BasicResourceStrategy();
        private FieldValidatingStrategy fieldValidatingStrategy = new BetaFieldStrategy();
        private CombatStrategy combatStrategy = new BetaCombatStrategy();
        private CardManagementStrategy cardManagementStrategy = new BetaCardManagementStrategy();
        private MagicEffectStrategy magicEffectStrategy = new BetaMagicEffectStrategy();
        private DemonicEffectStrategy demonicEffectStrategy = new BetaDemonicEffectStrategy();
        private TrapEffectStrategy trapEffectStrategy = new DeltaTrapEffectStrategy();
        private UnitStatStrategy unitStatStrategy = new DeltaUnitStatStrategy();
        private StructureStrategy structureStrategy = new GammaStructureStrategy();

        public Builder() { }

        public Builder setBoardLayoutStrategy(BoardLayoutStrategy strategy) {
            boardLayoutStrategy = strategy;
            return this;
        }
        public Builder setPlayerStatStrategy(PlayerStatStrategy strategy) {
            playerStatStrategy = strategy;
            return this;
        }
        public Builder setFieldValidatingStrategy(FieldValidatingStrategy strategy) {
            fieldValidatingStrategy = strategy;
            return this;
        }
        public Builder setCombatStrategy(CombatStrategy strategy) {
            combatStrategy = strategy;
            return this;
        }
        public Builder setCardManagementStrategy(CardManagementStrategy strategy) {
            cardManagementStrategy = strategy;
            return this;
        }
        public Builder setMagicEffectStrategy(MagicEffectStrategy strategy) {
            magicEffectStrategy = strategy;
            return this;
        }
        public Builder setDemonicEffectStrategy(DemonicEffectStrategy strategy) {
            demonicEffectStrategy = strategy;
            return this;
        }
        public Builder setTrapEffectStrategy(TrapEffectStrategy strategy) {
            trapEffectStrategy = strategy;
            return this;
        }
        public Builder setUnitStatStrategy(UnitStatStrategy strategy) {
            unitStatStrategy = strategy;
            return this;
        }
        public Builder setStructureStrategy(StructureStrategy strategy) {
            structureStrategy = strategy;
            return this;
        }

        public GameFactory build() {
            return new GameFactoryImpl(this);
        }
    }

    public BoardLayoutStrategy getBoardLayoutStrategy() {
        return boardLayoutStrategy;
    }
    public CardManagementStrategy getCardManagementStrategy() {
        return cardManagementStrategy;
    }
    public DemonicEffectStrategy getDemonicEffectStrategy() {
        return demonicEffectStrategy;
    }
    public FieldValidatingStrategy getFieldValidatingStrategy() {
        return fieldValidatingStrategy;
    }
    public MagicEffectStrategy getMagicEffectStrategy() {
        return magicEffectStrategy;
    }
    public PlayerStatStrategy getPlayerStatStrategy() {
        return playerStatStrategy;
    }
    public CombatStrategy getCombatStrategy() {
        return combatStrategy;
    }
    public TrapEffectStrategy getTrapEffectStrategy() {
        return trapEffectStrategy;
    }
    public UnitStatStrategy getUnitStatStrategy() {
        return unitStatStrategy;
    }
    public StructureStrategy getStructureStrategy() { return structureStrategy; }
}
