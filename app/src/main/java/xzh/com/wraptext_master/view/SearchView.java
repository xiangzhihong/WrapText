package xzh.com.wraptext_master.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import xzh.com.wraptext_master.R;
import xzh.com.wraptext_master.utils.Utils;

public class SearchView extends FrameLayout {
    @InjectView(R.id.content_view)
    FilterEditText contentEditView;
    @InjectView(R.id.clear_view)
    ImageView clearView;
    @InjectView(R.id.operation_view)
    TextView operationView;

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, -1);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }


    private void init(AttributeSet attrs, int defStyleAttr) {
        inflate(getContext(), R.layout.search_bar_layout, this);
        ButterKnife.inject(this, this);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SearchView, defStyleAttr, 0);
        String hint = typedArray.getString(R.styleable.SearchView_query_hint);
        int maxLength = typedArray.getInt(R.styleable.SearchView_input_max_length, 1000);
        typedArray.recycle();

        contentEditView.setMaxLength(maxLength);
        contentEditView.setHint(hint);
        contentEditView.setOnKeyListener(onKeyListener);
        contentEditView.addTextChangedListener(sampleTextWatcher);
    }

    @OnClick(R.id.operation_view)
    public void operation() {
        String key = getText().toString();
        if (TextUtils.isEmpty(key)) {
            Utils.hideSoftInput(getContext(), contentEditView);
            ((Activity) getContext()).finish();
        } else if (mSearchListener != null) {
            mSearchListener.search(key);
        }
    }

    @OnClick(R.id.clear_view)
    public void clearFilterKey() {
        contentEditView.setText("");
    }

    private OnKeyListener onKeyListener = new OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_UP) {
                sampleTextWatcher.afterTextChanged(null);
                return true;
            }
            return false;
        }
    };

    private SampleTextWatcher sampleTextWatcher = new SampleTextWatcher() {
        public void afterTextChanged(Editable s) {
            String key = getText().toString();
            boolean bool = TextUtils.isEmpty(key);
            clearView.setVisibility(bool ? GONE : VISIBLE);
            operationView.setText(bool ? "取消" : "搜索");
            if (mSearchListener != null) {
                mSearchListener.search(key);
            }
        }
    };

    public void hideOperationView(){
        operationView.setVisibility(GONE);
    }

    public CharSequence getText() {
        return contentEditView.getText().toString();
    }

    public FilterEditText getContentEditView() {
        return contentEditView;
    }

    public void setInputFilters(InputFilter[] filters){
        contentEditView.setFilters(filters);
    }

    private SearchListener mSearchListener = null;

    public void setSearchListener(SearchListener searchListener) {
        this.mSearchListener = searchListener;
    }

    public interface SearchListener {
        void search(String key);
    }
}
