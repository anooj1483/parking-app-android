package com.collaborativeClouds.PA.screenMappers;

import org.json.JSONObject;

import com.collaborativeClouds.PA.configs.Config;
import com.collaborativeClouds.PA.localWorkers.ServerConnector;
import com.collaborativeClouds.PA.serverWorkers.HttpRequestWorker;
import com.collaborativeClouds.parkingapp.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by anoojkrishnang on 14/12/14.
 */
public class RemoveSlot extends Activity{
    private Button mSubmit;
    private Button mBack;
    private EditText code;
    public ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_slot);
        mSubmit=(Button)findViewById(R.id.submit);
        mBack=(Button)findViewById(R.id.back);
        code = (EditText)findViewById(R.id.code);
        
        code.setText(Config.CODE);
        final Intent mBackIntent=new Intent(this,Dashboard.class);


        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mBackIntent);
            }
        });

    }
    private void showAlert(String title,String message){
		new AlertDialog.Builder(this)
	    .setTitle(title)
	    .setMessage(message)
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            //DO ANY CAFEBESIDE OPERATION
	        }
	     })
	    
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
	}
    class AttemptLogin extends AsyncTask<String, String, String> {


        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //Toast.makeText(getBaseContext(), "REACHED", Toast.LENGTH_LONG);


            progress = new ProgressDialog(RemoveSlot.this);
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
                            try{
                        	JSONObject mObject = new JSONObject();
                        	mObject.put("username", Config.USERNAME);
                        	mObject.put("code",Config.CODE);
                        	
                        	HttpRequestWorker mWorker = new HttpRequestWorker();
                        	String response = mWorker.PostRequest(ServerConnector.LOGIN, mObject.toString(), false);
                            
                        	if(response.equals("Success")){
                        		
                        	}else{
                        		showAlert("Alert","Invalid UserName or Password");
                        	}
                        	
                        	
                            }catch(Exception e){
                            	
                            }

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
