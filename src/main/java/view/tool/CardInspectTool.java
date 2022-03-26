package view.tool;

import constants.GameConstants;
import framework.Card;
import framework.Game;
import framework.Position;
import minidraw.framework.DrawingEditor;
import minidraw.framework.Figure;
import minidraw.standard.NullTool;
import utilities.GraphicsUtility;
import view.figure.CardFigure;

import java.awt.event.MouseEvent;

public class CardInspectTool extends NullTool {

    private final DrawingEditor editor;
    private final Game game;
    private Figure clickedFigure;
    private Position fromPos;
    private boolean isCard;

    public CardInspectTool(DrawingEditor editor, Game game) {
        this.editor = editor;
        this.game = game;
    }

    public void mouseDown(MouseEvent e, int x, int y) {
        clickedFigure = editor.drawing().findFigure(x, y);
        Card card = ((CardFigure) clickedFigure).getAssociatedCard();
        String cardName = card.getName();
        String cardDescription = card.getDescription();
        String cardQuote = card.getQuote();
        String cardType = card.getType();
        int cardCost = card.getCost();

        if (clickedFigure != null) {
            isCard = clickedFigure instanceof CardFigure;
        }

        if (clickedFigure != null && isCard) {

            editor.drawing().addToSelection(clickedFigure);
            editor.showStatus(">> Showing description of card: " + cardName);
            performInspectionAt(x, y);
        }
    }

    public void mouseUp(MouseEvent e, int x, int y) {
        clickedFigure = editor.drawing().findFigure(x, y);
        String cardName = "N/A";

        if (clickedFigure != null && isCard) {
            Card card = ((CardFigure) clickedFigure).getAssociatedCard();
            cardName = card.getName();

        }
        editor.showStatus(">> Showing description of card: " + cardName);
    }

    private void performInspectionAt(int x, int y) {
        clickedFigure = editor.drawing().findFigure(x, y);
        Card card = ((CardFigure) clickedFigure).getAssociatedCard();

        game.setInspectionOn(card);
    }
}
