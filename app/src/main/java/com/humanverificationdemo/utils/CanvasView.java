package com.humanverificationdemo.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.View;

public class CanvasView extends View {
    private Paint paint;
    float x, y;
    String text = "";
    boolean clearCanvas = false;
    public float radius = 8;

    public CanvasView(Context context) {
        super(context);
        this.x = 0.0f;
        this.y = 0.0f;
        this.text = "";
        paint = new Paint();
        paint.setColor(Color.WHITE);
    }


    public CanvasView(Context context, float x, float y, String text) {
        super(context);
        this.x = x;
        this.y = y;
        this.text = text;
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(24f);
    }
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (clearCanvas) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            return;
        }
        canvas.drawCircle(x, y, radius, paint);
//        canvas.drawText(text, x - 100, y - 25, paint);
//        drawString(text, x - 200, y + 90, paint, canvas);
        drawString(text, x - 200, y + 90, paint, canvas);
//        canvas.drawLine(0f, SharedPrefsUtils.INSTANCE.getWidth()/2, x,y,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(SharedPrefsUtils.INSTANCE.getWidth(), SharedPrefsUtils.INSTANCE.getHeight());
    }

    boolean isFirstTime = true;

    public void updateXAndY(float x, float y, String text) {
        this.clearCanvas = false;
        this.x = x;
        this.y = y;
        this.text = text;
        postInvalidate();
    }


    public void clear() {
        this.clearCanvas = true;
        postInvalidate();
    }

    public void drawString(String text, float x, float y, Paint paint, Canvas canvas) {
        if (text.contains("\n")) {
            String[] texts = text.split("\n");

            for (String txt : texts) {
                canvas.drawText(txt, x, y, paint);

                y += paint.getTextSize();
            }
        } else {
            canvas.drawText(text, x, y, paint);
        }
    }
}
