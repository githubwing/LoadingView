package com.wingsofts.loadingview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by wing on 16/9/9.
 */
public class MaterialLoadingView extends View {

  private int mWidth;
  private int mHeight;
  private Paint mPaint;
  private Path mPath;
  private int mColor = Color.WHITE;
  private int mMaxRadius = 30;
  //旋转的角度
  private float mDegrees;

  //两个小球的偏移量
  private float mOffset;

  private float mMaxOffset = 100;

  public MaterialLoadingView(Context context) {
    this(context, null);
  }

  public MaterialLoadingView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public MaterialLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    mPaint = new Paint();
    mPaint.setColor(mColor);
    mPaint.setAntiAlias(true);
    mPath = new Path();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    mWidth = MeasureSpec.getSize(widthMeasureSpec);
    mHeight = MeasureSpec.getSize(heightMeasureSpec);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    canvas.rotate(mDegrees += 3, mWidth / 2, mHeight / 2);

    float present = mDegrees / 360;
    if (present < 0.5) {
      mOffset = mMaxOffset * present;
    } else {
      mOffset = mMaxOffset * (1 - present);
    }
    drawCircle(canvas, present);
    if (present <= 0.37 || present >= 0.63) drawPath(canvas, present);
    if (mDegrees == 360) {
      mDegrees = 0;
    }
    invalidate();
  }

  private void drawPath(Canvas canvas, float present) {
    mPath.reset();
    mPath.moveTo(mWidth / 2 - mMaxRadius, mHeight / 2 - mOffset);
    mPath.lineTo(mWidth / 2 + mMaxRadius, mHeight / 2 - mOffset);

    float supportOffset = -30;

    if (present < 0.25) { //两个球相交
      supportOffset = 30;
    } else if (present >= 0.25 && present < 0.375f) {
      Log.e("present", present + "");
      supportOffset = -(480 * present - 150f);
    } else if (present > 0.625) {   //开始缩小

      supportOffset = (480 * present - 330f);
      if (present > 0.75) {  //两个球开始相交
        supportOffset = 30;
      }
      //supportOffset = 30;
    }

    Log.e("wing", supportOffset + "");

    mPath.quadTo(mWidth / 2 + supportOffset, mHeight / 2, mWidth / 2 + mMaxRadius,
        mHeight / 2 + mOffset);
    mPath.lineTo(mWidth / 2 - mMaxRadius, mHeight / 2 + mOffset);
    mPath.quadTo(mWidth / 2 - supportOffset, mHeight / 2, mWidth / 2 - mMaxRadius,
        mHeight / 2 - mOffset);
    mPath.close();
    mPaint.setStyle(Paint.Style.FILL);
    canvas.drawPath(mPath, mPaint);
  }

  private void drawCircle(Canvas canvas, float present) {

    canvas.drawCircle(mWidth / 2, mHeight / 2 - mOffset, mMaxRadius, mPaint);
    canvas.drawCircle(mWidth / 2, mHeight / 2 + mOffset, mMaxRadius, mPaint);
  }

}
