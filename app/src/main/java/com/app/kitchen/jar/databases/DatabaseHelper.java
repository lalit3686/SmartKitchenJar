package com.app.kitchen.jar.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.app.kitchen.jar.commons.AppLogs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

	private Context mContext;
	public static String DB_NAME = "smart_kitchen_jar.db";
	private static int DB_VERSION = 1;
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
	
	private void isDirectoryPresent(String db_path) {
		// create "databases" directory if not in existence in data/data/package_name/databases/
		File file = new File(db_path.substring(0, db_path.indexOf("/"+DB_NAME)));
		
		// check if databases folder exists or not.
		if(!file.isDirectory())
			file.mkdir();
	}
	
	public void createDatabaseFile(){
		
		// data/data/package_name/databases/db_name.db
		String db_path = mContext.getDatabasePath(DatabaseHelper.DB_NAME).toString();
		isDirectoryPresent(db_path); 
		
		File file = new File(db_path);
		AppLogs.d(getClass().getSimpleName(), file.getAbsolutePath());
		if(file.exists()){
			AppLogs.d(getClass().getSimpleName(), "File already exists");
		}
		else{
			copyDatabase(file);
		}
	}
	
	private void copyDatabase(File file) {
		try {
			file.createNewFile();   // create new file if it is not in existence
			InputStream is = mContext.getAssets().open(DB_NAME);
			OutputStream write = new FileOutputStream(file);
			
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				write.write(buffer, 0, length);
			}
			is.close();
			write.close();
			AppLogs.d(getClass().getSimpleName(), "File does not exists & Newly created");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}