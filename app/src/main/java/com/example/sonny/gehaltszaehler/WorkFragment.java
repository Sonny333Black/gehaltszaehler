/**
 *
 * Achtung: gegenueber dem Standard DetailsFragment der vorherigen Beispiele ist hier einiges
 *  erweitert worden:
 *
 *     - Logik
 *     - logging auf fllog umgestellt
 *     - Persistenz
 *
 */

package com.example.sonny.gehaltszaehler;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

//import fllog.Log;

public class WorkFragment extends Fragment implements View.OnClickListener,Controller.OnControllerInteractionListener {

    private static final String TAG = "sonnyDebug";
    private static final String SHARED_PREFERENCES_TAG = "MyMoney";

    private TextView textView;
    private Button button_stop_count;
    private Button button_cancel_count;

    SharedPreferences settings;
    SharedPreferences.Editor prefEditor;

    private Controller controller;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "in Klasse StartFragement");

        controller = Controller.getInstance();
        controller.init(this);

        View view = inflater.inflate(R.layout.fragment_work, container, false);

        textView = (TextView) view.findViewById(R.id.textView);

        button_stop_count = (Button) view.findViewById(R.id.stop_button);
        button_stop_count.setOnClickListener(this);

        button_cancel_count = (Button) view.findViewById(R.id.cancel_button);
        button_cancel_count.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stop_button:
                android.util.Log.d(TAG, "onClick(): Button stop Count");
                controller.sendSmMessage(Controller.SmMessage.PAUSE.ordinal(), 0, 0, null);


                break;
            case R.id.cancel_button:

                controller.sendSmMessage(Controller.SmMessage.ENDE.ordinal(), 0, 0, null);
                break;
        }
    }



    public void onControllerDisplay(String str){
        button_stop_count.setText(str);
    }

    @Override
    public void onCountDisplay(String str) {
        textView.setText(str);
    }
}
