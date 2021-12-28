package agh.ics.oop.gui;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class SingleQueryBox extends HBox {
    public TextField textField;
    public HBox hbox;

    public SingleQueryBox(String field, String defaultValue) {
        this.textField = new TextField(defaultValue);
        Text text = new Text(field);
        this.hbox = new HBox(text, this.textField);
        this.hbox.setAlignment(Pos.CENTER);
        this.hbox.setSpacing(10);
    }

}
