package com.example.carsimulation;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static com.example.carsimulation.HelloApplication.WIDTH;

class Minor {
    ImageView imageView;
    double x, y;
    double speed;
    boolean movingRight;

    Image kidLeft = new Image("file:/C:/Users/juanl/IdeaProjects/DrivingOverMinersNew/src/main/resources/com/example/carsimulation/DOM/KidLeft.png");
    Image kidRight = new Image("file:/C:/Users/juanl/IdeaProjects/DrivingOverMinersNew/src/main/resources/com/example/carsimulation/DOM/KidRight.png");

    public Minor(Image leftImage, Image rightImage, double x, double y, double speed) {
        this.imageView = new ImageView(leftImage);
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.movingRight = true; // Startet mit der Bewegung nach rechts
        updateImage(rightImage);
    }

    public void update() {
        x += (movingRight ? speed : -speed);
        if (x <= 0 || x + imageView.getFitWidth() >= WIDTH) {
            movingRight = !movingRight;
            updateImage(movingRight ? kidRight : kidLeft);
        }
    }

    private void updateImage(Image image) {
        imageView.setImage(image);
    }
}
