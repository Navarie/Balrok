package variants.configuration;

import common.GameFactoryImpl;
import utilities.GameFactory;
import utilities.GameFactoryBuilder;
import variants.beta.BetaFieldStrategy;
import variants.beta.BetaStatStrategy;
import variants.epsilon.EpsilonBoardStrategy;
import variants.epsilon.EpsilonCardManagementStrategy;

public class EpsilonFactoryBuilder implements GameFactoryBuilder {

    @Override
    public GameFactory build() {
        return new GameFactoryImpl.Builder()
                .setFieldValidatingStrategy(new BetaFieldStrategy())
                .setPlayerStatStrategy(new BetaStatStrategy())
                .setBoardLayoutStrategy(new EpsilonBoardStrategy())
                .setCardManagementStrategy(new EpsilonCardManagementStrategy())
                .build();
    }
}
