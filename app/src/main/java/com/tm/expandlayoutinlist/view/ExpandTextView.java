package com.tm.expandlayoutinlist.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tm.expandlayoutinlist.R;
import com.tm.expandlayoutinlist.entity.ExpandData;

import java.util.List;

/**
 * Created by Tian on 2017/7/3.
 */
public class ExpandTextView extends FrameLayout{

	private static final String TAG = ExpandTextView.class.getSimpleName();

    private ValueAnimator expandAnimator;
    private ObjectAnimator rotateAnimator;
    private static final int DURATION = 200;

    private float minValue;
    private float maxValue;

    private float btnHeight = 0;

    private float value;

    private TextView tvContent;
    private ImageView viewBtn;

    private boolean isExpand = false;

    private List<String> list;
    private SparseArray<ExpandData> expandStatus;
    private ExpandData expandData;
    private int position;

    private boolean isAnimating = false;

    private int unexpandedLines = 2;

    private float twoTextWidth = 0;


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
        expandAnimator.setDuration(DURATION);
        expandAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                value = (float) valueAnimator.getAnimatedValue();
                isAnimating = true;
                if (expandStatus.get(position).isLineFeed()) {
                    if (value >= (expandStatus.get(position).getMaxValue() + btnHeight) || value <= expandStatus.get(position).getMinValue()) {
                        isAnimating = false;
                        return;
                    }
                } else {
                    if (value >= expandStatus.get(position).getMaxValue() || value <= expandStatus.get(position).getMinValue()) {
                        isAnimating = false;
                        return;
                    }
                }
                requestLayout();
            }

        });

        expandAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (expandStatus.get(position).isExpand()) {
                    tvContent.setText(list.get(position));
                } else {
                    tvContent.setText(expandStatus.get(position).getMinString());
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });

        rotateAnimator = new ObjectAnimator().setDuration(DURATION);
        rotateAnimator.setProperty(View.ROTATION);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvContent = (TextView) getChildAt(0);
        twoTextWidth = tvContent.getPaint().measureText("测试");
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
                int spec;
                if (expandStatus.get(position).isLineFeed()) {
                    spec = MeasureSpec.makeMeasureSpec((int) (expandStatus.get(position).getMaxValue() + btnHeight), MeasureSpec.EXACTLY);
                } else {
                    spec = MeasureSpec.makeMeasureSpec((int) expandStatus.get(position).getMaxValue(), MeasureSpec.EXACTLY);
                }
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
                    int spec;
                    if (expandStatus.get(position).isLineFeed()) {
                        spec = MeasureSpec.makeMeasureSpec((int) (expandStatus.get(position).getMaxValue() + btnHeight), MeasureSpec.EXACTLY);
                    } else {
                        spec = MeasureSpec.makeMeasureSpec((int) expandStatus.get(position).getMaxValue(), MeasureSpec.EXACTLY);
                    }
                    setMeasuredDimension(widthMeasureSpec, spec);
                    viewBtn.setRotation(180);
                } else {
                    int spec = MeasureSpec.makeMeasureSpec((int) expandStatus.get(position).getMinValue(), MeasureSpec.EXACTLY);
                    setMeasuredDimension(widthMeasureSpec, spec);
                    viewBtn.setRotation(0);
                }
            }
        }
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
        boolean lineFeed = expandStatus.get(position).isLineFeed();

        if (isExpand) {
            if (lineFeed) {
                expandAnimator.setFloatValues(this.getHeight(), maxValue + btnHeight);
            } else {
                expandAnimator.setFloatValues(this.getHeight(), maxValue);
            }
            rotateAnimator.setFloatValues(0, 180);
        } else {
            expandAnimator.setFloatValues(this.getHeight(), minValue);
            rotateAnimator.setFloatValues(180, 0);
        }
        expandAnimator.start();
        rotateAnimator.start();
    }

    /**
     * use in list
     * @param list
     * @param expandStatus
     * @param position
     */
    public void setText(final List<String> list, final SparseArray<ExpandData> expandStatus, final int position) {
        this.list = list;
        this.expandStatus = expandStatus;
        this.expandData = expandStatus.get(position);
        this.isExpand = this.expandData.isExpand();
        this.position = position;
        tvContent.setText(list.get(position));
        tvContent.post(new Runnable() {
            @Override
            public void run() {
                if (tvContent.getLineCount() > 3) {
                    int lineEnd = tvContent.getLayout().getLineEnd(2);
                    Log.e(TAG, "run: lineEnd:" + lineEnd + ";position:" + position);
                    if (lineEnd > 0 && lineEnd <= list.get(position).length()) {
                        String substring = list.get(position).substring(0, lineEnd - 3);
                        substring += "...";
                        expandData.setMinString(substring);
                    }
                } else {
                    expandData.setMinString(null);
                }

                expandStatus.put(position, expandData);
                int tvWidth = tvContent.getWidth();

                float lineRight = tvContent.getLayout().getLineRight(tvContent.getLineCount() - 1);
                if (lineRight > tvWidth - twoTextWidth) {
                    expandData.setLineFeed(true);
                    expandStatus.put(position, expandData);
                }

                if (!TextUtils.isEmpty(expandStatus.get(position).getMinString()) && !expandStatus.get(position).isExpand()) {
                    tvContent.setText(expandStatus.get(position).getMinString());
                }
            }
        });

        if (btnHeight <= 0) {
            viewBtn.post(new Runnable() {
                @Override
                public void run() {
                    btnHeight = viewBtn.getHeight();
                }
            });
        }
        this.requestLayout();
    }
}
