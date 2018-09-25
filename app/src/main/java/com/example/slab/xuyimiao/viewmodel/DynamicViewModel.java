package com.example.slab.xuyimiao.viewmodel;

import android.databinding.BindingAdapter;
import android.util.Log;
import android.view.View;

import github.hotstu.xuyimiao.DynamicView;

/**
 * Created by hotstuNg on 2016/8/8.
 */
public class DynamicViewModel{

    private DynamicView dy;

    public DynamicViewModel(DynamicView dy) {
        this.dy = dy;
    }

    public void inspect(View view, String text) {
        dy.inspect(view, text);
    }

    @BindingAdapter({"bind:dynamic","bind:count"})
    public static void count(View v, DynamicViewModel dy,int count, DynamicViewModel dynew,  int countNew) {
        Log.d("DynamicViewModel", "count() called with: " + "v = [" + v + "], dy = [" + dy + "], count = [" + count + "], dynew = [" + dynew + "], countNew = [" + countNew + "]");
        if (dynew == null || count >= countNew) {
            return;
        }
        dy.inspect(v, "+" + (countNew - count) + "s");
    }
}
