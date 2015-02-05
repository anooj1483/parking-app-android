package com.collaborativeClouds.PA.screenMappers;

import java.lang.reflect.Array;
import java.util.Hashtable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.os.*;
import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import org.json.*;

import com.collaborativeClouds.PA.configs.Config;
import com.collaborativeClouds.PA.localWorkers.ServerConnector;
import com.collaborativeClouds.PA.serverWorkers.HttpRequestWorker;
import com.collaborativeClouds.parkingapp.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Created by anoojkrishnang on 14/12/14.
 */
public class ParkingSlot extends Activity {

	private ImageView slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8,
			slot9, slot10, slot11, slot12, slot13, slot14, slot15, slot16,
			slot17, slot18;
	private static ImageView firstView;
	private static Drawable firstImageID;
	private static Hashtable hashTable;
	private static Hashtable bookedSlots;
	private Button mConfirmSlot;
	private static String selectedSlot = null;
	public ProgressDialog progress;
	public static boolean isConfirmPressed = false;
	public static boolean isAlreadyBooked = false;
	//public static String CODE	=	null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parking_slot);
		selectedSlot = null;
		isConfirmPressed = false;

		mConfirmSlot = (Button) findViewById(R.id.confirm);
		slot1 = (ImageView) findViewById(R.id.car1);
		slot2 = (ImageView) findViewById(R.id.car2);
		slot3 = (ImageView) findViewById(R.id.car3);
		slot4 = (ImageView) findViewById(R.id.car4);
		slot5 = (ImageView) findViewById(R.id.car5);
		slot6 = (ImageView) findViewById(R.id.car6);
		slot7 = (ImageView) findViewById(R.id.car7);
		slot8 = (ImageView) findViewById(R.id.car8);
		// slot9 = (ImageView) findViewById(R.id.car9);
		slot10 = (ImageView) findViewById(R.id.car10);
		slot11 = (ImageView) findViewById(R.id.car11);
		slot12 = (ImageView) findViewById(R.id.car12);
		slot13 = (ImageView) findViewById(R.id.car13);
		slot14 = (ImageView) findViewById(R.id.car14);
		slot15 = (ImageView) findViewById(R.id.car15);
		slot16 = (ImageView) findViewById(R.id.car16);
		slot17 = (ImageView) findViewById(R.id.car17);
		// slot18 = (ImageView) findViewById(R.id.car18);

		hashTable = new Hashtable();
		hashTable.put(R.id.car1, "slot1");
		hashTable.put(R.id.car2, "slot2");
		hashTable.put(R.id.car3, "slot3");
		hashTable.put(R.id.car4, "slot4");
		hashTable.put(R.id.car5, "slot5");
		hashTable.put(R.id.car6, "slot6");
		hashTable.put(R.id.car7, "slot7");
		hashTable.put(R.id.car8, "slot8");
		// hashTable.put(R.id.car9, "slot1");
		hashTable.put(R.id.car10, "slot10");
		hashTable.put(R.id.car11, "slot11");
		hashTable.put(R.id.car12, "slot12");
		hashTable.put(R.id.car13, "slot13");
		hashTable.put(R.id.car14, "slot14");
		hashTable.put(R.id.car15, "slot15");
		hashTable.put(R.id.car16, "slot16");
		hashTable.put(R.id.car17, "slot17");
		// hashTable.put(R.id.car18, "slot1");

		Bundle mBundle = getIntent().getExtras();

		String parkingstatus = mBundle.getString("parkingstatus");
		Log.e("PRKING STATUS",""+parkingstatus);
		JsonParser mParser = new JsonParser();
		JsonElement mElement = mParser.parse(parkingstatus);
		bookedSlots = new Hashtable();
		for (int i = 0; i < mElement.getAsJsonArray().size(); i++) {
			String slot = mElement.getAsJsonArray().get(i).toString()
					.replace("\"", "").trim();
			SlotBuilder(slot, R.drawable.car_red_top);
		}
		new BookSlot().execute();
		slot1.setOnClickListener(ImageViewHandler);
		slot2.setOnClickListener(ImageViewHandler);
		slot3.setOnClickListener(ImageViewHandler);
		slot4.setOnClickListener(ImageViewHandler);
		slot5.setOnClickListener(ImageViewHandler);
		slot6.setOnClickListener(ImageViewHandler);
		slot7.setOnClickListener(ImageViewHandler);
		slot8.setOnClickListener(ImageViewHandler);
		// slot9.setOnClickListener(ImageViewHandler);
		slot10.setOnClickListener(ImageViewHandler);
		slot11.setOnClickListener(ImageViewHandler);
		slot12.setOnClickListener(ImageViewHandler);
		slot13.setOnClickListener(ImageViewHandler);
		slot14.setOnClickListener(ImageViewHandler);
		slot15.setOnClickListener(ImageViewHandler);
		slot16.setOnClickListener(ImageViewHandler);
		slot17.setOnClickListener(ImageViewHandler);
		// slot18.setOnClickListener(ImageViewHandler);

		mConfirmSlot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mConfirmSlot.setEnabled(false);
				if (selectedSlot != null) {
					Log.e("SELECTED SLOT", selectedSlot);
					isConfirmPressed = true;
					new BookSlot().execute();
				}else{
					mConfirmSlot.setEnabled(true);
				}
			}
		});

	}

	View.OnClickListener ImageViewHandler = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!isAlreadyBooked) {
				mConfirmSlot.setEnabled(true);
				if (!hashTable
						.get(v.getId())
						.toString()
						.equals(bookedSlots.get(hashTable.get(v.getId())
								.toString()))) {
					if (firstView != null) {
						firstView.setImageDrawable(firstImageID);
					}
					firstView = (ImageView) findViewById(v.getId());
					firstImageID = firstView.getDrawable();
					firstView.setImageDrawable(getResources().getDrawable(
							R.drawable.car_black_top));

					selectedSlot = hashTable.get(v.getId()).toString();
				}
			}else{
				//Toast.makeText(getApplicationContext(), "You are already booked to a Slot", Toast.LENGTH_LONG).show();
				mConfirmSlot.setEnabled(false);
				showAlert("Message", "You are already booked to a Slot.\nPlease remove it with your Code: "+Config.CODE);
			}
		}
	};
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

	public void SlotBuilder(String slot, int drawable) {

		bookedSlots.put(slot, slot);
		switch (slot) {
		case "slot1":
			slot1.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot2":
			slot2.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot3":
			slot3.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot4":
			slot4.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot5":
			slot5.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot6":
			slot6.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot7":
			slot7.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot8":
			slot8.setImageDrawable(getResources().getDrawable(drawable));
			break;
		// case "slot9":
		// slot9.setImageDrawable(getResources().getDrawable(R.drawable.car_red_top));
		// break;
		case "slot10":
			slot10.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot11":
			slot11.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot12":
			slot12.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot13":
			slot13.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot14":
			slot14.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot15":
			slot15.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot16":
			slot16.setImageDrawable(getResources().getDrawable(drawable));
			break;
		case "slot17":
			slot17.setImageDrawable(getResources().getDrawable(drawable));
			break;
		// case "slot18":
		// slot18.setImageDrawable(getResources().getDrawable(R.drawable.car_red_top));
		// break;

		}

	}

	class BookSlot extends AsyncTask<String, String, String> {

		String status;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// Toast.makeText(getBaseContext(), "REACHED", Toast.LENGTH_LONG);

			progress = new ProgressDialog(ParkingSlot.this);
			progress.setTitle("Loading");
			progress.setMessage("Wait while loading...");
			progress.setIndeterminate(false);
			progress.setCancelable(true);
			progress.show();
		}

		@Override
		protected String doInBackground(String... args) {

			try {
				if (isConfirmPressed) {
					HttpRequestWorker mWorker = new HttpRequestWorker();
					JSONObject mObject = new JSONObject();
					mObject.put("slotnumber", selectedSlot);
					status = mWorker.PostRequest(
							ServerConnector.BOOK_SLOT,
							mObject.toString(),true);
					return status;
				} else {
					HttpRequestWorker mWorker = new HttpRequestWorker();
					JSONObject mObject = new JSONObject();
					mObject.put("username", Config.USERNAME);
					status = mWorker.PostRequest(
							ServerConnector.GET_SLOT,
							mObject.toString(),true);

					return status;

				}
			} catch (Exception e) {
				mConfirmSlot.setEnabled(true);
				return null;
			}

			//return status;

		}

		protected void onPostExecute(String file_url) {
			//Log
			progress.dismiss();
			if(isConfirmPressed){
				if (!status.equals("Failed")) {
					Config.CODE=status;
					isAlreadyBooked=true;
					mConfirmSlot.setEnabled(false);
					
				} else {
					showAlert("Alert", "Failed to Book the Slot");
				}
			}else{
				try {

					JsonParser mParser = new JsonParser();
					JsonElement mElements = mParser
							.parse(status);
					JsonArray mArray = mElements
							.getAsJsonArray();
					JsonElement mElement = mArray.get(0);

					JSONObject mOb = new JSONObject(
							mElement.toString());
					final String slotno = mOb
							.getString("slotno");
					Config.CODE	=	mOb.getString("code");

					Log.e("PARSER ELEMENT",
							mOb.get("slotno") + "");
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method
							// stub
							isAlreadyBooked = true;
							mConfirmSlot.setEnabled(false);
							SlotBuilder(
									slotno,
									R.drawable.car_black_top);
						}
					});

				} catch (Exception e) {
					isAlreadyBooked = false;
					Log.e("PARSER ERROR", e + "");
					mConfirmSlot.setEnabled(true);
				}
			}
		}

	}

}
