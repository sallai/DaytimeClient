package vu.edu.daytimeclient;

import android.util.Log;
import android.view.View;

/**
 * Created by sallai on 10/21/16.
 */

public class MyClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Log.i("LISTENER", "Button is clicked");
    }
}
