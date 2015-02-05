package com.example.PWV;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
	
	static final String TABLE_NAME = "PASSWORD_DATABASE" ;
	static final String PRIMARY_ID = "_id" ;
	static final String TITLE = "Title" ;
	static final String USERNAME = "Username" ;
	static final String PASSWORD = "Password" ;
			
	public SQLHelper(Context context)
	{
		super(context, "PASSWORD_DATABASE", null, 1) ;
	}
	
	public void onCreate(SQLiteDatabase db)
	{
		try
		{
			db.execSQL("CREATE TABLE " + TABLE_NAME + " ("+PRIMARY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TITLE+" VARCHAR(255),"
			+ " "+USERNAME+" VARCHAR(255), "+PASSWORD+" VARCHAR(255));"); 
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL("DROP TABLE IF EXISTS PASSWORD_DATABASE") ;
			onCreate(db) ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void deleteRow(String row, SQLiteDatabase db)
	{
		db.execSQL("DELETE FROM "+ TABLE_NAME+" WHERE Title=" +"'"+ row+"'");
	}
}