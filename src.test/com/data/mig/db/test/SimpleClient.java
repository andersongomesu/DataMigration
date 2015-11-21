package com.data.mig.db.test;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;

import java.util.HashSet;
import java.util.Set;
import  java.util.UUID;


public class SimpleClient {
   private Cluster cluster;
   private Session session;
   

   public void connect() {
	   
	   
	  // Cluster.Builder builder = Cluster.builder();
	//   Cluster cluster=builder.withLoadBalancingPolicy(new TokenAwarePolicy(new DCAwareRoundRobinPolicy("datacenter1"))).addContactPoints("127.0.0.1").build();
      cluster = Cluster.builder()
            .addContactPoint("localhost")
            .build();
      Metadata metadata = cluster.getMetadata();
     System.out.printf("Connected to cluster: %s\n", 
            metadata.getClusterName());
      for ( Host host : metadata.getAllHosts() ) {
         System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n",
               host.getDatacenter(), host.getAddress(), host.getRack());
      }
      session = cluster.connect();
   }

   public void close() {
      cluster.close();
   }
   public void createCassandraKeyspace() {
	   
	   session.execute("CREATE KEYSPACE simplex WITH replication " + 
			      "= {'class':'SimpleStrategy', 'replication_factor':3};");
	   
	  
   }
   public void createCassandraColumnfamily() {
	       session.execute(
			      "CREATE TABLE simplex.songs (" +
			            "id uuid PRIMARY KEY," + 
			            "title text," + 
			            "album text," + 
			            "artist text," + 
			            "tags set<text>," + 
			            "data blob" + 
			            ");");
			session.execute(
			      "CREATE TABLE simplex.playlists (" +
			            "id uuid," +
			            "title text," +
			            "album text, " + 
			            "artist text," +
			            "song_id uuid," +
			            "PRIMARY KEY (id, title, album, artist)" +
			            ");");
   }
   public void loadData() { 
	   
	      session.execute(
			      "INSERT INTO simplex.songs (id, title, album, artist, tags) " +
			      "VALUES (" +
			          "756716f7-2e54-4715-9f00-91dcbea6cf50," +
			          "'La Petite Tonkinoise'," +
			          "'Bye Bye Blackbird'," +
			          "'Joséphine Baker'," +
			          "{'jazz', '2013'})" +
			          ";");
			session.execute(
			      "INSERT INTO simplex.playlists (id, song_id, title, album, artist) " +
			      "VALUES (" +
			          "2cc9ccb7-6221-4ccb-8387-f22b6a1b354d," +
			          "756716f7-2e54-4715-9f00-91dcbea6cf50," +
			          "'La Petite Tonkinoise'," +
			          "'Bye Bye Blackbird'," +
			          "'Joséphine Baker'" +
			          ");");
   }

   public void loaddatawithBoundstatement()
   {
	   PreparedStatement statement = session.prepare(
			      "INSERT INTO simplex.songs " +
			      "(id, title, album, artist, tags) " +
			      "VALUES (?, ?, ?, ?, ?);");
	   BoundStatement boundStatement = new BoundStatement(statement);
	   Set<String> tags = new HashSet<String>();
	   tags.add("jazz");
	   tags.add("2013");
	   session.execute(boundStatement.bind(
	         UUID.fromString("756716f7-2e54-4715-9f00-91dcbea6cf51"),
	         "La Petite Tonkinoise1'",
	         "Bye Bye Blackbird1'",
	         "Joséphine Baker1",
	         tags ) );
	   
	   
	   statement = session.prepare(
			      "INSERT INTO simplex.playlists " +
			      "(id, song_id, title, album, artist) " +
			      "VALUES (?, ?, ?, ?, ?);");
			boundStatement = new BoundStatement(statement);
			session.execute(boundStatement.bind(
			      UUID.fromString("2cc9ccb7-6221-4ccb-8387-f22b6a1b354e"),
			      UUID.fromString("756716f7-2e54-4715-9f00-91dcbea6cf51"),
			      "La Petite Tonkinoise1",
			      "Bye Bye Blackbird1",
			      "Joséphine Baker1") );
	   
   }
   
   public void querySchema()
   {
	   ResultSet results = session.execute("SELECT * FROM simplex.playlists; " );
		     //   "WHERE id = 2cc9ccb7-6221-4ccb-8387-f22b6a1b354e;");
	   System.out.println(String.format("%-30s\t%-20s\t%-20s\n%s", "title", "album", "artist",
		       "-------------------------------+-----------------------+--------------------"));
		for (Row row1 : results) {
		    System.out.println(String.format("%-30s\t%-20s\t%-20s", row1.getString("title"),
		    row1.getString("album"),  row1.getString("artist")));
		}
		System.out.println();
   }
   public static void main(String[] args) {
      SimpleClient client = new SimpleClient();
      client.connect();
      //client.createCassandraKeyspace();
      //client.createCassandraColumnfamily(); 
      client.loadData();
      client.loaddatawithBoundstatement();
      client.querySchema();
      client.close();
   }
}