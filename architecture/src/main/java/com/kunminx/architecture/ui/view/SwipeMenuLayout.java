package com.kunminx.architecture.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.kunminx.architecture.R;

/**
 * Created by zhangxutong .
 * Date: 16/04/24
 */
public class SwipeMenuLayout extends ViewGroup {
  private static final String TAG = "zxt/SwipeMenuLayout";

  private int mScaleTouchSlop;
  private int mMaxVelocity;
  private int mPointerId;
  private int mRightMenuWidths;
  private int mLimit;
  private View mContentView;
  private final PointF mLastP = new PointF();
  private boolean isUnMoved = true;
  private final PointF mFirstP = new PointF();
  private boolean isUserSwiped;
  @SuppressLint("StaticFieldLeak")
  private static SwipeMenuLayout mViewCache;
  private static boolean isTouching;
  private VelocityTracker mVelocityTracker;
  private boolean isSwipeEnable;
  private boolean isIos;
  private boolean iosInterceptFlag;
  private boolean isLeftSwipe;

  public SwipeMenuLayout(Context context) {
    this(context, null);
  }

  public SwipeMenuLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs, defStyleAttr);
  }

  public boolean isSwipeEnable() {
    return isSwipeEnable;
  }

  public void setSwipeEnable(boolean swipeEnable) {
    isSwipeEnable = swipeEnable;
  }

  public boolean isIos() {
    return isIos;
  }

  public SwipeMenuLayout setIos(boolean ios) {
    isIos = ios;
    return this;
  }

  public boolean isLeftSwipe() {
    return isLeftSwipe;
  }

  public SwipeMenuLayout setLeftSwipe(boolean leftSwipe) {
    isLeftSwipe = leftSwipe;
    return this;
  }

  public static SwipeMenuLayout getViewCache() {
    return mViewCache;
  }

  private void init(Context context, AttributeSet attrs, int defStyleAttr) {
    mScaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    mMaxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    isSwipeEnable = true;
    isIos = true;
    isLeftSwipe = true;
    TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwipeMenuLayout, defStyleAttr, 0);
    int count = ta.getIndexCount();
    for (int i = 0; i < count; i++) {
      int attr = ta.getIndex(i);
      if (attr == R.styleable.SwipeMenuLayout_swipeEnable) {
        isSwipeEnable = ta.getBoolean(attr, true);
      } else if (attr == R.styleable.SwipeMenuLayout_ios) {
        isIos = ta.getBoolean(attr, true);
      } else if (attr == R.styleable.SwipeMenuLayout_leftSwipe) {
        isLeftSwipe = ta.getBoolean(attr, true);
      }
    }
    ta.recycle();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    setClickable(true);
    mRightMenuWidths = 0;
    int height = 0;
    int contentWidth = 0;
    int childCount = getChildCount();
    final boolean measureMatchParentChildren = MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY;
    boolean isNeedMeasureChildHeight = false;
    for (int i = 0; i < childCount; i++) {
      View childView = getChildAt(i);
      childView.setClickable(true);
      if (childView.getVisibility() != GONE) {
        measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        final MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
        height = Math.max(height, childView.getMeasuredHeight());
        if (measureMatchParentChildren && lp.height == LayoutParams.MATCH_PARENT) {
          isNeedMeasureChildHeight = true;
        }
        if (i > 0) {
          mRightMenuWidths += childView.getMeasuredWidth();
        } else {
          mContentView = childView;
          contentWidth = childView.getMeasuredWidth();
        }
      }
    }
    setMeasuredDimension(getPaddingLeft() + getPaddingRight() + contentWidth,
            height + getPaddingTop() + getPaddingBottom());
    mLimit = mRightMenuWidths * 4 / 10;
    if (isNeedMeasureChildHeight) {
      forceUniformHeight(childCount, widthMeasureSpec);
    }
  }

  @Override
  public LayoutParams generateLayoutParams(AttributeSet attrs) {
    return new MarginLayoutParams(getContext(), attrs);
  }

  private void forceUniformHeight(int count, int widthMeasureSpec) {
    int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(),
            MeasureSpec.EXACTLY);
    for (int i = 0; i < count; ++i) {
      final View child = getChildAt(i);
      if (child.getVisibility() != GONE) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        if (lp.height == LayoutParams.MATCH_PARENT) {
          int oldWidth = lp.width;
          lp.width = child.getMeasuredWidth();
          measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
          lp.width = oldWidth;
        }
      }
    }
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int childCount = getChildCount();
    int left = getPaddingLeft();
    int right = getPaddingLeft();
    for (int i = 0; i < childCount; i++) {
      View childView = getChildAt(i);
      if (childView.getVisibility() != GONE) {
        if (i == 0) {
          childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
          left = left + childView.getMeasuredWidth();
        } else {
          if (isLeftSwipe) {
            childView.layout(left, getPaddingTop(), left + childView.getMeasuredWidth(), getPaddingTop() + childView.getMeasuredHeight());
            left = left + childView.getMeasuredWidth();
          } else {
            childView.layout(right - childView.getMeasuredWidth(), getPaddingTop(), right, getPaddingTop() + childView.getMeasuredHeight());
            right = right - childView.getMeasuredWidth();
          }
        }
      }
    }
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    if (isSwipeEnable) {
      acquireVelocityTracker(ev);
      final VelocityTracker verTracker = mVelocityTracker;
      switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
          isUserSwiped = false;
          isUnMoved = true;
          iosInterceptFlag = false;
          if (isTouching) return false;
          else isTouching = true;
          mLastP.set(ev.getRawX(), ev.getRawY());
          mFirstP.set(ev.getRawX(), ev.getRawY());
          if (mViewCache != null) {
            if (mViewCache != this) {
              mViewCache.smoothClose();
              iosInterceptFlag = isIos;
            }
            getParent().requestDisallowInterceptTouchEvent(true);
          }
          mPointerId = ev.getPointerId(0);
          break;
        case MotionEvent.ACTION_MOVE:
          if (iosInterceptFlag) break;
          float gap = mLastP.x - ev.getRawX();
          if (Math.abs(gap) > 10 || Math.abs(getScrollX()) > 10) {
            getParent().requestDisallowInterceptTouchEvent(true);
          }
          if (Math.abs(gap) > mScaleTouchSlop) isUnMoved = false;
          scrollBy((int) (gap), 0);
          if (isLeftSwipe) {
            if (getScrollX() < 0) scrollTo(0, 0);
            if (getScrollX() > mRightMenuWidths) scrollTo(mRightMenuWidths, 0);
          } else {
            if (getScrollX() < -mRightMenuWidths) scrollTo(-mRightMenuWidths, 0);
            if (getScrollX() > 0) scrollTo(0, 0);
          }
          mLastP.set(ev.getRawX(), ev.getRawY());
          break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
          if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) {
            isUserSwiped = true;
          }

          if (!iosInterceptFlag) {
            verTracker.computeCurrentVelocity(1000, mMaxVelocity);
            final float velocityX = verTracker.getXVelocity(mPointerId);
            if (Math.abs(velocityX) > 1000) {
              if (velocityX < -1000) {
                if (isLeftSwipe) smoothExpand();
                else smoothClose();
              } else {
                if (isLeftSwipe) smoothClose();
                else smoothExpand();
              }
            } else {
              if (Math.abs(getScrollX()) > mLimit) smoothExpand();
              else smoothClose();
            }
          }
          releaseVelocityTracker();
          isTouching = false;
          break;
        default:
          break;
      }
    }
    return super.dispatchTouchEvent(ev);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    if (isSwipeEnable) {
      switch (ev.getAction()) {
        case MotionEvent.ACTION_MOVE:
          if (Math.abs(ev.getRawX() - mFirstP.x) > mScaleTouchSlop) return true;
          break;
        case MotionEvent.ACTION_UP:
          if (isLeftSwipe) {
            if (getScrollX() > mScaleTouchSlop) {
              if (ev.getX() < getWidth() - getScrollX()) {
                if (isUnMoved) smoothClose();
                return true;
              }
            }
          } else {
            if (-getScrollX() > mScaleTouchSlop) {
              if (ev.getX() > -getScrollX()) {
                if (isUnMoved) smoothClose();
                return true;
              }
            }
          }
          if (isUserSwiped) return true;
          break;
      }
      if (iosInterceptFlag) {
        return true;
      }
    }
    return super.onInterceptTouchEvent(ev);
  }

  private ValueAnimator mExpandAnim, mCloseAnim;

  private boolean isExpand;

  public void smoothExpand() {
    mViewCache = SwipeMenuLayout.this;
    if (null != mContentView) {
      mContentView.setLongClickable(false);
    }

    cancelAnim();
    mExpandAnim = ValueAnimator.ofInt(getScrollX(), isLeftSwipe ? mRightMenuWidths : -mRightMenuWidths);
    mExpandAnim.addUpdateListener(animation -> scrollTo((Integer) animation.getAnimatedValue(), 0));
    mExpandAnim.setInterpolator(new OvershootInterpolator());
    mExpandAnim.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        isExpand = true;
      }
    });
    mExpandAnim.setDuration(300).start();
  }

  private void cancelAnim() {
    if (mCloseAnim != null && mCloseAnim.isRunning()) {
      mCloseAnim.cancel();
    }
    if (mExpandAnim != null && mExpandAnim.isRunning()) {
      mExpandAnim.cancel();
    }
  }

  public void smoothClose() {
    mViewCache = null;
    if (null != mContentView) {
      mContentView.setLongClickable(true);
    }

    cancelAnim();
    mCloseAnim = ValueAnimator.ofInt(getScrollX(), 0);
    mCloseAnim.addUpdateListener(animation -> scrollTo((Integer) animation.getAnimatedValue(), 0));
    mCloseAnim.setInterpolator(new AccelerateInterpolator());
    mCloseAnim.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        isExpand = false;

      }
    });
    mCloseAnim.setDuration(300).start();
  }

  private void acquireVelocityTracker(final MotionEvent event) {
    if (null == mVelocityTracker) {
      mVelocityTracker = VelocityTracker.obtain();
    }
    mVelocityTracker.addMovement(event);
  }

  private void releaseVelocityTracker() {
    if (null != mVelocityTracker) {
      mVelocityTracker.clear();
      mVelocityTracker.recycle();
      mVelocityTracker = null;
    }
  }

  @Override
  protected void onDetachedFromWindow() {
    if (this == mViewCache) {
      mViewCache.smoothClose();
      mViewCache = null;
    }
    super.onDetachedFromWindow();
  }

  @Override
  public boolean performLongClick() {
    if (Math.abs(getScrollX()) > mScaleTouchSlop) {
      return false;
    }
    return super.performLongClick();
  }

  public void quickClose() {
    if (this == mViewCache) {
      cancelAnim();
      mViewCache.scrollTo(0, 0);
      mViewCache = null;
    }
  }
}
