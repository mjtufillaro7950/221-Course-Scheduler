import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class MultiTableQueries 
{
    private static Connection connection;
    private static PreparedStatement getAllClassDescription;
    private static PreparedStatement clearDatabase;
    private static PreparedStatement getScheduledStudentsByClass;
    private static PreparedStatement getWaitlistedStudentsByClass;
    private static ResultSet resultSet;
    
    public static ArrayList<ClassDescription> getAllClassDescription(String semester)
    {
        connection = DBConnection.getConnection();
        ArrayList<ClassDescription> descriptions = new ArrayList<ClassDescription>();
        try
        {
            //collects all information about class from database into an arraylist and returns it
            getAllClassDescription = connection.prepareStatement("select app.class.courseCode, description, seats from app.class, app.course where semester = ? and app.class.courseCode = app.course.courseCode order by app.class.courseCode");
            getAllClassDescription.setString(1, semester);
            resultSet = getAllClassDescription.executeQuery();
            
            while(resultSet.next())
            {
                descriptions.add(new ClassDescription(resultSet.getString("CourseCode"), resultSet.getString("Description"), resultSet.getInt("seats")));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return descriptions;
    }
    
    //method that returns an arraylist of all students scheduled for a specific class
    public static ArrayList<StudentEntry> getScheduledStudentsByClass(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> students = new ArrayList<StudentEntry>();
        try
        {
            //finds all scheduled students for a specific course and semester
            getScheduledStudentsByClass = connection.prepareStatement("select studentID from app.schedule where semester = (?) and courseCode = (?) and status = (?) order by timestamp");
            getScheduledStudentsByClass.setString(1, semester);
            getScheduledStudentsByClass.setString(2, courseCode);
            getScheduledStudentsByClass.setString(3, "scheduled");
            resultSet = getScheduledStudentsByClass.executeQuery();
            
            while(resultSet.next())
            {
                //calls the student queries get student method to find the student associated with an ID
                students.add(StudentQueries.getStudent(resultSet.getString("studentID")));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return students;
    }
    
    //method that returns an arraylist of all students waitlisted for a specific class
    public static ArrayList<StudentEntry> getWaitlistedStudentsByClass(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> students = new ArrayList<StudentEntry>();
        try
        {
            //finds all waitlisted students for a specific course and semester
            getWaitlistedStudentsByClass = connection.prepareStatement("select studentID from app.schedule where semester = (?) and courseCode = (?) and status = (?) order by timestamp");
            getWaitlistedStudentsByClass.setString(1, semester);
            getWaitlistedStudentsByClass.setString(2, courseCode);
            getWaitlistedStudentsByClass.setString(3, "waitlisted");
            resultSet = getWaitlistedStudentsByClass.executeQuery();
            
            while(resultSet.next())
            {
                //calls the student queries get student method to find the student associated with an ID
                students.add(StudentQueries.getStudent(resultSet.getString("studentID")));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return students;
    }
    
    
    //method that clears all values from the database
    public static void clearDatabase()
    {
        //creates connection to database
        connection = DBConnection.getConnection();
        try
        {
            //removes all rows from all tables in the database
            clearDatabase = connection.prepareStatement("delete from app.class");
            clearDatabase.executeUpdate();
            clearDatabase = connection.prepareStatement("delete from app.course");
            clearDatabase.executeUpdate();
            clearDatabase = connection.prepareStatement("delete from app.schedule");
            clearDatabase.executeUpdate();
            clearDatabase = connection.prepareStatement("delete from app.semester");
            clearDatabase.executeUpdate();
            clearDatabase = connection.prepareStatement("delete from app.student");
            clearDatabase.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
}
