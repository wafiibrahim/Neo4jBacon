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
import org.neo4j.driver.v1.types.Path;


public class Neo4jBacon {

	
	private Driver driver;
	private String uriDb;
	private static Neo4jBacon ins;
	
	private String actorId, actorName, movieId, movieName;
	private boolean hasRelation;
	private int baconNumber ;
	private ArrayList<String> actorList;


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
		this.actorId = id;
		this.actorName = name;
		
		try (Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MERGE (a:Actor {name: $x, id: $y})", 
					parameters("x", actorName, "y", actorId)));
			session.close();
		}
		
	}
	
	public void addMovie(String id, String name) {
		this.movieId = id;
		this.movieName = name;
		try (Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MERGE (a:Movie {name: $x, id: $y})", 
					parameters("x", movieName, "y", movieId)));
			session.close();
		}
		
		
	}
	
	
	public void addRelationship(String actorId, String movieId) {
		this.actorId = actorId;
		this.movieId = movieId;
		try (Session session = driver.session()){
			session.writeTransaction(tx -> tx.run("MATCH(a:Actor) WITH (a) MATCH (m:Movie) WHERE m.id=$x AND a.id=$y CREATE (a)-[:ACTED_IN]->(m)", 
					parameters("x", this.movieId, "y", this.actorId)));
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
		this.actorId = actorId;
	     
		JSONObject response = new JSONObject();
	
        try(Session session = driver.session()){
        	
        	
        	try (Transaction tx = session.beginTransaction()){
        		
        		StatementResult result = tx.run("match (a:Actor{id:$x}) RETURN (a.name) as a ",
                        parameters( "x", this.actorId ));
        		
        		StatementResult resultArray = tx.run("match (m:Movie)--(:Actor{id:$x}) return m.id",parameters("x",this.actorId));

        		
        		
        		
        		
        	
                 if(result.hasNext()) {
					
					String name = result.next().get(0).asString();
					try {
						response.put("name", name);
						
						response.put("actorId", this.actorId);
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
		this.movieId = movieId;
		
		JSONObject response = new JSONObject();
		
		
		try(Session session = driver.session()){
			
			try(Transaction tx = session.beginTransaction()){
				
				StatementResult result = tx.run("match (m:Movie{id:$x}) RETURN (m.name) as m",parameters("x", this.movieId));
				
				StatementResult resultArray = tx.run("match (a:Actor)--(m:Movie{id:$x}) return a.id",parameters("x",this.movieId));
				
				
				
				
				
				if(result.hasNext()) {
					
					String name = result.next().get(0).asString();
					
					
					
					
					try {
						response.put("name", name);
						
						response.put("movieId", this.movieId);
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
		this.actorId = actorId;
		this.movieId = movieId;
		
		try(Session session = driver.session()){
			try(Transaction tx = session.beginTransaction()){
			StatementResult result = tx.run("match (m:Movie{id:$x})--(a:Actor{id:$y}) return (a) ",parameters("x", this.movieId,"y", this.actorId));
			hasRelation = result.hasNext();
			}
		}
		
		JSONObject response = new JSONObject();
		try 
		{
			response.put("actorId", this.actorId);
			response.put("movieId",this.movieId);	
			response.put("hasRelationship", hasRelation);
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
	
	// Match (n:Person{name:"Liv Tyler"}) with (n) MATCH (m:Person{name:"Lori Petty"}), p = shortestPath((n)-[*..15]-(m)) return length(p)
	
	
	public JSONObject computeBaconNumber(String actorId) {
		this.actorId = actorId;
		
		String bacon = "nm0000102";
		
		JSONObject response = new JSONObject();

		try(Session session = driver.session()){
			try(Transaction tx = session.beginTransaction()){
				StatementResult result = tx.run("Match (n:Actor{id:$x}) with (n) MATCH (m:Actor{id:$y}), p = shortestPath((n)-[*..15]-(m)) return length(p)",
						parameters("x",bacon,"y",this.actorId));
				
				if(result.hasNext()) {
					
					baconNumber = result.next().get(0).asInt();
					try {
						response.put("baconNumber", baconNumber);	
					} 
					catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				}
			}
			return response;	
		}	
	}
	
	public JSONObject computeBaconPath(String actorId) {
		this.actorId = actorId;
		
		JSONObject response = new JSONObject();
		
		actorList = new ArrayList<>();
		String bacon = "nm0000102";
		
		
		try(Session session = driver.session()){
			try(Transaction tx = session.beginTransaction()){		
				StatementResult result = tx.run("Match (n:Actor{id:$x}) with (n) MATCH (m:Actor{id:$y}), p = shortestPath((n)-[*..15]-(m)) UNWIND nodes(p) as nlist MATCH (nlist)<-[:ACTED_IN]-(a:Actor) RETURN a.id"
						,parameters("x",bacon,"y",this.actorId));
				
				while(result.hasNext()) {
					while(result.hasNext()) {						
						String id = result.next().get(0).asString();
						actorList.add(id);										
					}
				}				
				try {
					response.put("baconPath",actorList);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		return response;
		
		
	}


	
	
	//Getter & Setter
	public String getActorId() {
		return actorId;
	}


	public void setActorId(String actorId) {
		this.actorId = actorId;
	}


	public String getMovieId() {
		return movieId;
	}


	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}


	public String getMovieName() {
		return movieName;
	}


	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}


	public String getActorName() {
		return actorName;
	}


	public void setActorName(String actorName) {
		this.actorName = actorName;
	}


	public boolean isHasRelation() {
		return hasRelation;
	}


	public void setHasRelation(boolean hasRelation) {
		this.hasRelation = hasRelation;
	}
	
	public int getBaconNumber() {
		return baconNumber;
	}


	public void setBaconNumber(int baconNumber) {
		this.baconNumber = baconNumber;
	}
	
	public ArrayList<String> getActorList() {
		return actorList;
	}


	public void setActorList(ArrayList<String> actorList) {
		this.actorList = actorList;
	}

}