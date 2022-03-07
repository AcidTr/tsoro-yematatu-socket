package com.example.socket;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {

    private CliThread socketClient;

    private boolean isTurn;

    private boolean gameStarted;

    private CirclePiece selectedCircle;

    private ArrayList<CirclePiece> circles;

    private ArrayList<CirclePiece> possibleCirclesMovement;

    private HashMap<String, CirclePiece> circlePiecesMap;

    private int numberOfPiecesPlaced;

    @FXML
    private TextField inputMessage;

    @FXML
    private VBox vBoxMessages;

    @FXML
    private ScrollPane scrollPaneChat;

    @FXML
    private Circle circle1, circle2, circle3, circle4, circle5, circle6, circle7;

    @FXML
    private Label turnLabel;

    @FXML
    private  Label drawLabel;

    @FXML
    private Button drawButton;

    @FXML
    private Button cancelDrawButton;

    @FXML
    public void onSendMessage (ActionEvent event) {
        String messageToSend = inputMessage.getText();
        if (!messageToSend.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5,5,5,10));

            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);

            textFlow.setStyle("-fx-color: rgb(239,242,255);" +
                    "-fx-background-color: rgb(15,125,242);" +
                    "-fx-background-radius: 20px;");

            textFlow.setPadding(new Insets(5,10,5,10));
            text.setFill(Color.color(0.934,0.945,0.996));

            hBox.getChildren().add(textFlow);
            vBoxMessages.getChildren().add(hBox);

            socketClient.sendMessage("TX:" + messageToSend);

            inputMessage.clear();
        }
    }

    private void initializeCircles() {
        possibleCirclesMovement = new ArrayList<>();

        CirclePiece circlePiece1 = new CirclePiece(circle1);
        CirclePiece circlePiece2 = new CirclePiece(circle2);
        CirclePiece circlePiece3 = new CirclePiece(circle3);
        CirclePiece circlePiece4 = new CirclePiece(circle4);
        CirclePiece circlePiece5 = new CirclePiece(circle5);
        CirclePiece circlePiece6 = new CirclePiece(circle6);
        CirclePiece circlePiece7 = new CirclePiece(circle7);

        ArrayList<CirclePiece> circle1Neighbourhood = new ArrayList<>(Arrays.asList(
                circlePiece2,
                circlePiece3,
                circlePiece4,
                circlePiece5,
                circlePiece6,
                circlePiece7
        ));

        circlePiece1.setNeighbourhood(circle1Neighbourhood);

        ArrayList<CirclePiece> circle2Neighbourhood = new ArrayList<>(Arrays.asList(
                circlePiece1,
                circlePiece3,
                circlePiece4,
                circlePiece5
        ));

        circlePiece2.setNeighbourhood(circle2Neighbourhood);

        ArrayList<CirclePiece> circle3Neighbourhood = new ArrayList<>(Arrays.asList(
                circlePiece1,
                circlePiece2,
                circlePiece4,
                circlePiece6
        ));

        circlePiece3.setNeighbourhood(circle3Neighbourhood);

        ArrayList<CirclePiece> circle4Neighbourhood = new ArrayList<>(Arrays.asList(
                circlePiece1,
                circlePiece2,
                circlePiece3,
                circlePiece7
        ));

        circlePiece4.setNeighbourhood(circle4Neighbourhood);

        ArrayList<CirclePiece> circle5Neighbourhood = new ArrayList<>(Arrays.asList(
                circlePiece1,
                circlePiece2,
                circlePiece6,
                circlePiece7
        ));

        circlePiece5.setNeighbourhood(circle5Neighbourhood);

        ArrayList<CirclePiece> circle6Neighbourhood = new ArrayList<>(Arrays.asList(
                circlePiece1,
                circlePiece3,
                circlePiece5,
                circlePiece7
        ));

        circlePiece6.setNeighbourhood(circle6Neighbourhood);

        ArrayList<CirclePiece> circle7Neighbourhood = new ArrayList<>(Arrays.asList(
                circlePiece1,
                circlePiece4,
                circlePiece5,
                circlePiece6
        ));

        circlePiece7.setNeighbourhood(circle7Neighbourhood);


        circles = new ArrayList<>(Arrays.asList(
                circlePiece1,
                circlePiece2,
                circlePiece3,
                circlePiece4,
                circlePiece5,
                circlePiece6,
                circlePiece7

        ));

        circlePiecesMap = new HashMap<>();

        for (CirclePiece circle: circles
        ) {
            circlePiecesMap.put(circle.getCircle().getId(), circle);
        }
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {

        socketClient = new CliThread();
        this.receiveMessage(vBoxMessages);

        gameStarted = false;

        turnLabel.setText("Waiting for opponent to connect");

        initializeCircles();


        numberOfPiecesPlaced = 0;


        vBoxMessages.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPaneChat.setVvalue((Double) newValue);
            }
        });

    }

    public static void addLabel (String messageFromClient, VBox vBox) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,5,5,10));

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("-fx-background-color: rgb(233,233,235);" +
                "-fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5,10,5,10));

        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

    private void setCircleValue(boolean owner, CirclePiece circle){
        circle.setUserOwner(owner);
        numberOfPiecesPlaced += 1;
        //TODO: check if the call is for movement/placement of pieces
        if(numberOfPiecesPlaced == 6) {
            gameStarted = true;
        }
        isTurn = !owner;
        setTurnLabelText(isTurn);
    }

    private void setTurnLabelText(boolean isUserTurn) {
        String labelText = isUserTurn ? "You turn": "Opponent Turn";
        Platform.runLater(() -> {
            turnLabel.setText(labelText);
        });
    }

    private void processMessage(String message) {
        String type = message.split(":")[0];
        String messageContent = message.split(":")[1];

        switch (type) {
            case "PL":
                System.out.println("Place type");
                CirclePiece currentCircle = circlePiecesMap.get(messageContent);
                setCircleValue(false, currentCircle);

                break;
            case "MV":
                System.out.println("Movement type");

                String previousCircleId = messageContent.split("-")[0];
                String nextCircleId = messageContent.split("-")[1];

                CirclePiece previousCircle = circlePiecesMap.get(previousCircleId);
                CirclePiece nextCircle = circlePiecesMap.get(nextCircleId);

                previousCircle.clearCell();

                nextCircle.setUserOwner(false);

                isTurn = true;

                setTurnLabelText(true);

                break;
            case "TX":
                System.out.println("Text type");
                addLabel(messageContent, vBoxMessages);
                break;
            case "CF":
                boolean isFirst = messageContent.equals("true");
                isTurn = isFirst;


                setTurnLabelText(isFirst);
                System.out.println(isFirst);
                System.out.println("Config type");
                break;
            case "GU":
                System.out.println("GiveUp type");
                System.out.println("Opponent gave up. You're the winner!");

                Platform.runLater(() -> {
                    turnLabel.setText("Opponent gave up!");
                });
                isTurn = false;

                break;
            case "DR":
                System.out.println("Draw type");

                if(messageContent.equals("ask")) {
                    System.out.println("user is asking to draw game");
                    Platform.runLater(() -> {
                        drawLabel.setText("user is asking to draw");
                        drawLabel.setOpacity(1);
                        drawButton.setOpacity(1);
                        cancelDrawButton.setOpacity(1);
                    });
                }else {
                    System.out.println("draw game");
                    isTurn = false;
                    Platform.runLater(() -> {
                        turnLabel.setText("draw game!");
                    });
                }
                break;
            case "WI":
                isTurn = false;
                System.out.println("Opponent is the winner!");
                Platform.runLater(() -> {
                    turnLabel.setText("You lose!");
                });

                break;
            default:
                break;

        }
    }


    public void receiveMessage(VBox vBox) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                socketClient.isConnected = true;
                while (socketClient.isConnected) {
                    try {
                        String messageReceived = socketClient.objectInputStream.readUTF();

                        System.out.println(messageReceived);
                        processMessage(messageReceived);

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        }).start();
    }

    private boolean isWinner() {
        CirclePiece circlePiece1 = circles.get(0);
        CirclePiece circlePiece2 = circles.get(1);
        CirclePiece circlePiece3 = circles.get(2);
        CirclePiece circlePiece4 = circles.get(3);
        CirclePiece circlePiece5 = circles.get(4);
        CirclePiece circlePiece6 = circles.get(5);
        CirclePiece circlePiece7 = circles.get(6);


        return circlePiece1.isUserOwner() && circlePiece3.isUserOwner() && circlePiece6.isUserOwner()
                || circlePiece1.isUserOwner() && circlePiece2.isUserOwner() && circlePiece5.isUserOwner()
                || circlePiece1.isUserOwner() && circlePiece4.isUserOwner() && circlePiece7.isUserOwner()
                || circlePiece2.isUserOwner() && circlePiece3.isUserOwner() && circlePiece4.isUserOwner()
                || circlePiece5.isUserOwner() && circlePiece6.isUserOwner() && circlePiece7.isUserOwner();

    }

    @FXML
    private void onCellClicked (MouseEvent event) {

        System.out.println("circle clicked");

        Circle circle = (Circle) event.getSource();

        System.out.println(circle.getId());

        //TODO: (optional) add lock to prevent user click while processing move
        if(isTurn) {
            CirclePiece currentCircle = circlePiecesMap.get(circle.getId());

            if(!gameStarted) {
                // place

                if(currentCircle.isEmpty()){
                    // can place
                    setCircleValue(true, currentCircle);

                    socketClient.sendMessage("PL:" + circle.getId());

                } else {
                    //TODO: Create a toast;
                }
            } else {
                if(currentCircle.isUserOwner() || (selectedCircle != null && currentCircle.isEmpty())) {
                    // select
                    //TODO: ta matando movimento válido.
                    if(selectedCircle != null && !selectedCircle.equals(currentCircle) && !currentCircle.isEmpty() ){
                        possibleCirclesMovement.clear();

                        for (CirclePiece iCircle : circles
                        ) {
                            boolean isCellEmpty = iCircle.isEmpty();
                            if (isCellEmpty) {
                                iCircle.setCircleColor("#000");
                            }

                        }
                    }
                    if (selectedCircle == null) {

                        selectedCircle = currentCircle;

                        possibleCirclesMovement.clear();
                        for (CirclePiece iCircle : currentCircle.getNeighbourhood()
                        ) {
                            boolean isCellEmpty = iCircle.isEmpty();
                            if (isCellEmpty) {
                                iCircle.setCircleColor("#3f7");
                                possibleCirclesMovement.add(iCircle);
                            }

                        }

                    } else {
                        if (!selectedCircle.equals(currentCircle) && possibleCirclesMovement.contains(currentCircle)) {
                            possibleCirclesMovement.clear();

                            selectedCircle.clearCell();

                            currentCircle.setUserOwner(true);

                            isTurn = false;

                            if(isWinner()){
                                Platform.runLater(() -> {
                                    turnLabel.setText("You win!");
                                });
                                socketClient.sendMessage("MV:" + selectedCircle.getCircle().getId() + "-" + circle.getId());
                                socketClient.sendMessage("WI:22");
                            } else {
                                System.out.println("no win yet!");
                                setTurnLabelText(false);
                                socketClient.sendMessage("MV:" + selectedCircle.getCircle().getId() + "-" + circle.getId());
                            };

                        } else {
                            System.out.println("do nothing");

                            for (CirclePiece iCircle : currentCircle.getNeighbourhood()
                            ) {
                                boolean isCellEmpty = iCircle.isEmpty();
                                if (isCellEmpty) {
                                    iCircle.setCircleColor("#000");
                                }

                            }
                        }

                        selectedCircle = null;

                    }
                }
            }
        }
    }

    @FXML
    private void onGiveUp(MouseEvent event) {

        socketClient.sendMessage("GU:true");

        Platform.runLater(() -> {
            turnLabel.setText("You gave up!");
        });
        isTurn = false;
        System.out.println("Give up message sent!");
    }

    @FXML
    private void onAskToDraw(MouseEvent event) {
        socketClient.sendMessage("DR:ask");
        System.out.println("user is trying to draw game!");
    }

    @FXML
    private void confirmDraw(MouseEvent event) {
        System.out.println("you confirmed draw game!");
        isTurn = false;
        socketClient.sendMessage("DR:confirm");
        Platform.runLater(() -> {
            turnLabel.setText("draw game!");
            drawLabel.setOpacity(0);
            drawButton.setOpacity(0);
            cancelDrawButton.setOpacity(0);
        });

    }

    @FXML
    private void onCancelDraw(MouseEvent event) {
        System.out.println("you refused draw game");
        Platform.runLater(() -> {
            drawLabel.setOpacity(0);
            drawButton.setOpacity(0);
            cancelDrawButton.setOpacity(0);
        });
    }

}