package com.apps.newstudio.tasklist.ui.views;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.data.adapters.DataForLinearDiagram;
import com.apps.newstudio.tasklist.utils.ConstantsManager;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

import java.util.List;

public class CustomLinearDiagram extends View {

    private List<DataForLinearDiagram> mData = null;
    private int mHorizontalLineY;
    private int mMaxDiagramWidth, mMaxDiagramHeight;
    private int mR, mState = ConstantsManager.STATE_FLAG_ACTIVE, mColor = R.color.colorPrimary;
    private Resources mResources;
    private int mTextSize, mK;
    private String mDaysTitle;


    /**
     * Constructor of CustomLinearDiagram object
     *
     * @param context      Context object
     * @param attributeSet AttributeSet object
     */
    public CustomLinearDiagram(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mResources = TaskListApplication.getContext().getResources();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /**
     * Sets measure of main View object
     *
     * @param widthMeasureSpec  value for width value of View object
     * @param heightMeasureSpec value for height value of View object
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMaxDiagramWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        mMaxDiagramHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec) -
                mResources.getDimensionPixelSize(R.dimen.spacing_small_16dp) -
                mResources.getDimensionPixelSize(R.dimen.middle_size_48dp);

        mHorizontalLineY = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec)
                - mResources.getDimensionPixelOffset(R.dimen.small_size_24dp);
    }

    /**
     * Draws main View object
     *
     * @param canvas Canvas object of main View
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mData != null) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(mResources.getDimensionPixelOffset(R.dimen.spacing_start_1dp));
            paint.setColor(mResources.getColor(R.color.grey_two));
            paint.setAntiAlias(true);
            canvas.drawLine(0, mHorizontalLineY, getWidth(), mHorizontalLineY, paint);

            drawCustomText(canvas, mDaysTitle, 0,
                    mHorizontalLineY + mResources.getDimensionPixelSize(R.dimen.spacing_small_12dp),
                    R.color.grey, 0);

            paint.setColor(mResources.getColor(mColor));
            paint.setStrokeWidth(mResources.getDimensionPixelOffset(R.dimen.spacing_smallest_2dp));

            int padding = (mMaxDiagramWidth) / (mData.size() + 1);
            float startX, startY, endX = 0, endY = 0;

            Double maxValue = 0.0;
            for (DataForLinearDiagram d : mData) {
                if (maxValue < 0.0 + d.getCountActive()) maxValue = 0.0 + d.getCountActive();
                if (maxValue < 0.0 + d.getCountDone()) maxValue = 0.0 + d.getCountDone();

            }
            Double index = maxValue / (mMaxDiagramHeight);

            for (int i = 0; i < mData.size(); i++) {

                int value = mData.get(i).getCountActive();
                if (mState == ConstantsManager.STATE_FLAG_DONE) {
                    value = mData.get(i).getCountDone();
                }

                startX = padding + padding * i;
                if (maxValue != 0.0) {
                    startY = (float) (getHeight() - mResources.getDimensionPixelSize(R.dimen.small_size_24dp) -
                            mResources.getDimensionPixelSize(R.dimen.spacing_small_16dp) - value / index);
                } else {
                    startY = (float) (getHeight() - mResources.getDimensionPixelSize(R.dimen.small_size_24dp)) -
                            mResources.getDimensionPixelSize(R.dimen.spacing_small_16dp);
                }

                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(mResources.getColor(R.color.grey_two));
                paint.setStrokeWidth(mResources.getDimensionPixelOffset(R.dimen.spacing_start_1dp));
                canvas.drawLine(startX, mHorizontalLineY + mK, startX, mHorizontalLineY - mK, paint);

                if (i != 0) {
                    paint.setColor(mResources.getColor(mColor));
                    canvas.drawLine(endX, endY, startX, startY, paint);
                    drawCustomCircle(canvas, endX, endY, paint);
                }

                mData.get(i).setX(startX);
                mData.get(i).setY(startY);
                drawCustomCircle(canvas, startX, startY, paint);
                drawCustomText(canvas, "" + (i + 1), startX,
                        mHorizontalLineY + mResources.getDimensionPixelSize(R.dimen.spacing_small_12dp),
                        R.color.grey, 1);
                drawCustomText(canvas, "" + value, startX,
                        startY - mResources.getDimensionPixelSize(R.dimen.spacing_small_12dp),
                        mColor, 1);

                endX = startX;
                endY = startY;
            }
        }
    }

    /**
     * Draws custom circle for values of linear diagram
     *
     * @param canvas Canvas object of main View object
     * @param x      x coordinate
     * @param y      y coordinate
     * @param paint  Paint object to draw circles
     */
    private void drawCustomCircle(Canvas canvas, float x, float y, Paint paint) {
        paint.setColor(mResources.getColor(R.color.white));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, mR, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mResources.getDimensionPixelOffset(R.dimen.spacing_start_1dp));
        paint.setColor(mResources.getColor(mColor));
        canvas.drawCircle(x, y, mR, paint);
    }

    /**
     * Draws custom circle for values of linear diagram
     *
     * @param canvas Canvas object of main View object
     * @param x      x coordinate
     * @param y      y coordinate
     * @param color  color of text
     * @param type   type of text
     */
    private void drawCustomText(Canvas canvas, String number, float x, float y, int color, int type) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mResources.getColor(color));
        paint.setStyle(Paint.Style.STROKE);
        if (type == 1) {
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            paint.setTextSize(mTextSize);
        } else {
            paint.setTextSize(mResources.getDimensionPixelOffset(R.dimen.text_size_smallest_12sp));
        }

        Rect textBounds = new Rect();
        paint.getTextBounds("99", 0, "99".length(), textBounds);
        canvas.drawText(number, x, y - textBounds.exactCenterY(), paint);
    }

    /**
     * Sets DataForLinearDiagram list, state, title objects.
     * Redraws main View object and starts animations
     *
     * @param data  DataForLinearDiagram list object
     * @param state state of tasks
     * @param title title of OxOy coordinate line
     */
    public void setData(List<DataForLinearDiagram> data, int state, String title) {
        mData = data;
        mState = state;
        mColor = R.color.colorPrimary;
        mDaysTitle = title;
        if (state == ConstantsManager.STATE_FLAG_DONE) {
            mColor = R.color.colorPrimaryLightTwo;
        }

        ValueAnimator animator1 = ValueAnimator.ofInt(0, mResources.getDimensionPixelOffset(R.dimen.spacing_smallest_3dp));
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mR = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator1.setDuration(300);

        ValueAnimator animator2 = ValueAnimator.ofInt(0, mResources.getDimensionPixelOffset(R.dimen.text_size_smallest_12sp));
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mTextSize = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator2.setDuration(300);

        ValueAnimator animator3 = ValueAnimator.ofInt(0, mResources.getDimensionPixelOffset(R.dimen.spacing_smallest_3dp));
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mK = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator3.setDuration(300);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2, animator3);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.start();
    }
}
