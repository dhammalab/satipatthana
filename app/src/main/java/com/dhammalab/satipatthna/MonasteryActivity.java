package com.dhammalab.satipatthna;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dhammalab.satipatthna.repository.Settings;

public class MonasteryActivity extends BaseActivity {

    public static final int LESSONS_COUNT = 6;

    private TextView lessonNumberInfo;
    private TextView title;
    private TextView subtitle;
    private TextView idea;
    private TextView instruction;
    private Button next;
    private int currentLesson = 0;

    private Settings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monastery);
        title = (TextView) findViewById(R.id.monastery_title);
        subtitle = (TextView) findViewById(R.id.monastery_subtitle);
        idea = (TextView) findViewById(R.id.monastery_lesson_idea);
        lessonNumberInfo = (TextView) findViewById(R.id.monastery_lesson_number);
        instruction = (TextView) findViewById(R.id.monastery_lesson_instruction);
        next = (Button) findViewById(R.id.btn_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentLesson++;
                if (LESSONS_COUNT == currentLesson) {
                    finish();
                } else {
                    showLessonInfo(currentLesson);
                    if (currentLesson == LESSONS_COUNT - 1) {
                        settings.setLesson6VisitCount(settings.getLesson6VisitCount() + 1);
                    }
                }
            }
        });
        showLessonInfo(currentLesson);
        settings = getSettings();
    }

    private void showLessonInfo(int lessonNumber) {
        Resources resources = getResources();
        title.setText(resources.getTextArray(R.array.monastery_lessons_titles)[lessonNumber]);
        subtitle.setText(resources.getTextArray(R.array.monastery_lessons_subtitles)[lessonNumber]);
        idea.setText(resources.getTextArray(R.array.monastery_ideas)[lessonNumber]);
        instruction.setText(resources.getTextArray(R.array.monastery_instructions)[lessonNumber]);
        lessonNumberInfo.setText(getString(R.string.monastery_lesson_number, lessonNumber + 1));
    }

}
