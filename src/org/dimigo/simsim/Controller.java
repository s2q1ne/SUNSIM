package org.dimigo.simsim;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

public class Controller implements Initializable {
    @FXML private Button send;
    @FXML private TextField input;
    @FXML private ListView list_view;
    private static HttpURLConnection con;
    private static final String apiURL = "https://wsapi.simsimi.com/190410/talk";
    private static URL url;
    private static final String apiKey = "R5kHSoRajG4Jxj0jj01uC1Oilz1LMtg4irrnc5ae";
    private static int minLength = 1;
    private static int maxLength = 256;
    private static float minProb = 0;
    private static float maxProb = 1;
    private static List<String> ban= new ArrayList<>();

    public static void setMinLength(int len) {
        minLength = len;
    }

    public static void setMaxLength(int len) {
        maxLength = len;
    }

    public static void setMinProb(float prob) {
        minProb = prob;
    }

    public static void setMaxProb(float prob) {
        maxProb = prob;
    }

    @FXML
    public void handleAction(ActionEvent event) {
        if(!input.getText().equals("")) {
            String str = input.getText();
            input.setText("");
            sendMessage(str);
        }
    }

    @FXML
    public void onEnter(KeyEvent e){
        if (e.getCode().equals(KeyCode.ENTER))
        {
            if(!input.getText().equals("")) {
                String str = input.getText();
                input.setText("");
                sendMessage(str);
            }
        }

    }

    private static void connect() throws Exception{
        url = new URL(apiURL);
        con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("x-api-key", apiKey);
        con.setDoOutput(true);
    }

    private void sendMessage(String message){
        try {

            makeMyMessage(message);
            connect();
            String text = URLEncoder.encode(message, "UTF-8");
            String data;
            // post request
            data = String.format("{ \"utext\":\"%s\", \"lang\":\"ko\", \"cf_info\":[\"atext_bad_prob\"], \"atext_bad_prob_min\":%f, \"atext_bad_prob_max\":%f, \"atext_length_min\":%d, \"atext_length_max\":%d }", text,minProb, maxProb, minLength, maxLength);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(data);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            String res = URLDecoder.decode(response.toString(), "UTF-8");
            for ( String banText: ban) {
                res = res.replace(banText, "<검열됨>");
            }
            System.out.println(data);
            System.out.println(res);
            try {
                Map<String, Object> map = new ObjectMapper().readValue(res, Map.class);
                String mes = (String) map.get("atext");
                makeMyMessage(mes);
                System.out.println((int)map.get("atext_bad_prob"));
                TodayWords.addWordsCount();
                TodayWords.addBadStack((int)map.get("atext_bad_prob"));
            }catch( Exception e ){
                System.out.println("E in parsing or making message"+e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void makeMyMessage(String mes){
        list_view.getItems().add(mes);
        System.out.println("Listview add");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        list_view.setItems(FXCollections.observableArrayList());
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
    public void todayWordsScene(){
        try{
            Main main = Main.getInstance();
            main.goTodayWords();
        }catch(Exception e){
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
