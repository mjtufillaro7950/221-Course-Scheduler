import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class ScheduleQueries 
{
    private static Connection connection;
    private static PreparedStatement addScheduleEntry;
    private static PreparedStatement getScheduleByStudent;
    private static PreparedStatement getScheduledStudentCount;
    private static PreparedStatement dropStudentScheduleByCourse;
    private static PreparedStatement getWaitlistedStudentsByClass;
    private static PreparedStatement updateScheduleEntry;
    private static PreparedStatement dropScheduleByCourse;
    private static ResultSet resultSet;
    
    public static void addScheduleEntry(ScheduleEntry entry)
    {
        //creates connection to database
        connection = DBConnection.getConnection();
        try
        {
            //pulls info from schedule entry and adds it to database
            addScheduleEntry = connection.prepareStatement("insert into app.schedule (semester, coursecode, studentID, status, timestamp) values (?,?,?,?,?)");
            addScheduleEntry.setString(1, entry.getSemester());
            addScheduleEntry.setString(2, entry.getCourseCode());
            addScheduleEntry.setString(3, entry.getStudentID());
            addScheduleEntry.setString(4, entry.getStatus());
            addScheduleEntry.setTimestamp(5, entry.getTimestamp());
            addScheduleEntry.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    
    public static ArrayList<ScheduleEntry> getScheduleByStudent(String semester, String studentID)
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> entries = new ArrayList<ScheduleEntry>();
        try
        {
            //finds every schedule entry with a specific semester and student and adds it to result set
            getScheduleByStudent = connection.prepareStatement("select coursecode, status, timestamp from app.schedule where semester = (?) and studentID = (?)");
            getScheduleByStudent.setString(1, semester);
            getScheduleByStudent.setString(2, studentID);
            resultSet = getScheduleByStudent.executeQuery();
            
            //loops through the result set, making a new schedule entry object for each and adding it to the arraylist
            while(resultSet.next())
            {
                entries.add(new ScheduleEntry(semester, resultSet.getString("courseCode"), studentID, resultSet.getString("status"), resultSet.getTimestamp("timestamp")));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        //returns arraylist of requisite schedule entries
        return entries;
    }
    
    //method that removes an entry from the schedule database corresponding to a specific class and student
    public static void dropStudentScheduleByCourse (String semester, String studentID, String courseCode)
    {
        //creates connection to database
        connection = DBConnection.getConnection();
        try
        {
            dropStudentScheduleByCourse = connection.prepareStatement("delete from app.schedule where semester = (?) and studentID = (?) and courseCode = (?)");
            dropStudentScheduleByCourse.setString(1, semester);
            dropStudentScheduleByCourse.setString(2, studentID);
            dropStudentScheduleByCourse.setString(3, courseCode);
            dropStudentScheduleByCourse.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    
    public static int getScheduledStudentCount(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        int count = 0;
        try
        {
            //finds every instance of a student in a specific class and semester and adds it to resultset
            getScheduledStudentCount = connection.prepareStatement("select studentID from app.schedule where semester = (?) and courseCode = (?)");
            getScheduledStudentCount.setString(1, semester);
            getScheduledStudentCount.setString(2, courseCode);
            resultSet = getScheduledStudentCount.executeQuery();
            
            //counts the number of entries in the result set and returns it
            while(resultSet.next())
            {
                count++;
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return count;
    }
    
    //method that returns an arraylist of the schedules of all students waitlisted for a specific class
    public static ArrayList<ScheduleEntry> getWaitlistedStudentsByClass(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<ScheduleEntry> schedules = new ArrayList<ScheduleEntry>();
        try
        {
            //finds all waitlisted students for a specific course and semester
            getWaitlistedStudentsByClass = connection.prepareStatement("select studentID, timestamp from app.schedule where semester = (?) and courseCode = (?) and status = (?) order by timestamp");
            getWaitlistedStudentsByClass.setString(1, semester);
            getWaitlistedStudentsByClass.setString(2, courseCode);
            getWaitlistedStudentsByClass.setString(3, "waitlisted");
            resultSet = getWaitlistedStudentsByClass.executeQuery();
            
            while(resultSet.next())
            {
                //makes a new schedule entry object with the data in resultset and adds it to arraylist
                schedules.add(new ScheduleEntry(semester, courseCode, resultSet.getString("studentID"), "waitlisted", resultSet.getTimestamp("timestamp")));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return schedules;
    }
    
    //updates a waitlisted schedule entry to a scheduled one
    public static void updateScheduleEntry(ScheduleEntry entry)
    {
        //creates connection to database
        connection = DBConnection.getConnection();
        try
        {
            //sets the status for a schedule entry to scheduled
            updateScheduleEntry = connection.prepareStatement("update app.schedule set status = (?) where semester = (?) and coursecode = (?) and studentID = (?)");
            updateScheduleEntry.setString(1, "scheduled");
            updateScheduleEntry.setString(2, entry.getSemester());
            updateScheduleEntry.setString(3, entry.getCourseCode());
            updateScheduleEntry.setString(4, entry.getStudentID());
            updateScheduleEntry.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    //removes all schedules for a specific class in the schedule database
    public static void dropScheduleByCourse(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        try
        {
            
            dropScheduleByCourse = connection.prepareStatement("delete from app.schedule where semester = (?) and coursecode = (?)");
            dropScheduleByCourse.setString(1, semester);
            dropScheduleByCourse.setString(2, courseCode);
            dropScheduleByCourse.executeUpdate();
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }     
}
