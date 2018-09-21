package github.hotstu.xuyimiao;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态添加到activity中
 * Created by hotstuNg on 2016/8/4.
 */
public class DynamicView extends View {
    private float density;
    int mTextWidth, mTextHeight; // Our calculated text bounds
    private Paint mTextPaint;
    private AnimatorListenerAdapter animDelegate;
    private Map<Animator, AnimInfo> animations;
    private Rect swapRect;


    public DynamicView(Context context) {
        this(context, null);
    }

    public DynamicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DynamicView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        density = getResources().getDisplayMetrics().density;
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.RED);

        swapRect = new Rect();
        animations = new HashMap<>();
        animDelegate = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                animations.remove(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animations.remove(animation);
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Map.Entry<Animator, AnimInfo> animatorAnimiInfoEntry : animations.entrySet()) {
            AnimInfo temp = animatorAnimiInfoEntry.getValue();
            if (temp.postion != null) {
                //辅助线
                //mPaint.setColor(Color.BLUE);
                //canvas.drawRect(temp.postion, mPaint);

                mTextPaint.setTextSize(temp.drawTextSize);
                mTextPaint.setColor(temp.textColor);
                mTextPaint.setAlpha((int) (temp.alpha * 255));
                mTextPaint.getTextBounds(temp.text, 0, temp.text.length(), swapRect);
                mTextWidth = (int) mTextPaint.measureText(temp.text); // Use measureText to calculate width
                mTextHeight = swapRect.height(); // Use height from getTextBounds()
                canvas.drawText(temp.text, // Text to display
                        temp.postion.centerX() - (mTextWidth / 2f),
                        temp.postion.centerY() + (mTextHeight / 2f),
                        mTextPaint
                );
            }
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void inspect(final View view, String text) {
        insepctInternal(view, text);
    }

    private void insepctInternal(View view, String text) {
        RectF origanlRect = new RectF();
        Rect temp = new Rect();
        view.getGlobalVisibleRect(temp);//在窗口中的位置
        origanlRect.set(temp);
        int[] location = new int[2];
        getLocationInWindow(location);//r的位置是相对window，但draw的xy是相对parent的，所以要变换回来
        //iew.getLocationOnScreen(location);
        //System.out.println("getLocationOnScreen "+ Arrays.toString(location));
        //getLocationInWindow 与getLocationOnScreen的区别是window没有铺满屏幕的时候不同
        origanlRect.offset(-location[0], -location[1]);

        AnimInfo info = new AnimInfo();
        info.postion = new RectF();
        info.matrix = new Matrix();
        info.orignal = origanlRect;
        info.text = text;
        info.textColor = Color.RED;
        info.textSize = 60;
        ValueAnimator scaleAnim = ValueAnimator.ofFloat(0, 1);
        scaleAnim.setDuration(300);
        scaleAnim.setInterpolator(new OvershootInterpolator());
        scaleAnim.addUpdateListener(info.scaleAction);
        ValueAnimator tranlateAnim = ValueAnimator.ofFloat(0, 1);
        tranlateAnim.setDuration(400);
        tranlateAnim.setInterpolator(new DecelerateInterpolator());
        tranlateAnim.addUpdateListener(info.translateAction);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(scaleAnim, tranlateAnim);
        animatorSet.addListener(animDelegate);
        animations.put(animatorSet, info);
        animatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (Map.Entry<Animator, AnimInfo> animatorAnimiInfoEntry : animations.entrySet()) {
            Animator a = animatorAnimiInfoEntry.getKey();
            a.removeAllListeners();
            a.cancel();
        }
        animations.clear();
    }

    public void detachFromWindow(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        rootView.removeView(this);
    }

    public static DynamicView attach2Window(Activity activity) {
        ViewGroup rootView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        DynamicView v = new DynamicView(activity);
        rootView.addView(v, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return v;
    }

    //inner class
    public class AnimInfo {
        RectF orignal;
        RectF postion;
        int textSize = 60;      //目标size
        int drawTextSize;       //动画中的textsize
        int textColor = Color.RED;      //目标textColor
        String text;
        Matrix matrix;
        float alpha = 1;


        final ValueAnimator.AnimatorUpdateListener scaleAction = new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                fraction = fraction * fraction;
                matrix.setScale(fraction, fraction, orignal.centerX(), orignal.centerY());
                matrix.mapRect(postion, orignal);
                drawTextSize = (int) (textSize * fraction);
                ViewCompat.postInvalidateOnAnimation(DynamicView.this);
            }
        };

        final ValueAnimator.AnimatorUpdateListener translateAction = new ValueAnimator.AnimatorUpdateListener() {
            float lastFraction = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                matrix.setTranslate(0, -(fraction - lastFraction) * 80 * density);
                lastFraction = fraction;
                matrix.mapRect(postion);
                //alpha = Math.max(0f, Math.min(1f, 1 - fraction));
                ViewCompat.postInvalidateOnAnimation(DynamicView.this);
            }
        };
    }

}
