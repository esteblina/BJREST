# BJREST

  RestApi
  
  @GET
  http://localhost:8080/BJREST/ <br>
  Get simple rest-client (HTML+JQuery).
  
  @GET
  http://localhost:8080/BJREST/{userID} <br>
  Get user info. Return JsonObject with info.
  
  @POST
  http://localhost:8080/BJREST/{userID}/refill/{sum} <br>
  Refill the balance on {sum}. Return Response.status.
  
  @GET
  http://localhost:8080/BJREST/{userID}/newgame/{bet} <br>
  Create new game. Return gameID(long).
  
  @GET
  http://localhost:8080/BJREST/{userID}/{gameID} <br>
  Start the game. Return JsonObject with player's and dealer's cards and points.
  
  @POST
  http://localhost:8080/BJREST/{userID}/{gameID}/hit <br>
  Hit.  Return JsonObject with player's and dealer's cards, points and also winner in case the game will be ended.
  
  @POST
  http://localhost:8080/BJREST/{userID}/{gameID}/stand <br>
  Stand. Return JsonObject with player's and dealer's cards, points and winner.
  <hr> 
  
  DB: hsqldb-2.3.3 (contains in WEB-INF)<br>
  UnitTest: junit-4.12
