package uc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;

public class SearchEditText extends android.support.v7.widget.AppCompatEditText implements View.OnFocusChangeListener,OnKeyListener
{
	
	 private static final String TAG = SearchEditText.class.getSimpleName();
	    /**
	     * 是否是默认图标再左边的样式
	     */
	    private boolean isLeft = false;
	    /**
	     * 是否点击软键盘搜索
	     */
	    private boolean pressSearch = false;
	    /**
	     * 软键盘搜索键监听
	     */
	    private OnSearchClickListener listener;
	public void setOnSearchClickListener(OnSearchClickListener listener) {
		this.listener = listener;
	}

	public interface OnSearchClickListener {
		void onSearchClick(View view);
	}

	public SearchEditText(Context context) {
		super(context, null);
		init();
	}

	public SearchEditText(Context context, AttributeSet attrs) {
		super(context, attrs, android.R.attr.editTextStyle);
		init();
	}

	public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
        setOnFocusChangeListener(this);
        setOnKeyListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isLeft) { // 如果是默认样式，则直接绘制
            super.onDraw(canvas);
        } else { // 如果不是默认样式，需要将图标绘制在中间
            Drawable[] drawables = getCompoundDrawables();
            if (drawables != null) {
                Drawable drawableLeft = drawables[0];
                if (drawableLeft != null) {
                    float textWidth = getPaint().measureText(getHint().toString());
                    int drawablePadding = getCompoundDrawablePadding();
                    int drawableWidth = drawableLeft.getIntrinsicWidth();
                    float bodyWidth = textWidth + drawableWidth + drawablePadding;
                    canvas.translate((getWidth() - bodyWidth - getPaddingLeft() - getPaddingRight()) / 2, 0);
                }
            }
            super.onDraw(canvas);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d(TAG, "onFocusChange execute");
        // 恢复EditText默认的样式
        if (!pressSearch && TextUtils.isEmpty(getText().toString())) {
            isLeft = hasFocus;
        }
    }
/*
    @Override
    public boolean onKey(DialogInterface v, int keyCode, KeyEvent event) {
    
    	 pressSearch = (keyCode == KeyEvent.KEYCODE_ENTER);
         if (pressSearch && listener != null) {
     
             InputMethodManager imm = (InputMethodManager) ((View) v).getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
             if (imm.isActive()) {
                 imm.hideSoftInputFromWindow(((View) v).getApplicationWindowToken(), 0);
             }
             listener.onSearchClick((View) v);
         }
    	return false;
    }*/
    
    
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        pressSearch = (keyCode == KeyEvent.KEYCODE_ENTER);
        if (pressSearch && listener != null) {
            /*隐藏软键盘*/
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            listener.onSearchClick(v);
        }
        return false;
    }



  

}
