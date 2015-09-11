package com.dhammalab.satipatthna.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.devspark.robototextview.widget.RobotoTextView;
import com.dhammalab.satipatthna.R;
import com.dhammalab.satipatthna.domain.ReadingMaterialItem;

/**
 * Created by anthony.lipscomb on 10/12/2014.
 */
public class ReadingMaterialRowView extends RelativeLayout {
    private RobotoTextView name;
    private RobotoTextView author;
    private RobotoTextView pageCount;

    public ReadingMaterialRowView(Context context) {
        super(context);
        if(!isInEditMode()) initialize(context);
    }

    public ReadingMaterialRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()) initialize(context);
    }

    public ReadingMaterialRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(!isInEditMode()) initialize(context);
    }

    private void initialize(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_reading_material_row_view, this, true);

        name = (RobotoTextView) findViewById(R.id.reading_material_name);
        author = (RobotoTextView) findViewById(R.id.reading_material_author);
        pageCount = (RobotoTextView) findViewById(R.id.reading_material_page_count);
    }

    public void setReadingItem(ReadingMaterialItem item) {
        name.setText(item.getName());
        author.setText("By " + item.getAuthor());
        pageCount.setText(item.getPages() + "\nPages");
    }
}
