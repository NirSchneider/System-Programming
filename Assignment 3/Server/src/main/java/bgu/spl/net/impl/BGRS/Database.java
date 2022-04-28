package bgu.spl.net.impl.BGRS;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {

    private ConcurrentHashMap<String,User> users;
    private ConcurrentHashMap<Short,Course> courses;
    private boolean shouldTerminate=false;


    public static class SingltonHolder{
        private static Database instance = new Database();
    }

    //to prevent user from creating new Database
    private Database() {
        // TODO: implement
        this.courses = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Database getInstance() {
        return SingltonHolder.instance;
    }
    /**
     * loades the courses from the file path specified
     * into the Database, returns true if successful.
     */
    public boolean initialize(String coursesFilePath) {
        try {
            FileReader reader = new FileReader(coursesFilePath);
            BufferedReader buffReader = new BufferedReader(reader);
            String textLine;
            int index = 0;
            while ((textLine = buffReader.readLine()) != null) {
                String[] splitWords = textLine.split("\\|");
                short courseNum = Short.parseShort(splitWords[0]);
                String courseName = splitWords[1];
                LinkedList<Short> kdamCourses = new LinkedList<>();
                if (!splitWords[2].equals("[]")) {
                    String[] tmp = splitWords[2].substring(1, splitWords[2].length() - 1).split(",");
                    for (int i = 0; i < tmp.length; i++)
                        kdamCourses.addLast(Short.parseShort(tmp[i]));
                }
                int maxNumOfStudents = Integer.parseInt(splitWords[3]);
                this.courses.put(courseNum, new Course(courseNum,courseName, kdamCourses, maxNumOfStudents, index));
                index++;
            }
        }catch (Exception e){e.printStackTrace();}

        return false;
    }

    //Getters
    public ConcurrentHashMap<String, User> getUsersConnected(){return this.users;}
    public ConcurrentHashMap<Short, Course> getCourses() {return this.courses;}
}





