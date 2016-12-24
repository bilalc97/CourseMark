package bilalchaudhry.coursemark;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by bilalchaudhry on 2016-12-21.
 */
public class CourseManager {
    private ArrayList<Course> courses;

    public CourseManager() {
        this.courses = new ArrayList<Course>();
    }

    public ArrayList<Course> getCourseList() {
        return courses;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }

    public void loadCourseList(Context context, int stringId) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(stringId), Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPref.getString(context.getString(stringId), "");
        if (!json.isEmpty()) {
            this.courses = gson.fromJson(json, CourseManager.class).getCourseList();
        }
    }

    public void save(Context context, int stringId) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(stringId), Context.MODE_PRIVATE)
                ;
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this);
        prefsEditor.putString(context.getString(stringId), json);
        prefsEditor.apply();
    }
}
