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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;



public class HomeActivity extends Activity {

	public ProgressDialog progress;
	private Button mLogin;
	private EditText mUserName,mPassword;
	public static String username = null;
	public static String password = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mLogin		=	(Button) findViewById(R.id.login);
        mUserName	=	(EditText) findViewById(R.id.username);
        mPassword	=	(EditText) findViewById(R.id.password);
        
        mLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				
				username	=	mUserName.getText().toString();
				password	=	mPassword.getText().toString();
				
				if(!username.equals("") && !password.equals("")){
					new AttemptLogin().execute();
				}
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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


            progress = new ProgressDialog(HomeActivity.this);
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
                        	mObject.put("username", username);
                        	mObject.put("password",password);
                        	
                        	HttpRequestWorker mWorker = new HttpRequestWorker();
                        	String response = mWorker.PostRequest(ServerConnector.LOGIN, mObject.toString(), false);
                            
                        	if(response.equals("Success")){
                        		Config.USERNAME	=	username;
                        		Intent mParkIntent = new Intent(HomeActivity.this, Dashboard.class);
                                startActivity(mParkIntent);
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
