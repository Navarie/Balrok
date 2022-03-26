package view.tool;

import framework.Game;
import framework.Position;
import utilities.GraphicsUtility;
import view.figure.UnitFigure;

import java.awt.event.MouseEvent;
import minidraw.framework.DrawingEditor;
import minidraw.framework.Figure;
import minidraw.standard.NullTool;

public class MoveUnitTool extends NullTool {

    private final DrawingEditor editor;
    private final Game game;
    private Figure draggedFigure;
    private Position fromPos;

    private int lastX;
    private int lastY;
    private boolean isMovableUnit;

    public MoveUnitTool(DrawingEditor editor, Game game) {
        this.editor = editor;
        this.game = game;
    }

    public void mouseDown(MouseEvent e, int x, int y) {
        draggedFigure = editor.drawing().findFigure(x, y);
        fromPos = GraphicsUtility.getPositionFromCoordinates(x, y);
        lastX = x;
        lastY = y;

        if (draggedFigure != null) {
            isMovableUnit = draggedFigure instanceof UnitFigure;
        }
    }

    public void mouseDrag(MouseEvent e, int x, int y) {
            // show the moving figure
        boolean isDragAllowed = draggedFigure != null && isMovableUnit;
        if (!isDragAllowed) return;
            // compute screen location difference
        draggedFigure.moveBy(x - lastX, y - lastY);

        lastX = x;
        lastY = y;
    }

    public void mouseUp(MouseEvent e, int x, int y) {
        Position toPos = GraphicsUtility.getPositionFromCoordinates(x, y);
        if (draggedFigure != null && isMovableUnit) {

            boolean validMove = game.moveUnit(fromPos, toPos);
            if (!validMove) {

                draggedFigure.changed();
                int newX = GraphicsUtility.getXFromColumn(fromPos.getColumn());
                int newY = GraphicsUtility.getYFromRow(fromPos.getRow());
                draggedFigure.displayBox().setLocation(newX, newY);
                draggedFigure.changed();
            }
        }
    }
}
