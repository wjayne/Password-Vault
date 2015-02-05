package com.example.PWV;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListPage extends Activity{
	
	SQLHelper helper ;
	SharedPreferences sp ;
	SharedPreferences.Editor edit ;
	//ArrayList<String> titles = new ArrayList<String>() ;
	//final Dialog dialog = new Dialog(ListPage.this) ;
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_page);
		sp = MainActivity.sp ;
		edit = sp.edit() ;
		helper = new SQLHelper(this) ;
		final Dialog dialog = new Dialog(ListPage.this) ;
		
		populateList() ;
		Button settings = (Button) findViewById(R.id.settings_button) ;
		settings.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				dialog.setContentView(R.layout.pw_enter) ;
				dialog.setTitle("Password");
				final EditText pw1 = (EditText) dialog.findViewById(R.id.pw1b) ;
				final EditText pw2 = (EditText) dialog.findViewById(R.id.pw2b) ;
				final EditText pw3 = (EditText) dialog.findViewById(R.id.pw3bb) ;
				final Button enter = (Button) dialog.findViewById(R.id.ent1b) ;
				final Button cancel = (Button) dialog.findViewById(R.id.cancelb) ;
				
				cancel.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						dialog.dismiss() ;
					}
					
				}) ;
				enter.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						if(pw1.getText().toString().equals(sp.getString("password", "will")))
						{
							if(!(pw2.getText().toString().equals(pw3.getText().toString())))
							{
								Context context = getApplicationContext() ;
								CharSequence text = "Passwords do not match!" ; 
								Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT) ;
								toast.show() ;
							}
							else if(pw2.getText().toString().length() != 4)
							{
								Context context = getApplicationContext() ;
								CharSequence text = "Please enter 4 digit password!" ; 
								Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT) ;
								toast.show() ;
							}
							else if(!(MainActivity.isNumber(pw2.getText().toString())))
							{
								Context context = getApplicationContext() ;
								CharSequence text = "Numbers only!" ; 
								Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT) ;
								toast.show() ;
							}
							else
							{
								Context context = getApplicationContext() ;
								CharSequence text = "Password is " + pw2.getText().toString(); 
								Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG) ;
								toast.show() ;
								edit.putString("password", pw2.getText().toString()) ;
								edit.putBoolean("pw_set", true) ;
								edit.commit() ;
								dialog.dismiss() ;
							}
						}
						else
						{
							Context context = getApplicationContext() ;
							CharSequence text = "Incorrect Password" ; 
							Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT) ;
							toast.show() ;
						}
						
					}
					
				});
				dialog.show();
			}
			
		}) ;
		Button add = (Button) findViewById(R.id.add_button) ;
		add.setOnClickListener(new OnClickListener(){
			

			@Override
			public void onClick(View v) {
				
				dialog.setContentView(R.layout.input_element);
				dialog.setTitle("Enter new Password");
				Button submitItem = (Button) dialog.findViewById(R.id.enter_button) ;
				submitItem.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						final SQLiteDatabase data = helper.getWritableDatabase() ;
						EditText desc = (EditText) dialog.findViewById(R.id.pw1) ;
						EditText dialogUsr = (EditText) dialog.findViewById(R.id.EditText01) ;
						EditText dialogPass = (EditText) dialog.findViewById(R.id.EditText02) ;
						ArrayList<String> titles = getTitleData() ;
						boolean allowed = true ;
						for(int i = 0 ; i < titles.size() ; i++)
						{
							if (desc.getText().toString().equals(titles.get(i)))
							{
								allowed = false ;
								break ;
							}
						}
						if(allowed)
						{
							ContentValues cv = new ContentValues() ;
							cv.put(SQLHelper.TITLE, desc.getText().toString());
							cv.put(SQLHelper.USERNAME, dialogUsr.getText().toString());
							cv.put(SQLHelper.PASSWORD, dialogPass.getText().toString());
							data.insert(SQLHelper.TABLE_NAME, null, cv) ;
							populateList() ;
							dialog.dismiss();
						}
						else
						{
							Context context = getApplicationContext() ;
							CharSequence text = "Description must be unique from other passwords" ; 
							Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG) ;
							toast.show() ;
						}
						
					}
					
				}) ;
				dialog.show();
			}
		});
		
	}
	
	private void populateList()
	{
		final Dialog dialog2 = new Dialog(ListPage.this) ;
		@SuppressWarnings("unchecked")
		ArrayList<String> titles = getTitleData() ;
		String[] items = new String[titles.size()] ;
		for(int i = 0 ; i<titles.size(); i++)
		{
			items[i] = titles.get(i) ;
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item,items) ;
		ListView list = (ListView) findViewById(R.id.listView1) ;
		list.setAdapter(adapter) ;
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				dialog2.setContentView(R.layout.pw_element);
				TextView un = (TextView) dialog2.findViewById(R.id.un_textview) ;
				TextView pw = (TextView) dialog2.findViewById(R.id.pw_textView) ;
				Button ok = (Button) dialog2.findViewById(R.id.ok) ;
				Button delete = (Button) dialog2.findViewById(R.id.delete) ;
				ok.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						dialog2.dismiss() ;
						
					}
					
				});
				
				delete.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						SQLiteDatabase db = helper.getWritableDatabase() ;
						ArrayList<String> titles = getTitleData() ;
						String row = "" + titles.get(position) ;
						SQLHelper.deleteRow(row, db) ;
						dialog2.dismiss();
						populateList() ;
					}
					
				}) ;
				
				ArrayList<String> titles = getTitleData() ;
				ArrayList<String> passwords = getPWData() ;
				ArrayList<String> usernames = getUNData() ;
				dialog2.setTitle(titles.get(position));
				un.setText(usernames.get(position));
				pw.setText(passwords.get(position));
				dialog2.show();
			}
			
		});
		
	}
	@SuppressWarnings("rawtypes")
	public ArrayList getTitleData()
	{
		ArrayList<String> titles = new ArrayList<String>() ;
		SQLiteDatabase db = helper.getWritableDatabase() ;
		String[] columns = {SQLHelper.TITLE, SQLHelper.USERNAME, SQLHelper.PASSWORD} ;
		Cursor cursor = db.query(SQLHelper.TABLE_NAME, columns,null,null,null,null,null ) ;
		while(cursor.moveToNext())
		{
			titles.add(cursor.getString(0)) ;
		}
		return titles ;
	}
	public ArrayList getUNData()
	{
		ArrayList<String> titles = new ArrayList<String>() ;
		SQLiteDatabase db = helper.getWritableDatabase() ;
		String[] columns = {SQLHelper.TITLE, SQLHelper.USERNAME, SQLHelper.PASSWORD} ;
		Cursor cursor = db.query(SQLHelper.TABLE_NAME, columns,null,null,null,null,null ) ;
		while(cursor.moveToNext())
		{
			titles.add(cursor.getString(1)) ;
		}
		return titles ;
	}
	public ArrayList getPWData()
	{
		ArrayList<String> titles = new ArrayList<String>() ;
		SQLiteDatabase db = helper.getWritableDatabase() ;
		String[] columns = {SQLHelper.TITLE, SQLHelper.USERNAME, SQLHelper.PASSWORD} ;
		Cursor cursor = db.query(SQLHelper.TABLE_NAME, columns,null,null,null,null,null ) ;
		while(cursor.moveToNext())
		{
			titles.add(cursor.getString(2)) ;
		}
		return titles ;
	}
	
}
