package bilalchaudhry.coursemark;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

public class CourseScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_screen);
        setTitle("Hello");

        Intent intent = getIntent();
        int index = intent.getIntExtra("indexOfCourse", 0);
        final Context context = this.getApplicationContext();

        System.out.print(index);
        final int stringId = R.id.courseList;
        final CourseManager courses = new CourseManager();
        courses.loadCourseList(context, stringId);
        final Course course = courses.getCourseList().get(index);
        setTitle(course.getName());

        final ScrollView marksView = (ScrollView) findViewById(R.id.marks_view);
        inflateMarksList(context, course, marksView);

        Button addMarkButton = (Button) findViewById(R.id.add_mark_button);
        addMarkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseScreen.this);
                builder.setTitle("New Mark");

                LinearLayout markInputView = new LinearLayout(context);
                markInputView.setOrientation(LinearLayout.VERTICAL);

                final EditText inputName = new EditText(context);
                markInputView.addView(inputName);
                final EditText inputNum = new EditText(context);
                markInputView.addView(inputNum);
                final EditText inputDenom = new EditText(context);
                markInputView.addView(inputDenom);
                final EditText inputWeight = new EditText(context);
                markInputView.addView(inputWeight);

                builder.setView(markInputView);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputName.getText().toString();
                        int num = Integer.valueOf(inputNum.getText().toString());
                        int denom = Integer.valueOf(inputDenom.getText().toString());
                        int weight = Integer.valueOf(inputWeight.getText().toString());
                        if (0 <= num && 0 <= denom && 0 <= weight && weight <= 100) {
                            Mark newMark = new Mark(name, num, denom, weight);
                            course.addMark(newMark);
                            addMarkToView(context, newMark, marksView);
                            courses.save(context, stringId);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    public void inflateMarksList(Context context, Course course, ScrollView marksView) {
        for (Mark mark : course.getMarks()) {
            addMarkToView(context, mark, marksView);
        }
    }

    public void addMarkToView(Context context, Mark mark, ScrollView marksView) {
        LinearLayout markRow = new LinearLayout(context);

        LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        checkBoxParams.weight = 1.0f;
        CheckBox markCheck = new CheckBox(context);
        markCheck.setLayoutParams(checkBoxParams);
        markRow.addView(markCheck);

        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        nameParams.weight = 3.0f;
        TextView nameView = new TextView(context);
        nameView.setLayoutParams(nameParams);
        nameView.setText(mark.getName());
        markRow.addView(nameView);

        LinearLayout.LayoutParams markNumParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        markNumParams.weight = 1.0f;
        TextView markNum = new TextView(context);
        markNum.setLayoutParams(markNumParams);
        markNum.setText((String.valueOf(mark.getNumerator())));
        markRow.addView(markNum);

        LinearLayout.LayoutParams slashParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        slashParams.weight = 1.0f;
        TextView slash = new TextView(context);
        slash.setLayoutParams(slashParams);
        slash.setText(" / ");
        markRow.addView(slash);

        LinearLayout.LayoutParams markDenomParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        markDenomParams.weight = 1.0f;
        TextView markDenom = new TextView(context);
        markDenom.setLayoutParams(markDenomParams);
        markDenom.setText(String.valueOf(mark.getDenominator()));
        markRow.addView(markDenom);

        LinearLayout.LayoutParams weightParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        weightParams.weight = 1.0f;
        TextView weight = new TextView(context);
        weight.setLayoutParams(weightParams);
        weight.setText(String.valueOf(mark.getWeight()));
        markRow.addView(weight);

        marksView.addView(markRow);
    }
}
