package bgu.spl.net.impl.BGRS;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Course {

    private int courseNum;
    private String courseName;
    private LinkedList<Short> kdamCourses;
    private int numOfMaxStudents;
    private LinkedList<Student> studentsRegistered;
    private int lineInFile; //starts from 1
    private CourseByFileComparator comp = new CourseByFileComparator();

    //CTR
    public Course(int courseNum, String courseName, LinkedList<Short> kdamCourses, int numOfMaxStudents, int line) {
        this.courseName = courseName;
        this.courseNum = courseNum;
        this.kdamCourses = kdamCourses;
        this.numOfMaxStudents = numOfMaxStudents;
        this.studentsRegistered = new LinkedList<>();
        this.lineInFile = line;
    }

    //Getters
    public LinkedList<Short> getKdamCourses() {
        Collections.sort(this.kdamCourses,this.comp);
        return this.kdamCourses;
    }
    public LinkedList<Student> getStudentsRegistered(){return this.studentsRegistered;}
    public int getLineInFile() {return lineInFile;}

    public boolean isFull(){return numOfMaxStudents == studentsRegistered.size();}

    public void registerStudent(Student s){this.studentsRegistered.add(s);}

    public void unregisterStudent(Student s){this.studentsRegistered.remove(s);}

    public String toString()
    {
        String output = "";
        output += "Course: ("+this.courseNum+") " + this.courseName + "\n" + "Seats Available: "+(this.numOfMaxStudents-this.studentsRegistered.size())+"/"+this.numOfMaxStudents+"\n";
        output +="Students Registered: " + sortAlphabetically(this.getStudentsRegistered());
        return output;
    }

    public String sortAlphabetically(List<Student> l)
    {
        LinkedList result = new LinkedList<String>();
        for(Student s : l)
        {
            result.add(s.getUserName());
        }
        java.util.Collections.sort(result,String.CASE_INSENSITIVE_ORDER); //case sensitive or not???
        return result.toString().replace(" ","");
    }


}














