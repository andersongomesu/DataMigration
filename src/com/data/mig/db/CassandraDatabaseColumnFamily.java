package com.data.mig.db;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraDatabaseColumnFamily {

	
	public Session createCassandraColumnFamily(Cluster dbcluster, String keyspaceName,String columnfamilyName) {

		Session dbsession = null;

		try {
			dbsession= dbcluster.connect(keyspaceName);

			dbsession.execute("CREATE TABLE "+ columnfamilyName + "(title text PRIMARY KEY" +");");


		} catch (Exception ce) {
			System.out.println("ColumnFamily create error code :" + ce.getMessage());
		}

		return dbsession;

	}
	
	public Session deleteCassandraColumnFamily(Cluster dbcluster, String keyspaceName,String columnfamilyName) {

		Session dbsession = null;
		try {

			dbsession= dbcluster.connect(keyspaceName);
			dbsession.execute("DROP TABLE "+columnfamilyName);


		} catch (Exception ce) {
			System.out.println("ColumnFamily drop error code :" + ce.getMessage());
		}

		return dbsession;

	
	}	

}
