# BJREST

  RestApi
  
  @GET
	http://localhost:8080/BJREST/ <br>
  Get simple rest client (HTML+JQuery).
  
  @GET
  http://localhost:8080/BJREST/{userID} <br>
  Get user info. Return JsonObject.
  
  @POST
  http://localhost:8080/BJREST/{userID}/refill/{sum} <br>
  Balance refill the sum of {sum}. Return status.
  
  @GET
  http://localhost:8080/BJREST/{userID}/newgame/{bet} <br>
  Create new game. Return gameID(long).
  
  @GET
  http://localhost:8080/BJREST/{userID}/{gameID} <br>
  Start game. Return JsonObject with player's and dealer's cards, points.
  
  @POST
  http://localhost:8080/BJREST/{userID}/{gameID}/hit <br>
  Hit. Return JsonObject with player's and dealer's cards, points.
  
  @POST
  http://localhost:8080/BJREST/{userID}/{gameID}/stand <br>
  Stand. Return JsonObject with player's and dealer's cards, points.
  
  
  
