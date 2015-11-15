package com.data.mig.db;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.ResultSet;

public class CassandraDatabaseExecute {

	
	public ResultSet executeCassandraQuery(Cluster dbcluster, String keyspaceName,String query) {

		Session dbsession = null;
		ResultSet results=null;

		try {
			dbsession= dbcluster.connect(keyspaceName);

			results = dbsession.execute(query);


		} catch (Exception ce) {
			System.out.println("Query execute error code :" + ce.getMessage());
		}

		return results;

	}
	
	
}
