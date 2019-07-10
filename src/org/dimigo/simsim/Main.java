package org.dimigo.simsim;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static Main instance;
    private Stage stage;

    public Main(){
        super();
        instance = this;
    }

    public static Main getInstance(){
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("SUMSIMI");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();

        this.stage = primaryStage;
    }
    public static void main(String[] args) {
        launch(args);
    }

    public void goSetting(){
        try{
            replaceSceneContent("setting.fxml");
            System.out.println("Scene changed : -> Setting");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goMain(){
        try{
            replaceSceneContent("sample.fxml");
            System.out.println("Scene changed : -> Main");
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goTodayWords(){
        try{
            replaceSceneContent("today_words.fxml");
            System.out.println("Scene changed : -> Today Words");
        }catch(Exception e){
            System.out.println("E: " +e.getMessage());
        }
    }

    public void goTestWords(){
        try{
            replaceSceneContent("test_words.fxml");
            System.out.println("Scene changed : -> Test Words");
        }catch (Exception e){
            System.out.println("E: "+ e.getMessage());
        }
    }

    private Parent replaceSceneContent(String fxml) throws Exception {
        Parent page = (Parent) FXMLLoader.load(Main.class.getResource(fxml), null, new JavaFXBuilderFactory());
        Scene scene = stage.getScene();
        if (scene == null) {
            scene = new Scene(page, 900, 600);
            stage.setScene(scene);
        } else {
            stage.getScene().setRoot(page);
        }
        stage.sizeToScene();
        return page;
    }

}
