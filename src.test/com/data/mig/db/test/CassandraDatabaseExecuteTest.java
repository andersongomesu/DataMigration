package com.data.mig.db.test;

import org.junit.Test;

import com.data.mig.db.CassandraDatabaseColumnFamily;
import com.data.mig.db.CassandraDatabaseConnect;
import com.data.mig.db.CassandraDatabaseExecute;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class CassandraDatabaseExecuteTest {
	
	
	
	
	@Test
	public void executeCassandraQueryTest() {
		
		CassandraDatabaseConnect cassandraDatabaseConnect = new CassandraDatabaseConnect();
		CassandraDatabaseExecute cassandraexecute = new CassandraDatabaseExecute();
	    
		Cluster cluster = cassandraDatabaseConnect.getCassandraDBConnection();
		
		String query ="select * from users;";
		boolean results = cassandraexecute.executeCassandraQuery(cluster, "mykeyspace", query);
		Assert.assertNotNull(results);
		
	}	
	

}
