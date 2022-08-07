package ca.yorku.eecs;

import static org.neo4j.driver.v1.Values.parameters;

import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.naming.spi.DirStateFactory.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.neo4j.driver.internal.InternalStatementResult;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;


public class Neo4jBacon {

	
	private Driver driver;
	private String uriDb;
	
	private static Neo4jBacon ins;
	
	private Neo4jBacon() {
		uriDb = "bolt://localhost:7687"; // may need to change if you used a different port for your DBMS
		Config config = Config.builder().withoutEncryption().build();
		driver = GraphDatabase.driver(uriDb, AuthTokens.basic("neo4j","1234"), config);
	}
	
	
	public static Neo4jBacon getInstance(){
		
		if(ins == null) {
			
			ins = new Neo4jBacon();
		}
		
		return ins;
		
		
		
	}
	
	
	public void addActor(String id, String name) {
		
		try (Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MERGE (a:Actor {name: $x, id: $y})", 
					parameters("x", name, "y", id)));
			session.close();
		}
		
	}
	
	public void addMovie(String id, String name) {
		
		try (Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MERGE (a:Movie {name: $x, id: $y})", 
					parameters("x", name, "y", id)));
			session.close();
		}
		
		
	}
	
	
	public void addRelationship(String actorId, String movieId) {
		
		try (Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MATCH(a:Actor) WITH (a) MATCH (m:Movie) WHERE m.id=$x AND a.id=$y CREATE (a)-[:ACTED_IN]->(m)", 
					parameters("x", movieId, "y", actorId)));
			session.close();
		}
		
		
	}
	
	
	/*
	 * 
	 * 
	 *     tx.run( "match (a:Actor{id:$x}) RETURN (a) ",
                                        parameters( "x", actorId ) );
	 * 
	 * /
	 */
	public JSONObject getActor(String actorId) {
		
	     
		JSONObject response = new JSONObject();
	
        try(Session session = driver.session()){
        	
        	
        	try (Transaction tx = session.beginTransaction()){
        		
        		StatementResult result = tx.run("match (a:Actor{id:$x}) RETURN (a.name) as a ",
                        parameters( "x", actorId ));
        		
        		StatementResult resultArray = tx.run("match (m:Movie)--(:Actor{id:$x}) return m.id",parameters("x",actorId));

        		
        		
        		
        		
        	
                 if(result.hasNext()) {
					
					String name = result.next().get(0).asString();
					try {
						response.put("name", name);
						
						response.put("actorId", actorId);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
                 
		      ArrayList<String> movieList = new ArrayList<>();
				
				while(resultArray.hasNext()) {
					
					
					String id = resultArray.next().get(0).asString();
					movieList.add(id);
					
					
				}
				
				
								
				try {
					response.put("movies", movieList);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                 
                
        	}
        }
		return response;
       
	       
	}
	
	
	public JSONObject getMovie(String movieId) {
		
		JSONObject response = new JSONObject();
		
		
		try(Session session = driver.session()){
			
			try(Transaction tx = session.beginTransaction()){
				
				StatementResult result = tx.run("match (m:Movie{id:$x}) RETURN (m.name) as m",parameters("x", movieId));
				
				StatementResult resultArray = tx.run("match (a:Actor)--(m:Movie{id:$x}) return a.id",parameters("x",movieId));
				
				
				
				
				
				if(result.hasNext()) {
					
					String name = result.next().get(0).asString();
					
					
					
					
					try {
						response.put("name", name);
						
						response.put("movieId", movieId);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
				}
				
				ArrayList<String> actorList = new ArrayList<>();
				
				while(resultArray.hasNext()) {
					
					
					String id = resultArray.next().get(0).asString();
					actorList.add(id);
					
					
				}
				
				
								
				try {
					response.put("actors", actorList);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
		}
		return response;
	}
	
	public JSONObject hasRelationship(String actorId, String movieId) {
		
		
		
		boolean hasRelation;
		
		try(Session session = driver.session()){
			
			
			try(Transaction tx = session.beginTransaction()){
				
				
			StatementResult result = tx.run("match (m:Movie{id:$x})--(a:Actor{id:$y}) return (a) ",parameters("x", movieId,"y", actorId));
			
			
			
			hasRelation = result.hasNext();
			
			}
			
		
			
		
		}
		
		
		JSONObject response = new JSONObject();
		
		try {
			response.put("actorId", actorId);
			response.put("movieId",movieId);
			
			response.put("hasRelationship", hasRelation);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	return response;
		
		
	}
	
	
}
