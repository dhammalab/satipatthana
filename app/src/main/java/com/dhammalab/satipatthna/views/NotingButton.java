package com.dhammalab.satipatthna.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.devspark.robototextview.widget.RobotoTextView;
import com.dhammalab.satipatthna.R;

public class NotingButton extends LinearLayout {
    private String title = "No title yet";
    private String subtitle = "No subtitle yet";

    public NotingButton(Context context) {
        super(context);
        initializeViews(context);
    }

    public NotingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
        readAttributes(context, attrs);
    }

    public NotingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
        readAttributes(context, attrs);
    }
    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.noting_button_view, this);
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NotingButton);
        title = typedArray.getString(R.styleable.NotingButton_title);
        subtitle = typedArray.getString(R.styleable.NotingButton_subtitle);
        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ((RobotoTextView) this.findViewById(R.id.meditate_title)).setText(title);
        ((RobotoTextView) this.findViewById(R.id.meditate_subtitle)).setText(subtitle);
    }
}
