import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class StudentQueries 
{
    private static Connection connection;
    private static PreparedStatement addStudent;
    private static PreparedStatement getAllStudents;
    private static PreparedStatement getStudent;
    private static PreparedStatement dropStudent;
    private static ResultSet resultSet;
    
    //method that adds a student to a database
    public static void addStudent(StudentEntry student)
    {
        //creates connection to database
        connection = DBConnection.getConnection();
        try
        {
            //pulls info from student entry and adds it to database
            addStudent = connection.prepareStatement("insert into app.student (studentID, firstName, lastName) values (?,?,?)");
            addStudent.setString(1, student.getStudentID());
            addStudent.setString(2, student.getFirstName());
            addStudent.setString(3, student.getLastName());
            addStudent.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }
    
    //method that returns a list of all students
    public static ArrayList<StudentEntry> getAllStudents()
    {
        connection = DBConnection.getConnection();
        ArrayList<StudentEntry> students = new ArrayList<StudentEntry>();
        try
        {
            //collects all information about students from database and returns it
            getAllStudents = connection.prepareStatement("select studentID, firstName, lastName from app.student order by lastName");
            resultSet = getAllStudents.executeQuery();
            
            while(resultSet.next())
            {
                students.add(new StudentEntry(resultSet.getString("studentID"), resultSet.getString("firstName"), resultSet.getString("lastName")));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return students;
    }
    
    //gets and returns the student associated with a specific ID
    public static StudentEntry getStudent(String studentID)
    {
        StudentEntry student = null;
        connection = DBConnection.getConnection();
        try
        {
            
            //collects all information about a student from database and returns it
            getStudent = connection.prepareStatement("select studentID, firstName, lastName from app.student where studentID = (?)");
            getStudent.setString(1, studentID);
            resultSet = getStudent.executeQuery();
            
            while(resultSet.next())
            {
                student = new StudentEntry(resultSet.getString("studentID"), resultSet.getString("firstName"), resultSet.getString("lastName"));
            }
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
        return student;
    }
  
    
    //removes a student from the student database
    public static void dropStudent(String studentID)
    {
        connection = DBConnection.getConnection();
        try
        {
            
            dropStudent = connection.prepareStatement("delete from app.student where studentID = (?)");
            dropStudent.setString(1, studentID);
            dropStudent.executeUpdate();
        }
        catch(SQLException sqlException)
        {
            sqlException.printStackTrace();
        }
    }     
}
