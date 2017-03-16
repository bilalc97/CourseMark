package bilalchaudhry.coursemark;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CourseScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_screen);

        Intent intent = getIntent();
        int index = intent.getIntExtra("indexOfCourse", 0);
        final Context context = this.getApplicationContext();

        System.out.print(index);
        final int stringId = R.id.courseList;
        final CourseManager courses = new CourseManager();
        courses.loadCourseList(context, stringId);
        final Course course = courses.getCourseList().get(index);
        setTitle(course.getName());

        final ScrollView marksScrollView = (ScrollView) findViewById(R.id.marks_scroll_view);
        final LinearLayout marksView = (LinearLayout) findViewById(R.id.marks_view);
        inflateMarksList(context, course, marksView);

        final EditText targetMark = (EditText) findViewById(R.id.target_mark);

        Button addMarkButton = (Button) findViewById(R.id.add_mark_button);
        addMarkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseScreen.this);
                builder.setTitle("New Mark");

                LinearLayout markInputView = new LinearLayout(context);
                markInputView.setOrientation(LinearLayout.VERTICAL);

                final EditText inputName = new EditText(context);
                inputName.setHint("name");
                markInputView.addView(inputName);

                final EditText inputNum = new EditText(context);
                inputNum.setHint("marks");
                inputNum.setInputType(InputType.TYPE_CLASS_NUMBER);
                markInputView.addView(inputNum);

                final EditText inputDenom = new EditText(context);
                inputDenom.setHint("total");
                inputDenom.setInputType(InputType.TYPE_CLASS_NUMBER);
                markInputView.addView(inputDenom);

                final EditText inputWeight = new EditText(context);
                inputWeight.setHint("weight (0 - 100)");
                inputWeight.setInputType(InputType.TYPE_CLASS_NUMBER);
                markInputView.addView(inputWeight);

                builder.setView(markInputView);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!inputNum.getText().toString().isEmpty() &&
                                !inputDenom.getText().toString().isEmpty() &&
                                !inputWeight.getText().toString().isEmpty()) {
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

        Button removeMarksButton = (Button) findViewById(R.id.remove_button);
        removeMarksButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                for (int i = 0; i < marksView.getChildCount(); i++) {
                    LinearLayout row = (LinearLayout)marksView.getChildAt(i);
                    CheckBox markCheckBox = (CheckBox)row.getChildAt(0);
                    if (markCheckBox.isChecked()) {
                        marksView.removeView(row);
                        course.getMarks().remove(i);
                        courses.save(context, stringId);
                        i -= 1;
                    }
                }
            }
        });

        Button calculateMarkButton = (Button) findViewById(R.id.calculate_button);
        calculateMarkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                double currentMark = 0.0;
                double currentTotalWeight = 0.0;
                for (Mark mark : course.getMarks()) {
                    currentMark += (double)mark.getNumerator() / (double)mark.getDenominator() * (double)mark.getWeight();
                    currentTotalWeight += (double)mark.getWeight();
                }
                currentMark /= (currentTotalWeight / 100.0);

                currentMark = Math.round(currentMark * 100.0) / 100.0;
                Context context = getApplicationContext();
                CharSequence text = "Current mark in " + Double.toString(currentTotalWeight) + "% of course is: " + Double.toString(currentMark);

                if (!targetMark.getText().toString().isEmpty()) {
                    double neededMark = 0.0;
                    neededMark = Double.valueOf(targetMark.getText().toString()) - (currentMark * currentTotalWeight / 100.0);
                    neededMark = Math.round((100 * neededMark) / (100.0 - currentTotalWeight) * 100.0) / 100.0;
                    text = text.toString() + "\nNeeded mark in remaining " + Double.toString(100.0 - currentTotalWeight) + "% of course is: " + Double.toString(neededMark);
                }

                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
    }

    public void inflateMarksList(Context context, Course course, LinearLayout marksView) {
        for (Mark mark : course.getMarks()) {
            addMarkToView(context, mark, marksView);
        }
    }

    public void addMarkToView(Context context, Mark mark, LinearLayout marksView) {
        LinearLayout markRow = new LinearLayout(context);

        LinearLayout.LayoutParams checkBoxParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        CheckBox markCheck = new CheckBox(context);
        markCheck.setGravity(Gravity.CENTER);
        markCheck.setLayoutParams(checkBoxParams);
        markRow.addView(markCheck);

        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 3.0f);
        TextView nameView = new TextView(context);
        nameView.setLayoutParams(nameParams);
        nameView.setGravity(Gravity.CENTER);
        nameView.setText(mark.getName());
        markRow.addView(nameView);

        LinearLayout.LayoutParams marksParam = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1.5f);
        TextView markNum = new TextView(context);
        markNum.setLayoutParams(marksParam);
        markNum.setGravity(Gravity.CENTER);
        markNum.setText((String.valueOf(mark.getNumerator()) + " / " + String.valueOf(mark.getDenominator())));
        markRow.addView(markNum);

        LinearLayout.LayoutParams weightParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        TextView weight = new TextView(context);
        weight.setLayoutParams(weightParams);
        weight.setGravity(Gravity.CENTER);
        weight.setText(String.valueOf(mark.getWeight() + "%"));
        markRow.addView(weight);

        marksView.addView(markRow);
    }
}
