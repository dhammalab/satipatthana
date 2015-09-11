package com.dhammalab.satipatthna;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.dhammalab.satipatthna.domain.ReadingMaterialItem;
import com.dhammalab.satipatthna.views.ReadingMaterialRowView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anthony.lipscomb on 10/10/2014.
 */
public class ReadingMaterialListActivity extends Activity {
    private ListView readingList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_material);

        readingList = (ListView) findViewById(R.id.reading_material_list);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        final ArrayList<ReadingMaterialItem> names = new ArrayList<ReadingMaterialItem>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ReadingMaterial");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                for (ParseObject object : parseObjects) {
                    ReadingMaterialItem item = new ReadingMaterialItem();
                    item.setName(object.getString("name"));
                    item.setAuthor(object.getString("author"));
                    item.setUrl(object.getString("url"));
                    item.setPages(object.getInt("pages"));
                    names.add(item);
                }
                initializeList(names);
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void initializeList(final List<ReadingMaterialItem> objects) {
        ArrayAdapter<ReadingMaterialItem> adapter = new ArrayAdapter<ReadingMaterialItem>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, objects) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ReadingMaterialRowView view = new ReadingMaterialRowView(getContext());
                view.setReadingItem(getItem(position));
                return view;
            }
        };

        readingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReadingMaterialItem item = (ReadingMaterialItem) parent.getItemAtPosition(position);
                Intent readIntent = new Intent(getBaseContext(), ReadActivity.class);
                readIntent.putExtra(ReadActivity.URL_EXTRA, item.getUrl());
                startActivity(readIntent);
            }
        });
        readingList.setAdapter(adapter);
        showLoading(false);
    }
}
