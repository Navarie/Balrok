package variants.configuration;

import common.GameFactoryImpl;
import utilities.GameFactory;
import utilities.GameFactoryBuilder;
import variants.beta.BetaBoardStrategy;
import variants.beta.BetaFieldStrategy;
import variants.beta.BetaStatStrategy;
import variants.delta.DeltaBoardStrategy;

public class DeltaFactoryBuilder implements GameFactoryBuilder {

    @Override
    public GameFactory build() {
        return new GameFactoryImpl.Builder()
                .setFieldValidatingStrategy(new BetaFieldStrategy())
                .setPlayerStatStrategy(new BetaStatStrategy())
                .setBoardLayoutStrategy(new DeltaBoardStrategy())
                .build();
    }
}
