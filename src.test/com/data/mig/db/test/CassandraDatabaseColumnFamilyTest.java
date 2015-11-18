package com.data.mig.db.test;

import junit.framework.Assert;

import org.junit.Test;

import com.data.mig.db.CassandraDatabaseColumnFamily;
import com.data.mig.db.CassandraDatabaseConnect;
import com.data.mig.db.CassandraDatabaseKeyspace;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

@SuppressWarnings("deprecation")
public class CassandraDatabaseColumnFamilyTest {
	
	
	
	@Test
	public void createCassandraColumnFamilyAlreadyExistsTest() {
		
	    CassandraDatabaseConnect cassandraDatabaseConnect = new CassandraDatabaseConnect();
	    CassandraDatabaseColumnFamily cassandraColumnFamily = new CassandraDatabaseColumnFamily ();
	    
		Cluster cluster = cassandraDatabaseConnect.getCassandraDBConnection();
		
		Session dbsession = cassandraColumnFamily.createCassandraColumnFamily(cluster, "mykeyspace", "users1");
		Assert.assertNotNull(dbsession);
		
	}
	
	@Test
	public void createCassandraColumnFamilyTest() {
		
		  CassandraDatabaseConnect cassandraDatabaseConnect = new CassandraDatabaseConnect();
		    CassandraDatabaseColumnFamily cassandraColumnFamily = new CassandraDatabaseColumnFamily ();
		    
			Cluster cluster = cassandraDatabaseConnect.getCassandraDBConnection();
			
			Session dbsession = cassandraColumnFamily.createCassandraColumnFamily(cluster, "mykeyspace", "junitusers1");
			Assert.assertNotNull(dbsession);
		
	}	
	
	
	@Test
	public void deleteCassandraColumnFamilyTest() {
		
		CassandraDatabaseConnect cassandraDatabaseConnect = new CassandraDatabaseConnect();
	    CassandraDatabaseColumnFamily cassandraColumnFamily = new CassandraDatabaseColumnFamily ();
	    
		Cluster cluster = cassandraDatabaseConnect.getCassandraDBConnection();
		
		Session dbsession = cassandraColumnFamily.deleteCassandraColumnFamily(cluster, "mykeyspace", "junitusers1");
		Assert.assertNotNull(dbsession);
		
	}	
	

}
