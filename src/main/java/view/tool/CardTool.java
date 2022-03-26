package view.tool;

import framework.Card;
import framework.Game;
import framework.Position;
import framework.Unit;
import minidraw.framework.DrawingEditor;
import minidraw.framework.Figure;
import minidraw.standard.NullTool;
import utilities.GraphicsUtility;
import view.figure.CardFigure;

import java.awt.event.MouseEvent;

import static constants.CardConstants.*;

public class CardTool extends NullTool {

    private final DrawingEditor editor;
    private final Game game;
    private Figure draggedFigure;
    private Position fromPos;

    private int lastX;
    private int lastY;
    private boolean isMovableCard;

    public CardTool(DrawingEditor editor, Game game) {
        this.editor = editor;
        this.game = game;
    }

    public void mouseDown(MouseEvent e, int x, int y) {
        draggedFigure = editor.drawing().findFigure(x, y);
        fromPos = GraphicsUtility.getPositionFromCoordinates(x, y);
        lastX = x;
        lastY = y;

        if (draggedFigure != null) {
            isMovableCard = draggedFigure instanceof CardFigure;
        }
    }

    public void mouseDrag(MouseEvent e, int x, int y) {
        // show the moving figure
        boolean isDragAllowed = (draggedFigure != null && isMovableCard);
        if (!isDragAllowed) return;
        // compute screen location difference
        draggedFigure.moveBy(x - lastX, y - lastY);

        lastX = x;
        lastY = y;
    }

    public void mouseUp(MouseEvent e, int x, int y) {
        Position to = GraphicsUtility.getPositionFromCoordinates(x, y);
        Unit unitAtTo = game.getUnitAt(to);
        Card card = ((CardFigure) draggedFigure).getAssociatedCard();
        String cardType = card.getType();
        String cardName = card.getName();
        boolean withinBoardDimensions = (to.getColumn() >= 0 && to.getRow() >= 0
                && to.getColumn() < 13 && to.getRow() < 11);
        boolean playableCard = game.playCard(card, unitAtTo, to);

        if (draggedFigure != null && isMovableCard) {

            if (cardType.equals(UNIT_CARD) && withinBoardDimensions && playableCard) {

                editor.showStatus(cardName + " was played.");
                signalRemoval();
            } else if ((cardType.equals(MAGIC_CARD) || cardType.equals(DEMONIC_CARD)) && playableCard) {

                String unitName = "N/A";
                if (unitAtTo != null) unitName = unitAtTo.getName();

                editor.showStatus("Magic card " + cardName + " was played. Target: " + unitName);
                signalRemoval();
            } else if ((cardType.equals(TRAP_CARD) || cardType.equals(DEMONIC_TRAP_CARD))
                    && withinBoardDimensions && playableCard) {

                editor.showStatus("Trap card " + cardName + " was set at: " + to);
                signalRemoval();
            } else if (cardType.equals(FIELD_CARD) && playableCard) {

                editor.showStatus("Field card " + cardName + " was played.");
                signalRemoval();
            } else if (cardType.equals(STRUCTURE_CARD) && withinBoardDimensions && playableCard) {

                editor.showStatus("Structure card was played at: " + to + ", erecting: " + cardName);
                signalRemoval();
            } else {
                editor.showStatus("Card could not be played. Send an angry letter to the developers.");

                draggedFigure.changed();
                int newX = GraphicsUtility.getXFromColumn(fromPos.getColumn());
                int newY = GraphicsUtility.getYFromRow(fromPos.getRow());
                draggedFigure.displayBox().setLocation(newX, newY);
            }
            draggedFigure.changed();
        }
    }

    private void signalRemoval() {
        draggedFigure.changed();
        editor.drawing().removeFromSelection(draggedFigure);
        editor.drawing().requestUpdate();
    }
}
