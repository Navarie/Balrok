package view.tool;

import framework.*;
import minidraw.framework.DrawingEditor;
import minidraw.framework.Tool;
import minidraw.standard.NullTool;
import view.figure.BalrokFigure;

import java.awt.event.MouseEvent;

import static view.GraphicsConstants.*;

public class StateTool extends NullTool implements GameObserver {

    private final DrawingEditor editor;
    private final Game game;
    private Tool state;

    public StateTool(DrawingEditor editor, Game game) {
        state = new NullTool();
        this.editor = editor;
        this.game = game;

        game.addObserver(this);
    }

    @Override
    public void mouseDown(MouseEvent e, int x, int y) {
            // Find any figure below the mouse click position
        BalrokFigure figureBelowClickPoint = (BalrokFigure) editor.drawing().findFigure(x, y);

        if (figureBelowClickPoint == null) {
            state = createState( new NullTool() );
        } else {
            String figureType = figureBelowClickPoint.getTypeString();
            if (e.isShiftDown()) {
                editor.showStatus("(Shift) >>");

                if (figureType.equals( UNIT_TYPE_STRING )) {
                    state = new AttackTool(editor, game);
                } else if (figureType.equals( GENERIC_CARD_STRING )) {
                    state = new CardInspectTool(editor, game);
                }
            } else if (!e.isShiftDown()) {

                switch (figureType) {
                    case TURN_BUTTON_STRING -> state = new EndTurnTool(editor, game);
                    case REFRESH_BUTTON_STRING -> state = new RefreshTool(editor, game);
                    case UNIT_TYPE_STRING -> state = createState(new MoveUnitTool(editor, game));
                    case GENERIC_CARD_STRING -> state = new CardTool(editor, game);
                    default -> state = createState(new NullTool());
                }
            }
        }
        state.mouseDown(e, x, y);
    }

    public void mouseDrag(MouseEvent e, int x, int y) {
        if (e.isShiftDown()) {
            editor.showStatus("(Shift) >> Release mouse onto target...");
        } else {
            editor.showStatus(">> Release mouse onto target...");
        }
        state.mouseDrag(e, x, y);
    }
    public void mouseUp(MouseEvent e, int x, int y) {
        if (!state.getClass().toString().equals("class view.tool.EndTurnTool")) {
            editor.showStatus(">>");
        }
        state.mouseUp(e, x, y);
    }

        // Factory method
    private Tool createState(Tool decoratee) { return new BoardInspectTool(decoratee, game); }

    public void boardChangeAt(Position p) {}
    public void removeCardFromView(Card card) {}
    public void updateHands() {}
    public void turnEnds(Player nextPlayer, int age) {}
    public void fieldInspectedAt(Position p) {}
    public void cardInspectedAt(Card card) {}
}
