package com.data.mig.db.test;

import org.junit.Test;

import com.data.mig.db.MongoDatabaseCollection;
import com.data.mig.db.MongoDatabaseConnect;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class MongoDatabaseCollectionRemoveAllTest {

	@Test
	public void removeAllMongoCollectionTest() {

		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection();

		DB mongodb = mangoDatabaseConnect.getMongoDBConnection();

		DBCollection dbCollection = mangoDatabaseCollection.removeAllMongoCollection(mongodb, "mycol111");

		System.out.println("Collection count :" + dbCollection.count());

		Assert.assertNotNull(dbCollection);

	}

}
