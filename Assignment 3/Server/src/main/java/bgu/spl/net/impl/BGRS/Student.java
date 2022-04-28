package bgu.spl.net.impl.BGRS;


import java.util.Collections;
import java.util.LinkedList;

public class Student implements User{
    private String userName;
    private String password;
    private boolean isConnedcted;
    private LinkedList<Short> listOfCourses;
    private CourseByFileComparator comp = new CourseByFileComparator();

    private Database database = Database.getInstance();

    //CTR
    public Student(String userName, String password)
    {
        this.userName = userName;
        this.password = password;
        this.isConnedcted = false;
        this.listOfCourses = new LinkedList<>();
    }

    //Getters
    public String getUserName(){return this.userName;}
    public String getPassword(){return this.password;}
    public boolean getIsConnected(){return this.isConnedcted;}
    public LinkedList<Short> getListOfCourses(){
        Collections.sort(this.listOfCourses,this.comp);
        return this.listOfCourses;
    }

    //Setters
    public void setConnedcted(boolean connedcted) {isConnedcted = connedcted;}

    public boolean hasKdam(short courseNum)
    {
        synchronized (database.getCourses())
        {
            Course c = database.getCourses().get(courseNum);
            LinkedList<Short> l = c.getKdamCourses();
            for(short i :l)
            {
                if(!this.listOfCourses.contains(i))
                    return false;
            }
            return true;
        }
    }

    public boolean isRegToCourse(short courseNum) {return this.listOfCourses.contains(courseNum); }

    public void registerCourse(short courseNum)
    {
        synchronized (database.getCourses())
        {
            this.listOfCourses.add(courseNum);
            Course course = database.getCourses().get(courseNum);
            course.registerStudent(this); // update the field in Course class as well
        }
    }

    public void unregisterCourse(short courseNum){
        synchronized (database.getCourses())
        {
            int index = this.listOfCourses.indexOf(courseNum);
            this.listOfCourses.remove(index);
            Course course = database.getCourses().get(courseNum);
            course.unregisterStudent(this); // update the field in Course class as well
        }
    }

    public String toString()
    {
        String output = "";
        String list = this.getListOfCourses().toString().replace(" ","");
        output += "Student: "+this.userName + "\n";
        output += "Courses: "+list;
        return output;
    }

}
