package view.figure;

import minidraw.standard.AbstractFigure;

import java.awt.*;

public class InformationFigure extends AbstractFigure {

    private Point coordinate;
    private String information;
    private Font font;
    FontMetrics metrics = null;

    public InformationFigure(String information, Point coordinate) {
        this.coordinate = coordinate;
        this.information = information;

        font = new Font("Serif", Font.BOLD, 30);
    }

    public void setInformation(String newInformation) {
        willChange();
        information = newInformation;
        changed();
    }

    protected void basicMoveBy(int dx, int dy) {
        coordinate.translate(dx, dy);
    }

    public void draw(Graphics graphics) {
        graphics.setFont(font);
        graphics.setColor(Color.LIGHT_GRAY);
        metrics = graphics.getFontMetrics(font);
        graphics.drawString(information, coordinate.x, coordinate.y + metrics.getAscent());
    }

    public Rectangle displayBox() {
        Dimension extent = informationExtent();
        return new Rectangle(coordinate.x, coordinate.y, extent.width, extent.height);
    }

    private Dimension informationExtent() {
        int frameWidth;
        int frameHeight;

        if (metrics == null) {
            frameWidth = 50;
            frameHeight = 20;
        } else {
            frameWidth = metrics.stringWidth(information);
            frameHeight = metrics.getHeight();
        }
        return new Dimension(frameWidth, frameHeight);
    }
}
