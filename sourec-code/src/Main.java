import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement; // Use the standard JDBC Statement class

public class Main {
    public static void main(String[] args) {

        Connection connection = DBConnection.createDBConnection();

        if (connection != null) {
            try {
                // Create a statement
                Statement statement = connection.createStatement();

                // Execute a sample SQL query
                ResultSet resultSet = ((java.sql.Statement) statement).executeQuery("SELECT 'Database connection is working!' AS message");

                // Display the result
                if (resultSet.next()) {
                    String message = resultSet.getString("message");
                    System.out.println(message);
                }

                // Close the statement and connection
                resultSet.close();
                ((java.sql.Statement) statement).close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Failed to establish a database connection.");
        }
    }
}