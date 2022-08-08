package ca.yorku.eecs;
import java.io.IOException;
import java.io.OutputStream;

import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MyApp implements HttpHandler{
	
	Neo4jBacon db = Neo4jBacon.getInstance();

	@Override
	public void handle(HttpExchange request) throws IOException {
		
		
		// TODO Auto-generated method stub
		
		try {
            if (request.getRequestMethod().equals("GET")) {
                handleGet(request);
                System.out.println("get");
            }else if(request.getRequestMethod().equals("PUT")){
            	
            	 handlePut(request);
            	 System.out.println("put");
            	 
            	
            	
            } else
            	sendString(request, "Unimplemented method\n", 501);
        } catch (Exception e) {
        	e.printStackTrace();
        	sendString(request, "Server error\n", 500);
        }
		
	}
	
	private void sendString(HttpExchange request, String data, int restCode) 
			throws IOException {
		request.sendResponseHeaders(restCode, data.length());
        OutputStream os = request.getResponseBody();
        os.write(data.getBytes());
        os.close();
	}
	
private void handleGet(HttpExchange request) throws IOException {
    	
        URI uri = request.getRequestURI();
        
       
      
        
        
        if(uri.getPath().contains("getActor")) {
        	
        	JSONObject jsonBody = null;
        	
        	String id = null;
        	
        	String body = Utils.convert(request.getRequestBody());
        	
        	try {
				jsonBody = new JSONObject(body);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	
        	
        	try {
				 id = jsonBody.getString("actorId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	JSONObject response = db.getActor(id);
        	
        	sendString(request,response.toString(),200);
        	
        	
        	
        	 
        }else if(uri.getPath().contains("getMovie")) {
        	
        	JSONObject jsonBody = null;
        	
        	String id = null;
        	
        	String body = Utils.convert(request.getRequestBody());
        	
        	try {
				jsonBody = new JSONObject(body);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	
        	
        	try {
				 id = jsonBody.getString("movieId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	JSONObject str = db.getMovie(id);
        	

        	
        	
        	
        	sendString(request,str.toString(),200);
        }
        
        else if(uri.getPath().contains("hasRelationship")) {
        	
        	JSONObject jsonBody = null;
        	
        	String actorId = null;
        	
        	String movieId = null;
        	
        	String body = Utils.convert(request.getRequestBody());
        	
        	try {
				jsonBody = new JSONObject(body);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	
        	
        	try {
				 movieId = jsonBody.getString("movieId");
				 actorId = jsonBody.getString("actorId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        	
        	
        
        	JSONObject str = db.hasRelationship(actorId, movieId);
        	
        	sendString(request,str.toString(),200);
        	
        	
        	
        }
        
        else if(uri.getPath().contains("computeBaconNumber")) {
     	   
     	   
     	   String actorId = null;
     	   
     	   String body = Utils.convert(request.getRequestBody());
     	   JSONObject jsonBody = null;
     	   
     	   try {
    			jsonBody = new JSONObject(body);
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	
    	
    	
    	try {
    			 actorId = jsonBody.getString("actorId");
    			 
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
     	  
    	JSONObject response = db.computeBaconNumber(actorId);
    	
    	sendString(request,response.toString(),200);
     	   
        }
        
        
        else if(uri.getPath().contains("computeBaconPath")) {
     	   
     	   
            String actorId = null;
     	   
     	   String body = Utils.convert(request.getRequestBody());
     	   JSONObject jsonBody = null;
     	   JSONObject response = null;
     	   
     	   try {
    			jsonBody = new JSONObject(body);
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	
    	
    	
    	try {
    			 actorId = jsonBody.getString("actorId");
    			 
    		} catch (JSONException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
     	  
    	       response = db.computeBaconPath(actorId);
    	
    	       sendString(request,response.toString(),200);
        }
	

       
    }

private void handlePut(HttpExchange request) throws IOException{
	
	
	 URI uri = request.getRequestURI();
     String query = uri.getQuery();
     
     
    
     
     
    
     
     if(uri.getPath().contains("addActor")) {
    	 
    	 
    	 
    	 
    	 String name = null;
         
         String actorId = null;
         
         JSONObject jsonBody = null;
         
         String body = Utils.convert(request.getRequestBody());
         
         
         try {
				jsonBody = new JSONObject(body);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     	
     	
     	
     	try {
				 name = jsonBody.getString("name");
				 actorId = jsonBody.getString("actorId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
     	
         
         db.addActor(actorId, name);
    	 
    	 String response = "Actor "+name +" has been added";
    	 
    	
     	sendString(request,response,200);
    	 
     
     }else if(uri.getPath().contains("addMovie")){
    	 
    	 String name = null;
         
         String movieId = null;
         
         JSONObject jsonBody = null;
         
         String body = Utils.convert(request.getRequestBody());
         
         try {
				jsonBody = new JSONObject(body);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  	
  	
  	
  	try {
				 name = jsonBody.getString("name");
				 movieId = jsonBody.getString("movieId");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
  	
         
         
         
    	 
    	 db.addMovie(movieId, name);
    	 
    	 String response = "Movie "+name +" has been added";
    	 sendString(request,response,200);
    	 
       }else if(uri.getPath().contains("addRelationship")) {
   	 
   	    String actorId = null;
        
        String movieId = null;
        
        String body = Utils.convert(request.getRequestBody());
        
        JSONObject jsonBody = null;
        
        try {
			jsonBody = new JSONObject(body);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
	
	try {
			 actorId = jsonBody.getString("actorId");
			 movieId = jsonBody.getString("movieId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
      
        
    	db.addRelationship(actorId, movieId);
    	 
   	   String response = "Relationship Added";
   	   
   	   sendString(request,response, 200);
   }
     

     

     
   
}



}
