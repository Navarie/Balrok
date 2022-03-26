package view.tool;

import constants.GameConstants;
import framework.Game;
import framework.Position;
import minidraw.framework.Tool;
import minidraw.standard.NullTool;
import utilities.GraphicsUtility;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class BoardInspectTool extends NullTool {

    private final Game game;
    private final Tool decoratee;

        // === Null-Object pattern =======
    public BoardInspectTool(Tool decoratee, Game game) {
        this.decoratee = decoratee;
        this.game = game;
    }

    public void mouseDown(MouseEvent e, int x, int y) {
        performInspectionAt(x, y);
        decoratee.mouseDown(e, x, y);
    }

    public void mouseUp(MouseEvent e, int x, int y) {
        decoratee.mouseUp(e, x, y);
        performInspectionAt(x, y);
    }

    private void performInspectionAt(int x, int y) {
        Position p = GraphicsUtility.getPositionFromCoordinates(x, y);
        boolean isWithinWorldLimits = (0 <= p.getRow()
                                    && 0 <= p.getColumn()
                                    && p.getRow() < GameConstants.EPSILON_BOARD_HEIGHT
                                    && p.getColumn() < GameConstants.EPSILON_BOARD_WIDTH);
        if (isWithinWorldLimits) {
            game.setFieldInspectionAt(p);
        }
    }

    public void mouseDrag(MouseEvent e, int x, int y) {
        decoratee.mouseDrag(e, x, y);
    }
    public void mouseMove(MouseEvent e, int x, int y) {
        decoratee.mouseMove(e, x, y);
    }
    public void keyDown(KeyEvent e, int key) {
        decoratee.keyDown(e, key);
    }
}
