package org.dimigo.simsim;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

public class TestWords {

    @FXML private Button send;
    @FXML private TextField input;
    @FXML private ListView list_view;

    private static HttpURLConnection con;
    private static final String apiURL = "https://wsapi.simsimi.com/190410/talk";
    private static URL url;
    private static final String apiKey = "R5kHSoRajG4Jxj0jj01uC1Oilz1LMtg4irrnc5ae";

    private static int chatCount=0;
    private static int badCount=0;
    private static int chatLengthSum=0;

    public static void addChatCount() {
        TestWords.chatCount ++;
    }

    public static void addBadCount(int bad) {
        TestWords.badCount += bad;
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
            data = String.format("{ \"utext\":\"%s\", \"lang\":\"ko\", \"cf_info\":[\"atext_bad_prob\"] }", text);
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
            String res = URLDecoder.decode(response.toString(),"UTF-8").replaceAll("\n","").replace("www.redtube.com","자체 필터링 되었습니다.").replace("섹스","자체 필터링 되었습니다.");
            System.out.println(data);
            System.out.println(res);
            try {
                Map<String, Object> map = new ObjectMapper().readValue(res, Map.class);
                String mes = (String) map.get("atext");
                makeMyMessage(mes);
                System.out.println((int)map.get("atext_bad_prob"));
                addChatCount();
                addBadCount((int)map.get("atext_bad_prob"));
                chatLengthSum += message.length();
                if( chatCount >= 10 ){
                    //테스트 완료.
                    chatLengthSum /= 10;
                    badCount /= 10;
                    if( chatLengthSum < 5 ){ // 단답형
                        Controller.setMinLength(0);
                        Controller.setMaxLength(15);
                        System.out.print("단답형");
                        makeMyMessage("단답형");
                    }else if( chatLengthSum > 15){ // 장문형
                        Controller.setMinLength(20);
                        Controller.setMaxLength(256);
                        System.out.print("장문형");
                        makeMyMessage("장문형");
                    }else{ // 기본형
                        Controller.setMinLength(0);
                        Controller.setMaxLength(256);
                        System.out.print("기본형");
                        makeMyMessage("기본형");
                    }
                    if( badCount > 10){ // 나쁨
                        Controller.setMinProb(0.1f);
                        Controller.setMaxProb(1);
                        System.out.println(" 나쁨");
                        makeMyMessage(" 나쁨");
                    }else if( badCount < 5 ){ // 착함
                        Controller.setMinProb(0);
                        Controller.setMaxProb(0.09f);
                        System.out.println(" 착함");
                        makeMyMessage(" 착함");
                    }else{ // 기본
                        Controller.setMinProb(0);
                        Controller.setMaxProb(1);
                        System.out.println(" 기본");
                        makeMyMessage(" 기본");
                    }
                    Thread.sleep(2000);
                    mainScene();
                }
            }catch( Exception e ){
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e);
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
    private void makeMyMessage(String mes){
        list_view.getItems().add(mes);
        System.out.println("Listview add");
    }
}
