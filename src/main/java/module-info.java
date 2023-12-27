module com.example.carsimulation {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.carsimulation to javafx.fxml;
    exports com.example.carsimulation;
}