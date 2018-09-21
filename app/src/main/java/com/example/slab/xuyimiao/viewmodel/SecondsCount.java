package com.example.slab.xuyimiao.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.example.slab.xuyimiao.BR;

/**
 * Created by hotstuNg on 2016/8/6.
 */
public class SecondsCount extends BaseObservable {
    volatile int second;

    public SecondsCount(int second) {
        this.second = second;
    }

    @Bindable
    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
        notifyPropertyChanged(BR.second);
    }

    public void increaseBy(int second) {
        setSecond(getSecond() + second);
    }
}
