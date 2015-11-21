package com.data.mig.db;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraDatabaseKeyspace {

	public Session getCassandraKeyspace(Cluster dbcluster, String keyspaceName) {

		
		Session dbsession = dbcluster.connect(keyspaceName);
		return dbsession;
		

	}

	public Session createCassandraKeyspace(Cluster dbcluster, String keyspaceName) {

		Session dbsession = null;

		try {
			dbsession= dbcluster.connect();
			dbsession.execute("CREATE KEYSPACE "+ keyspaceName +" WITH replication " + 
				      "= {'class':'SimpleStrategy', 'replication_factor':3};");


		} catch (Exception ce) {
			System.out.println("Keyspace create error code :" + ce.getMessage());
		}

		return dbsession;

	}
	
	public Session deleteCassandraKeyspace(Cluster dbcluster, String keyspaceName) {

		Session dbsession = null;
		try {

			dbsession= dbcluster.connect();
			dbsession.execute("DROP KEYSPACE "+keyspaceName);


		} catch (Exception ce) {
			System.out.println("Keyspace drop error code :" + ce.getMessage());
		}

		return dbsession;

	
	}	

}
