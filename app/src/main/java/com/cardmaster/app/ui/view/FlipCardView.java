package com.cardmaster.app.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FlipCardView extends FrameLayout {

    private View frontView;
    private View backView;
    private boolean isShowingFront = true;
    private boolean isFlipping = false;

    public FlipCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public FlipCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlipCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Set camera distance for smooth 3D perspective without warping
        float scale = getResources().getDisplayMetrics().density;
        setCameraDistance(8000 * scale);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() >= 2) {
            frontView = getChildAt(0);
            backView = getChildAt(1);
            frontView.setVisibility(VISIBLE);
            backView.setVisibility(GONE);
            backView.setRotationY(180f);
        }
    }

    public interface OnFlipListener {
        void onFlip(boolean isShowingFront);
    }

    private OnFlipListener onFlipListener;

    public void setOnFlipListener(OnFlipListener listener) {
        this.onFlipListener = listener;
    }

    public boolean isShowingFront() {
        return isShowingFront;
    }

    public void flip() {
        if (isFlipping || frontView == null || backView == null) return;
        isFlipping = true;

        final View currentVisible = isShowingFront ? frontView : backView;
        final View currentHidden = isShowingFront ? backView : frontView;

        // 1st half of flip: 0 to 90 degrees
        ObjectAnimator flipOut = ObjectAnimator.ofFloat(this, "rotationY", isShowingFront ? 0f : 180f, isShowingFront ? 90f : 270f);
        flipOut.setDuration(125);
        flipOut.setInterpolator(new AccelerateDecelerateInterpolator());

        // 2nd half of flip: 90 to 180 degrees
        final ObjectAnimator flipIn = ObjectAnimator.ofFloat(this, "rotationY", isShowingFront ? 270f : 90f, isShowingFront ? 360f : 0f);
        flipIn.setDuration(125);
        flipIn.setInterpolator(new AccelerateDecelerateInterpolator());

        flipOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentVisible.setVisibility(GONE);
                currentHidden.setVisibility(VISIBLE);
                isShowingFront = !isShowingFront;
                flipIn.start();
            }
        });

        flipIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setRotationY(0f);
                isFlipping = false;
                if (onFlipListener != null) {
                    onFlipListener.onFlip(isShowingFront);
                }
            }
        });

        flipOut.start();
    }

    public void resetToFront() {
        isShowingFront = true;
        setRotationY(0f);
        if (frontView != null) frontView.setVisibility(VISIBLE);
        if (backView != null) backView.setVisibility(GONE);
    }
}
