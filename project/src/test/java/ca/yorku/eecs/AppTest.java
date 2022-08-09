package ca.yorku.eecs;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public void addActorPass() {
    	Neo4jBacon newActor = Neo4jBacon.getInstance();
    	newActor.addActor("nm1004", "Jamie Fox");
    	String id = newActor.getActorId();
    	assertEquals(id,"nm1004");
    }
    
    public void addActorFail() {
    	Neo4jBacon newActor = Neo4jBacon.getInstance();
    	newActor.addActor("777", "Jamie Fox");
    	String id = newActor.getActorId();
    	assertNotSame(id,"nm1004");
    }
    
    public void addMoviePass() {
    	Neo4jBacon newMovie = Neo4jBacon.getInstance();
    	newMovie.addMovie("nm7003", "Django Unchained");
    	String id = newMovie.getMovieId();
    	assertEquals(id,"nm7003");
    }
    
    public void addMovieFail() {
    	Neo4jBacon newMovie = Neo4jBacon.getInstance();
    	newMovie.addMovie("111", "Django Unchained");
    	String id = newMovie.getMovieId();
    	assertNotSame(id,"nm7003");
    }
    
    public void addRelationshipPass() {
    	Neo4jBacon newRelationship = Neo4jBacon.getInstance();
    	newRelationship.addRelationship("nm1004", "nm7003");
    	String actorId = newRelationship.getActorId();
    	String movieId = newRelationship.getMovieId();
    	assertEquals(movieId,"nm7003");
    	assertEquals(actorId,"nm1004");
    }
    
    public void addRelationshipFail() {
    	Neo4jBacon newRelationship = Neo4jBacon.getInstance();
    	newRelationship.addRelationship("777", "111");
    	String actorId = newRelationship.getActorId();
    	String movieId = newRelationship.getMovieId();
    	assertNotSame(movieId,"nm7003");
    	assertNotSame(actorId,"nm1004");
    }
    
    public void getActorPass() {
    	Neo4jBacon actor = Neo4jBacon.getInstance();
    	actor.getActor("nm1001");
    	
    	String getActor = actor.getActorId();
    	
    	assertEquals(getActor, "nm1001");
    }
    
    public void getActorFail() {
    	Neo4jBacon actor = Neo4jBacon.getInstance();
    	actor.getActor("nm1001");
    	
    	String getActor = actor.getActorId();
    	
    	assertNotSame(getActor, "777");
    }
    
    public void getMoviePass() {
    	Neo4jBacon movie = Neo4jBacon.getInstance();
    	movie.getMovie("nm7002");
    	
    	String getmovie = movie.getMovieId();
    	
    	assertEquals(getmovie, "nm7002");
    }
    
    public void getMovieFail() {
    	Neo4jBacon movie = Neo4jBacon.getInstance();
    	movie.getMovie("nm7002");
    	
    	String getmovie = movie.getMovieId();
    	
    	assertNotSame(getmovie, "111");
    }
    
    public void hasRelationshipPass() {
    	Neo4jBacon relationship = Neo4jBacon.getInstance();
    	relationship.hasRelationship("nm1001", "nm7001");
    	
    	boolean relation = relationship.isHasRelation();
    	
    	assertEquals(relation, true);  	
    }
    
    public void hasRelationshipFail() {
    	Neo4jBacon relationship = Neo4jBacon.getInstance();
    	relationship.hasRelationship("111", "444");
    	
    	boolean relation = relationship.isHasRelation();
    	
    	assertEquals(relation, false);   	
    }
    
    public void computeBaconNumberPass() { //NOT WORKING
    	Neo4jBacon bcNum = Neo4jBacon.getInstance();
    	bcNum.computeBaconNumber("nm1001");
    	
    	int baconNum = bcNum.getBaconNumber();
    	
    	assertEquals(baconNum, 3);
    }
    
    public void computeBaconNumberFail() {
    	Neo4jBacon bcNum = Neo4jBacon.getInstance();
    	bcNum.computeBaconNumber("nm1001");
    	
    	int baconNum = bcNum.getBaconNumber();
    	
    	assertNotSame(baconNum, 10);
    }
    
    public void computeBaconPathPass() { //NOT WORKING
    	Neo4jBacon bcPath = Neo4jBacon.getInstance();
    	bcPath.computeBaconPath("nm1001");
    	ArrayList<String> baconPath = new ArrayList<>();
    	baconPath = bcPath.getActorList();
    	    	
    	assertEquals(baconPath, "{\"baconPath\":['nm0000102'','nm1002','nm1001']}");
    }
    
    public void computeBaconPathFail() { 
    	Neo4jBacon bcPath = Neo4jBacon.getInstance();
    	bcPath.computeBaconPath("nm1001");
    	ArrayList<String> baconPath = new ArrayList<>();
    	baconPath = bcPath.getActorList();
    	    	
    	assertNotSame(baconPath, "{\"baconPath\":['nm0000102'',blueberry,'nm1001']}");
    }
}
