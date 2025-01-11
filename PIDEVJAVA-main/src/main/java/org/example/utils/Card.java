package org.example.utils;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Color;
import org.example.models.Reclamation;

public class Card extends HBox {
    public Card(Reclamation reclamation) {
        setSpacing(10); // Set spacing between elements

        Label idLabel = new Label(reclamation.getId().toString());
        Label titleLabel = new Label(reclamation.getTitle());
        Label dateLabel = new Label(reclamation.getDateReclamation().toString());

        FontAwesomeIconView showIcon = new FontAwesomeIconView(FontAwesomeIcon.EYE);
        showIcon.setFill(Color.BLUE); // Set icon color
        FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
        deleteIcon.setFill(Color.RED); // Set icon color
        showIcon.setSize("2em"); // Set icon size (larger than default)
        deleteIcon.setSize("2em"); // Set icon size (larger than default)

        getChildren().addAll(idLabel, titleLabel, dateLabel, showIcon, deleteIcon);
        setAlignment(Pos.CENTER); // Align elements to the center horizontally
    }
}

