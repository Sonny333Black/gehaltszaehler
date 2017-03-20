package com.example.sonny.gehaltszaehler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "sonnyDebug";

    private StartFragment startFrag;
    private WorkFragment workFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.util.Log.v(TAG, "onCreate()");

        Log.d(TAG, "onCreate(): LogFragment initialisiert");

        setContentView(R.layout.activity_main);
        showStartCount();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Log.d(TAG, "onOptionsItemSelected( " + item.toString() + " )");
        Log.d(TAG, "vor Swtich Case **************************************");
        switch (id) {
            case R.id.start_count:
                Controller controller = Controller.getInstance();
                controller.sendSmMessage(Controller.SmMessage.RESTART.ordinal(), 0, 0, null);
                return true;
            default:
                Log.d(TAG, "ERROR :)");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showStartCount() {
        removeAllFrags();
        startFrag = new StartFragment(this);
        Log.d(TAG, "start Count");
        getFragmentManager().beginTransaction()
                .add(R.id.main_fragment_container, startFrag)
                .commit();
        Log.d(TAG, "nach commit");
        getFragmentManager().executePendingTransactions();
    }
    public void showWorkFrag() {
        removeAllFrags();
        workFragment = new WorkFragment();
        Log.d(TAG, "Work Frag");
        getFragmentManager().beginTransaction()
                .add(R.id.main_fragment_container, workFragment)
                .commit();
        Log.d(TAG, "nach commit");
        getFragmentManager().executePendingTransactions();
    }

    private void removeAllFrags() {
        Log.d(TAG, "removeAllFrags():");
        if (startFrag != null)
            getFragmentManager()
                    .beginTransaction()
                    .remove(startFrag)
                    .commit();
        if (workFragment != null)
            getFragmentManager()
                    .beginTransaction()
                    .remove(workFragment)
                    .commit();
        startFrag = null;
        workFragment = null;

        getFragmentManager().executePendingTransactions();
        Log.d(TAG, "removeAllFrags Done");
    }


}
