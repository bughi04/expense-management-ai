module org.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;

    // Export our package to javafx modules
    exports org.example.project;

    // Open our package to JavaFX for reflection access
    opens org.example.project to javafx.base, javafx.controls, javafx.graphics;
}