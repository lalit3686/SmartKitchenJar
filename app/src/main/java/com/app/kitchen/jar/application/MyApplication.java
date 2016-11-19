package com.app.kitchen.jar.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.app.kitchen.jar.databases.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;

public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication instance;
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        getDatabaseHelperInstance().createDatabaseFile();
    }

    public static MyApplication getApplicationInstance() {
        return instance;
    }

    private static DatabaseHelper getDatabaseHelperInstance() {
        if(databaseHelper == null){
            databaseHelper = new DatabaseHelper(getApplicationInstance());
        }
        return databaseHelper;
    }

    public static SQLiteDatabase getDatabaseInstance() {
        if(database == null || !database.isOpen()){
            database = getDatabaseHelperInstance().getWritableDatabase();
        }
        return database;
    }

    public static EventBus getEventBusInstance(){
        return EventBus.getDefault();
    }
}