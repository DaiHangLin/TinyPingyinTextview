package com.explore.lin.test.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @author lin
 * @date 18/4/2
 * @license Copyright (c) 2016 那镁克
 */

@SuppressLint("AppCompatCustomView")
public class TinyPingYinView extends TextView {

    private static final String TAG = TinyPingYinView.class.getSimpleName();
    private final static String NEW_LINE_PLACE_HOLDER = "newLine";

    private List<String> hanzi = Arrays.asList(
            "雪", "人", "大", "肚", "子", "一", "挺", " ",
            "雪", "人", "大", "肚", "子", "一", "挺", " ",
            "雪", "人", "大", "肚", "子", "一", "挺", " ",
            "雪", "人", "大", "肚", "子", "一", "挺", " ",
            "雪", "人", "大", "肚", "子", "一", "挺", " ",
            "雪", "人", "大", "肚", "子", "一", "挺", " ",
            "雪", "人", "大", "肚", "子", "一", "挺", " ",
            "雪", "人", "大", "肚", "子", "一", "挺", " ",
            "雪", "人", "大", "肚", "子", "一", "挺", " ",
            "雪", "人", "大", "肚", "子", "一", "挺", " ",
            "雪", "人", "大", "肚", "子", "一", "挺", " "
    );
    private List<String> pinyin = Arrays.asList(
            "xuě", "rén", "dà", "dù", "zi", "yì", "tǐng", NEW_LINE_PLACE_HOLDER,
            "xuě", "rén", "dà", "dù", "zi", "yì", "tǐng", NEW_LINE_PLACE_HOLDER,
            "xuě", "rén", "dà", "dù", "zi", "yì", "tǐng", NEW_LINE_PLACE_HOLDER,
            "xuě", "rén", "dà", "dù", "zi", "yì", "tǐng", NEW_LINE_PLACE_HOLDER,
            "xuě", "rén", "dà", "dù", "zi", "yì", "tǐng", NEW_LINE_PLACE_HOLDER,
            "xuě", "rén", "dà", "dù", "zi", "yì", "tǐng", NEW_LINE_PLACE_HOLDER,
            "xuě", "rén", "dà", "dù", "zi", "yì", "tǐng", NEW_LINE_PLACE_HOLDER,
            "xuě", "rén", "dà", "dù", "zi", "yì", "tǐng", NEW_LINE_PLACE_HOLDER,
            "xuě", "rén", "dà", "dù", "zi", "yì", "tǐng", NEW_LINE_PLACE_HOLDER,
            "xuě", "rén", "dà", "dù", "zi", "yì", "tǐng", NEW_LINE_PLACE_HOLDER,
            "xuě", "rén", "dà", "dù", "zi", "yì", "tǐng", NEW_LINE_PLACE_HOLDER
    );
    private Set<Integer> newLineIndex = new HashSet<>(Arrays.asList(7, 15, 23, 31, 39, 47, 55, 63, 71, 79, 87));
    private float height;
    private TextPaint myTextPaint = new TextPaint();
    private int viewWidth;
    private int viewHeight;
    private float currProgress = 0;
    private int parentHeight;

    // 父容器的height
    public void setParentHeight(int height) {
        this.parentHeight = height;
    }

    public TinyPingYinView(Context context) {
        this(context, null);
    }

    public TinyPingYinView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TinyPingYinView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private int baseLine = 0;
    private int descent = 0;
    private int lineHeight = 0;
    private int linePadding = 20;
    private void initPaint() {
        myTextPaint.setAntiAlias(true);
        myTextPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        myTextPaint.setColor(0xFF000000);
        Paint.FontMetricsInt fontMetrics = myTextPaint.getFontMetricsInt();
        baseLine = Math.abs(fontMetrics.top);
        descent = fontMetrics.descent;
        lineHeight = baseLine * 2 + descent * 2;
    }

    /**
     * 设置字体所占的高度
     * @param baseLine
     */
    public void setBaseLine(int baseLine) {
        this.baseLine = baseLine;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        viewHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setHanzi(List hanzi) {
      this.hanzi = hanzi;
    }

    public void setPinyin(List pinyin) {
        this.pinyin = pinyin;
    }

    public void setNewLineIndex(Set<Integer> newLineIndex) {
        this.newLineIndex = newLineIndex;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        myTextPaint.setColor(Color.RED);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        int initX = getPaddingLeft();
        int initY = getPaddingTop() + baseLine;
        int startX = initX; // 开始绘制的x起点
        int startY = initY; // 开始绘制的y起点
        int widthPadding = 20;

        Rect hanZiRect, pinYinRect;
        String hanZi, pinYin;
        int combineWidth, hanZiWidth, pinYinWidth;
        int hanZiCenterPadding, pinYinCenterPadding;
        for (int i = 0; i < hanzi.size(); i++) {
            // 拿到hanzi，与宽高
            hanZi = this.hanzi.get(i);
            hanZiRect = new Rect();
            myTextPaint.getTextBounds(hanZi, 0, hanZi.length(), hanZiRect);
            hanZiWidth = hanZiRect.right - hanZiRect.left;
            // 拿到拼音，与宽高
            pinYin = this.pinyin.get(i);
            pinYinRect = new Rect();
            myTextPaint.getTextBounds(pinYin, 0, pinYin.length(), pinYinRect);
            pinYinWidth = pinYinRect.right - pinYinRect.left;
            // 比较 拼音 - 汉字 的宽度，取最宽的为标准
            combineWidth = Math.max(pinYinWidth, hanZiWidth);

            // 居中显示
            hanZiCenterPadding = (combineWidth - hanZiWidth) / 2;
            pinYinCenterPadding = (combineWidth - pinYinWidth) / 2;

            // 渲染颜色
            if (i < currProgress) {
                myTextPaint.setColor(Color.RED);
            } else {
                myTextPaint.setColor(Color.BLACK);
            }

            // 换行 无需绘制
            if (pinYin.equalsIgnoreCase(NEW_LINE_PLACE_HOLDER) || startX +  pinYinWidth >= viewWidth || startX +  hanZiWidth >= viewWidth) {
                startX = initX;
                startY = startY + lineHeight + linePadding;
            } else {
                // startY 是 baseLine
                canvas.drawText(pinYin, startX + pinYinCenterPadding, startY, myTextPaint);
                canvas.drawText(hanZi, startX + hanZiCenterPadding, startY + lineHeight/2, myTextPaint);
                // 下一个绘制的x
                startX = startX + combineWidth + widthPadding;
            }
        }
    }

    public void setCurrProgress(float currProgress) {
        this.currProgress = currProgress;
    }

    private ScrollListener scrollListener;
    public void setScrollListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public interface ScrollListener {
        void scrollToPosition(int position);
        void scrollByDy(int dy);
    }
    private Disposable subscribe;
    private int currentLineIndex = 0;
    public void startRenderColor(long start) {
        if (subscribe != null) {
            subscribe.dispose();
            subscribe = null;
            currentLineIndex = 0;
            currProgress = 0;
        }
        final int length = pinyin.size();
        subscribe =  Observable.intervalRange(start, length, 0, 150, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        setCurrProgress(aLong);
                        invalidate();
//                        if (newLineIndex.contains(aLong.intValue())) {
//                            currentLineIndex ++;
//                            Log.e(TAG, "accept: " + currentLineIndex );
//                            scrollListener.scrollToPosition(currentLineIndex * scrollDy);
//                            scrollListener.scrollByDy(scrollDy);
//                        }
                        if (pinyin.get(aLong.intValue()).equalsIgnoreCase(NEW_LINE_PLACE_HOLDER)) {
                            currentLineIndex ++;
                            scrollListener.scrollToPosition(currentLineIndex * lineHeight);
                            scrollListener.scrollByDy(lineHeight);
                        }
                    }
                });
    }
    public void pauseOrResumeRenderColor() {
        if (subscribe != null) {
            subscribe.dispose();
            subscribe = null;
        } else {
            startRenderColor((long) currProgress);
        }
    }

}
