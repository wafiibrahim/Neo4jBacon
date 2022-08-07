package ca.yorku.eecs;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;

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
        String query = uri.getQuery();
       
        Map<String, String> queryParam = splitQuery(query);
        
        
        
        if(uri.getPath().contains("getActor")) {
        	
        	
        	String id = queryParam.get("id");
        	
        	JSONObject str = db.getActor(id);
        	
        	sendString(request,str.toString(),200);
        	
        	
        	
        	
        	 
        }else if(uri.getPath().contains("getMovie")) {
        	
        	String id = queryParam.get("id");
        	
        	JSONObject str = db.getMovie(id);
        	

        	
        	
        	
        	sendString(request,str.toString(),200);
        }
        
        else if(uri.getPath().contains("hasRelationship")) {
        	
        	
        	String actorId = queryParam.get("actorId");
        	
        	String movieId = queryParam.get("movieId");
        	
        	
        	
        	JSONObject str = db.hasRelationship(actorId, movieId);
        	
        	sendString(request,str.toString(),200);
        	
        	
        	
        }
	

       
    }

private void handlePut(HttpExchange request) throws IOException{
	
	
	 URI uri = request.getRequestURI();
     String query = uri.getQuery();
     Map<String, String> queryParam = splitQuery(query);
     
    
     
     
    
     
     if(uri.getPath().contains("addActor")) {
    	 
    	 
    	 
    	 
    	 String name = queryParam.get("name");
         
         String id = queryParam.get("id");
    	 
    	 db.addActor(id, name);
    	 
    	 
    	
    	
    	 
     	 String response = "Actor "+name +" has been added";
    	 
    	
     	sendString(request,response,200);
    	 
    	 
    	 
  	 
    	 
     }else if(uri.getPath().contains("addMovie")){
    	 
    	 String name = queryParam.get("name");
         
         String id = queryParam.get("id");
    	 
    	 db.addMovie(id, name);
    	 
    	 String response = "Movie "+name +" has been added";
    	 sendString(request,response,200);
    	 
       }else if(uri.getPath().contains("addRelationship")) {
   	 
   	    String actorId = queryParam.get("actorId");
        
        String movieId = queryParam.get("movieId");
    	db.addRelationship(actorId, movieId);
    	 
   	   String response = "Relationship Added";
   	   
   	   sendString(request,response, 200);
   }
     
     
	//http://localhost:8080/api/v1/addRelationship?actorId=nm00012&movieId=0010
	
}

private static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
    Map<String, String> query_pairs = new LinkedHashMap<String, String>();
    String[] pairs = query.split("&");
    for (String pair : pairs) {
        int idx = pair.indexOf("=");
        query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
    }
    return query_pairs;
}


}
