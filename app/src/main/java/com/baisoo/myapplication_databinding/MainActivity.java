package com.baisoo.myapplication_databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baisoo.myapplication_databinding.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    ViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout
                .activity_main);
        viewModel= new ViewModel(this);
        activityMainBinding.setViewModel(viewModel);
        activityMainBinding.listView.addHeaderView(new TextView(this));
        viewModel.add();
    }


    public void test(View v ){
        Toast.makeText(v.getContext(),v.getTag()+"",Toast.LENGTH_LONG).show();
        viewModel.addto();
    }


    public void url(View v){
        viewModel.url();
    }


    public void but(View v){
        viewModel.but();
    }
}
