package com.apps.newstudio.tasklist.ui.views;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

public class CustomCircleDiagram extends View {

    private int mMaxValue;
    private int mFirstValue, mSecondValue;
    private int mPlusDegreesZero, mPlusDegreesFirst, mPlusDegreesSecond;
    private int mStrokeWidth;
    private int mZeroColor, mFirstColor, mSecondColor;
    private int mDuration;

    private Double mIndex;
    private int mTextSize;

    /**
     * Constructor for CustomCircleDiagram object, gets all needed values
     *
     * @param context      Context object
     * @param attributeSet AttributeSet object which contains all needed parameters for CustomCircleDiagram object
     */
    public CustomCircleDiagram(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.CustomCircleDiagram);
        mMaxValue = typedArray.getInt(R.styleable.CustomCircleDiagram_dia_max_value, 0);
        mFirstValue = typedArray.getInt(R.styleable.CustomCircleDiagram_dia_first_value, 0);
        mSecondValue = typedArray.getInt(R.styleable.CustomCircleDiagram_dia_second_value, 0);
        mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.CustomCircleDiagram_dia_stroke_width, 0);
        mZeroColor = typedArray.getColor(R.styleable.CustomCircleDiagram_dia_zero_color,
                TaskListApplication.getContext().getResources().getColor(R.color.grey_two));
        mFirstColor = typedArray.getColor(R.styleable.CustomCircleDiagram_dia_first_color,
                TaskListApplication.getContext().getResources().getColor(R.color.tr));
        mSecondColor = typedArray.getColor(R.styleable.CustomCircleDiagram_dia_second_color,
                TaskListApplication.getContext().getResources().getColor(R.color.tr));
        mDuration = typedArray.getInt(R.styleable.CustomCircleDiagram_dia_duration, 0);
        typedArray.recycle();
        calculateIndex();
        mPlusDegreesZero = getPlusDegrees(mMaxValue);
        mPlusDegreesFirst = getPlusDegrees(mFirstValue);
        mPlusDegreesSecond = mPlusDegreesFirst + getPlusDegrees(mSecondValue);
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
        if (getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec) >
                TaskListApplication.getContext().getResources().getDimensionPixelSize(R.dimen.big_size_240dp)) {
            setMeasuredDimension(TaskListApplication.getContext().getResources().getDimensionPixelSize(R.dimen.big_size_240dp),
                    TaskListApplication.getContext().getResources().getDimensionPixelSize(R.dimen.big_size_240dp));
        } else {
            setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                    getDefaultSize(getSuggestedMinimumHeight(), widthMeasureSpec));
        }
    }

    /**
     * Draws main View object
     *
     * @param canvas Canvas object of main View
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mStrokeWidth);
        paint.setAntiAlias(true);

        int Rx = getWidth() / 2;
        int radius = getWidth() / 2 - mStrokeWidth;

        paint.setColor(mZeroColor);
        RectF oval = new RectF((float) (Rx - radius), (float) (Rx - radius), (float) (Rx + radius), (float) (Rx + radius));
        canvas.drawArc(oval, -180, mPlusDegreesZero, false, paint);

        radius = getWidth() / 2 - 2 * mStrokeWidth -
                TaskListApplication.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_smaller_4dp);
        oval = new RectF((float) (Rx - radius), (float) (Rx - radius), (float) (Rx + radius), (float) (Rx + radius));

        int startAngle = -180;
        paint.setColor(mFirstColor);
        canvas.drawArc(oval, startAngle, mPlusDegreesFirst, false, paint);
        startAngle = startAngle + mPlusDegreesFirst;
        paint.setColor(mSecondColor);
        canvas.drawArc(oval, startAngle, mPlusDegreesSecond, false, paint);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(TaskListApplication.getContext().getResources().getColor(R.color.grey));
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(mTextSize);
        Rect textBounds = new Rect();
        paint.getTextBounds("99", 0, "99".length(), textBounds);
        if(mMaxValue==mFirstValue+mSecondValue) {
            canvas.drawText("" + mMaxValue, Rx, Rx - textBounds.exactCenterY(), paint);
        }else{
            canvas.drawText("0", Rx, Rx - textBounds.exactCenterY(), paint);
        }
    }

    /**
     * Calculate index to draw circle components
     */
    public void calculateIndex() {
        mIndex = (double) mMaxValue / 360;
    }

    /**
     * Getter for calculated value to draw circle components
     *
     * @param value input value
     * @return plus degrees value
     */
    public int getPlusDegrees(int value) {
        return (int) (value / mIndex);
    }

    /**
     * Setter for mMaxValue,  mFirstValue, mSecondValue objects.
     * Does all new calculations and redraws main View object.
     * Starts all animation effects
     *
     * @param maxValue    value of max value
     * @param firstValue  value of first value
     * @param secondValue value of second value
     */
    public void setValues(int maxValue, int firstValue, int secondValue) {
        mMaxValue = maxValue;
        mFirstValue = firstValue;
        mSecondValue = secondValue;
        calculateIndex();
        mPlusDegreesZero = getPlusDegrees(mMaxValue);
        mPlusDegreesFirst = getPlusDegrees(mFirstValue);
        mPlusDegreesSecond = mPlusDegreesFirst + getPlusDegrees(mSecondValue);

        ValueAnimator animator = ValueAnimator.ofInt(0, mPlusDegreesZero);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mPlusDegreesZero = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator animator2 = ValueAnimator.ofInt(0, mPlusDegreesFirst);
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mPlusDegreesFirst = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator animator3 = ValueAnimator.ofInt(0, mPlusDegreesSecond);
        animator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mPlusDegreesSecond = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator animator4 = ValueAnimator.ofInt(0, TaskListApplication.getContext().getResources()
                .getDimensionPixelOffset(R.dimen.spacing_small_16dp));
        animator4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mTextSize = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(mDuration);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(animator, animator2, animator3, animator4);
        animatorSet.start();
    }
}
