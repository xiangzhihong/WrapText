package xzh.com.wraptext_master.view.wrap;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xzh.com.wraptext_master.R;

public class WrapTextView extends ViewGroup implements IPlainAdapterView {

    private Adapter adapter;
    private OnItemClickListener listener;
    private Observer observer = new Observer(this);
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int horizontalSpacing = 0;
    private int verticalSpacing = 0;
    private int orientation = 0;

    public WrapTextView(Context context) {
        super(context);
    }

    public WrapTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, -1);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public WrapTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WrapTextView, defStyleAttr, 0);
        horizontalSpacing = typedArray.getDimensionPixelSize(R.styleable.WrapTextView_horizontal_spacing, 0);
        verticalSpacing = typedArray.getDimensionPixelSize(R.styleable.WrapTextView_vertical_spacing, 0);
        typedArray.recycle();
    }

    @Override
    public Adapter getAdapter() {
        return adapter;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (this.adapter != null)
            this.adapter.unregisterDataSetObserver(observer);

        this.adapter = adapter;
        adapter.registerDataSetObserver(observer);
        observer.onChanged();
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private class Observer extends DataSetObserver {
        WrapTextView context;

        public Observer(WrapTextView context) {
            this.context = context;
        }

        @Override
        public void onChanged() {
            if (context.adapter == null)
                return;
            int i = 0;
            List<View> oldViews = new ArrayList<View>();
            for (i = 0; i < context.getChildCount(); i++) {
                oldViews.add(context.getChildAt(i));
            }
            context.removeAllViews();

            Iterator<View> iterItem = oldViews.iterator();
            for (i = 0; i < context.adapter.getCount(); i++) {
                final int index = i;
                View convertView = iterItem.hasNext() ? iterItem.next() : null;
                convertView = context.adapter.getView(i, convertView, context);
                LayoutParams lp = generateDefaultLayoutParams();
                convertView.setLayoutParams(lp);
                if (listener != null) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClick(WrapTextView.this, v, index);
                        }
                    });
                }
                context.addView(convertView);
            }
            super.onChanged();
        }

        @Override
        public void onInvalidated() {
            context.removeAllViews();
            super.onInvalidated();
        }
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - this.getPaddingRight() - this.getPaddingLeft();
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - this.getPaddingTop() - this.getPaddingBottom();

        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int size;
        int mode;

        if (orientation == HORIZONTAL) {
            size = sizeWidth;
            mode = modeWidth;
        } else {
            size = sizeHeight;
            mode = modeHeight;
        }

        int lineThicknessWithSpacing = 0;
        int lineThickness = 0;
        int lineLengthWithSpacing = 0;
        int lineLength;

        int prevLinePosition = 0;

        int controlMaxLength = 0;
        int controlMaxThickness = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            child.measure(
                    getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight(), lp.width),
                    getChildMeasureSpec(heightMeasureSpec, this.getPaddingTop() + this.getPaddingBottom(), lp.height)
            );

            int hSpacing = this.getHorizontalSpacing(lp);
            int vSpacing = this.getVerticalSpacing(lp);

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            int childLength;
            int childThickness;
            int spacingLength;
            int spacingThickness;

            if (orientation == HORIZONTAL) {
                childLength = childWidth;
                childThickness = childHeight;
                spacingLength = hSpacing;
                spacingThickness = vSpacing;
            } else {
                childLength = childHeight;
                childThickness = childWidth;
                spacingLength = vSpacing;
                spacingThickness = hSpacing;
            }

            lineLength = lineLengthWithSpacing + childLength;
            lineLengthWithSpacing = lineLength + spacingLength;

            boolean newLine = lp.newLine || (mode != MeasureSpec.UNSPECIFIED && lineLength > size);
            if (newLine) {
                prevLinePosition = prevLinePosition + lineThicknessWithSpacing;

                lineThickness = childThickness;
                lineLength = childLength;
                lineThicknessWithSpacing = childThickness + spacingThickness;
                lineLengthWithSpacing = lineLength + spacingLength;
            }

            lineThicknessWithSpacing = Math.max(lineThicknessWithSpacing, childThickness + spacingThickness);
            lineThickness = Math.max(lineThickness, childThickness);

            int posX;
            int posY;
            if (orientation == HORIZONTAL) {
                posX = getPaddingLeft() + lineLength - childLength;
                posY = getPaddingTop() + prevLinePosition;
            } else {
                posX = getPaddingLeft() + prevLinePosition;
                posY = getPaddingTop() + lineLength - childHeight;
            }
            lp.setPosition(posX, posY);

            controlMaxLength = Math.max(controlMaxLength, lineLength);
            controlMaxThickness = prevLinePosition + lineThickness;
        }

        if (orientation == HORIZONTAL) {
            controlMaxLength += getPaddingLeft() + getPaddingRight();
            controlMaxThickness += getPaddingBottom() + getPaddingTop();
        } else {
            controlMaxLength += getPaddingBottom() + getPaddingTop();
            controlMaxThickness += getPaddingLeft() + getPaddingRight();
        }

        if (orientation == HORIZONTAL) {
            this.setMeasuredDimension(resolveSize(controlMaxLength, widthMeasureSpec), resolveSize(controlMaxThickness, heightMeasureSpec));
        } else {
            this.setMeasuredDimension(resolveSize(controlMaxThickness, widthMeasureSpec), resolveSize(controlMaxLength, heightMeasureSpec));
        }
    }

    private int getVerticalSpacing(LayoutParams lp) {
        int vSpacing;
        if (lp.verticalSpacingSpecified()) {
            vSpacing = lp.verticalSpacing;
        } else {
            vSpacing = this.verticalSpacing;
        }
        return vSpacing;
    }

    private int getHorizontalSpacing(LayoutParams lp) {
        int hSpacing;
        if (lp.horizontalSpacingSpecified()) {
            hSpacing = lp.horizontalSpacing;
        } else {
            hSpacing = this.horizontalSpacing;
        }
        return hSpacing;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y + child.getMeasuredHeight());
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        private static int NO_SPACING = -1;
        private int x;
        private int y;
        private int horizontalSpacing = NO_SPACING;
        private int verticalSpacing = NO_SPACING;
        private boolean newLine = false;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public boolean horizontalSpacingSpecified() {
            return horizontalSpacing != NO_SPACING;
        }

        public boolean verticalSpacingSpecified() {
            return verticalSpacing != NO_SPACING;
        }

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
