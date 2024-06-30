package diseasewebapp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

// import javax.servlet.ServletException;
// import javax.servlet.http.*;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class servlet extends HttpServlet {
    private NB nb;

    @Override
    public void init(){
        String filename = "code\\Test\\diseasewebapp\\src\\main\\resources\\hypertension_data.csv";
        nb = new NB();
        int[] numclm = {0, 3, 4, 7};

        List<String[]> data = CSVReaderExample.readCSV(filename);

        boolean isFirstRow = true;

        //train model
        for(String[] row: data){
            if(isFirstRow){
                isFirstRow=false;
                continue;
            }

            String label = row[row.length -1];
            String[] features = new String[row.length - 1];
            System.arraycopy(row, 0, features, 0, row.length-1);

            for(int x : numclm){
                if(features[x].endsWith(".0")){
                    features[x] = features[x].substring(0, features[x].length()-2);
                }
            }

            nb.train(features, label);

        }
    }

    public void processrequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset-UTF-8");
        PrintWriter out = response.getWriter();
        try {
            //Reatrieve from input
            String[] newExample = {
                request.getParameter("age"),
                request.getParameter("sex"),
                request.getParameter("cp"),
                request.getParameter("trestbps"),
                request.getParameter("cholesterol"),
                request.getParameter("fbs"),
                request.getParameter("restecg"),
                request.getParameter("thalach"),
                request.getParameter("exang"),
                request.getParameter("oldpeak"),
                request.getParameter("slope"),
                request.getParameter("ca"),
                request.getParameter("thal")
            };

            //predict class
            String predictedClass = nb.predict(newExample);

            out.println("<html>");
            out.println("<h2>Disease Prediction Results</h2>");
            out.println("<h2>"+newExample+"</h2>");
            out.println("<h3>Predicted Class " + predictedClass + "</h3>");
            out.println("</html>");

        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            out.close();
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
