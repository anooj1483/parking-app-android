package com.collaborativeClouds.PA.screenMappers;

import com.collaborativeClouds.PA.localWorkers.JSONUtils;
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
				Log.e("CLICK", "CLICK");
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

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent mHome = new Intent(this, HomeActivity.class);
		mHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(mHome);
	}

	public String getParkingStatus() {

		HttpRequestWorker mWorker = new HttpRequestWorker();
		String status = mWorker.GetRequest(ServerConnector.PARKING_STATUS);
		Log.e("CONTENT SLOT", status);
		/*
		 * JsonParser mParse=new JsonParser(); JsonElement
		 * mElement=mParse.parse(status); for(int
		 * i=0;i<mElement.getAsJsonArray().size();i++){ String
		 * slot=mElement.getAsJsonArray().get(i).toString().replace("\"","");
		 * Log.e("CONTENT SLOT",slot); }
		 */
		return status;
	}

	class AttemptLogin extends AsyncTask<String, String, String> {

		String status;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Toast.makeText(getBaseContext(), "REACHED", Toast.LENGTH_LONG);

			progress = new ProgressDialog(Dashboard.this);
			progress.setTitle("Loading");
			progress.setMessage("Wait while loading...");
			progress.setIndeterminate(false);
			progress.setCancelable(true);
			progress.show();
		}

		@Override
		protected String doInBackground(String... args) {

			try {
				status = getParkingStatus();
				return status;
			} catch (Exception e) {
				return null;
			}

		}

		protected void onPostExecute(String file_url) {
			progress.dismiss();
			if (status != null) {
				JSONUtils mUtils = new JSONUtils();
				if (mUtils.isJSONValid(status)) {
					Intent mParkIntent = new Intent(Dashboard.this,
							ParkingSlot.class);
					mParkIntent.putExtra("parkingstatus", status);
					startActivity(mParkIntent);
				}
			}
		}

	}

}
