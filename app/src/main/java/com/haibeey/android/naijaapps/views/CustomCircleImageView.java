package com.haibeey.android.naijaapps.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomCircleImageView extends CircleImageView {
    private Paint paint=new Paint();
    public CustomCircleImageView(Context context) {
        super(context);
    }

    public CustomCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);
        canvas.drawText("Tap to Select App Image",5,getHeight()/2,paint);
    }

}
