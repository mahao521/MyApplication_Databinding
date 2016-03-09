package com.baisoo.myapplication_databinding;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.widget.Toast;

/**
 * Created by Sense on 2016/3/9.
 */
public class ViewModel extends BaseObservable {
    public ObservableList<String> mData = new ObservableArrayList<>();
    public ObservableField<String> url = new ObservableField<>();
    public BindingString ext = new BindingString();
    public ObservableBoolean isOk = new ObservableBoolean(true);
    private Context context;

    public ViewModel(Context context) {
        this.context = context;
    }

    public void add() {
        for (int i = 0; i < 50; i++) {
            mData.add(i + "wo shi shu ju ");
        }
    }

    public void addto() {
        mData.add(" wo shi  hou  lia  de");
        mData.add(" wo shi  hou  lia  de2");
        isOk.set(false);
    }

    public void url() {
        url.set("http://img.68design.net/art/20121102/Txto1cjJDMUp0d2.png");
    }

    public void but() {
        Toast.makeText(context,"wo shi "+ext.srt,Toast.LENGTH_LONG).show();
        isOk.set(true);
    }
}
