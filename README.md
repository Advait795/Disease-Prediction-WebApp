# Disease-Prediction-WebApp

Disease Prediction Website. Final year project MSc Computer Science

**Project Name** : Disease Prediction Website

**Description** : This website is designed to help users assess the likelihood of having a specific disease using a machine learning model know as Naïve Bayes. The process is simple and user friendly:

1. Data Collection: User input their relevant data through a simple form. The data points include various symptoms, medical history, lifestyle factors, or other relevant information.
2. Machine Learning Analysis: Once the user submits their data, our backend system processes it using Naïve Bayes algorithm. This algorithm, know for its simplicity and effectiveness in classification tasks, calculates the probability of the user having the disease based on the provided information.
3. Results and Reporting: After the analysis, the website displays the probability of having the disease. Users can also download a detailed report summarizing the findings for their records.
4. Email Notification: If desired, users can opt to receive an email that outlines the entire process, and the source of data etc.

Website aims to provide a quick, easy and informative way for users to assess their health risk using advanced machine learning techniques.

**Features** :

1. User-Friendly Data Collection Form
   A simple, intuitive form that allows users to input relevant health data such as symptoms, medical history, and other key indicators.
2. Real-Time Disease Probability Calculation
   The website uses a Naive Bayes machine learning model to analyze the submitted data and instantly calculate the probability of having the disease.
3. Detailed Results Display
   Users receive a clear and concise display of the calculated probability, helping them understand their risk level based on the provided information.
4. Report Generation and Download
   After receiving the results, users have the option to generate a detailed report that summarizes their input data, the calculated probability, and any other relevant information. The report is available for download in PDF format.
5. Email Notification System
   Users can opt to receive the detailed information about project via email. Which informs about the entire process and machine learning model with link to access the data resource.
6. Comprehensive Explanation of Results
   The website provides an explanation of how the Naive Bayes model works, helping users understand the basis of the probability calculation.
7. Access to Data Sources
   Users can access information about the data sources used to train the Naive Bayes model, ensuring transparency and trust in the results provided through email.
8. Responsive Design
   The website is fully responsive, ensuring a seamless user experience across different devices, including desktops, tablets, and smartphones.

**Installation**
To set up and run this project locally, follow the steps below:

- Prerequisites
  Before you begin, ensure that you have the following installed on your system:
  • Java Development Kit (JDK) 8 or higher: Required to compile and run the Java application.
  • Apache Maven 3.6.0 or higher: Used for project build and dependency management.
  • Apache Tomcat 10.2.1: A web server used to deploy and test the application locally.

- Step-by-Step Installation

1. Clone the Repository
   a. Begin by cloning the project repository from GitHub:

git clone https://github.com/Advait795/Disease-Prediction-WebApp.git

2. Navigate to the project directory:
   cd diseasewebapp

3. Build the Project with Maven
   Use Maven to compile the project and resolve dependencies:
   mvn clean install
   This will generate a .war file in the target directory, which is required for deployment.

4. Configure Apache Tomcat
   download and install Apache Tomcat 10.2.1.
   Once installed, navigate to the webapps directory in your Tomcat installation folder.
   Copy the generated .war file from the target directory into the webapps directory.

5. Deploy and Run the Application
   Start Apache Tomcat by running the startup script:

- On Windows:
  C:\path\to\tomcat\bin\startup.bat
- On Linux/MacOS:
  /path/to/tomcat/bin/startup.sh
  Once Tomcat is running, open your web browser and go to:
  http://localhost:8080/diseasewebapp/
  This will load the application, and you can start interacting with the form, submitting data, and viewing the results.

6. Access the Application
   You can now access the application locally through your web browser at:
   http://localhost:8080/diseasewebapp/
   Here, you can use the form to input data, receive a probability analysis using the Naive Bayes model, and generate/download reports.

**Usage**
Usage
This section guides you through using the key features of the website, from navigating the homepage to generating detailed reports.

1. Access the Website
   • Open your web browser and navigate to the following URL:
   http://localhost:8080/diseasewebapp/
   This will load the homepage of your application, where you can begin interacting with its features.

2. Navigate the Homepage
   The homepage consists of three main sections:
   Welcome Section
   • This is the introductory section of the website, greeting you with a brief overview.
   • Explore “Get Started” Button: Click this button to smoothly scroll down to the Services section.
   Service Section
   • The Services section provides an overview of what the website offers.
   • Evaluate Button: Click the "Evaluation" button to proceed to the form page, where you can input your data for analysis.
   Contact Section
   • The Contact section allows you to request more detailed information about the project.
   • Get Info Button: Enter your email address in the provided field and click the "Get Info" button. This action will send an email containing detailed information about the project's working mechanism, data sources, and the machine learning model used.

3. Fill Out the Form
   • After clicking "Evaluate" in the Services section, you will be redirected to the form page.
   • Input Data: Fill out the form with your relevant health data, such as symptoms, medical history, and other factors.
   • Submit the Form: After filling in the required fields, click the "Submit" button to process your data.

4. View Results
   • After submitting the form, you will be taken to the results page.
   • Results Display: The page displays the probability of having various diseases, presented with accompanying charts for better visualization.
   • Disease Tabs: Each disease has its own tab where you can view specific results.
   • Analyze Report Button: In each disease tab, there's an "Analyze Report" button. Click this button to go to the report page for a detailed analysis.

5. Generate and Download the Report
   • On the report page, you'll find an in-depth report that includes detailed calculations and insights derived from your input data.
   • Download Report: To save the report, simply click the "Download Report" button. This action will generate and download the report in PDF format to your device.

6. Receive Detailed Information via Email
   • If you used the "Get Info" button in the Contact section, check your email for a message that includes:
   o An overview of how the project works.
   o Information on the data sources used.
   o Details about the Naive Bayes machine learning model implemented in the analysis.

7. Close the Application
   • simply close your web browser to end your session.

**Machine Learning Model**
Our application utilizes the Naive Bayes machine learning model, a probabilistic classifier ideal for predicting disease likelihood based on user input.

Model Overview
Naive Bayes applies Bayes' Theorem, assuming that all input features are independent, to calculate the probability of each disease. To enhance computational stability and efficiency, we use logarithmic transformations of the probabilities, which allows us to add log-probabilities rather than multiply small probabilities together. After calculation, the results are rounded for easy interpretation.

Handling Data
• Categorized Training Data: The model is trained on categorized datasets, meaning continuous values were converted into discrete categories to simplify the analysis.
• Direct Input Use: In the application, user inputs are directly categorized through the form, aligning with the model's training data format.

Implementation
• Data Processing: User inputs, categorized as necessary, are processed to align with the model's trained features.
• Probability Calculation: The Naive Bayes model evaluates the likelihood of various diseases based on these inputs, applying logarithmic transformations.
• Result Presentation: Final probabilities are rounded and presented clearly, with options for detailed reporting.
This approach ensures that the model effectively utilizes categorized data, providing accurate and understandable predictions for users.

**Code Flow**
The following is a step-by-step explanation of the code flow within the application, detailing how different components interact with each other, including relevant Java files:

1. Welcome Page (welcome.html)
   • Get Info Button:
   When the "Get Info" button is clicked, the request is sent to the backend, triggering SendEmail.java and EmailServlet.java.
   SendEmail.java: Handles the logic for preparing the email content.
   EmailServlet.java: Manages the sending of the email containing detailed information about the project.

   • Evaluation Button:
   Clicking the "Evaluate" button navigates the user to predict.html, where the evaluation form is presented.

2. Form Submission (predict.html)
   • The user fills out the form on predict.html and submits it.
   • The form submission is handled by Servlet.java, which processes the input data and interacts with the Naive Bayes model.
   Servlet.java: Receives the form data and forwards it to NB.java for processing.
   NB.java: Implements the Naive Bayes algorithm to calculate the probability of each disease based on the user's input.
   • The results from NB.java are returned to Servlet.java, which then prepares and formats the response.

3. Processing and Displaying Results
   • Loader:
   While the results are being calculated, predict.js runs loader.html to display a loading animation to the user.
   • Results Display:
   Once the processing is complete, the results are passed back to predict.js, which updates result.html with the calculated probabilities.
   result.html: Displays the results along with visual charts for each disease, providing an easy-to-understand overview.

4. Analyze Report (result.html to report.html)
   • On result.html, if the user clicks the "Analyze Report" button:
   The relevant data is sent to report.html, where a comprehensive report is generated.
   report.html: Presents an in-depth analysis and calculations based on the user’s input, offering detailed insights.

5. PDF Report Generation
   • Download Report:
   On report.html, there is an option to download the detailed report in PDF format.
   Clicking the "Report Download" button triggers the generation of a PDF file that encapsulates the entire HTML structure of the report.

**Acknowledgements**
I would like to extend my gratitude to the following individuals and resources that contributed to the successful completion of this project:

Supervisor: Special thanks to Mr. Sergey Ovchinnik, Lecturer in Computer Science at the University of Kent, for his invaluable guidance and support throughout the development of this project. His insights and suggestions were crucial in refining the project's scope and enhancing its quality.

Technologies Used:

- Apache Tomcat: For serving as the web server that enabled local deployment and testing of the application.
- HTML, CSS, and JavaScript: For creating the user interface and providing a seamless user experience.
- Java and Servlets: For handling server-side processing and implementing the application's backend logic.
- Naive Bayes Algorithm: For its simplicity and effectiveness in predicting disease probabilities based on user input.
- Email Server: For enabling email notifications and detailed project information to users.
- Open Source Libraries and Tools: The project utilized various open-source libraries and tools that were integral to its development. Thank you to the open-source community for providing these valuable resources.

**Contact**
Adwait Dalvi (ad918)
Email: ad918@kent.ac.uk
