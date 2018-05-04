package com.explore.lin.test;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.explore.lin.mylibrary.TinyPingYinView;
import com.explore.lin.test.utils.ParseUtils;

import java.util.List;

/**
 * @author lin
 * @date 18/4/3
 * @license Copyright (c) 2016 那镁克
 */

public class AActivity extends Activity {

    private Button btnStart;
    private Button btnColor;
    private Button btnPause;
    private TinyPingYinView myTv;
    private ScrollView scrollParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}
