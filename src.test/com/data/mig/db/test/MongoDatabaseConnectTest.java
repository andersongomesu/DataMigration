package com.data.mig.db.test;

import junit.framework.Assert;

import org.junit.Test;

import com.data.mig.db.MongoDatabaseConnect;
import com.mongodb.DB;

@SuppressWarnings("deprecation")
public class MongoDatabaseConnectTest {
	
	@Test
	public void getMongoDBConnectionTest() {
		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect ();
		
		DB mongodb = mangoDatabaseConnect.getMongoDBConnection();
		
		Assert.assertNotNull(mongodb);
		
		
	}

}
