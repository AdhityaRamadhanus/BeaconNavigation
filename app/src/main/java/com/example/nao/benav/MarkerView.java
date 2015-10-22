package com.example.nao.benav;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by nao on 8/15/15.
 */
public class MarkerView extends SubsamplingScaleImageView {
    private PointF sPin;
    private Bitmap pin;

    public MarkerView(Context context) {
        this(context, null);
    }

    public MarkerView(Context context, AttributeSet attr) {
        super(context, attr);
        loadResources();
    }

    public void setPin(PointF sPin) {
        this.sPin = sPin;
        loadResources();
        invalidate();
    }

    public PointF getPin() {
        return sPin;
    }

    private void loadResources() {
        float density = getResources().getDisplayMetrics().densityDpi;
        pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker_50);
        float w = (density/420f) * pin.getWidth();
        float h = (density/420f) * pin.getHeight();
        pin = Bitmap.createScaledBitmap(pin, (int)w, (int)h, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        if (sPin != null && pin != null) {
            PointF vPin = sourceToViewCoord(sPin);
            float vX = vPin.x - (pin.getWidth()/2);
            float vY = vPin.y - pin.getHeight();
            canvas.drawBitmap(pin, vX, vY, paint);
        }

    }
}
