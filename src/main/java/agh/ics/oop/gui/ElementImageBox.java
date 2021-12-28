package agh.ics.oop.gui;

import agh.ics.oop.IMapElement;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class ElementImageBox extends VBox{

    public ElementImageBox(IMapElement element, HashMap<String, Image> pictures) throws FileNotFoundException {
        super();
        String imagePath = element.getImagePath();
        ImageView imageView;
        if(pictures.containsKey(imagePath)){
            imageView = new ImageView(pictures.get(imagePath));
        }
        else{
            Image image = new Image(new FileInputStream(imagePath));
            imageView = new ImageView(image);
            pictures.put(imagePath, image);
        }
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(imageView);
    }

}
