package com.haibeey.android.naijaapps.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class CircleTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint paint=new Paint();
    private Canvas canvas;
    public CircleTextView(Context context) {
        super(context);
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas=canvas;
        super.onDraw(canvas);
        int height=getHeight();
        int width=getWidth();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(width/2,height/2,Math.min(width/2,height/2),paint);
    }



}
