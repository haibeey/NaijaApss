package com.haibeey.android.naijaapps.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;


public class CustomConstraintLayout  extends ConstraintLayout{
    private Paint paint=new Paint();
    private RectF rectf=new RectF();
    public CustomConstraintLayout(Context context) {
        super(context);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(Color.parseColor("#FF9800"));
        rectf.bottom=0;
        rectf.left=0;
        rectf.top=0;
        rectf.right=getWidth();
        canvas.drawArc(rectf,0,90,false,paint);


    }
}
