package com.apps.newstudio.tasklist.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.apps.newstudio.tasklist.R;
import com.apps.newstudio.tasklist.utils.TaskListApplication;

public class CustomCircleDiagram extends View {

    private int mMaxValue;
    private int mFirstValue, mSecondValue;
    private int mStrokeWidth;
    private int mZeroColor, mFirstColor, mSecondColor;

    private Double mIndex;


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
        typedArray.recycle();
        calculateIndex();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(mStrokeWidth);
        paint.setAntiAlias(true);

        int Rx = getWidth() / 2;
        int Radius = getWidth() / 2 - mStrokeWidth;

        paint.setColor(mZeroColor);
        RectF oval = new RectF((float) (Rx - Radius), (float) (Rx - Radius), (float) (Rx + Radius), (float) (Rx + Radius));
        canvas.drawOval(oval, paint);

        Radius = getWidth() / 2 - 2 * mStrokeWidth -
                TaskListApplication.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_smaller_4dp);
        oval = new RectF((float) (Rx - Radius), (float) (Rx - Radius), (float) (Rx + Radius), (float) (Rx + Radius));

        int startAngle = -180;
        int plusDegrees = (int) (mFirstValue / mIndex);
        paint.setColor(mFirstColor);
        canvas.drawArc(oval, startAngle, plusDegrees, false, paint);
        startAngle = startAngle + plusDegrees;
        plusDegrees = (int) (mSecondValue / mIndex);
        paint.setColor(mSecondColor);
        canvas.drawArc(oval, startAngle, plusDegrees, false, paint);
    }

    public void calculateIndex() {
        mIndex = (double) mMaxValue / 360;
    }

    public void setValues(int maxValue, int firstValue,int secondValue ) {
        mMaxValue = maxValue;
        mFirstValue = firstValue;
        mSecondValue = secondValue;
        calculateIndex();
        invalidate();
    }
}
