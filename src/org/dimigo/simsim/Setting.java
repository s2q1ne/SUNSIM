package org.dimigo.simsim;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Setting implements Initializable {

    @FXML private ComboBox<String> settingLength;
    @FXML private ComboBox<String> settingPersonality;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Controller.setMinLength(0);
        Controller.setMaxLength(0);
        Controller.setMinProb(0);
        Controller.setMaxProb(1);
        settingLength.getSelectionModel().selectLast();
        settingPersonality.getSelectionModel().selectLast();
    }
    @FXML
    public void saveSetting(){
        switch (settingLength.getValue()){
            case "장문형":
                Controller.setMinLength(20);
                Controller.setMaxLength(256);
                break;
            case "단답형":
                Controller.setMinLength(1);
                Controller.setMaxLength(15);
                break;
            case "일반형":
            default:
                Controller.setMinLength(1);
                Controller.setMaxLength(256);
                break;
        }
        switch (settingPersonality.getValue()){
            case "착함":
                Controller.setMinProb(0);
                Controller.setMaxProb(0.09f);
                break;
            case "나쁨":
                Controller.setMinProb(0.1f);
                Controller.setMaxProb(1);
                break;
            case "기본":
            default    :
                Controller.setMinProb(0);
                Controller.setMaxProb(1);
                break;
        }
        System.out.println(settingLength.getValue()+","+settingPersonality.getValue());
    }
    @FXML void mainScene(){
        try{
            Main main = Main.getInstance();
            main.goMain();
        }catch (Exception e){
            System.out.println("E: "+ e.getMessage());
        }
    }

    @FXML
    public void todayWordsScene(){
        try{
            Main main = Main.getInstance();
            main.goTodayWords();
        }catch(Exception e){
            System.out.println("E: "+e.getMessage());
        }
    }
    @FXML
    public void testWordsScene(){
        try{
            Main main = Main.getInstance();
            main.goTestWords();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
