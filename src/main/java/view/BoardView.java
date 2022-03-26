package view;

import framework.*;
import minidraw.framework.DrawingEditor;
import minidraw.standard.ImageManager;
import minidraw.standard.StdViewWithBackground;
import utilities.BoardUtility;
import utilities.GraphicsUtility;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class BoardView extends StdViewWithBackground {

    private final Game game;

    public BoardView(DrawingEditor editor, Game game) {
        super(editor, "Balrok-background");
        this.game = new FieldTypeProxy(game);
    }

    public void drawBackground(Graphics graphics) {
        super.drawBackground(graphics);

        ImageManager manager = ImageManager.getSingleton();
        Image image;
        Position fieldTypePosition;
        Rectangle clip = graphics.getClipBounds();
        int[] boardDimensions = BoardUtility.retrieveBoardDimensions("KAPPA");

        for (int row = 0; row < boardDimensions[1]; row++) {
            for (int column = 0; column < boardDimensions[0]; column++) {

                fieldTypePosition = new Position(row, column);
                int xPosition = GraphicsUtility.getXFromColumn(column);
                int yPosition = GraphicsUtility.getYFromRow(row);
                Rectangle fieldTypeRectangle = new Rectangle(xPosition, yPosition, GraphicsConstants.FIELD_TYPE_SIZE, GraphicsConstants.FIELD_TYPE_SIZE);

                if (clip.intersects(fieldTypeRectangle)) {
                    FieldType fieldTypeTTT = game.getFieldTypeAt(fieldTypePosition);
                    String imageName = fieldTypeTTT.getFieldType();

                        // Particular handling of algorithmic selection goes here
                    image = manager.getImage(imageName);
                    graphics.drawImage(image, xPosition, yPosition, null);
                }
            }
        }

    }

    private class FieldTypeProxy implements Game {

        private final Game proxee;
        private FieldType fieldTypeCache[][];

        public FieldTypeProxy(Game game) {
            this.proxee = game;
            int[] boardDimensions = BoardUtility.retrieveBoardDimensions("KAPPA");
            fieldTypeCache = new FieldType[boardDimensions[1]][boardDimensions[0]];
        }

        public FieldType getFieldTypeAt(Position p) {
            FieldType fieldType = fieldTypeCache[p.getRow()][p.getColumn()];

            if (fieldType == null) {
                fieldTypeCache[p.getRow()][p.getColumn()] = proxee.getFieldTypeAt(p);
                fieldType = fieldTypeCache[p.getRow()][p.getColumn()];
            }

            return fieldType;
        }

        public List<Player> getPlayers() { return proxee.getPlayers(); }
        public Player getPlayerInTurn() { return proxee.getPlayerInTurn(); }
        public List<Player> getOpponents() { return proxee.getOpponents(); }
        public Unit getUnitAt(Position p) { return proxee.getUnitAt(p); }
        public Map<Position, Unit> getUnits() { return proxee.getUnits(); }
        public List<Unit> getDeadUnitsAtCemetery(Position p) { return proxee.getDeadUnitsAtCemetery(p); }
        public Map<Position, List<Unit>> getCemetery(Position p) { return proxee.getCemetery(p); }
        public Card getCard(String name) { return proxee.getCard(name); }
        public List<Card> getDeck(Player owner) { return proxee.getDeck(owner); }
        public List<Card> getHand(Player owner) { return proxee.getHand(owner); }
        public int getRoundCounter() { return proxee.getRoundCounter(); }
        public int getCurrentYear() { return proxee.getCurrentYear(); }
        public void startGame() { proxee.startGame(); }
        public void endTurn() { proxee.endTurn(); }
        public void addCardToHand(Card c) { proxee.addCardToHand(c); }
        public void drawCardFor(Player p) { proxee.drawCardFor(p); }
        public void removeFromHand(Card c, Player player) { proxee.removeFromHand(c, player); }
        public void sendToCemetery(Unit unit, Player owner) { proxee.sendToCemetery(unit, owner); }
        public void sendToPrisonKeep(Unit unit, Player owner) { proxee.sendToPrisonKeep(unit, owner); }
        public boolean activateCard(Card c, Unit target, Position to, Player p) { return proxee.activateCard(c, target, to, p); }
        public boolean playCard(Card c, Unit target, Position to) { return proxee.playCard(c, target, to); }
        public boolean attackOpponent(Position from, Position to) { return proxee.attackOpponent(from, to); }
        public boolean moveUnit(Position from, Position to) { return proxee.moveUnit(from, to); }
        public void addObserver(GameObserver newObserver) {}
        public void setFieldInspectionAt(Position p) {}
        public void setInspectionOn(Card card) {}
    }
}
