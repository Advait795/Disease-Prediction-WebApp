package diseasewebapp;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class emailServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // email add from form
        String email = request.getParameter("email");

        // create an instance of send email class
        sendEmail sender = new sendEmail();
        try {
            sender.sendEmail(email, "Notoication from CareBioMed", "Thank you");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Email Sent Successfully");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error sending email: " + e.getMessage());
        }

    }
}
