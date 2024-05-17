package com.hasan.storyvibrance.Notification;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BadgeDrawable extends Drawable {
    private static final float RADIUS = 10f;
    private static final float TEXT_SIZE = 15f;

    private final Paint badgePaint;
    private final Paint textPaint;
    private final Rect bounds = new Rect();
    private int notificationCount = 0;

    public BadgeDrawable() {
        badgePaint = new Paint();
        badgePaint.setColor(Color.RED);
        badgePaint.setAntiAlias(true);
        badgePaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (notificationCount <= 0) return;

        String text = String.valueOf(notificationCount);
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        Rect rect = getBounds();
        float cx = rect.right - bounds.width() / 2f - RADIUS;
        float cy = rect.top + bounds.height() / 2f + RADIUS;

        canvas.drawCircle(cx, cy, RADIUS, badgePaint);
        canvas.drawText(text, cx, cy + bounds.height() / 2f, textPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        // Not needed
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }


    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    public void setNotificationCount(int count) {
        this.notificationCount = count;
        invalidateSelf();
    }
}
