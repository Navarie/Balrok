package view.tool;

import framework.Game;
import minidraw.framework.DrawingEditor;
import minidraw.standard.NullTool;
import view.GraphicsConstants;
import view.figure.BalrokFigure;

import java.awt.event.MouseEvent;
import java.util.Random;

public class EndTurnTool extends NullTool {

    private final DrawingEditor editor;
    private final Game game;

    public EndTurnTool(DrawingEditor editor, Game game) {
        this.editor = editor;
        this.game = game;
    }

    @Override
    public void mouseDown(MouseEvent e, int x, int y) {
        BalrokFigure figure = (BalrokFigure) editor.drawing().findFigure(x, y);
        if (figure == null) return;

        boolean isEndTurnButton = figure.getTypeString().equals(GraphicsConstants.TURN_BUTTON_STRING);
        if (!isEndTurnButton) return;

        Random randomNumberGenerator = new Random();
        int upperBound = 8;
        int randomInteger = randomNumberGenerator.nextInt(upperBound);
        String generatedMessage = null;
        switch (randomInteger) {
            case (0) -> generatedMessage = " ended their turn. Nice moves.";
            case (1) -> generatedMessage = " ended their turn. Is that all you got?";
            case (2) -> generatedMessage = " is all out of ideas (turn ended).";
            case (3) -> generatedMessage = " scratches his head (turn ended).";
            case (4) -> generatedMessage = " ended their turn, but might as well concede.";
            case (5) -> generatedMessage = " is fornicating with promiscuous females and receiving currency (turn ended).";
            case (6) -> generatedMessage = " should uninstall the game (turn ended).";
            case (7) -> generatedMessage = " ended their turn. What a match!";
        }

        editor.showStatus(">> " + game.getPlayerInTurn().toString() + generatedMessage);
        game.endTurn();
    }
}
