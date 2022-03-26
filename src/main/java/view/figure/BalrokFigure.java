package view.figure;

import minidraw.standard.ImageFigure;

import java.awt.*;

public class BalrokFigure extends ImageFigure {

    private final String typeString;

    public BalrokFigure(String imageName, Point coordinate, String typeString) {
        super(imageName, coordinate);
        this.typeString = typeString;
    }

    public String getTypeString() {
        return typeString;
    }
}

