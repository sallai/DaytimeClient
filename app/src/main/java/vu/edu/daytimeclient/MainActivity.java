package vu.edu.daytimeclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the button and textview objects
        // note that they need to be final, so that we can use them from closures
        final TextView tv = (TextView) findViewById(R.id.textView);
        final Button b = (Button) findViewById(R.id.button);

        // set the button's onClickListener
        b.setOnClickListener(new View.OnClickListener() {

            // this method gets called when the button is clicked
            public void onClick(View v) {

                // we set the textview to display "Contacting server"
                tv.setText("Contacting server");

                // we need to do network operations on a background thread, but then we need
                // to update the UI elements on the main thread, so we create an AsyncTask
                new AsyncTask<String,Void,String>() {

                    // this method will be executed on a background thread
                    @Override
                    protected String doInBackground(String... params) {

                        Socket s = null;
                        try {
                            // create a socket connection to the server
                            String serverAddress = params[0];
                            s = new Socket(serverAddress, 13);

                            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

                            // the server returns an empty line first, we read it, but we don't use the result
                            String emptyLine = in.readLine();

                            // the second line contains the current time
                            String line = in.readLine();

                            Log.i("MainActivity", "Read line from server: "+line);

                            in.close();

                            // return the line read
                            return line;
                        } catch (IOException e) {
                            // there was an error: we return the error message
                            return "Exception: " + e.getMessage();
                        } finally {

                            // make sure the socket gets closed, no matter what
                            if(s != null && !s.isClosed()) {
                                try { s.close(); } catch (IOException ignored) {}
                            }
                        }
                    }

                    // this method get called from the main thread with whatever value
                    // doInBackground returned (in this case, the line read from the server)
                    @Override
                    protected void onPostExecute(String line) {
                        super.onPostExecute(line);
                        // set the textview to the line received
                        tv.setText(line);
                    }
                }.execute("wwv.nist.gov"); // make sure to call the .execute() method of the AsyncTask
            }
        });

    }
}
