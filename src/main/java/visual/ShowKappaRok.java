package visual;

import common.GameImpl;
import framework.Game;
import minidraw.framework.DrawingEditor;
import minidraw.standard.MiniDrawApplication;
import variants.configuration.KappaFactoryBuilder;
import view.tool.StateTool;

public class ShowKappaRok {

    public static void main(String[] args) {
        Game game = new GameImpl(new KappaFactoryBuilder().build());
        DrawingEditor editor = new MiniDrawApplication( "KappaRok!",
                               new BalrokFactory(game) );
        StateTool stateTool = new StateTool(editor, game);

        editor.open();
        editor.showStatus("Initialised.");
        game.addObserver(stateTool);
        editor.setTool(stateTool);
    }
}
