package common;

import framework.Card;

public class CardImpl implements Card {

    private final String description;
    private final String type;
    private final String name;
    private final String quote;
    private final int cost;
    private int turnPlayed;

    public CardImpl(String name, String quote, String type, int cost, String description) {
        this.description = description;
        this.quote = quote;
        this.type = type;
        this.name = name;
        this.cost = cost;
        this.turnPlayed = 0;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public String getType() {
        return type;
    }
    public String getQuote() {
        return quote;
    }

    public int getCost() {
        return cost;
    }
    public int getTurnPlayed() {
        return turnPlayed;
    }

    public void registerTurnPlayed(int playerTurn) {
        turnPlayed = playerTurn;
    }
}
