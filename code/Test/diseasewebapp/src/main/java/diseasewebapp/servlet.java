package diseasewebapp;

import java.io.IOException;
import java.io.PrintWriter;

// import javax.servlet.ServletException;
// import javax.servlet.http.*;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class servlet extends HttpServlet {
    public void processrequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset-UTF-8");
        PrintWriter out = response.getWriter();
        try {
            
            out.println("<html>");
            out.println("<h2>Hello all from servlet</h2>");
            out.println("<h3>Servlet NewServlet at " + request.getContextPath() + "</h3>");
            String user = request.getParameter("age");
            out.println("<h2> age is " + user + " .</h2>");
            out.println("</html>");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
 
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processrequest(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processrequest(request, response);
    }

}
