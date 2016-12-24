package bilalchaudhry.coursemark;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class CourseMark extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_mark);

        final Context context = this.getApplicationContext();
        final int stringId = R.id.courseList;
        final CourseManager courses = new CourseManager();
        courses.loadCourseList(context, stringId);
        final LinearLayout courseList = (LinearLayout) findViewById(R.id.courseList);
        inflateCourseList(courses, courseList, context, stringId);

        Button addCourseButton = (Button) findViewById(R.id.addCourseButton);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v){
                addCourseDialog(context, stringId, courseList, courses);
            }
        });
    }

    private void addCourseDialog(final Context context, final int stringId, final LinearLayout courseList, final CourseManager courses) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CourseMark.this);
        builder.setTitle("New Course");

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                courses.addCourse(new Course(input.getText().toString()));
                final Button newCourse = new Button(context);
                newCourse.setText(input.getText().toString());
                newCourse.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(final View v) {
                        editCourseDialog(context, stringId, courseList, courses, newCourse);
                        return false;
                    }
                });
                newCourse.setOnClickListener(new View.OnClickListener() {
                    public void onClick(final View v) {
                        courseClick(courseList.indexOfChild(newCourse));
                    }
                });
                courseList.addView(newCourse);
                courses.save(context, stringId);
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

    private void editCourseDialog(final Context context, final int stringId, final LinearLayout courseList, final CourseManager courses, final Button course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CourseMark.this);

        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);

        builder.setPositiveButton("Change Name", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newName = input.getText().toString();
                courses.getCourseList().get(courseList.indexOfChild(course)).setName(newName);
                course.setText(newName);
                courses.save(context, stringId);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                courses.getCourseList().remove(courseList.indexOfChild(course));
                courseList.removeView(course);
                courses.save(context, stringId);
            }
        });

        builder.show();
    }

    public void courseClick(int index) {
        Intent myIntent = new Intent(CourseMark.this, CourseScreen.class);
        myIntent.putExtra("indexOfCourse", index);
        CourseMark.this.startActivity(myIntent);
    }

    private void inflateCourseList(final CourseManager courses, final LinearLayout courseList, final Context context, final int stringId) {
        for (Course course : courses.getCourseList()) {
            final Button newCourse = new Button(this);
            newCourse.setText(course.getName());
            newCourse.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(final View v) {
                    editCourseDialog(context, stringId, courseList, courses, newCourse);
                    return false;
                }
            });
            newCourse.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {
                    courseClick(courseList.indexOfChild(newCourse));
                }
            });
            courseList.addView(newCourse);
        }
    }
}
