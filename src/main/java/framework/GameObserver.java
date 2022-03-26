package framework;

public interface GameObserver {

    void removeCardFromView(Card card);
    void updateHands();
    void turnEnds(Player nextPlayer, int age);

    void boardChangeAt(Position p);
    void fieldInspectedAt(Position p);
    void cardInspectedAt(Card card);
}
