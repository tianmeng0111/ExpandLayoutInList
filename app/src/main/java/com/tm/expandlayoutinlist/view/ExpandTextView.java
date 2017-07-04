package com.tm.expandlayoutinlist.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tm.expandlayoutinlist.entity.ExpandData;
import com.tm.expandlayoutinlist.R;

public class ExpandTextView extends FrameLayout{

	private static final String TAG = ExpandTextView.class.getSimpleName();

    private ValueAnimator expandAnimator;
    private ObjectAnimator rotateAnimator;
    private int duration = 200;

    private float minValue;
    private float maxValue;

    private float value;

    private TextView tvContent;
    private ImageView viewBtn;

    private boolean isExpand = false;
    
    private SparseArray<ExpandData> expandStatus;
    private ExpandData expandData;
    private int position;

    private boolean isAnimating = false;

    private int unexpandedLines = 2;


    public ExpandTextView(Context context) {
        super(context);
        init(context, null);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandTextView);
            if (typedArray != null) {
                unexpandedLines = typedArray.getInteger(R.styleable.ExpandTextView_unexpanded_lines, 2);
            }
        }
        initAnim();
    }

    private void initAnim() {
        expandAnimator = new ValueAnimator();
        expandAnimator.setDuration(duration);
        expandAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                value = (float) valueAnimator.getAnimatedValue();
//                tvContent.setMaxHeight((int) value);
                isAnimating = true;
                if (value >= expandStatus.get(position).getMaxValue() || value <= expandStatus.get(position).getMinValue()) {
                    isAnimating = false;
                    return;
                }
//                Log.e(TAG, "onAnimationUpdate: value--------------->>" + value);
                requestLayout();
            }

        });

        expandAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                isAnimating = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
//                isAnimating = true;
            }
        });

        rotateAnimator = new ObjectAnimator().setDuration(200);
        rotateAnimator.setProperty(View.ROTATION);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvContent = (TextView) getChildAt(0);
        viewBtn = (ImageView) getChildAt(1);
        rotateAnimator.setTarget(viewBtn);
        viewBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                setExpand(isExpand);
                expandData.setExpand(isExpand);
                if (expandStatus != null) {
                    expandStatus.put(position, expandData);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (expandStatus.get(position).getMinValue() == 0) {
            maxValue = getRealTextViewHeight(tvContent);
            int lineCount = tvContent.getLineCount();
            if (lineCount > unexpandedLines) {
                tvContent.setMaxLines(unexpandedLines);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                minValue = this.getMeasuredHeight();
                tvContent.setMaxLines(255);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            } else {
                minValue = maxValue;
            }
            expandData.setMaxValue(maxValue);
            expandData.setMinValue(minValue);
            expandStatus.put(position, expandData);
            if (expandStatus.get(position).isExpand()) {
                int spec = MeasureSpec.makeMeasureSpec((int) expandStatus.get(position).getMaxValue(), MeasureSpec.EXACTLY);
                setMeasuredDimension(widthMeasureSpec, spec);
                viewBtn.setRotation(180);
            } else {
                int spec = MeasureSpec.makeMeasureSpec((int) expandStatus.get(position).getMinValue(), MeasureSpec.EXACTLY);
                setMeasuredDimension(widthMeasureSpec, spec);
                viewBtn.setRotation(0);
            }
        } else {
            if (isAnimating) {
                int spec = MeasureSpec.makeMeasureSpec((int) value, MeasureSpec.EXACTLY);
                setMeasuredDimension(widthMeasureSpec, spec);
            } else {
                if (expandStatus.get(position).isExpand()) {
                    int spec = MeasureSpec.makeMeasureSpec((int) expandStatus.get(position).getMaxValue(), MeasureSpec.EXACTLY);
                    setMeasuredDimension(widthMeasureSpec, spec);
                    viewBtn.setRotation(180);
                } else {
                    int spec = MeasureSpec.makeMeasureSpec((int) expandStatus.get(position).getMinValue(), MeasureSpec.EXACTLY);
                    setMeasuredDimension(widthMeasureSpec, spec);
                    viewBtn.setRotation(0);
                }
            }
        }
        Log.e(TAG, "onMeasure: maxValue:" + maxValue);
        Log.e(TAG, "onMeasure: minValue:" + minValue);
        if (expandStatus.get(position).getMaxValue() == expandStatus.get(position).getMinValue()) {
            viewBtn.setVisibility(GONE);
        } else {
            viewBtn.setVisibility(VISIBLE);
        }
    }

    private static int getRealTextViewHeight(@NonNull TextView textView) {
        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }

    public void setExpand(boolean isExpand) {
        minValue = expandStatus.get(position).getMinValue();
        maxValue = expandStatus.get(position).getMaxValue();

        if (isExpand) {
            expandAnimator.setFloatValues(this.getHeight(), maxValue);
            rotateAnimator.setFloatValues(0, 180);
        } else {
            expandAnimator.setFloatValues(this.getHeight(), minValue);
            rotateAnimator.setFloatValues(180, 0);
        }
        expandAnimator.start();
        rotateAnimator.start();
    }

    /**
     *
     * @param text
     * @param expandStatus
     * @param position
     */
    public void setText(String text, SparseArray<ExpandData> expandStatus, int position) {
        this.expandStatus = expandStatus;
        this.expandData = expandStatus.get(position);
        this.isExpand = this.expandData.isExpand();
        this.position = position;
        tvContent.setText(text);
        this.requestLayout();
    }
}
