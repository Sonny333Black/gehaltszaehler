package com.example.sonny.gehaltszaehler;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Sonny on 12.03.2017.
 */

public class Controller extends StateMachine {
    private static final String TAG = "sonnyDebug";
    private static final String SHARED_PREFERENCES_TAG = "MyMoney";

    SharedPreferences settings;
    SharedPreferences.Editor prefEditor;

    private MainActivity mainActivity;
    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    private OnControllerInteractionListener mListener;

    private boolean zeit = true;
    private CountDownTimer timer;

    private long totalSeconds = 1000000;
    private long intervalSeconds = 1;
    private float moneyPerSecond;
    private float money;
    private int bisherigeArbeitsZeit;
    private boolean timeRun;

    // Eine (versteckte) Klassenvariable vom Typ der eigenen Klasse
    private static Controller instance;
    // Verhindere die Erzeugung des Objektes über andere Methoden
    private Controller () {}
    // Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein konkretes
    // Objekt erzeugt und dieses zurückliefert.
    public static Controller getInstance () {
        if (Controller.instance == null) {
            Controller.instance = new Controller ();
        }
        return Controller.instance;
    }


    private enum State {
        IDEL, START, PAUSE
    }

    public enum SmMessage {
         GO, PAUSE, ENDE, RESTART
    }

    private State state = State.IDEL;

    public static SmMessage[] messageIndex = SmMessage.values();



    public void init(Fragment frag) {
        Log.d(TAG, "init()");

        // init InterfaceListener
        try {
            mListener = (OnControllerInteractionListener) frag;
        } catch (ClassCastException e) {
            throw new ClassCastException(frag.toString()
                    + " must implement OnFragmentInteractionListener!");
        }

    }
    public void fragementChanged(Fragment frag) {
        // init InterfaceListener
        try {
            mListener = (OnControllerInteractionListener) frag;
        } catch (ClassCastException e) {
            throw new ClassCastException(frag.toString()
                    + " must implement OnFragmentInteractionListener!");
        }
    }

    @Override
    void theBrain(android.os.Message message) {
        SmMessage inputSmMessage = messageIndex[message.what];

        Log.v(TAG, "in THE BRAIN *************");

        Log.i(TAG, "SM: state: " + state + ", input message: " +
                inputSmMessage.toString() + ", arg1: " +
                message.arg1 + ", arg2: " + message.arg2);
        if (message.obj != null) {
            Log.i(TAG, "SM: data: " + message.obj.toString());
        }
        switch (state) {
            case IDEL:
                switch (inputSmMessage) {
                    case GO:
                        Log.v(TAG, "in Start *****************");
                        mainActivity.showWorkFrag();
                        initTimer();
                        state = State.START;
                        break;
                    case RESTART:
                        
                        break;
                }

                break;
            case START:
                switch (inputSmMessage) {
                    case PAUSE:
                        Log.v(TAG, "wenn er anhält");

                        timeRun=false;
                        timer.cancel();

                        prefEditor.putFloat("moneyTotal", money);
                        prefEditor.putInt("time", bisherigeArbeitsZeit);
                        prefEditor.commit();

                        mListener.onControllerDisplay("Weiter");
                        state = State.PAUSE;
                        break;
                    case ENDE:
                        doEnde();
                    case RESTART:
                        doEnde();
                }
                break;
            case PAUSE:
                switch (inputSmMessage) {
                    case PAUSE:
                        mListener.onControllerDisplay(mainActivity.getString(R.string.stop_button));

                        timeRun=true;
                        zeit= true;
                        startTime();

                        state = State.START;
                        break;
                    case ENDE:
                        doEnde();
                        break;
                    case RESTART:
                        doEnde();
                }
                break;
            default:
                Log.d(TAG, "in Klasse StartFragement Switch Case hat nicht geklappt");
                break;
        }

    }

    private void initTimer() {
        settings = mainActivity.getSharedPreferences(SHARED_PREFERENCES_TAG, MODE_PRIVATE);
        prefEditor = settings.edit();

        float moneyPerHouer = 0;
        moneyPerHouer = settings.getFloat("moneyPerHouer",moneyPerHouer);

        moneyPerSecond = moneyPerHouer/60/60;

        prefEditor.putFloat("moneyTotal", 0);
        prefEditor.putInt("time", 0);

        timeRun=true;
        startTime();

    }

    private void startTime(){
        if(zeit){

            bisherigeArbeitsZeit = 0;
            bisherigeArbeitsZeit = settings.getInt("time",bisherigeArbeitsZeit);

            timer = new CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    bisherigeArbeitsZeit = (int) ((totalSeconds * 1000 - millisUntilFinished) / 1000)+bisherigeArbeitsZeit;
                    money = bisherigeArbeitsZeit*moneyPerSecond;
                    String ausgabe = mainActivity.getString(R.string.money_result) + " \n" + money + mainActivity.getString(R.string.euro);

                    mListener.onCountDisplay(ausgabe);
                }

                @Override
                public void onFinish() {
                }
            }.start();
            zeit = false;
        }else{
            // Den Timer stoppen
            timer.cancel();
            zeit = true;
        }
    }


    private void doEnde(){
        if(timeRun){
            timeRun=false;
            zeit= false;
            timer.cancel();
        }

        final String SHARED_PREFERENCES_TAG = "MyMoney";
        SharedPreferences settings = mainActivity.getSharedPreferences(SHARED_PREFERENCES_TAG, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = settings.edit();

        prefEditor.putFloat("moneyTotal", money);
        prefEditor.putInt("time", 0);
        prefEditor.commit();

        mainActivity.showStartCount();
        mListener.onControllerDisplay(mainActivity.getString(R.string.current_result_01) + Float.toString(money) + mainActivity.getString(R.string.euro) + " " + mainActivity.getString(R.string.current_result_02));
        state = State.IDEL;
        zeit=true;
    }

    public interface OnControllerInteractionListener {
        public void onControllerDisplay(String str);
        public void onCountDisplay(String str);

    }
}