package view.figure;

import framework.Card;
import view.GraphicsConstants;

import java.awt.*;

public class CardFigure extends BalrokFigure {

    protected Card associatedCard;

    private final String name;

    public CardFigure(String name, Point origin, Card card) {
        super(name, origin, GraphicsConstants.GENERIC_CARD_STRING);
        associatedCard = card;
        this.name = name;
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(fImage, fDisplayBox.x,
                fDisplayBox.y - GraphicsConstants.UNIT_OFFSET_Y,
                fDisplayBox.width, fDisplayBox.height, null);
    }

    public String getName() {
        return name;
    }

    public Card getAssociatedCard() {
        return associatedCard;
    }
}
