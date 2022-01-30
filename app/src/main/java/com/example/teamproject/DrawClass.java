package com.example.teamproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

public class DrawClass extends SurfaceView {

    private Bitmap hermit;

    public DrawClass(Context context) {
        super(context);
        initialize();
    }

    public DrawClass(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {

        setWillNotDraw(false);

        hermit = BitmapFactory.decodeResource(getResources(), R.drawable.hermit);

    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(hermit, 400.0f, 400.0f, new Paint());
    }
}
