# jawbone-to-fitbit

How to migrate data form Jawbone to Fitbit?

Recently I switched to Fitbit tracker, but I wanted to import data from my old Jawbone tracker. After some investigation, I did not find any support for import of data to Fitbit.  But Jawbone provides functionality to export all stored data as CSV file.

So I decided to parse the file and log the data via the Fitbit API. Fitbit API requires authorization with OAuth 2 which means that a redirect to the Fitbit authentication website is required to provide permissions for usage of the user data. Hence will be needed a web container and web application. For the implementation is used Gradle (with jetty plugin - web container) and Groovy (Java Servlet API - web application).

The application has two servlets one for the permissions request called FitbitAuthServlet and one which does the actual import - FitbitImportServlet. The import servlet parses a given CSV file and executes requests to the Fitbit API.

To use the application, few steps are required:

1. Start the jetty server with deployed web application.
The server can be started with the gradle command: gradlew clean jettyRunWar -PimportFile=&lt;path-to-CSV-file&gt;. The application requires the importFile property to work properly. Currently it does not has any validation.
When the server is started, the 8080 port will be available. Please ensure that another software does not use this port.

2. Open http://localhost:8080/jawbone-to-fitbit/auth - this page will redirect to the Fitbit authentication site and will requirest permission for usage of the user data.
When the permissions are granted, Fitbit will redirect back to http://localhost:8080/import which will parse the CSV file and start execution of requests to the Fitbit API.


Note: The data will be represented in Fitbit as walk activity starting at 12 PM with duration total active minutes (from Jawbone) and distance in steps format. As final result, the user will see the steps taken for the day and will have 1 logged activity starting at 12 PM.

Note: Jawbone CSV files contains data for the whole year, but Fitbit has 150 requests per hour limitation. This means that only 150 days (only activities, sleep logs not included) can be migrated per hour. Hence before every execution the import file must be changed to hold only 150 records.

Note: The tool does not support sleep logs yet.

I wrote the tool for my needs. If you want to use it, but have questions, you can contact me at todor@mazgalov.name. If somebody is interested, I will extend the instructions, description and validation during the execution.