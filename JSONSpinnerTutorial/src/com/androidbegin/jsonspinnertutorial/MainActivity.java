package com.androidbegin.jsonspinnertutorial;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	JSONObject jsonobject;
	JSONArray jsonarray;
	ProgressDialog mProgressDialog;
	ArrayList<String> worldlist;
	ArrayList<WorldPopulation> world;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Download JSON file AsyncTask
		new DownloadJSON().execute();

	}

	// Download JSON file AsyncTask
	private class DownloadJSON extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// Locate the WorldPopulation Class	
			world = new ArrayList<WorldPopulation>();
			// Create an array to populate the spinner 
			worldlist = new ArrayList<String>();
			// JSON file URL address
			jsonobject = JSONfunctions
					.getJSONfromURL("http://10.0.2.2:8888/jsonparsetutorial.txt");

			try {
				// Locate the NodeList name
				jsonarray = jsonobject.getJSONArray("worldpopulation");
				for (int i = 0; i < jsonarray.length(); i++) {
					jsonobject = jsonarray.getJSONObject(i);
					
					WorldPopulation worldpop = new WorldPopulation();

					worldpop.setRank(jsonobject.optString("hall_name"));
					worldpop.setCountry(jsonobject.optString("month"));
					worldpop.setPopulation(jsonobject.optString("Available_dates"));
					worldpop.setFlag(jsonobject.optString("flag"));
					world.add(worldpop);
					
					// Populate spinner with country names
					worldlist.add(jsonobject.optString("month"));

				}
			} catch (Exception e) {
				Log.e("Error", e.getMessage());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void args) {
			// Locate the spinner in activity_main.xml
			Spinner mySpinner = (Spinner) findViewById(R.id.my_spinner);
			
			// Spinner adapter
			mySpinner
					.setAdapter(new ArrayAdapter<String>(MainActivity.this,
							android.R.layout.simple_spinner_dropdown_item,
							worldlist));

			// Spinner on item click listener
			mySpinner
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							// TODO Auto-generated method stub
							// Locate the textviews in activity_main.xml
							TextView txtrank = (TextView) findViewById(R.id.rank);
							TextView txtcountry = (TextView) findViewById(R.id.country);
							TextView txtpopulation = (TextView) findViewById(R.id.population);
							
							// Set the text followed by the position 
							txtrank.setText("hall_name : "
									+ world.get(position).getRank());
							txtcountry.setText("month : "
									+ world.get(position).getCountry());
							txtpopulation.setText("Available_dates : "
									+ world.get(position).getPopulation());
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
						}
					});
		}
	}

}
