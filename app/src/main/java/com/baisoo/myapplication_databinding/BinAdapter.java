package com.baisoo.myapplication_databinding;

import android.databinding.BindingAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sense on 2016/3/9.
 */
public class BinAdapter {
    /**
     * ListView 通用Adapter
     * @param view
     * @param item
     * @param data
     */
    @android.databinding.BindingAdapter({"app:item", "app:data"})
    public static void setListener(final ListView view, final int item, final List data) {

        if (view.getTag() == null) {
            ListViewAdapter listViewAdapter = new ListViewAdapter(view.getContext(), item, data);
            view.setAdapter(listViewAdapter);
            view.setTag(listViewAdapter);
        } else {

            view.post(new Runnable() {
                @Override
                public void run() {
                    ((ListViewAdapter) (view.getTag())).notifyDataSetChanged();
                    view.setSelection(data.size());
                }
            });
        }
    }

    @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext()).load(url).into(view);
    }

    @BindingAdapter({"app:editText"})
    public static void EditText(EditText text, final BindingString editText) {
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.srt= s.toString();
                editText.notifyChange();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
