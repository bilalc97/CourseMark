package bilalchaudhry.coursemark;

import java.util.ArrayList;

/**
 * Course information
 */
public class Course {

    private String name;
    private ArrayList<Mark> marks;

    public Course(String name) {
        this.name = name;
        this.marks = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Mark> getMarks() {
        return this.marks;
    }

    public void addMark(Mark mark) {
        this.marks.add(mark);
    }

}
