package view.figure;

import framework.Unit;
import utilities.GraphicsUtility;
import view.GraphicsConstants;

import java.awt.*;

public class UnitFigure extends BalrokFigure {

    protected Unit associatedUnit;

    public UnitFigure(String name, Point origin, Unit unit) {
        super(name, origin, GraphicsConstants.UNIT_TYPE_STRING);
        associatedUnit = unit;
    }

    public void draw(Graphics graphics) {
        graphics.drawImage(fImage, fDisplayBox.x,
                fDisplayBox.y - GraphicsConstants.UNIT_OFFSET_Y,
                fDisplayBox.width, fDisplayBox.height, null);

        // Draw the owner circle
        Color color = GraphicsUtility.getColorForPlayer(associatedUnit.getOwner());
        graphics.setColor(color);
        graphics.fillOval(fDisplayBox.x, fDisplayBox.y, 8, 6);
        graphics.setColor(Color.BLACK);
        graphics.drawOval(fDisplayBox.x, fDisplayBox.y, 8, 6);

        // Draw the 'movable' box
        graphics.setColor(associatedUnit.getCurrentMovementSpeed() > 0 ? Color.GREEN : Color.BLACK);
        graphics.fillOval(fDisplayBox.x, fDisplayBox.y + 7, 8, 6);
        graphics.setColor(Color.BLACK);
        graphics.drawOval(fDisplayBox.x, fDisplayBox.y + 7, 8, 6);
    }
}
