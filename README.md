Coding challenge(original task in file Coding_Evaluation.txt)

To run the Application:

  1.Run "requests-0.0.1-SNAPSHOT.jar" bz double clicking on Windows or by comand "java -jar requests-0.0.1-SNAPSHOT.jar" on Linux
  
  2.Send the Json request to http://localhost:8080/

Possible Json formats and endpoints:

  1. http://localhost:8080/ipBlacklist -> GET endpoint which lists all blacklisted ip's
  2. http://localhost:8080/requests -> GET enpoint which lists all requests in database
  3. http://localhost:8080/statisticCustomer/? -> GET endpoint which lists statistics for customer per day(instead of ? insert number)
  4. http://localhost:8080/statisticDay/? -> GET enpoint which lists statistics for each day(instead of ? insert date e.g. 2020-07-109(preferable format) )
  5. http://localhost:8080/requests -> POST endpoint which checks, validates and stores requests

JSON format for POST endpoint:
{
    "customerID":8,
    "tagID":2,
    "userID":"TEST",
    "remoteIP":"123.234.56.78",
    "timestamp":1594573581150 //date is calculated in LONG as was provided as example in task document
}

DATATABASE
Database used for this project was postgres. Exporterd datatabase will be in //TODO
Addition to the sql's for creating database, one more table was created in which are stored entire requests with original data.

Additions to the Project:
  1. As stated in Database section one more table was created. Table name is incoming_requests. I noticed that provided tables did not contain original requests format and it was not visible the amount of traffic or possible reasons why the requests would be invalid. By adding this table it will be easier to maintain and give customer feedback on possible reason for "declining" or marking the request as invalid. This would be possible as well through Logging system on Listening ports or implementing one in tthe Code itself.
 
 2.Quartz Scheduler was implemented in the code. Quartz is a scheduler witch activates when called by interval or triggered by event. For this task Quartz is called every 6 hours and when called it deletes data from new table incoming_requests older than 1 day. This serves as cleaning mechanism of the table which stores all of the incoming requests. Of course 6 hour interval is very small for deleting data from database, but it is used to showcase functionality which would be very useful in the project with high traffic.
 
 3. Basic Junit test were created to check the validity of each Controller.



