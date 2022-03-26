package framework;

import java.util.List;
import java.util.Map;

public interface Card {

    String getName();
    String getDescription();
    String getType();
    String getQuote();

    int getCost();
    int getTurnPlayed();

    void registerTurnPlayed(int playerTurn);
}
