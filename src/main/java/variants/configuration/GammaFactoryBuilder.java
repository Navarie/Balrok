package variants.configuration;

import common.GameFactoryImpl;
import utilities.GameFactory;
import utilities.GameFactoryBuilder;
import variants.beta.BetaFieldStrategy;
import variants.beta.BetaStatStrategy;
import variants.epsilon.EpsilonCardManagementStrategy;
import variants.gamma.GammaBoardStrategy;

public class GammaFactoryBuilder implements GameFactoryBuilder {

    @Override
    public GameFactory build() {
        return new GameFactoryImpl.Builder()
                .setFieldValidatingStrategy(new BetaFieldStrategy())
                .setPlayerStatStrategy(new BetaStatStrategy())
                .setBoardLayoutStrategy(new GammaBoardStrategy())
                .setCardManagementStrategy(new EpsilonCardManagementStrategy())
                .build();
    }
}
