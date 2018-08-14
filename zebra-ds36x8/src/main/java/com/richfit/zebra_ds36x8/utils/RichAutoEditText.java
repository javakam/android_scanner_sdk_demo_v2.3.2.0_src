package com.richfit.zebra_ds36x8.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.richfit.zebra_ds36x8.R;


/**
 * 带按钮的AutoCompleteTextView。
 */
public class RichAutoEditText extends AppCompatAutoCompleteTextView implements TextWatcher {

    private Drawable mSuffixIcon;

    private Drawable mPrefixIcon;


    public RichAutoEditText(final Context context) {
        super(context);
        init(context);
    }

    public RichAutoEditText(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RichAutoEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        //设置左边的icon.数组0,1,2,3,对应着左，上，右，下 这4个位置的图片
        Drawable[] drawables = getCompoundDrawables();
        Drawable leftDrawable = drawables[0];
        Drawable rightDrawable = drawables[2];
        if (leftDrawable != null) {
            //DrawableCompat，包装drawable使得它可以使用hintColor。
            mPrefixIcon = DrawableCompat.wrap(leftDrawable);
            setPrefixIconTintColor(R.color.black);
            mPrefixIcon.setBounds(0, 0, mPrefixIcon.getIntrinsicHeight(), mPrefixIcon.getIntrinsicHeight());
        }

        if (rightDrawable == null) {
            rightDrawable = ContextCompat.getDrawable(context, R.mipmap.icon_find);
        }
        mSuffixIcon = rightDrawable;
        setSuffixIconVisible(false);
        addTextChangedListener(this);
    }

    public interface OnRichAutoEditTouchListener {
        void onTouchRichEdit(View view, String text);
    }

    private OnRichAutoEditTouchListener mListener;

    public void setOnRichAutoEditTouchListener(OnRichAutoEditTouchListener listener) {
        this.mListener = listener;
    }


    /**
     * 注意onTouchListener的onTouch事件先于onTouch方法执行
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                final int x = (int) event.getX();
                if (mSuffixIcon.isVisible() && x > getWidth() - getPaddingRight() - mSuffixIcon.getIntrinsicWidth() * 2) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (mListener != null) {
                            mListener.onTouchRichEdit(this, getText().toString().trim());
                        }
                    }
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public final void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (isFocused()) {
            setSuffixIconVisible(text.length() > 0);
            setPrefixIconTintColor(text.length() > 0 ? R.color.colorPrimary : R.color.grey_700);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 设置右边的Icon是否可见
     *
     * @param visible
     */
    public void setSuffixIconVisible(final boolean visible) {
        if (mSuffixIcon == null) {
            return;
        }
        mSuffixIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(compoundDrawables[0],
                compoundDrawables[1], visible ? mSuffixIcon : null, compoundDrawables[3]);
    }

    /**
     * 设置左边的icon的着色
     */
    public void setPrefixIconTintColor(int tintColor) {
        if (tintColor == 0 || mPrefixIcon == null) {
            return;
        }
        DrawableCompat.setTint(mPrefixIcon, ContextCompat.getColor(getContext(), tintColor));
    }
}


