import groovy.json.JsonSlurper

import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FitbitImportServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        println 'Import'

        def code = req.getParameter('code')
        URL tokenUrl = new URL('https://api.fitbit.com/oauth2/token')
        HttpURLConnection tokenConnection = (HttpURLConnection) tokenUrl.openConnection()
        tokenConnection.requestMethod = 'POST'
        tokenConnection.doOutput = true
        tokenConnection.setRequestProperty('Authorization', 'Basic MjI4MlFEOmVkM2Y4YWZjNmI0M2I5MmU0MDRkM2MyNWFkMGI0MDk5')
        tokenConnection.setRequestProperty('Content-Type', 'application/x-www-form-urlencoded')
        tokenConnection.outputStream.withWriter {
            it.write("client_id=2282QD&grant_type=authorization_code&code=$code")
            it.flush()
        }
        tokenConnection.responseCode
        def tokenJson = new JsonSlurper().parseText tokenConnection.inputStream.text

        CsvParser.parseImportFile().each { CsvEntity entity ->
            println "activityId=90013&startTime=12:00:00&durationMillis=${entity.activeTime}&date=${entity.date}&distance=${entity.steps}&distanceUnit=steps"
            URL activitiesUrl = new URL("https://api.fitbit.com/1/user/$tokenJson.user_id/activities.json")
            def activitiesUrlConnection = (HttpURLConnection) activitiesUrl.openConnection()
            activitiesUrlConnection.requestMethod = 'POST'
            activitiesUrlConnection.doOutput = true
            activitiesUrlConnection.setRequestProperty('Authorization', "Bearer $tokenJson.access_token")
            activitiesUrlConnection.outputStream.withWriter {
                it.write("activityId=90013&startTime=12:00:00&durationMillis=${entity.activeTime}&date=${entity.date}&distance=${entity.steps}&distanceUnit=steps")
                it.flush()
            }
            println activitiesUrlConnection.inputStream.text
            Thread.sleep 1000
        }

    }
}
