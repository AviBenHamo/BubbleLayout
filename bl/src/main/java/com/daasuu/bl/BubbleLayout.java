package com.daasuu.bl;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.view.ViewCompat;

/**
 * Bubble View for Android with custom stroke width and color, arrow size, position and direction.
 * Created by sudamasayuki on 16/04/04.
 */
public class BubbleLayout extends FrameLayout {

    public static float DEFAULT_STROKE_WIDTH = -1;

    private ArrowDirection mArrowDirection;
    private Bubble mBubble;

    private float mArrowWidth;
    private float mCornersRadius;
    private float mArrowHeight;
    private float mArrowPosition;
    private int mBubbleColor;
    private float mStrokeWidth;
    private int mStrokeColor;
    private boolean isLTR = true;
    public BubbleLayout(Context context) {
        this(context, null, 0);
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BubbleLayout);
        mArrowWidth = a.getDimension(R.styleable.BubbleLayout_bl_arrowWidth,
                convertDpToPixel(8, context));
        mArrowHeight = a.getDimension(R.styleable.BubbleLayout_bl_arrowHeight,
                convertDpToPixel(8, context));
        mCornersRadius = a.getDimension(R.styleable.BubbleLayout_bl_cornersRadius, 0);
        mArrowPosition = a.getDimension(R.styleable.BubbleLayout_bl_arrowPosition,
                convertDpToPixel(0, context));
        mBubbleColor = a.getColor(R.styleable.BubbleLayout_bl_bubbleColor, Color.WHITE);

        mStrokeWidth =
                a.getDimension(R.styleable.BubbleLayout_bl_strokeWidth, DEFAULT_STROKE_WIDTH);
        mStrokeColor = a.getColor(R.styleable.BubbleLayout_bl_strokeColor, Color.GRAY);

        int location = a.getInt(R.styleable.BubbleLayout_bl_arrowDirection, ArrowDirection.LEFT.getValue());
        mArrowDirection = ArrowDirection.fromInt(location);

        isLTR = !getResources().getBoolean(R.bool.is_rtl);


        a.recycle();
        initPadding();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        initDrawable(0, getWidth(), 0, getHeight());
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mBubble != null) mBubble.draw(canvas);
        super.dispatchDraw(canvas);
    }

    private void initDrawable(int left, int right, int top, int bottom) {
        if (right < left || bottom < top) return;

        RectF rectF = new RectF(left, top, right, bottom);
        float arrowPosition = mArrowPosition;

        ArrowDirection arrowDirection = mArrowDirection;
        if(isLTR ){
            if(mArrowDirection == ArrowDirection.START)
                 arrowDirection = ArrowDirection.LEFT;
            if(mArrowDirection == ArrowDirection.END)
                arrowDirection = ArrowDirection.RIGHT;
        } else {
            if(mArrowDirection == ArrowDirection.START)
                arrowDirection = ArrowDirection.RIGHT;
            if(mArrowDirection == ArrowDirection.END)
                arrowDirection = ArrowDirection.LEFT;
        }

        switch (mArrowDirection) {
            case LEFT:
            case RIGHT:
            case START:
            case END: {
                float center = (bottom - top) / 2f - mArrowHeight / 2;
                float validArrowPosition = getValidArrowPosition(center, mArrowPosition);
                arrowPosition = center + validArrowPosition;
                break;
            }
            case TOP:
            case BOTTOM:{
                float center = (right - left) / 2f - mArrowWidth;
                float validArrowPosition = getValidArrowPosition(center, mArrowPosition);

                if(isLTR) {
                    arrowPosition = center + validArrowPosition;
                } else {
                    arrowPosition = center - validArrowPosition;
                }
                break;
            }
            default:
                break;
        }
        mBubble = new Bubble(rectF, mArrowWidth, mCornersRadius, mArrowHeight, arrowPosition,
                mStrokeWidth, mStrokeColor, mBubbleColor, arrowDirection);
    }
    private float getValidArrowPosition(float center , float arrowPosition)  {
        float min = Math.min(center,Math.abs(arrowPosition));
        min -= mCornersRadius/2;
        if(arrowPosition < 0)
            min = min * -1;
        return min;
    }

    private void initPadding() {
        updatePadding(true);
    }

    private void resetPadding() {
        updatePadding(false);
    }

    /**
     * Consolidate the logic of resetPadding and the initPadding to one plase
     * @param add true to Add false to remove the Padding
     */
    private void updatePadding(Boolean add) {
        int addOrRemove = add ? 1 : -1;
        int paddingLeft = getPaddingLeft();
        int paddingStart = getPaddingStart();
        int paddingRight = getPaddingRight();
        int paddingEnd = getPaddingEnd();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        String lds = isLTR ? "LTR" : "RTL";
        Log.d("Avi before",
                "lds:" + lds
                        + ", mArrowDirection:" + mArrowDirection.name()
                        +  ", paddingLeft :" + paddingLeft
                        + ", paddingStart:" + paddingStart
                        + ", paddingRight:" + paddingRight
                        + ", paddingEnd:" + paddingEnd
                        + ", paddingTop:" + paddingTop
                        + ", paddingBottom:" + paddingBottom

        );

        switch (mArrowDirection) {
            case LEFT:
                paddingLeft += (mArrowWidth * addOrRemove);
                if(isLTR)
                    paddingStart += (mArrowWidth * addOrRemove);
                else
                    paddingEnd += (mArrowWidth * addOrRemove);
                break;
            case START:
                paddingLeft += (mArrowWidth * addOrRemove);
                paddingStart += (mArrowWidth * addOrRemove);
                break;
            case RIGHT:
                paddingRight += (mArrowWidth * addOrRemove);
                if(isLTR)
                    paddingEnd += (mArrowWidth * addOrRemove);
                else
                    paddingStart += (mArrowWidth * addOrRemove);
                break;
            case END:
                paddingRight += (mArrowWidth * addOrRemove);
                paddingEnd += (mArrowWidth * addOrRemove);
                break;
            case TOP:
                paddingTop += (mArrowHeight * addOrRemove);
                break;
            case BOTTOM:
                paddingBottom += (mArrowHeight * addOrRemove);
                break;
        }
        if (mStrokeWidth > 0) {
            paddingLeft += (mStrokeWidth * addOrRemove);
            paddingStart += (mStrokeWidth * addOrRemove);
            paddingRight += (mStrokeWidth * addOrRemove);
            paddingEnd += (mStrokeWidth * addOrRemove);
            paddingTop += (mStrokeWidth * addOrRemove);
            paddingBottom += (mStrokeWidth * addOrRemove);
        }
         Log.d("Avi after",
                 "lds:" + lds
                         + ", mArrowDirection:" + mArrowDirection.name()
                        +  ", paddingLeft :" + paddingLeft
                        + ", paddingStart:" + paddingStart
                        + ", paddingRight:" + paddingRight
                        + ", paddingEnd:" + paddingEnd
                        + ", paddingTop:" + paddingTop
                        + ", paddingBottom:" + paddingBottom

        );
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom);
    }

    static float convertDpToPixel(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public BubbleLayout setArrowDirection(ArrowDirection arrowDirection) {
        resetPadding();
        mArrowDirection = arrowDirection;
        initPadding();
        return this;
    }

    public BubbleLayout setArrowWidth(float arrowWidth) {
        resetPadding();
        mArrowWidth = arrowWidth;
        initPadding();
        return this;
    }

    public BubbleLayout setCornersRadius(float cornersRadius) {
        mCornersRadius = cornersRadius;
        requestLayout();
        return this;
    }

    public BubbleLayout setArrowHeight(float arrowHeight) {
        resetPadding();
        mArrowHeight = arrowHeight;
        initPadding();
        return this;
    }

    public BubbleLayout setArrowPosition(float arrowPosition) {
        resetPadding();
        mArrowPosition = arrowPosition;
        initPadding();
        return this;
    }

    public BubbleLayout setBubbleColor(int bubbleColor) {
        mBubbleColor = bubbleColor;
        requestLayout();
        return this;
    }

    public BubbleLayout setStrokeWidth(float strokeWidth) {
        resetPadding();
        mStrokeWidth = strokeWidth;
        initPadding();
        return this;
    }

    public BubbleLayout setStrokeColor(int strokeColor) {
        mStrokeColor = strokeColor;
        requestLayout();
        return this;
    }

    public ArrowDirection getArrowDirection() {
        return mArrowDirection;
    }

    public float getArrowWidth() {
        return mArrowWidth;
    }

    public float getCornersRadius() {
        return mCornersRadius;
    }

    public float getArrowHeight() {
        return mArrowHeight;
    }

    public float getArrowPosition() {
        return mArrowPosition;
    }

    public int getBubbleColor() {
        return mBubbleColor;
    }

    public float getStrokeWidth() {
        return mStrokeWidth;
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }
}
