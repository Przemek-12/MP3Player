package player;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;



public class display extends JFrame{

    playercode pc = new playercode();

    private JButton previous;
    private JPanel panel1;
    private JButton next;
    private JButton rewind;
    private JButton pause;
    private JButton play;
    private JList list1;
    private JTextField textField1;
    private JButton Browse;
    private JLabel labmiliseconds;
    private JLabel labminutes;
    private JLabel labseconds;
    private JSlider slider1;
    private JButton Add;
    private JButton loop;
    private JSlider volumeslider;
    private JScrollPane scroll;
    public  DefaultListModel dlm;
    public String path;
    public String songname;
    private int minutes=0;//all
    private int seconds=0;//all
    private int miliseconds=0;//all
    private int seconds2=0;//display
    private int miliseconds2=0;//display
    public static boolean state = true;
    public int isrewind=0;
    public int dur;//duration, slider max value
    public int value;//bits amount
    public int isplaylist=0; //browse playlist
    public int issuspend=0;
    public int isloop=0;
    public int selectedindex=0;


    public display() throws Exception {

        dlm = new DefaultListModel();

        sqlmethods sql = new sqlmethods();
        ArrayList<String> list = sql.array();
        for(String set:list){
            dlm.addElement(set);
        }

        list1.setModel(dlm);
        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        slider1.setMinimum(0);
        slider1.setValue(0);
        volumeslider.setMinimum(0);
        volumeslider.setMaximum(100);
        volumeslider.setValue(100);
        textField1.setEditable(false);
        labmiliseconds.setText("0000");
        labseconds.setText("00");
        labminutes.setText("00");
        setTitle("MP3 player");
        setSize(800,500);
        loop.setBackground(Color.DARK_GRAY);
        loop.setText("loop: OFF");
        setResizable(false);
        add(panel1);

        Thread t=new Thread(){
            @Override
            public void run(){
                            try{
                                while(state==true){
                                        try {
                                            sleep(1);
                                            miliseconds++;
                                            value=value+40;
                                            slider1.setValue(value);

                                            miliseconds=value/40;
                                            seconds=miliseconds/1000;
                                            minutes=seconds/60;
                                            miliseconds2++;

                                            if(miliseconds%1000==0){
                                                miliseconds2=0;
                                                seconds2++;
                                            }
                                            if(seconds%60==0){
                                                seconds2=0;
                                            }

                                            labmiliseconds.setText(String.valueOf(miliseconds2));
                                            labseconds.setText(String.valueOf(seconds2));
                                            labminutes.setText(String.valueOf(minutes));

                                            if(value>dur){
                                                labmiliseconds.setText("0000");
                                                labseconds.setText("00");
                                                labminutes.setText("00");
                                                miliseconds=0;
                                                seconds=0;
                                                minutes=0;
                                                slider1.setValue(0);
                                                value=0;

                                                if(isloop==1){
                                                    pc.rewind();
                                                    pc.Play(path);
                                                }
                                                if(isloop==0&&isplaylist==0){
                                                    issuspend=1;
                                                    suspend();
                                                }
                                                if(isloop==0&&isplaylist==1){
                                                    selectedindex++;
                                                    list1.setSelectedIndex(selectedindex);
                                                }
                                            }
                                        }
                                        catch(InterruptedException e){
                                            System.out.println(e);
                                        }
                                }
                            }
                            catch (Exception e) {
                                System.out.println(e);
                            }
            }
        };

        pause.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                pc.Pause();
                t.suspend();
            }
        });


        play.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                    if(textField1.getText()==null) {
                        isrewind=0;
                        pc.Play(path);
                        t.resume();
                    }
                    if(pc.player.isComplete()==true){
                        isrewind=0;
                        pc.Play(path);
                        t.resume();
                    }
                    if(isrewind==1){
                        isrewind=0;
                        pc.Play(path);
                        t.resume();
                    }
                    if(pc.ispause==1){
                        pc.ispause=0;
                        t.resume();
                        pc.Resume();
                    }


            }
        });



        rewind.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                isrewind=1;
                pc.rewind();
                t.suspend();
                labmiliseconds.setText("0000");
                labseconds.setText("00");
                labminutes.setText("00");
                miliseconds=0;
                seconds=0;
                minutes=0;
                slider1.setValue(0);
                value=0;
            }
        });

        Browse.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                isplaylist=0;
                JFileChooser chooser = new JFileChooser("C:\\Users");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3 files", "mp3", "mpeg3");
                chooser.setFileFilter(filter);
                chooser.setDialogTitle("choose music");
                int returnval = chooser.showOpenDialog(null); //zwraca integer jak plik zostanie wybrany

                if(returnval==JFileChooser.APPROVE_OPTION){
                    File file = chooser.getSelectedFile();//pobranie pliku z choosera do file
                    songname=file.getName();
                    textField1.setText(songname);
                    path = file.getPath();

                    pc.rewind();
                    pc.Play(path);
                    labmiliseconds.setText("0000");
                    labseconds.setText("00");
                    labminutes.setText("00");
                    miliseconds=0;
                    seconds=0;
                    minutes=0;

                    dur=pc.totalsonglength;
                    slider1.setValue(0);
                    value=0;
                    slider1.setMaximum(dur);

                    if(issuspend==0){
                        t.start();
                    }
                    if(issuspend==1){
                        t.resume();
                        issuspend=0;
                    }
                }
            }
        });

        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listevent) {
                if (listevent.getValueIsAdjusting() == false) {
                    isplaylist=1;
                    String selection = list1.getSelectedValue().toString();
                    sqlmethods sqlm = new sqlmethods();
                    songname=selection;
                    selectedindex=list1.getSelectedIndex();
                    try {
                        textField1.setText(songname);
                        path = sqlm.sqlPath(songname);

                        pc.rewind();
                        pc.Play(path);
                        labmiliseconds.setText("0000");
                        labseconds.setText("00");
                        labminutes.setText("00");
                        miliseconds=0;
                        seconds=0;
                        minutes=0;
                        dur=pc.totalsonglength;
                        slider1.setValue(0);
                        value=0;
                        slider1.setMaximum(dur);
                        t.start();

                    }catch(Exception exce){}
                }
            }
        });


        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                value=source.getValue();
                if(seconds>60){
                    seconds2=seconds%60;
                }
                if(seconds<=60){
                    seconds2=seconds;
                }
                if(miliseconds>1000){
                    miliseconds2=miliseconds%1000;
                }
                if(miliseconds<=1000){
                    miliseconds2=miliseconds;
                }
            }
        });


        slider1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                pc.pauselocation=dur-value;
                if(pc.ispause==0) {
                    pc.player.close();
                    pc.Resume();
                }
            }
        });


        Add.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                JFileChooser chooser2 = new JFileChooser("C:\\Users\\Altair\\Desktop\\muzyka");
                FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3 files", "mp3", "mpeg3");
                chooser2.setFileFilter(filter);
                chooser2.setDialogTitle("choose music");
                int returnval = chooser2.showOpenDialog(null); //zwraca integer jak plik zostanie wybrany

                if(returnval==JFileChooser.APPROVE_OPTION) {
                    File file2 = chooser2.getSelectedFile();//pobranie pliku z choosera do file
                    songname = file2.getName();
                    path = file2.getPath();
                    String path2 = path.replace("\\","+");

                    GetAndSet gas = new GetAndSet(songname,path2);
                    sqlmethods sm = new sqlmethods();
                    try {
                        sm.insert(gas);
                    }catch(Exception ex){}

                    dlm.addElement(songname);
                }
            }
        });


        loop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    if(isloop==0){
                        isloop=1;
                        loop.setBackground(Color.LIGHT_GRAY);
                        loop.setText("loop: ON");
                    }
                    else if(isloop==1){
                        isloop=0;
                        loop.setBackground(Color.DARK_GRAY);
                        loop.setText("loop: OFF");
                    }
            }
        });


        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedindex++;
                list1.setSelectedIndex(selectedindex);
            }
        });


        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedindex--;
                list1.setSelectedIndex(selectedindex);
            }
        });


        volumeslider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();

               Mixer.Info[] mixers = AudioSystem.getMixerInfo();//get mixer information from audio system

               for(Mixer.Info mixerInfo: mixers){//list all mixers
                   Mixer mixer = AudioSystem.getMixer(mixerInfo);//get mixer
                   Line.Info[] lineInfos = mixer.getTargetLineInfo(); //get target line

                   for(Line.Info lineInfo: lineInfos){//list of lines
                       Line line=null;
                       boolean opened =true;

                       try{
                           line=mixer.getLine(lineInfo);
                           opened = line.isOpen()|| line instanceof Clip;//instanceof return true if object belongs to class

                           if(!opened){
                               line.open();
                           }

                           FloatControl volControl = (FloatControl) line.getControl(FloatControl.Type.VOLUME);
                           float currentVolume = volControl.getValue();
                           float change=((float)source.getValue())/100;
                           volControl.setValue(change);

                       }catch(Exception exception){}
                   }
               }
            }
        });
    }
}




