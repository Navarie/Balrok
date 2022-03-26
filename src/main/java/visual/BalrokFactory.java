package visual;

import framework.Game;
import minidraw.framework.Drawing;
import minidraw.framework.DrawingEditor;
import minidraw.framework.DrawingView;
import minidraw.framework.Factory;
import view.BoardView;
import view.RokDraw;

import javax.swing.*;

class BalrokFactory implements Factory {

    private final Game game;

    public BalrokFactory(Game game) { this.game = game; }

    public DrawingView createDrawingView(DrawingEditor editor) {
        return new BoardView(editor, game);
    }

    public Drawing createDrawing(DrawingEditor editor) { return new RokDraw(game); }
    public JTextField createStatusField(DrawingEditor editor) {
        JTextField jField = new JTextField("Throwaway");
        jField.setEditable(false);
        return jField;
    }
}