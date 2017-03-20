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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

//import fllog.Log;

public class StartFragment extends Fragment implements View.OnClickListener,Controller.OnControllerInteractionListener {

    private static final String TAG = "sonnyDebug";

    private static final String SHARED_PREFERENCES_TAG = "MyMoney";

    private EditText moneyPerHour;
    private TextView textInfo;
    private Button button_start_count;
    private MainActivity mainActivity;


    private Controller controller;

    public StartFragment(MainActivity mainActivity) {
        super();
        this.mainActivity = mainActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "in Klasse StartFragement");

        controller = Controller.getInstance();
        controller.init(this);  // also init for OnControllerInteractionListener
        controller.setMainActivity(mainActivity);

        View view = inflater.inflate(R.layout.fragment_zaehler, container, false);

        moneyPerHour = (EditText) view.findViewById(R.id.moneyPerHour);
        button_start_count = (Button) view.findViewById(R.id.button_start_count);
        textInfo = (TextView) view.findViewById(R.id.textInfo);
        button_start_count.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_start_count:

                SharedPreferences settings = getActivity().getSharedPreferences(SHARED_PREFERENCES_TAG, MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = settings.edit();
                String getInput = moneyPerHour.getText().toString();
                float moneyPerHouer = 0;
                if(!getInput.equals("") && Float.parseFloat(getInput) != 0.0){
                    moneyPerHouer = Float.parseFloat(getInput);
                } else {
                    Toast.makeText(mainActivity, getString(R.string.fill_money_input), Toast.LENGTH_SHORT).show();
                    return;
                }
                prefEditor.putFloat("moneyPerHouer", moneyPerHouer);
                prefEditor.commit();

                android.util.Log.d(TAG, "onClick(): Button start Count");
                controller.sendSmMessage(Controller.SmMessage.GO.ordinal(), 0, 0, null);
                break;
        }
    }

    public void onControllerDisplay(String str){
        android.util.Log.d(TAG, "onControllerDisplay(): " + str);
        textInfo.setText(str);
        textInfo.setTextSize(25);
    }

    @Override
    public void onCountDisplay(String str) {

    }
}
