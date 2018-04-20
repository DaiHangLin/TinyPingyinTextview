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

public class SecondActivity extends Activity {

    private Button btnStart;
    private Button btnColor;
    private Button btnPause;
    private TinyPingYinView myTv;
    private ScrollView scrollParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        btnStart = findViewById(R.id.btnStart);
        btnColor = findViewById(R.id.btnColor);
        btnPause = findViewById(R.id.btnPause);
        myTv = findViewById(R.id.tv);
        scrollParent = findViewById(R.id.scrollParent);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair<List<String>, List<String>> data = ParseUtils.readDataFormResource(getResources());
                myTv.setHanzi(data.first);
                myTv.setPinyin(data.second);
                myTv.setNewLineIndex(ParseUtils.parsePinYinLineIndex(data.second));
                myTv.invalidate();
            }
        });
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTv.startRenderColor(0);
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTv.pauseOrResumeRenderColor();
            }
        });
        myTv.setParentHeight(30);
        myTv.setScrollListener(new TinyPingYinView.ScrollListener() {
            @Override
            public void scrollToPosition(int position) {
//                scrollParent.scrollTo(0, position);
                myTv.scrollTo(0, position);
            }

            @Override
            public void scrollByDy(int dy) {

            }
        });
    }
}
