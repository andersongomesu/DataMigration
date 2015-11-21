package com.data.mig.db;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;


public class CassandraDatabaseConnect {
	
	private String node = "127.0.0.1";
	
	
	public Cluster getCassandraDBConnection() {

		
		Cluster cluster = null;

		try {

			 cluster = Cluster.builder()
			            .addContactPoint(node)
			            .build();
			      Metadata metadata = cluster.getMetadata();
			      System.out.println("Connected to cluster:"+ 
				            metadata.getClusterName());
			      for ( Host host : metadata.getAllHosts() ) {
			         System.out.println("Datatacenter: "+host.getDatacenter() +"Host: "+host.getAddress()+" Rack : "+host.getRack());
			      }
			      

			
			}

		 catch (Exception e) {
			e.printStackTrace();
		}

		return cluster;

	}
	
	
	public Cluster getCassandraDBConnection(String nodename) {

		
		Cluster cluster = null;

		try {

			 cluster = Cluster.builder()
			            .addContactPoint(node)
			            .build();
			      Metadata metadata = cluster.getMetadata();
			      System.out.println("Connected to cluster:"+ 
			            metadata.getClusterName());
			      for ( Host host : metadata.getAllHosts() ) {
			         System.out.println("Datatacenter: "+host.getDatacenter() +"Host: "+host.getAddress()+" Rack : "+host.getRack());
			      }
			    

			
			}

		 catch (Exception e) {
			e.printStackTrace();
		}

		return cluster;

	}
	
	
	public boolean closeCassandraDBConnection(Cluster cluster) {
		
		boolean success = false;

		try {

			cluster.close();
			
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;

	}
	
	
}
