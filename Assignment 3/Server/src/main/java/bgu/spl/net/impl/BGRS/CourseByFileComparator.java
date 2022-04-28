package bgu.spl.net.impl.BGRS;

import java.util.Comparator;

public class CourseByFileComparator implements Comparator<Short> {

    private Database database = Database.getInstance();

    public int compare (Short first, Short second)
    {
        Course c1 = database.getCourses().get(first);
        Course c2 = database.getCourses().get(second);
        return Integer.compare(c1.getLineInFile(),c2.getLineInFile());
    }
}
