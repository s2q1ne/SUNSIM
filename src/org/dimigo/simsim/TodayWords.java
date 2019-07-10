package org.dimigo.simsim;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class TodayWords implements Initializable {

    @FXML private Label display_text;

    private static int words_count=0;
    private static int bad_stack=0;

    public static void addWordsCount() {
        TodayWords.words_count++;
    }

    public static void addBadStack(int bad) {
        TodayWords.bad_stack+=bad;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(String.format("bad_stack: %d, words_count: %d",bad_stack,words_count));
        if(bad_stack!=0&&words_count!=0)
            display_text.setText(String.format(" 당신의 말에 불순물 %.0f만큼", (float)TodayWords.bad_stack*100/TodayWords.words_count));
        else display_text.setText(" 당신의 말에 불순물 0%");
    }

    @FXML
    void mainScene(){
        try{
            Main main = Main.getInstance();
            main.goMain();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void settingScene(){
        try {
            Main main = Main.getInstance();
            main.goSetting();
        }catch(Exception e) {
            System.out.println(e.getMessage());
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
