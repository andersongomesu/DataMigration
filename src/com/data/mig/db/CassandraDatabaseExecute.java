package com.data.mig.db;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

public class CassandraDatabaseExecute {

	
	@SuppressWarnings("unused")
	public boolean executeCassandraQuery(Cluster dbcluster, String keyspaceName,String query) {

		Session dbsession = null;
		ResultSet results=null;
		boolean executeSuccess =false;

		try {
			dbsession= dbcluster.connect(keyspaceName);

			results = dbsession.execute(query);
			executeSuccess = true;    

		} catch (Exception ce) {
			executeSuccess=false;
			System.out.println("Query execute error code :" + ce.getMessage());
		}

		return executeSuccess;

	}
	
	
}
