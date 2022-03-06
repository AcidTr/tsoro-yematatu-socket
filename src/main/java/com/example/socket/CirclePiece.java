package com.example.socket;

import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.HashMap;

public class CirclePiece {

    private Circle circle;
    private boolean isSelected;
    private boolean isEmpty;
    private boolean isUserOwner;
    public ArrayList<CirclePiece> neighbourhood;



    public CirclePiece(Circle circle){
        this.circle = circle;
        this.isEmpty = true;
        this.neighbourhood = new ArrayList<>();
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public boolean isUserOwner() {
        return isUserOwner;
    }

    public void setUserOwner(boolean userOwner) {
        circle.setStyle(userOwner ? "-fx-fill:#448ccd;": "-fx-fill:#53369e;");
        isUserOwner = userOwner;
        isEmpty = false;
    }

    public void setCircleColor(String color){
        circle.setStyle("-fx-fill:" + color + ";");
    }

    public void setNeighbourhood(ArrayList<CirclePiece> neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public ArrayList<CirclePiece> getNeighbourhood() {
        return neighbourhood;
    }

    public void clearCell() {
        isUserOwner = false;
        circle.setStyle("-fx-fill:#000;");
        isEmpty = true;
    }
}
