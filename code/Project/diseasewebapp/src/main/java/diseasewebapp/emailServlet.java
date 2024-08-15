package diseasewebapp;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.*;
import java.io.IOException;
import java.io.PrintWriter;

public class emailServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // email add from form
        String email = request.getParameter("email");
        String body = "<html><body style='font-family: Arial, sans-serif; color: black;'>"
                + "<p>Dear user,</p>"
                + "<p>I hope this message finds you well.</p>"
                + "<p>At CareBioMed, we are committed to providing comprehensive health risk assessments to help identify and manage potential health conditions effectively. Our assessment form gathers detailed data to evaluate risks for conditions such as cardiovascular diseases and diabetes. We use the Naive Bayes algorithm to analyze this data and generate insightful risk scores. Here’s an overview of the data we collect, how we process it using Naive Bayes, and how this benefits your health management.</p>"
                + "<h3><b>1. Data Collection:</b></h3>"
                + "<ul>"
                + "<li><b>Demographic Information:</b><ul>"
                + "<li>Sex</li>"
                + "<li>Age</li></ul></li>"
                + "<li><b>Health Conditions:</b><ul>"
                + "<li>Hypertension</li>"
                + "<li>Heart Disease</li>"
                + "<li>Smoking History</li></ul></li>"
                + "<li><b>Key Health Metrics:</b><ul>"
                + "<li>BMI (Body Mass Index)</li>"
                + "<li>HbA1c Level: Indicates average blood glucose over the past three months.</li>"
                + "<li>Current Blood Glucose Level</li></ul></li>"
                + "<li><b>Cardiovascular Data:</b><ul>"
                + "<li>Chest Pain Type (cp)</li>"
                + "<li>Resting Blood Pressure (trestbps)</li>"
                + "<li>Cholesterol Level (chol)</li>"
                + "<li>Fasting Blood Sugar (fbs)</li>"
                + "<li>Resting Electrocardiographic Results (restecg)</li>"
                + "<li>Maximum Heart Rate Achieved (thalach)</li>"
                + "<li>Exercise-Induced Angina (exang)</li>"
                + "<li>ST Depression Induced by Exercise (oldpeak)</li>"
                + "<li>Slope of the Peak Exercise ST Segment (slope)</li>"
                + "<li>Number of Major Vessels Colored by Fluoroscopy (ca)</li>"
                + "<li>Thalassemia Status (thal)</li></ul></li>"
                + "<li><b>Socioeconomic Factors:</b><ul>"
                + "<li>Marital Status</li>"
                + "<li>Work Type</li>"
                + "<li>Residence Type</li>"
                + "<li>Average Glucose Level</li></ul></li>"
                + "</ul>"
                + "<h3><b>2. Analyzing Data with Naive Bayes:</b></h3>"
                + "<p>We use the Naive Bayes algorithm to analyze your health data and generate risk assessments. Here’s how it works:</p>"
                + "<ul>"
                + "<li><b>Naive Bayes Algorithm:</b> The Naive Bayes classifier is a probabilistic model based on Bayes' Theorem. It estimates the likelihood of a particular disease given your symptoms and health metrics. The key aspect of Naive Bayes is its assumption that features (in this case, symptoms and metrics) are independent of each other given the disease. Despite this simplification, it often provides highly accurate predictions.</li>"
                + "<li><b>Bayes' Theorem:</b> The algorithm uses Bayes' Theorem to calculate the probability of a disease given your data. The formula is:<br>"
                + "<code><b>P(Disease | Symptoms) = (P(Symptoms | Disease) * P(Disease)) / P(Symptoms)</b></code></li><br>"
                + "<ul>"
                + "<li><b>P(Disease | Symptoms):</b> The probability of having the disease given your symptoms and metrics.</li>"
                + "<li><b>P(Symptoms | Disease):</b> The likelihood of observing the specific symptoms if the disease is present.</li>"
                + "<li><b>P(Disease):</b> The prior probability of the disease based on its general occurrence rate.</li>"
                + "<li><b>P(Symptoms):</b> The probability of observing the symptoms.</li>"
                + "</ul></ul>"
                + "<h3><b>3. Generating Risk Scores and Reports:</b></h3>"
                + "<p>Our system processes your data using the Naive Bayes algorithm to calculate risk scores for conditions such as cardiovascular diseases and diabetes. The results are presented in a detailed report that includes:</p>"
                + "<ul>"
                + "<li><b>Risk Levels:</b> Indicating the probability of having certain conditions based on your data.</li>"
                + "</ul>"
                + "<h3><b>4. Benefits:</b></h3>"
                + "<p>By using Naive Bayes, we can efficiently analyze complex health data and provide you with accurate risk assessments. This allows healthcare providers to identify high-risk individuals and implement targeted, personalized interventions for improved health outcomes.</br></p>"
                +
                "<p>The data used in our assessments comes from Kaggle datasets, which ensures that we leverage high-quality, diverse data to enhance the accuracy of our risk predictions.</p>"
                + "<p>If you have any questions about our health risk assessment process or how Naive Bayes is used in our analysis, please feel free to reach out. We are here to support you and provide any additional information you may need.</p>"
                + "<p>Thank you for choosing CareBioMed.</p>"
                + "<p>Best regards,<br>CareBioMed Family</p>"
                + "</body></html>";

        // create an instance of send email class
        sendEmail Emailsender = new sendEmail();
        response.setContentType("application/json");
        PrintWriter output = response.getWriter();
        try {
            Emailsender.sendEmail(email, "Notification from CareBioMed", body);
            response.setStatus(HttpServletResponse.SC_OK);
            output.println("{\"status\":\"success\",\"message\":\"Email Sent Successfully\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            output.println("{\"status\":\"error\",\"message\":\"Error sending email: " + e.getMessage() + "\"}");
        }

    }
}
