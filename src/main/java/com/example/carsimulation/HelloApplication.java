package com.example.carsimulation;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

public class HelloApplication extends Application {

    public static final double WIDTH = 1500;
    public static final double HEIGHT = 900;
    private static final double CAR_WIDTH = 50;
    private static final double CAR_HEIGHT = 50;
    private double velocity = 0;
    private final double acceleration = 0.03;
    private double angle = 0;
    private final double maxSpeed = 7;
    private final Set<String> pressedKeys = new HashSet<>();

    private final double turnAngle = 2;

    private final boolean smoothMovement = false;

    private final List<Minor> minors = new ArrayList<>();

    private boolean isDrifting = false;
    private double driftAngle = 0;
    private final double driftIntensity = 2; // Adjust as needed for noticeable but controlled drift
    private boolean wasDrifting = false;

    private final boolean blueCarMode = true;

    private final int minorCount = 1;



    @Override
    public void start(Stage stage) {

        Image kidLeft = new Image("file:/C:/Users/juanl/IdeaProjects/DrivingOverMinersNew/src/main/resources/com/example/carsimulation/DOM/KidLeft.png");
        Image kidRight = new Image("file:/C:/Users/juanl/IdeaProjects/DrivingOverMinersNew/src/main/resources/com/example/carsimulation/DOM/KidRight.png");

        Pane root = new Pane();
        root.setStyle("-fx-background-color: Silver");
        root.setPrefSize(1500, 900);
        root.setLayoutX(150);
        root.setLayoutY(50);
        Pane backGround = new Pane(root);
        backGround.setStyle("-fx-background-color: Gray");
        Scene scene = new Scene(backGround, 1800, 1000);

        Image carImage = new Image("file:/C:/Users/juanl/IdeaProjects/DrivingOverMinersNew/src/main/resources/com/example/carsimulation/DOM/car.png");
        Image blueCarImage = new Image("file:/C:/Users/juanl/IdeaProjects/DrivingOverMinersNew/src/main/resources/com/example/carsimulation/DOM/BlueCar.png");
        ImageView carImageView = new ImageView();
        if (blueCarMode){
            carImageView.setImage(blueCarImage);
        }
        else {
            carImageView.setImage(carImage);
        }

        carImageView.setFitWidth(CAR_WIDTH);
        carImageView.setFitHeight(CAR_HEIGHT);
        carImageView.setX(WIDTH / 2 - CAR_WIDTH / 2);
        carImageView.setY(HEIGHT / 2 - CAR_HEIGHT / 2);

        TextArea tempoDisplay = new TextArea();
        tempoDisplay.setEditable(false);
        tempoDisplay.setLayoutX(scene.getWidth() - 150);
        tempoDisplay.setLayoutY(10);
        tempoDisplay.setPrefSize(140, 30);

        root.getChildren().add(carImageView);
        backGround.getChildren().add(tempoDisplay);

        scene.setOnKeyPressed(e -> pressedKeys.add(e.getCode().toString()));
        scene.setOnKeyReleased(e -> pressedKeys.remove(e.getCode().toString()));

        for (int i = 0; i < minorCount; i++) {
            Minor minor = new Minor(kidLeft, kidRight, 750, 450, 1);
            minors.add(minor);
            root.getChildren().add(minor.imageView);
        }

        new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (pressedKeys.contains("W") && velocity <= maxSpeed) {
                    velocity += acceleration;
                }
                if (pressedKeys.contains("S") && velocity >= maxSpeed - 1.5 * maxSpeed) {
                    velocity -= acceleration / 2;
                }
                if (pressedKeys.contains("A") && (velocity > 0.3 || velocity < -0.3)) {
                    if (velocity > 0) {
                        if (isDrifting){
                            angle -= turnAngle - 1;
                        } else {
                            angle -= turnAngle;
                        }
                    } else {
                        if (isDrifting){
                            angle += turnAngle + 1;
                        } else {
                            angle += turnAngle;
                        }
                    }
                }
                if (pressedKeys.contains("D") && (velocity > 0.3 || velocity < -0.3)) {
                    if (velocity > 0) {
                        if (isDrifting){
                            angle += turnAngle - 1;
                        } else {
                            angle += turnAngle;
                        }

                    } else {
                        if (isDrifting){
                            angle -= turnAngle + 1;
                        } else {
                            angle -= turnAngle;
                        }
                    }
                }


                // Update car position
                double radian = Math.toRadians(angle);
                double deltaX = velocity * Math.cos(radian);
                double deltaY = velocity * Math.sin(radian);

                double newX = carImageView.getX() + deltaX;
                double newY = carImageView.getY() + deltaY;

                boolean collision = false;
                if (newX < 0 || newX + CAR_WIDTH > WIDTH) {
                    collision = true;
                    newX = Math.max(0, Math.min(newX, WIDTH - CAR_WIDTH));
                }
                if (newY < 0 || newY + CAR_HEIGHT > HEIGHT) {
                    collision = true;
                    newY = Math.max(0, Math.min(newY, HEIGHT - CAR_HEIGHT));
                }

                if (collision) {
                    if (velocity > 0) {
                        if (smoothMovement) {
                            angle += 180;
                        } else {
                            if (velocity > 0.5){
                                velocity *= -0.5;}
                            else {
                                velocity = 0;
                            }
                        }
                    } else if (velocity < 0) {
                        if (velocity < -0.5){
                            velocity *= -0.5;}
                        else {
                            velocity = 0;
                        }
                    }
                    if (angle >= 360) {
                        angle -= 360;
                    }
                }
                if (pressedKeys.contains("UP") && pressedKeys.contains("DOWN") && Math.abs(velocity) > 1) {
                    if (!wasDrifting) {
                        driftAngle = angle;
                        wasDrifting = true;
                    }
                } else {
                    wasDrifting = false;
                }
                if (pressedKeys.contains("W") && pressedKeys.contains("S") && Math.abs(velocity) > 1) {
                    isDrifting = true;
                    if (!wasDrifting) {
                        driftAngle = angle;
                        wasDrifting = true;
                    }
                } else {
                    isDrifting = false;
                    wasDrifting = false;
                }

                if (isDrifting) {

                    if (velocity > 0){
                        velocity -= 0.07;
                    } else {
                        velocity += 0.07;
                    }

                    double driftRadian = Math.toRadians(driftAngle);
                    double driftDeltaX = driftIntensity * Math.cos(driftRadian);
                    double driftDeltaY = driftIntensity * Math.sin(driftRadian);
                    newX += driftDeltaX;
                    newY += driftDeltaY;
                }

                carImageView.setX(newX);
                carImageView.setY(newY);
                carImageView.setRotate(angle);

                if (velocity > 0) {
                    velocity -= 0.01;
                } else if (velocity < 0) {
                    velocity += 0.01;
                }




                tempoDisplay.setText("Speed: " + String.format("%.0f", (velocity/6)*100));


            }
        }.start();

        stage.setScene(scene);
        stage.setTitle("Driving over Minors");
        stage.setResizable(false);
        stage.show();

        scene.getRoot().requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}