package com.hetulpatel.fellow.fellowv2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hetulpatel on 20/06/17.
 */

public class user {

    public Boolean firsttime;
    public String username;
    public int current_semester;
    public int current_courses_count;
    public ArrayList<String> current_courses;

    public Boolean getFirsttime() {
        return firsttime;
    }

    public void setFirsttime(Boolean firsttime) {
        this.firsttime = firsttime;
    }

    public void setCurrent_courses(ArrayList<String> current_courses) {
        this.current_courses = current_courses;
    }

    public ArrayList<String> getCurrent_courses() {
        return current_courses;
    }

    public int getCurrent_semester() {
        return current_semester;
    }

    public void setCurrent_semester(int current_semester) {
        this.current_semester = current_semester;
    }

    public int getCurrent_courses_count() {
        return current_courses_count;
    }

    public void setCurrent_courses_count(int current_courses_count) {
        this.current_courses_count = current_courses_count;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {

        return username;
    }
}
