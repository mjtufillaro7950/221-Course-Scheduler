import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassQueries 
{
    private static Connection connection;
    private static PreparedStatement addClass;
    private static PreparedStatement getAllCourseCodes;
    private static PreparedStatement getClassSeats;
    private static PreparedStatement dropClass;
    private static ResultSet resultSet;
    
    public static void addClass(ClassEntry classEntry)
    {
        connection = DBConnection.getConnection();
        try
        {
            //pulls data from classEntry and adds it to database
            addClass = connection.prepareStatement("insert into app.class (semester, coursecode, seats) values (?,?,?)");
            addClass.setString(1, classEntry.getSemester());
            addClass.setString(2, classEntry.getCourseCode());
            addClass.setInt(3, classEntry.getSeats());
            addClass.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    //gets and returns all coursecodes for a specific semester
    public static ArrayList<String> getAllCourseCodes(String semester)
    {
        connection = DBConnection.getConnection();
        ArrayList<String> codes = new ArrayList<String>();
        try
        {
            getAllCourseCodes = connection.prepareStatement("select coursecode from app.class where semester = (?) order by coursecode");
            getAllCourseCodes.setString(1, semester);
            resultSet = getAllCourseCodes.executeQuery();
            
            while(resultSet.next())
            {
                codes.add(resultSet.getString("coursecode"));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return codes;
    }
    
    //gets and returns the number of seats for a specific class in a specific semester
    public static int getClassSeats(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        int seats = 0;
        try
        {        
            getClassSeats = connection.prepareStatement("select seats from app.class where semester = (?) and courseCode = (?)");
            getClassSeats.setString(1, semester);
            getClassSeats.setString(2, courseCode);
            resultSet = getClassSeats.executeQuery();
            while(resultSet.next())
            {
                seats = resultSet.getInt("seats");
            }  
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return seats;
    }
    
    //removes all schedules for a specific class in the class database
    public static void dropClass(String semester, String courseCode)
    {
        connection = DBConnection.getConnection();
        try
        {

            dropClass = connection.prepareStatement("delete from app.class where semester = (?) and coursecode = (?)");
            dropClass.setString(1, semester);
            dropClass.setString(2, courseCode);
            dropClass.executeUpdate();
            
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }  
}
