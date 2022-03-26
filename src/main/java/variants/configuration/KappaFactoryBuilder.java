package variants.configuration;

import common.GameFactoryImpl;
import utilities.GameFactory;
import utilities.GameFactoryBuilder;
import variants.beta.BetaFieldStrategy;
import variants.beta.BetaStatStrategy;
import variants.kappa.KappaBoardStrategy;
import variants.kappa.KappaCardManagementStrategy;
import variants.kappa.KappaPlayerStrategy;

public class KappaFactoryBuilder implements GameFactoryBuilder {

    @Override
    public GameFactory build() {
        return new GameFactoryImpl.Builder()
                .setFieldValidatingStrategy(new BetaFieldStrategy())
                .setPlayerStatStrategy(new KappaPlayerStrategy())
                .setCardManagementStrategy(new KappaCardManagementStrategy())
                .build();
    }
}
