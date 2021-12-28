package agh.ics.oop.gui;

import agh.ics.oop.IMapElement;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


import java.io.FileNotFoundException;

public class ElementImageBox {
    public VBox vbox;

    public ElementImageBox(IMapElement element) throws FileNotFoundException {
        Image image = element.setOrientationImage();
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        this.vbox = new VBox(imageView);
    }

}
