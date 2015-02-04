package com.collaborativeClouds.PA.screenMappers;

import org.json.JSONObject;

import com.collaborativeClouds.PA.configs.Config;
import com.collaborativeClouds.PA.localWorkers.ServerConnector;
import com.collaborativeClouds.PA.serverWorkers.HttpRequestWorker;
import com.collaborativeClouds.parkingapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by anoojkrishnang on 14/12/14.
 */
public class RemoveSlot extends Activity {
	private Button mSubmit;
	private Button mBack;
	private EditText code;
	public ProgressDialog progress;
	public static boolean isDeletePressed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_slot);
		mSubmit = (Button) findViewById(R.id.submit);
		mBack = (Button) findViewById(R.id.back);
		code = (EditText) findViewById(R.id.code);
		code.setText(Config.CODE);
		isDeletePressed = false;
		new AttemptLogin().execute();
		final Intent mBackIntent = new Intent(this, Dashboard.class);

		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(mBackIntent);
			}
		});

		mSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				isDeletePressed = true;
				new AttemptLogin().execute();
			}
		});

	}

	private void showAlert(String title, String message) {
		new AlertDialog.Builder(this)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(android.R.string.yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// DO ANY CAFEBESIDE OPERATION
							}
						})

				.setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	class AttemptLogin extends AsyncTask<String, String, String> {

		String status;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Toast.makeText(getBaseContext(), "REACHED", Toast.LENGTH_LONG);

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
							if (!isDeletePressed) {
								try {
									HttpRequestWorker mWorker = new HttpRequestWorker();
									JSONObject mObject = new JSONObject();
									mObject.put("username", Config.USERNAME);
									status = mWorker.PostRequest(
											ServerConnector.GET_SLOT,
											mObject.toString(), true);

									JsonParser mParser = new JsonParser();
									JsonElement mElements = mParser
											.parse(status);
									JsonArray mArray = mElements
											.getAsJsonArray();
									JsonElement mElement = mArray.get(0);

									JSONObject mOb = new JSONObject(mElement
											.toString());
									final String slotno = mOb
											.getString("slotno");
									Config.CODE = mOb.getString("code");

									Log.e("PARSER ELEMENT", mOb.get("slotno")
											+ "");
									runOnUiThread(new Runnable() {

										@Override
										public void run() {
											// TODO Auto-generated method
											// stub
											code.setText(Config.CODE);
										}
									});

								} catch (Exception e) {

									Log.e("PARSER ERROR", e + "");
								}
							} else {
								try {
									JSONObject mObject = new JSONObject();
									mObject.put("username", Config.USERNAME);
									mObject.put("code", Config.CODE);

									HttpRequestWorker mWorker = new HttpRequestWorker();
									String response = mWorker.PostRequest(
											ServerConnector.REMOVE_SLOT,
											mObject.toString(), true);

									if (response.equals("Success")) {
										showAlert("Message", "Succesfully Removed !!");
										//code.setText("");
									} else {
										showAlert("Alert",
												"Invalid UserName or Password");
									}

								} catch (Exception e) {

								}
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
