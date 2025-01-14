module com.zyphenvisuals.tweeter {
    requires javafx.fxml;

    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires atlantafx.base;
    requires static lombok;
    requires java.net.http;
    requires com.google.gson;

    opens com.zyphenvisuals.tweeter.views to javafx.fxml;
    exports com.zyphenvisuals.tweeter;
    exports com.zyphenvisuals.tweeter.router;
}