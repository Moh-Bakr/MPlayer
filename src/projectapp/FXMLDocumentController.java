/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projectapp;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author xps
 */
public class FXMLDocumentController implements Initializable {
    //declaring :
    //song selected num  is variable mean what the numper of song we selected
    static int songselectednum=0; 
    //list of songs we selected
    static List<File> files;
    //media and media player conserned with  the music
    static Media mediaa;
    static MediaPlayer mediaview;
    //play mean if our song is play
    //selected mean if our
    //change mean if you change the current song
    static boolean play=false,selected=false,change=false;
    @FXML
    private ImageView con;
    @FXML
    private ImageView wait;
    @FXML
    private ListView list;
    @FXML
    private Slider sliderv;
    @FXML
    private Slider sliderm;
    @FXML
    private Label totalDuration;
    @FXML
    private Label currentDuration;
    @FXML
    private Label artistName;
    @FXML
    private Label albumName;
    private Stage stage;
    @FXML
    private void changing() {
        //if you will change the music slider he will know whre you stop and start the song at this time
        mediaview.seek(Duration.seconds(sliderm.getValue()));
    }
    @FXML
    private void volume() {
        //if you will change the volume slider he will know whre you stop and make the song volume as you choosed
        mediaview.setVolume(sliderv.getValue()/100);
    }
    @FXML
    private void closebtn() {
        //get stage in variable stage
        stage=Projectapp.getStage();
        //close the stage (it also close the program its the main stage)
        stage.close();
    } 
    @FXML
    private void minimized() {
        //get stage in variable stage
        stage=Projectapp.getStage();
        //this code mean take the stage and put it in toolbar
        stage.setIconified(true);
    }  
    //chose one file
    @FXML
    private void choosefile() {
        //means you select new songs
        selected=true;
        change=true;
        //declare chooser
        FileChooser fc =new FileChooser();
        // it just declare inithial folder ____________not important_____________
        fc.setInitialDirectory(new File("C:\\Users\\xps\\Downloads\\Music"));
        // it declare the type of folder ____________important_____________
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("mp3 files","*.mp3"));
        //show the chooser to choose
        File file =fc.showOpenDialog(null);
        if (file !=null) {
            //clear the list
            list.getItems().clear();
            //put new song
            list.getItems().add(file.getAbsolutePath());
        }else{
            System.out.println("file not valied");
        }
    }
    @FXML
    private void choosefiles() {
        //means you select new songs
        change=true;
        selected=true;
        //as previus
        FileChooser fc =new FileChooser();
        fc.setInitialDirectory(new File("C:\\Users\\xps\\Downloads\\Music"));
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("mp3 files","*.mp3"));
        //open chooser ________put to chose multiple files________
        files =fc.showOpenMultipleDialog(null);
        if (files !=null) {
            list.getItems().clear();
            //add new list
            for (int i = 0; i < files.size(); i++)
                list.getItems().add(files.get(i).getAbsolutePath());
        }else{
            System.out.println("file not valied");
        }
            
    }
    @FXML
    private void constop() {
        if(selected){
            if(change){
                //take the path and convert to string and put it in textview
                artistName.setText(files.get(songselectednum).toURI().toString());
                //split the uri
                String arr[]=files.get(songselectednum).toURI().toString().split("/");
                //get the name of song _______its the last index of array__________
                albumName.setText(arr[arr.length-1]);
                //media is class to hold the file url 
                mediaa =new Media(files.get(songselectednum).toURI().toString());
                //mediaview change the media url and prepare it for dealing with
                mediaview =new MediaPlayer(mediaa);
                //change is false becose we alredy change the song 
                change=false;
                //this listenr consider with taking the current time 
                mediaview.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    
                    //but the time on slider its works in back ground (meaning you just call it once and it will work untill song is finished)
                    sliderm.setValue(newValue.toSeconds());
                    currentDuration.setText(((int)newValue.toMinutes()%60)+":"+((int)newValue.toSeconds()%60)+"");
                    }
                });
                mediaview.setOnReady(new Runnable() {
                @Override
                public void run() {
                    //this listener called once
                    //take the  all time of somg and put it on totalDuration pic
                    Duration total =mediaa.getDuration();
                    sliderm.setMax(total.toSeconds());
                    totalDuration.setText(((int)total.toMinutes()%60)+":"+((int)total.toSeconds()%60)+"");
                    }});
                //take the media volume and put it on sliderv
                sliderv.setValue(mediaview.getVolume()*100);
                //this listener is working in background consider of:
                //handle the event of slider and make action
                sliderv.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    //get volume when we change the slider and make the songs volume with this volume
                        mediaview.setVolume(sliderv.getValue()/100);
                    }
                });
        
                }
            if(!play){
                //if stop
                //make it play
                mediaview.play();
                //make play value true
                play =true;
            }
            else{
                //if play
                //make it sto[p
                mediaview.pause();
                //make play value false
                play =false;
            }
            }
    }
    @FXML
    private void nextvedio() {
        //if you are select the songs and its not the last song
        if(selected ==true && songselectednum < files.size() ){
            mediaview.pause();
            play =false;
            change=true;
             ++songselectednum;
            }
    }
    @FXML
    private void previousvedio() {
        //if you are select the songs and its the firist song
        if(songselectednum > 0 && selected){
                 mediaview.pause();
                play =false;
                change=true;
                --songselectednum;
            }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
