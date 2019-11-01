package player;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import javax.sound.sampled.AudioFileFormat;
import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javax.sound.sampled.AudioSystem.getAudioFileFormat;

public class playercode {

    public static void main(String[]args) throws Exception{
        display disp = new display();
        disp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        disp.setVisible(true);
    }

    public FileInputStream fis;
    public BufferedInputStream bis;
    public int pauselocation;
    public int totalsonglength;
    public Player player;
    public static String filelocation;
    public int ispause=0;


    public void Pause(){

        //this.position = player.getPosition(); //pozycja w milisekundach
        //position = fis.available();//bajty do konca
        if(player!=null){
            try {
                ispause=1;
                pauselocation = fis.available();
                player.close();
            }
            catch(java.io.IOException e){
                System.out.println(e);
            }
        }

    }

    public void rewind (){
        if(player!=null){
            player.close();
            pauselocation=0;
        }
    }

    public void Play(String path){

            try {
                fis = new FileInputStream(path); //otwieranie pliku
                bis = new BufferedInputStream(fis);//zmniejsza czas operacji, rozdziela na mniejsze bajty czy cos takiego
                filelocation = path;
                player = new Player(bis);
                totalsonglength = fis.available();

                System.out.println(totalsonglength);

            } catch (FileNotFoundException e) {
                System.out.println("file not found");
            } catch (JavaLayerException ex) {
                System.out.println("layer exception");
            } catch (IOException e) {
                System.out.println(e);
            }

                                                                                        // mozna zapisac Thread thread = new Thread(){
                                                                                        // tutaj cos}
                                                                                        // thrad.start() - startuje thread


            new Thread() {
                @Override
                public void run() {
                    try {
                            player.play();
                    } catch (JavaLayerException exc) {
                        System.out.println("layer exception");
                    }
                }
            }.start();
    }


    public void Resume(){
        try {
            fis = new FileInputStream(filelocation);
            bis = new BufferedInputStream(fis);

            player = new Player(bis);
            fis.skip(totalsonglength - pauselocation);

        }
        catch(FileNotFoundException e){
            System.out.println("file not found");
        }
        catch(JavaLayerException ex){
            System.out.println("layer exception");
        }
        catch(java.io.IOException e){
            System.out.println(e);
        }


        new Thread() {
            @Override
            public void run() {
                try {
                    player.play();
                } catch (JavaLayerException exc) {
                    System.out.println("layer exception");
                }
            }
        }.start();


    }




}
