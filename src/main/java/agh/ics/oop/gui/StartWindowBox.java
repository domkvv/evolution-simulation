package agh.ics.oop.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class StartWindowBox extends VBox {

    public SingleQueryBox animalsNoBox = new SingleQueryBox("number of animals", "10");
    public SingleQueryBox widthBox = new SingleQueryBox("width", "10");
    public SingleQueryBox heightBox = new SingleQueryBox("height", "10");
    public SingleQueryBox startEnergyBox = new SingleQueryBox("start energy", "100");
    public SingleQueryBox moveEnergyBox = new SingleQueryBox("move energy", "5");
    public SingleQueryBox plantEnergyBox = new SingleQueryBox("plant energy", "50");
    public SingleQueryBox jungleRatioBox = new SingleQueryBox("jungle ratio", "0.25");
    public SingleQueryBox moveDelay = new SingleQueryBox("move delay [ms]", "300");
    public CheckBox strategyRolledMap = new CheckBox("select magic strategy for rolled map");
    public CheckBox strategyLimitedMap = new CheckBox("select magic strategy for limited map");
    public Button goButton = new Button("go");

    public StartWindowBox() throws FileNotFoundException {
        super();
        Image image = new Image(new FileInputStream("src/main/resources/welcome.png"));
        ImageView welcomeImage = new ImageView(image);
        welcomeImage.setFitWidth(90);
        welcomeImage.setFitHeight(90);
        VBox queryBox = new VBox(this.animalsNoBox.hbox,
                this.widthBox.hbox,
                this.heightBox.hbox,
                this.startEnergyBox.hbox,
                this.moveEnergyBox.hbox,
                this.plantEnergyBox.hbox,
                this.jungleRatioBox.hbox,
                this.moveDelay.hbox,
                this.strategyRolledMap,
                this.strategyLimitedMap);
        queryBox.setAlignment(Pos.CENTER);
        queryBox.setSpacing(10);
        VBox query = new VBox(queryBox, welcomeImage);
        query.setAlignment(Pos.CENTER);
        query.setSpacing(25);
        VBox startWindowBox = new VBox(query, this.goButton);
        startWindowBox.setSpacing(20);
        startWindowBox.setAlignment(Pos.CENTER);
        this.getChildren().add(startWindowBox);
        this.setAlignment(Pos.CENTER);
    }

}
