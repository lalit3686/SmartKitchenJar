package com.app.kitchen.jar.dialogs;

import android.app.Dialog;
import android.content.Context;

public abstract class BaseDialog extends Dialog {

    private Context context;

    public BaseDialog(Context context) {
        super(context);

        this.context = context;
    }

    abstract void initComponents();

    abstract void addListeners();

    protected void dismissDialog(){
        dismiss();
    }
}