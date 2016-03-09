package org.panda_lang.panda.lang.ui.util;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PandaInterface extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setWidth(bounds.getWidth() - 20);
        stage.setHeight(bounds.getHeight() * 0.8);
        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 2);

        Group root = new Group();
        Scene scene = new Scene(root, stage.getWidth(), stage.getHeight());

        WebView webView = new WebView();
        webView.setPrefSize(stage.getWidth(), stage.getHeight());
        webView.getEngine().load("https://github.com/Panda-Programming-Language");
        root.getChildren().add(webView);

        stage.setTitle("Panda Interface");
        stage.setScene(scene);
        stage.show();
    }

    public void run() {
        launch();
    }

}
