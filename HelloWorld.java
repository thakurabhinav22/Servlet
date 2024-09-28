import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class HelloWorldExample extends HttpServlet {

    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/users"; // Change to your database name
    private static final String DB_USER = "root"; // Change to your database username
    private static final String DB_PASSWORD = "root"; // Change to your database password
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver"; // MySQL JDBC Driver

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("UserName");
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Load the JDBC driver
            Class.forName(DB_DRIVER);
            // Establish the connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // SQL query to check if the user exists with the given password
            String sql = "SELECT * FROM user_cred WHERE username = ? AND password = ?";

            // Create a PreparedStatement object
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Check if a record was found
            if (resultSet.next()) {
                pw.println("Login Successful:<br>");

                // Create a session
                HttpSession session = request.getSession();
                
                // Optionally, you can store information in the session
                session.setAttribute("username", username);
                
                // Retrieve and display the session ID
                String sessionId = session.getId();
                pw.println("Session ID: " + sessionId);
            } else {
                pw.println("Login Failed:");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace(pw);
            pw.println("Database Driver not found.");
        } catch (SQLException e) {
            e.printStackTrace(pw);
            pw.println("Database error: " + e.getMessage());
        } finally {
            // Close the resources
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace(pw);
            }
        }

        pw.println("<br>UserName: ");
        pw.println(username);
        pw.println("<br>Password: ");
        pw.println(password);

        pw.close();
    }
}
