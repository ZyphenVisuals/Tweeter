module com.zyphenvisuals.tweeter {
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires atlantafx.base;
    requires static lombok;
    requires java.net.http;
    requires com.google.gson;
    requires javafx.graphics;
    requires java.sql;

    opens com.zyphenvisuals.tweeter.views to javafx.fxml;
    opens com.zyphenvisuals.tweeter.components to javafx.fxml;
    opens com.zyphenvisuals.tweeter.model to com.google.gson;
    exports com.zyphenvisuals.tweeter;
    exports com.zyphenvisuals.tweeter.router;
}