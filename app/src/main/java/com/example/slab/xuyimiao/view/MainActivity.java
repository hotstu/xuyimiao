package com.example.slab.xuyimiao.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.slab.xuyimiao.R;
import com.example.slab.xuyimiao.databinding.ActivityMainBinding;
import com.example.slab.xuyimiao.viewmodel.DynamicViewModel;
import com.example.slab.xuyimiao.viewmodel.SecondsCount;

import github.hotstu.xuyimiao.DynamicView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        DynamicView dy = DynamicView.attach2Window(this);
        DynamicViewModel dm = new DynamicViewModel(dy);
        SecondsCount count = new SecondsCount(0);
        binding.setCount(count);
        binding.setDynamicModel(dm);
    }
}
