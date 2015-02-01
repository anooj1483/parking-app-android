package com.collaborativeClouds.PA.screenMappers;

import com.collaborativeClouds.PA.localWorkers.ServerConnector;
import com.collaborativeClouds.PA.serverWorkers.HttpRequestWorker;
import com.collaborativeClouds.parkingapp.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class Dashboard extends Activity {

    public Button mParkSlot;
    private Button mRemoveSlot;
    public ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mParkSlot = (Button) findViewById(R.id.parkslot);
        mRemoveSlot = (Button) findViewById(R.id.removeslot);


        final Intent mRemoveIntent = new Intent(this, RemoveSlot.class);

        mParkSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CLICK","CLICK");
                new AttemptLogin().execute();
            }
        });

        mRemoveSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mRemoveIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }


    public String getParkingStatus() {

        HttpRequestWorker mWorker = new HttpRequestWorker();
        String status = mWorker.GetRequest(ServerConnector.parkingstatus);
        Log.e("CONTENT SLOT", status);
        /*JsonParser mParse=new JsonParser();
        JsonElement mElement=mParse.parse(status);
        for(int i=0;i<mElement.getAsJsonArray().size();i++){
            String slot=mElement.getAsJsonArray().get(i).toString().replace("\"","");
            Log.e("CONTENT SLOT",slot);
        }
        */
        return status;
    }


    class AttemptLogin extends AsyncTask<String, String, String> {


        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(getBaseContext(), "REACHED", Toast.LENGTH_LONG);


            progress = new ProgressDialog(Dashboard.this);
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setIndeterminate(false);
            progress.setCancelable(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... args) {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    Thread mThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            status = getParkingStatus();

                            Intent mParkIntent = new Intent(Dashboard.this, ParkingSlot.class);
                            mParkIntent.putExtra("parkingstatus",status);
                            startActivity(mParkIntent);

                        }
                    });
                    mThread.start();
                }

            });

            return status;

        }

        protected void onPostExecute(String file_url) {
            progress.dismiss();
        }

    }


}





