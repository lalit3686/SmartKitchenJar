package com.app.kitchen.jar.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.firebase.crash.FirebaseCrash;

import java.lang.ref.WeakReference;

/**
 * Created by Lalit T. Poptani on 10/24/2016.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    abstract void initComponents();

    abstract void addListeners();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler{

        private WeakReference<BaseActivity> reference;

        MyUncaughtExceptionHandler(BaseActivity activity){
            this.reference = new WeakReference<BaseActivity>(activity);
        }

        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            FirebaseCrash.report(throwable);
            reference.get().startActivity(new Intent(reference.get(), FoodItemsListActivity.class));
            System.exit(0);
        }
    }
}
