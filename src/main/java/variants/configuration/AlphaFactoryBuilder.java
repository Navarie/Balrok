package variants.configuration;

import common.GameFactoryImpl;
import utilities.GameFactory;
import utilities.GameFactoryBuilder;
import variants.alpha.AlphaBoardStrategy;

public class AlphaFactoryBuilder implements GameFactoryBuilder {

    @Override
    public GameFactory build() {
        return new GameFactoryImpl.Builder()
                .setBoardLayoutStrategy(new AlphaBoardStrategy())
                .build();
    }
}
