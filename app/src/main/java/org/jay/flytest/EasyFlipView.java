package org.jay.flytest;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

/**
 * A quick and easy flip view through which you can create views with two sides like credit cards,
 * poker cards, flash cards etc.
 */
public class EasyFlipView extends FrameLayout {

    public static final int DEFAULT_FLIP_DURATION = 400;

    public enum FlipState {
        FRONT_SIDE,
        BACK_SIDE
    }

    private AnimatorSet mSetRightOut;

    private AnimatorSet mSetLeftIn;

    private Rotate3dAnimation mFront3DRotation;

    private Rotate3dAnimation mBack3DRotation;

    private boolean mIsBackVisible = false;

    private boolean mIs3D = true;

    private View mFrontLayout;

    private View mBackLayout;

    private boolean flipOnTouch;

    private int flipDuration;

    private boolean flipEnabled;

    private Context context;

    private float x1;

    private float y1;

    private int centerX;

    private int centerY;

    private int depthZ = 500;

    private FlipState mFlipState = FlipState.FRONT_SIDE;

    public EasyFlipView(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    public EasyFlipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Setting Defaul Values
        flipOnTouch = true;
        flipDuration = DEFAULT_FLIP_DURATION;
        flipEnabled = true;

        // Check for the attributes
        if (attrs != null) {
            // Attribute initialization
            final TypedArray attrArray = context
                .obtainStyledAttributes(attrs, R.styleable.easy_flip_view, 0, 0);
            try {
                flipOnTouch = attrArray.getBoolean(R.styleable.easy_flip_view_flipOnTouch, true);
                flipDuration = attrArray
                    .getInt(R.styleable.easy_flip_view_flipDuration, DEFAULT_FLIP_DURATION);
                flipEnabled = attrArray.getBoolean(R.styleable.easy_flip_view_flipEnabled, true);
            } finally {
                attrArray.recycle();
            }

        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() > 2) {
            throw new IllegalStateException("EasyFlipView can host only two direct children!");
        }

        findViews();
        loadAnimations();
        changeCameraDistance();
    }

    private void findViews() {
        mFrontLayout = getChildAt(1);
        mBackLayout = getChildAt(0);

        mFlipState = FlipState.FRONT_SIDE;
        if (!isFlipOnTouch()) {
            mFrontLayout.setVisibility(VISIBLE);
            mBackLayout.setVisibility(GONE);
        }
    }

    private void loadAnimations() {
        if(mIs3D){
            mFront3DRotation=generate3dRotation(mBackLayout,0,90,true);
            mFront3DRotation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    mBackLayout.setVisibility(View.GONE);
                    mFrontLayout.setVisibility(View.VISIBLE);
                    Rotate3dAnimation rotate3dAnimation=generate3dRotation(mFrontLayout,270,360,false);
                    mFrontLayout.startAnimation(rotate3dAnimation);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mBack3DRotation=generate3dRotation(mBackLayout,360,270,true);
            mBack3DRotation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    mFrontLayout.setVisibility(View.GONE);
                    mBackLayout.setVisibility(View.VISIBLE);
                    Rotate3dAnimation rotate3dAnimation=generate3dRotation(mBackLayout,90,0,false);
                    mBackLayout.startAnimation(rotate3dAnimation);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }else{
            mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, R.animator.animation_flip_out);
            mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this.context, R.animator.animation_flip_in);
            mSetRightOut.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {

                    if (mFlipState == FlipState.FRONT_SIDE) {
                        mBackLayout.setVisibility(GONE);
                        mFrontLayout.setVisibility(VISIBLE);
                    } else {
                        mBackLayout.setVisibility(VISIBLE);
                        mFrontLayout.setVisibility(GONE);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }

//        setFlipDuration(flipDuration);
    }

    private Rotate3dAnimation generate3dRotation(View view,float fromDegree,float toDegree,boolean reverse) {
        final float centerX = view.getWidth() / 2.0f;
        final float centerY = view.getHeight() / 2.0f;
        Rotate3dAnimation rotate3dAnimation = new Rotate3dAnimation(context, fromDegree, toDegree, centerX, centerY, depthZ, reverse);
        rotate3dAnimation.setDuration(flipDuration);
        rotate3dAnimation.setFillAfter(true);
        rotate3dAnimation.setInterpolator(new DecelerateInterpolator());
        return  rotate3dAnimation;
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;
        mFrontLayout.setCameraDistance(scale);
        mBackLayout.setCameraDistance(scale);
    }

    /**
     * Play the animation of flipping and flip the view for one side!
     */
    public void flipTheView() {
        if (!flipEnabled)
            return;
        if(mIs3D){
            if (mFront3DRotation.hasStarted() && !mFront3DRotation.hasEnded()) {
                return;
            }
            if (mBack3DRotation.hasStarted() && !mBack3DRotation.hasEnded()) {
                return;
            }
            if (mFlipState == FlipState.FRONT_SIDE) {
                // From front to back
                mFrontLayout.startAnimation(mFront3DRotation);
                mFlipState = FlipState.BACK_SIDE;
            } else {
                // from back to front
                mBackLayout.startAnimation(mBack3DRotation);
                mFlipState = FlipState.FRONT_SIDE;
            }
        }else{
            if (mSetRightOut.isRunning() || mSetLeftIn.isRunning())
                return;

            mBackLayout.setVisibility(VISIBLE);
            mFrontLayout.setVisibility(VISIBLE);

            if (mFlipState == FlipState.FRONT_SIDE) {
                // From front to back
                mSetRightOut.setTarget(mFrontLayout);
                mSetLeftIn.setTarget(mBackLayout);
                mSetRightOut.start();
                mSetLeftIn.start();
                mFlipState = FlipState.BACK_SIDE;
            } else {
                // from back to front
                mSetRightOut.setTarget(mBackLayout);
                mSetLeftIn.setTarget(mFrontLayout);
                mSetRightOut.start();
                mSetLeftIn.start();
                mFlipState = FlipState.FRONT_SIDE;
            }
        }

    }

    /**
     * Flip the view for one side with or without animation.
     *
     * @param withAnimation true means flip view with animation otherwise without animation.
     */
    public void flipTheView(boolean withAnimation) {
        if (!withAnimation) {
            mSetLeftIn.setDuration(0);
            mSetRightOut.setDuration(0);
            boolean oldFlipEnabled = flipEnabled;
            flipEnabled = true;

            flipTheView();

            mSetLeftIn.setDuration(flipDuration);
            mSetRightOut.setDuration(flipDuration);
            flipEnabled = oldFlipEnabled;
        } else {
            flipTheView();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isEnabled() && flipOnTouch) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x1 = event.getX();
                    y1 = event.getY();
                    return true;
                case MotionEvent.ACTION_UP:
                    float x2 = event.getX();
                    float y2 = event.getY();
                    float dx = x2 - x1;
                    float dy = y2 - y1;
                    float MAX_CLICK_DISTANCE = 0.5f;
                    if ((dx >= 0 && dx < MAX_CLICK_DISTANCE) && (dy >= 0 && dy <
                        MAX_CLICK_DISTANCE)) {
                        flipTheView();
                    }
                    return true;
            }
        } else {
            return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    /**
     * Whether view is set to flip on touch or not.
     *
     * @return true or false
     */
    public boolean isFlipOnTouch() {
        return flipOnTouch;
    }

    /**
     * Set whether view should be flipped on touch or not!
     *
     * @param flipOnTouch value (true or false)
     */
    public void setFlipOnTouch(boolean flipOnTouch) {
        this.flipOnTouch = flipOnTouch;
    }

    /**
     * Returns duration of flip in milliseconds!
     *
     * @return duration in milliseconds
     */
    public int getFlipDuration() {
        return flipDuration;
    }

    /**
     * Sets the flip duration (in milliseconds)
     *
     * @param flipDuration duration in milliseconds
     */
    public void setFlipDuration(int flipDuration) {
        this.flipDuration = flipDuration;

        //mSetRightOut.setDuration(flipDuration);
        mSetRightOut.getChildAnimations().get(0).setDuration(flipDuration);
        mSetRightOut.getChildAnimations().get(1).setStartDelay(flipDuration / 2);

        //mSetLeftIn.setDuration(flipDuration);
        mSetLeftIn.getChildAnimations().get(1).setDuration(flipDuration);
        mSetLeftIn.getChildAnimations().get(2).setStartDelay(flipDuration / 2);
    }

    /**
     * Returns whether flip is enabled or not!
     *
     * @return true or false
     */
    public boolean isFlipEnabled() {
        return flipEnabled;
    }

    /**
     * Enable / Disable flip view.
     *
     * @param flipEnabled true or false
     */
    public void setFlipEnabled(boolean flipEnabled) {
        this.flipEnabled = flipEnabled;
    }

    /**
     * Returns which flip state is currently on of the flip view.
     *
     * @return current state of flip view
     */
    public FlipState getCurrentFlipState() {
        return mFlipState;
    }

    /**
     * Returns true if the front side of flip view is visible.
     *
     * @return true if the front side of flip view is visible.
     */
    public boolean isFrontSide() {
        return (mFlipState == FlipState.FRONT_SIDE);
    }

    /**
     * Returns true if the back side of flip view is visible.
     *
     * @return true if the back side of flip view is visible.
     */
    public boolean isBackSide() {
        return (mFlipState == FlipState.BACK_SIDE);
    }
}
