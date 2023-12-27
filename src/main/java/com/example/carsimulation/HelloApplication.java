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

    public static final double WIDTH = 800;
    public static final double HEIGHT = 800;
    private static final double CAR_WIDTH = 50;
    private static final double CAR_HEIGHT = 50;
    private double velocity = 0;
    private final double acceleration = 0.07;
    private double angle = 0;
    private final double maxSpeed = 7;
    private final Set<String> pressedKeys = new HashSet<>();

    private final boolean smoothMovement = false;

    private final List<Blob> blobs = new ArrayList<>(); // List to manage Blob objects

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: Silver");
        root.setPrefSize(800, 800);
        root.setLayoutX(500);
        root.setLayoutY(100);
        Pane backGround = new Pane(root);
        backGround.setStyle("-fx-background-color: Gray");
        Scene scene = new Scene(backGround, 1800, 1000);

        Image carImage = new Image("file:/C:/Users/Luej9/Desktop/DOM/car.png");
        ImageView carImageView = new ImageView(carImage);

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

        // Create Blob objects and add them to the list
        for (int i = 0; i < 10; i++) {
            Blob blob = new Blob(20, 20, 20);
            blobs.add(blob);
            root.getChildren().add(blob);
        }

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Handle car movement
                if (pressedKeys.contains("UP") && velocity <= maxSpeed) {
                    velocity += acceleration;
                }
                if (pressedKeys.contains("DOWN") && velocity >= maxSpeed - 1.5 * maxSpeed) {
                    velocity -= acceleration / 2;
                }
                if (pressedKeys.contains("LEFT") && (velocity > 0.3 || velocity < -0.3)) {
                    if (velocity > 0) {
                        angle -= 3;
                    } else {
                        angle += 3;
                    }
                }
                if (pressedKeys.contains("RIGHT") && (velocity > 0.3 || velocity < -0.3)) {
                    if (velocity > 0) {
                        angle += 3;
                    } else {
                        angle -= 3;
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
                            velocity = 0;
                        }
                    } else if (velocity < 0) {
                        velocity = 0;
                    }
                    if (angle >= 360) {
                        angle -= 360;
                    }
                }

                carImageView.setX(newX);
                carImageView.setY(newY);
                carImageView.setRotate(angle);

                if (velocity > 0) {
                    velocity -= 0.01;
                } else if (velocity < 0) {
                    velocity += 0.01;
                }

                for (Blob bl : blobs) {
                    bl.update();
                    if (carImageView.getBoundsInParent().intersects(bl.getBoundsInParent())) {
                        if (velocity > 2.5|| velocity < -2.5) {
                            // Remove the blob from the list and the root
                            root.getChildren().remove(bl);
                            blobs.remove(bl);
                            Blob blob = new Blob(20, 20, 20);
                            blobs.add(blob);
                            root.getChildren().add(blob);

                        }
                    }
                }


                tempoDisplay.setText("Speed: " + String.format("%.1f", velocity));

                // Update Blob movements
                for (Blob blob : blobs) {
                    blob.update();
                }
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
