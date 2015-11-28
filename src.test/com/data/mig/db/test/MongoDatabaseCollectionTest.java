package com.data.mig.db.test;

import org.junit.Test;

import com.data.mig.db.MongoDatabaseCollection;
import com.data.mig.db.MongoDatabaseConnect;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class MongoDatabaseCollectionTest {

	@Test
	public void getMongoDBCollectionTest() {

		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection();

		DB mongodb = mangoDatabaseConnect.getMongoDBConnection();

		DBCollection dbCollection = mangoDatabaseCollection.getMongoCollection(mongodb, "mycolbbbb");

		System.out.println("Collection count :" + dbCollection.count());

		Assert.assertNotNull(dbCollection);

	}

	@Test
	public void createMongoCollectionAlreadyExistsTest() {

		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection();

		DB mongodb = mangoDatabaseConnect.getMongoDBConnection();

		DBCollection dbCollection = mangoDatabaseCollection.createMongoCollection(mongodb, "mycol");

		System.out.println("Collection count :" + dbCollection.count());

		Assert.assertNotNull(dbCollection);

	}

	@Test
	public void createMongoCollectionTest() {

		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection();

		DB mongodb = mangoDatabaseConnect.getMongoDBConnection();

		DBCollection dbCollection = mangoDatabaseCollection.createMongoCollection(mongodb, "customer");

		System.out.println("Collection count :" + dbCollection.count());

		Assert.assertNotNull(dbCollection);

	}

	@Test
	public void dropMongoCollectionTest() {

		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection();

		DB mongodb = mangoDatabaseConnect.getMongoDBConnection();

		DBCollection dbCollection = mangoDatabaseCollection.dropMongoCollection(mongodb, "mycol1");

		System.out.println("Collection count :" + dbCollection.count());

		Assert.assertNotNull(dbCollection);

	}

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
