package com.wingsofts.loadingview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/1/8.
 */
public class LoadingView extends View {
    private int mWidth;
    private int mHeight;
    private Context mContext;

    //横线变对勾的百分比
    private int mLinePercent;

    public void start() {
        if (isDrawing == false) {
            canStartDraw = true;
            isRiseDone = false;
            mRisePercent = 0;
            mLineShrinkPercent = 0;
            mCirclePercent = 0;
            mPathPercent = 0;
            mLinePercent = 0;
            invalidate();
        }
    }

    //标记是否可以开始动画
    private boolean canStartDraw = true;

    private int mRisePercent = 0;

    //竖线缩短的百分比
    private int mLineShrinkPercent = 0;

    //圆形进度百分比
    private int mCirclePercent = 0;

    //标记上升是否完成
    private boolean isRiseDone = false;

    //对勾变形的百分比
    private int mPathPercent = 0;

    //判断是不是正在draw
    private boolean isDrawing = false;

    private boolean isPathToLine = false;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = 200;
        }


        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = 200;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint();

        Path path = new Path();
        p.setColor(Color.parseColor("#2EA4F2"));
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(8);
        p.setAntiAlias(true);

        //百分比弧的矩形
        RectF rectF = new RectF(5, 5, mWidth - 5, mHeight - 5);

        //绘制圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2 - 5, p);


        if (canStartDraw) {
            isDrawing = true;
            //开始变形
            p.setColor(Color.WHITE);

            //如果小于95 就继续缩短。 95是微调值 和point大小相等
            if (mLineShrinkPercent < 95) {
                //线段逐渐缩短（终点为mWidth/2,mHeight/2）
                float tmp = (mWidth / 2 - mHeight / 4) * mLineShrinkPercent / 100;
                canvas.drawLine(mWidth / 2, mHeight / 4 + tmp, mWidth / 2, mHeight * 0.75f - tmp, p);
                mLineShrinkPercent += 5;
            } else {
                //path变成直线
                isPathToLine = true;
                if (mPathPercent < 100) {

                    path.moveTo(mWidth / 4, mHeight * 0.5f);
                    path.lineTo(mWidth / 2, mHeight * 0.75f - mPathPercent / 100f * 0.25f * mHeight);
                    path.lineTo(mWidth * 0.75f, mHeight * 0.5f);
                    canvas.drawPath(path, p);
                    mPathPercent += 5;

                    //在变成直线的过程中这个点一直存在
                    canvas.drawCircle(mWidth / 2, mHeight / 2,2.5f, p);
                } else {
                    //绘制把点上弹的直线

                    //画上升的点
                    if (mRisePercent < 100) {

                        //在点移动到圆弧上的时候 线是一直存在的
                        canvas.drawLine(mWidth / 4, mHeight * 0.5f, mWidth * 0.75f, mHeight * 0.5f, p);

                        canvas.drawCircle(mWidth / 2, mHeight / 2 - mHeight / 2 * mRisePercent / 100 + 5,2.5f, p);

                        mRisePercent += 5;
                    } else {
                        //上升的点最终的位置
                        canvas.drawPoint(mWidth / 2, 5, p);
                        isRiseDone = true;

                        //改变对勾形状
                        if (mLinePercent < 100) {

                            path.moveTo(mWidth / 4, mHeight * 0.5f);
                            path.lineTo(mWidth / 2, mHeight * 0.5f+ mLinePercent/100f * mHeight * 0.25f);
                            path.lineTo(mWidth * 0.75f, mHeight * 0.5f - mLinePercent / 100f * mHeight * 0.3f);
                            canvas.drawPath(path, p);
                            mLinePercent += 5;

                            //动态绘制圆形百分比
                            if (mCirclePercent < 100) {
                                canvas.drawArc(rectF, 270, -mCirclePercent / 100.0f * 360, false, p);
                                mCirclePercent += 5;
                            }

                        } else {
                            //绘制最终的path
                            path.moveTo(mWidth / 4, mHeight * 0.5f);
                            path.lineTo(mWidth / 2, mHeight * 0.75f);
                            path.lineTo(mWidth * 0.75f, mHeight * 0.3f);
                            canvas.drawPath(path, p);
//                            绘制最终的圆
                            canvas.drawArc(rectF, 270, -360, false, p);

                            isDrawing = false;

                        }
//                        }
                    }


                }


            }

            if (!isPathToLine) {
                path.moveTo(mWidth / 4, mHeight * 0.5f);
                path.lineTo(mWidth / 2, mHeight * 0.75f);
                path.lineTo(mWidth * 0.75f, mHeight * 0.5f);
                canvas.drawPath(path, p);
            }

        } else {
            //绘制静态箭头
            p.setColor(Color.WHITE);
            canvas.drawLine(mWidth / 2, mHeight / 4, mWidth / 2, mHeight * 0.75f, p);
//            Path path = new Path();
            path.moveTo(mWidth / 4, mHeight * 0.5f);
            path.lineTo(mWidth / 2, mHeight * 0.75f);
            path.lineTo(mWidth * 0.75f, mHeight * 0.5f);
            canvas.drawPath(path, p);
        }


        postInvalidateDelayed(10);
        super.onDraw(canvas);
    }


}
