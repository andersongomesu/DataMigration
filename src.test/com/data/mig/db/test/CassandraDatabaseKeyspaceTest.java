package com.data.mig.db.test;

import junit.framework.Assert;

import org.junit.Test;

import com.data.mig.db.CassandraDatabaseConnect;
import com.data.mig.db.CassandraDatabaseKeyspace;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

@SuppressWarnings("deprecation")
public class CassandraDatabaseKeyspaceTest {
	
	@Test
	public void getCassandraKeyspaceTest() {
		
	    CassandraDatabaseConnect cassandraDatabaseConnect = new CassandraDatabaseConnect();
	    CassandraDatabaseKeyspace cassandraKeyspace = new CassandraDatabaseKeyspace ();
	    
		Cluster cluster = cassandraDatabaseConnect.getCassandraDBConnection();
		
		Session dbsession = cassandraKeyspace.getCassandraKeyspace(cluster, "mykeyspace");
		Assert.assertNotNull(dbsession);
		
	}
	
	@Test
	public void createCassandraKeyspaceAlreadyExistsTest() {
		
	    CassandraDatabaseConnect cassandraDatabaseConnect = new CassandraDatabaseConnect();
	    CassandraDatabaseKeyspace cassandraKeyspace = new CassandraDatabaseKeyspace ();
	    
		Cluster cluster = cassandraDatabaseConnect.getCassandraDBConnection();
		
		Session dbsession = cassandraKeyspace.createCassandraKeyspace(cluster, "classicmodels");
		Assert.assertNotNull(dbsession);
		
	}
	
	@Test
	public void createCassandraKeyspaceTest() {
		
		CassandraDatabaseConnect cassandraDatabaseConnect = new CassandraDatabaseConnect();
		CassandraDatabaseKeyspace cassandraKeyspace = new CassandraDatabaseKeyspace ();
		    
	    Cluster cluster = cassandraDatabaseConnect.getCassandraDBConnection();
			
	    Session dbsession = cassandraKeyspace.createCassandraKeyspace(cluster, "mytestkeyspace");
		Assert.assertNotNull(dbsession);
		
	}	
	
	
	@Test
	public void deleteCassandraKeyspaceTest() {
		
		CassandraDatabaseConnect cassandraDatabaseConnect = new CassandraDatabaseConnect();
		CassandraDatabaseKeyspace cassandraKeyspace = new CassandraDatabaseKeyspace ();
		    
	    Cluster cluster = cassandraDatabaseConnect.getCassandraDBConnection();
			
	    Session dbsession = cassandraKeyspace.deleteCassandraKeyspace(cluster, "mykeyspace");
		Assert.assertNotNull(dbsession);
		
	}	
	

}
