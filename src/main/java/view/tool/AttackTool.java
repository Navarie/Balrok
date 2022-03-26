package view.tool;

import framework.Game;
import framework.Position;
import minidraw.framework.DrawingEditor;
import minidraw.framework.Figure;
import minidraw.standard.ImageFigure;
import minidraw.standard.NullTool;
import utilities.GraphicsUtility;
import view.figure.BalrokFigure;
import view.figure.UnitFigure;

import java.awt.*;
import java.awt.event.MouseEvent;

import static view.GraphicsConstants.B_F_SWORD_STRING;
import static view.GraphicsConstants.N_B_F_SWORD;

public class AttackTool extends NullTool {

    private final DrawingEditor editor;
    private final Game game;
    private Figure draggedFigure;
    private ImageFigure BFSwordImage;
    private Position from;

    private int lastX;
    private int lastY;
    private boolean isMovableUnit;

    public AttackTool(DrawingEditor editor, Game game) {
        this.editor = editor;
        this.game = game;
    }

    public void mouseDown(MouseEvent e, int x, int y) {
        if (!e.isShiftDown()) return;

        Point BF_Point = retrieveSwordPointFromLazyCoordinates(x, y);
        BFSwordImage = new BalrokFigure(N_B_F_SWORD, BF_Point, B_F_SWORD_STRING);
        editor.drawing().addToSelection(BFSwordImage);

        draggedFigure = editor.drawing().findFigure(x, y);
        from = GraphicsUtility.getPositionFromCoordinates(x, y);
        lastX = x;
        lastY = y;

        if (draggedFigure != null) {
            isMovableUnit = draggedFigure instanceof UnitFigure;
        }
    }

    private Point retrieveSwordPointFromLazyCoordinates(int x, int y) {

        Position fieldPosition = GraphicsUtility.getPositionFromCoordinates(x, y);
        int pointX = GraphicsUtility.getXFromColumn(fieldPosition.getColumn());
        int pointY = GraphicsUtility.getYFromRow(fieldPosition.getRow());
        int xDisplacement = 10;

        return new Point(pointX + xDisplacement, pointY);
    }

    public void mouseDrag(MouseEvent e, int x, int y) {
        // show the moving figure
        boolean isDragAllowed = (draggedFigure != null && isMovableUnit);
        if (!isDragAllowed) return;

        boolean isSwordDragAllowed = (BFSwordImage != null);
        if (!isSwordDragAllowed) return;
        // compute screen location difference
        draggedFigure.moveBy(x - lastX, y - lastY);
        BFSwordImage.moveBy(x - lastX, y - lastY);

        lastX = x;
        lastY = y;
    }

    public void mouseUp(MouseEvent e, int x, int y) {
        Position to = GraphicsUtility.getPositionFromCoordinates(x, y);
        if (draggedFigure != null && isMovableUnit) {

            game.attackOpponent(from, to);

            draggedFigure.changed();
            int newX = GraphicsUtility.getXFromColumn(from.getColumn());
            int newY = GraphicsUtility.getYFromRow(from.getRow());
            draggedFigure.displayBox().setLocation(newX, newY);
            draggedFigure.changed();

            BFSwordImage.changed();
            int newSwordX = GraphicsUtility.getXFromColumn(from.getColumn());
            int newSwordY = GraphicsUtility.getYFromRow(from.getRow());
            BFSwordImage.displayBox().setLocation(newSwordX, newSwordY);
            editor.drawing().removeFromSelection(BFSwordImage);
            BFSwordImage.changed();

            editor.drawing().requestUpdate();

        }
    }
}
