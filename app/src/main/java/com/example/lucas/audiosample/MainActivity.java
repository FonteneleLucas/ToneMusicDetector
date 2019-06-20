package com.example.lucas.audiosample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import com.example.lucas.audiosample.audio.calculators.AudioCalculator;
import com.example.lucas.audiosample.audio.core.Callback;
import com.example.lucas.audiosample.audio.core.Recorder;

import static java.lang.Thread.*;

public class MainActivity extends Activity {

    private Recorder recorder;
    private AudioCalculator audioCalculator;
    private Handler handler;

    private TextView textAmplitude;
    private TextView textDecibel;
    private TextView textOutput;
    private TextView notas_Encontradas;
    private TextView textFrequency;
    private TextView cont;



    float[] Ci =    {-57,-45,-33,-21,-9,3,15,27,39};
    float[] Csusi = {-56,-44,-32,-20,-8,4,16,28,40};
    float[] Di =    {-55,-43,-31,-19,-7,5,17,29,41};
    float[] Dsusi = {-54,-42,-30,-18,-6,6,18,30,42};
    float[] Ei =    {-53,-41,-29,-17,-5,7,19,31,43};
    float[] Fi =    {-52,-40,-28,-16,-4,8,20,32,44};
    float[] Fsusi = {-51,-39,-27,-15,-3,9,21,33,45};
    float[] Gi =    {-50,-38,-26,-14,-2,10,22,34,46};
    float[] Gsusi = {-49,-37,-25,-13,-1,11,23,35,47};
    float[] Ai =    {-48,-36,-24,-12,0,12,24,36,48};
    float[] Asusi = {-47,-35,-23,-11,1,13,25,37,49};
    float[] Bi =    {-46,-34,-22,-10,2,14,26,38,50};

    double[] tones = {261.63, 277.18, 293.66, 311.13, 329.63, 349.23, 369.99, 392, 415.3, 440, 466.16, 493.88, 523.25, 554.37, 587.33, 622.25,
                      659.25, 698.46, 739.99, 783.99, 830.61, 880, 932.33, 987.77, 1046.5};

//    int[] notasEncotradas = {0,0,0,0,0,0,0,0,0,0,0};

    public List<Double> input = new ArrayList<>();
    public List<String> discover = new ArrayList<>();
    public List<Integer> notasEncontradas = new ArrayList<>();


    //Notes intervals values
    public List<Float> C = new ArrayList<>();
    public List<Float> Csus = new ArrayList<>();
    public List<Float> D = new ArrayList<>();
    public List<Float> Dsus = new ArrayList<>();
    public List<Float> E = new ArrayList<>();
    public List<Float> F = new ArrayList<>();
    public List<Float> Fsus = new ArrayList<>();
    public List<Float> G = new ArrayList<>();
    public List<Float> Gsus = new ArrayList<>();
    public List<Float> A = new ArrayList<>();
    public List<Float> Asus = new ArrayList<>();
    public List<Float> B = new ArrayList<>();




    boolean iniciar = false;
    boolean parar = false;
    double lastValue = 0;

    double result = 0;
    int contador = 0;


    public void iniciar(View view){
        iniciar = true;
        parar = false;
    }

    public void parar(View view){
        iniciar = false;
        parar = true;
    }

    public double nCalc(double freq){
        double result = ((Math.log(freq/440)) / Math.log(1.059463));
        return result;
    }

    public BigDecimal nExact(double freq){
        BigDecimal exact = new BigDecimal(Math.round(nCalc(freq)));
        return exact;
    }

    public  boolean isNote(double freq){
        BigDecimal normal = new BigDecimal(nCalc(freq));


        if(Math.abs(normal.subtract(nExact(freq)).floatValue()) >= 0.1){
            return false;
        }else{
            return true;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        recorder = new Recorder(callback);
        audioCalculator = new AudioCalculator();
        handler = new Handler(Looper.getMainLooper());

        textAmplitude = findViewById(R.id.textAmplitude);
        textDecibel = (TextView) findViewById(R.id.textDecibel);
        textFrequency = (TextView) findViewById(R.id.textFrequency);
        textOutput = findViewById(R.id.outputText);
        notas_Encontradas = findViewById(R.id.notes);
        cont = findViewById(R.id.cont);

        for (int i = 0; i < Ai.length; i++){
            C.add(Ci[i]);
            Csus.add(Csusi[i]);
            D.add(Di[i]);
            Dsus.add(Dsusi[i]);
            E.add(Ei[i]);
            F.add(Fi[i]);
            Fsus.add(Fsusi[i]);
            G.add(Gi[i]);
            Gsus.add(Gsusi[i]);
            A.add(Ai[i]);
            Asus.add(Asusi[i]);
            B.add(Bi[i]);
        }

        for(int i = 0; i < 12; i++){
            notasEncontradas.add(0);
        }










    }

    private Callback callback = new Callback() {

        @Override
        public void onBufferAvailable(byte[] buffer) {
            audioCalculator.setBytes(buffer);
            int amplitude = audioCalculator.getAmplitude();
            double decibel = audioCalculator.getDecibel();
            final double frequencyRaw = audioCalculator.getFrequency();
            final double frequency = Math.round(frequencyRaw);

            final String amp = String.valueOf(amplitude + " Amp");
            final String db = String.valueOf(decibel + " db");
            final String hz = String.valueOf(frequency + " Hz");




            handler.post(new Runnable() {
                @Override
                public void run() {


                    textAmplitude.setText(String.valueOf(isNote(frequency)));
                    textFrequency.setText(hz);
//                    textDecibel.setText("void");


                    if(iniciar && frequency > 300 && isNote(frequency)){
                        if(C.contains(nExact(frequency).floatValue())){
                            textDecibel.setText("C");
                            if(notasEncontradas.get(0) == 0){
                                notasEncontradas.set(0,1);
                            }
                        }
//
                        if(Csus.contains(nExact(frequency).floatValue())){
                            textDecibel.setText("C#");
                            if(notasEncontradas.get(1) == 0){
                                notasEncontradas.set(1,1);
                            }
                        }

                        if(D.contains(nExact(frequency).floatValue())){
                            textDecibel.setText("D");
                            if(notasEncontradas.get(2) == 0){
                                notasEncontradas.set(2,1);
                            }
                        }

                        if(Dsus.contains(nExact(frequency).floatValue())){
                            textDecibel.setText("D#");
                            if(notasEncontradas.get(3) == 0){
                                notasEncontradas.set(3,1);
                            }
                        }

                        if(E.contains(nExact(frequency).floatValue())){
                            textDecibel.setText("E");
                            if(notasEncontradas.get(4) == 0){
                                notasEncontradas.set(4,1);
                            }
                        }

                        if(F.contains(nExact(frequency).floatValue())){
                            textDecibel.setText("F");
                            if(notasEncontradas.get(5) == 0){
                                notasEncontradas.set(5,1);
                            }
                        }

                        if(Fsus.contains(nExact(frequency).floatValue())){
                            textDecibel.setText("F#");
                            if(notasEncontradas.get(6) == 0){
                                notasEncontradas.set(6,1);
                            }
                        }

                        if(G.contains(nExact(frequency).floatValue())){
                            textDecibel.setText("G");
                            if(notasEncontradas.get(7) == 0){
                                notasEncontradas.set(7,1);
                            }
                        }

                        if(Gsus.contains(nExact(frequency).floatValue())){
                            textDecibel.setText("G#");
                            if(notasEncontradas.get(8) == 0){
                                notasEncontradas.set(8,1);
                            }
                        }

                        if(A.contains(nExact(frequency).floatValue())){
                            textDecibel.setText("A");
                            if(notasEncontradas.get(9) == 0){
                                notasEncontradas.set(9,1);
                            }
                        }

                        if(Asus.contains(nExact(frequency).floatValue())){
                            textDecibel.setText("A#");
                            if(notasEncontradas.get(10) == 0){
                                notasEncontradas.set(10,1);
                            }
                        }

                        if(B.contains(nExact(frequency).floatValue())){
                            textDecibel.setText("B");
                            if(notasEncontradas.get(11) == 0){
                                notasEncontradas.set(11,1);
                            }
                        }
//
//
//                        if(!discover.contains(frequency)){
//                            discover.add(frequency);
//                            lastValue = frequency;
//                        }
                    }



//                    if(iniciar && lastValue != frequency){
//                        for(int i = 0; i < input.size(); i++){
////                            if(frequency >= input.get(i) - ((input.get(i)*1.02) - 1.05) && frequency <= input.get(i)*1.02 && ){
//                            if(frequency > 260 && isNote(frequency) && !discover.contains(frequency)){
//                                discover.add(frequency);
//                                lastValue = frequency;
//                            }
//                        }
//                    }

                    if(parar == true){
                        textOutput.setText(discover.toString());
                        notas_Encontradas.setText(notasEncontradas.toString());


                        cont.setText(String.valueOf(discover.size()));

                    }

                }
            });
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        recorder.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        recorder.stop();
    }
}
