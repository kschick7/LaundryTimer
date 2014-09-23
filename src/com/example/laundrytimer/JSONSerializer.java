package com.example.laundrytimer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import android.content.Context;

public class JSONSerializer {
	private Context mContext;
	private String mFilename;
	
	public JSONSerializer(Context c, String f) {
		mContext = c;
		mFilename = f;
	}
	
	public void saveMachines(ArrayList<Machine> machines) throws JSONException, IOException {
		// Build an array in JSON
		JSONArray array = new JSONArray();
		for (Machine m : machines) {
			array.put(m.toJSON());
		}
		
		// Write the file to disk
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		} finally {
			if (writer != null)
				writer.close();
		}
	}
	
	public ArrayList<Machine> loadMachines() throws IOException, JSONException {
		ArrayList<Machine> machines = new ArrayList<Machine>();
		BufferedReader reader = null;
		try {
			// Open and read the file into a StringBuilder
			InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				// Line breaks are omitted and irrelevant
				jsonString.append(line);
			}
			// Parse the JSON using JSONTokener
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			
			// Build the array of machines from JSONObjects
			for (int i = 0; i < array.length(); i++) {
				machines.add(new Machine(array.getJSONObject(i)));
			}
		} catch (FileNotFoundException e) {
			// IGNORE
		} finally {
			if (reader != null)
				reader.close();
		}
		return machines;
	}
}
