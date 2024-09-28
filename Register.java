import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.util.logging.*;

public class Register extends HttpServlet {
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/users";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private static final Logger logger = Logger.getLogger(Register.class.getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String Fname = request.getParameter("Name");
        String sname = request.getParameter("Sname");
        String userid = request.getParameter("uid");
        String password = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();

        // pw.println("DataReceive");
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the database
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // SQL query to insert the data
            String sql = "INSERT INTO user_cred (firstname, lastname, username, password) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, Fname);
            pstmt.setString(2, sname);
            pstmt.setString(3, userid);
            pstmt.setString(4, password); 

            // Execute the update
            int rowsInserted = pstmt.executeUpdate();
            pw.println(rowsInserted);
            if (rowsInserted > 0) {
                pw.println("Registration successful!");
            } else {
                pw.println("Failed to register. Please try again.");
            }
        } catch (ClassNotFoundException e) {
            logger.severe("Database Driver not found: " + e.getMessage());
            pw.println("Database Driver not found.");
        } catch (SQLException e) {
            logger.severe("Database error: " + e.getMessage());
            pw.println("Database error: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                logger.severe("Failed to close resources: " + e.getMessage());
            }
        }

        pw.close();
    }
}
