package com.example.PWV;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button submit ;
	EditText input ;
	static SharedPreferences sp ; 
	SharedPreferences.Editor edit ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sp = this.getPreferences(Context.MODE_PRIVATE) ;
		edit = sp.edit();
		
		if(!sp.getBoolean("pw_set", false))
		{
			final Dialog dialog = new Dialog(MainActivity.this) ;
			dialog.setContentView(R.layout.initial_input);
			dialog.setTitle("Welcome new user!");
			Button dsubmit = (Button) dialog.findViewById(R.id.submit) ;
			dsubmit.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					EditText in1 = (EditText)dialog.findViewById(R.id.input1) ;
					EditText in2 = (EditText)dialog.findViewById(R.id.input2) ;
					
					if(!(in1.getText().toString().equals(in2.getText().toString())))
					{
						Context context = getApplicationContext() ;
						CharSequence text = "Passwords do not match!" ; 
						Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT) ;
						toast.show() ;
					}
					else if(in1.getText().toString().length() != 4)
					{
						Context context = getApplicationContext() ;
						CharSequence text = "Please enter 4 digit password!" ; 
						Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT) ;
						toast.show() ;
					}
					else if(!(isNumber(in1.getText().toString())))
					{
						Context context = getApplicationContext() ;
						CharSequence text = "Numbers only!" ; 
						Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT) ;
						toast.show() ;
					}
					else
					{
						Context context = getApplicationContext() ;
						CharSequence text = "Password is " + in1.getText().toString(); 
						Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG) ;
						toast.show() ;
						edit.putString("password", in1.getText().toString()) ;
						edit.putBoolean("pw_set", true) ;
						edit.commit() ;
						dialog.dismiss() ;
					}
				}
				
			});
			dialog.show();
		}
		
			final EditText pwIn = (EditText) findViewById(R.id.EditText) ;
			Button sub = (Button) findViewById(R.id.enter) ;
			sub.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					if(pwIn.getText().toString().equals(sp.getString("password", "vwrgvtgt")))
					{
						Intent intent = new Intent(MainActivity.this, ListPage.class);
						startActivity(intent);
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
	}
	
	public static boolean isNumber(String a)
	{
		try
		{
			int b = Integer.parseInt(a) ;
		}
		catch(NumberFormatException e)
		{
			return false ;
		}
		return true ;
	}
}
