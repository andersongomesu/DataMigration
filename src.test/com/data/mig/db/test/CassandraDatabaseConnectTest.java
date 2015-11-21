package com.data.mig.db.test;

import org.junit.Test;

import com.data.mig.db.CassandraDatabaseConnect;
import com.datastax.driver.core.Cluster;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class CassandraDatabaseConnectTest {
	

	@Test
	public void getCassandraDatabaseConnectTest() {
		CassandraDatabaseConnect cassandraDatabaseConnect = new CassandraDatabaseConnect();
		
		Cluster cluster = cassandraDatabaseConnect.getCassandraDBConnection();
		
		Assert.assertNotNull(cluster);
		
		
	}

}
