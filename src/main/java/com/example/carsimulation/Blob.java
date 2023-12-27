package com.example.carsimulation;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.util.Random;

public class Blob extends Circle {
    private double x;
    private double y;
    private double velocityX;
    private double velocityY;
    private Random random = new Random();
    public static final double BLOB_RADIUS = 20; // Define the Blob's radius here

    public Blob(double x, double y, double blobRadius) {
        super(x, y, BLOB_RADIUS, Color.RED);
        this.x = x;
        this.y = y;
        reset();
    }

    public void reset() {
        // Reset Blob's position and velocity
        this.x = random.nextDouble() * (HelloApplication.WIDTH - 2 * BLOB_RADIUS);
        this.y = random.nextDouble() * (HelloApplication.HEIGHT - 2 * BLOB_RADIUS);
        this.velocityX = random.nextDouble() * 2 - 1;
        this.velocityY = random.nextDouble() * 2 - 1;
        setCenterX(x + BLOB_RADIUS);
        setCenterY(y + BLOB_RADIUS);
    }

    public void update() {
        // Update Blob's position based on velocity
        x += velocityX;
        y += velocityY;

        // Bounce off walls if Blob hits them
        if (x < 0 || x + 2 * BLOB_RADIUS > HelloApplication.WIDTH) {
            velocityX *= -1;
        }
        if (y < 0 || y + 2 * BLOB_RADIUS > HelloApplication.HEIGHT) {
            velocityY *= -1;
        }

        setCenterX(x + BLOB_RADIUS);
        setCenterY(y + BLOB_RADIUS);
    }
}
