package view.tool;

import framework.Game;
import minidraw.framework.DrawingEditor;
import minidraw.standard.NullTool;
import view.GraphicsConstants;
import view.figure.BalrokFigure;

import java.awt.event.MouseEvent;

public class RefreshTool extends NullTool {

    private final DrawingEditor editor;
    private final Game game;

    public RefreshTool(DrawingEditor editor, Game game) {
        this.editor = editor;
        this.game = game;
    }

    @Override
    public void mouseUp(MouseEvent e, int x, int y) {
        BalrokFigure figure = (BalrokFigure) editor.drawing().findFigure(x, y);
        if (figure == null) return;

        boolean isRefreshButton = figure.getTypeString().equals(GraphicsConstants.REFRESH_BUTTON_STRING);
        if (!isRefreshButton) return;

        editor.drawing().requestUpdate();
        editor.showStatus(">> Game refreshed. Ahh, that's better.");
    }
}
