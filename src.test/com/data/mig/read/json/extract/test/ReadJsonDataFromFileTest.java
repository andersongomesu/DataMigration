package com.data.mig.read.json.extract.test;

import java.sql.SQLException;

import org.junit.Test;

import com.data.mig.read.json.extract.ReadJsonDataFromFile;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class ReadJsonDataFromFileTest {
	
	@Test
	public void getMysqlDataExtractTest() throws SQLException {
		
		ReadJsonDataFromFile readJsonDataFromFile = new ReadJsonDataFromFile();

		boolean fileReadSuccess = readJsonDataFromFile.readJsonDataFromFile("test","mycol111", "D:\\Sampath\\MS\\Dissertation\\MySQL\\extract.json", "customers");

		Assert.assertTrue(fileReadSuccess);

	}
	@Test
	public void getMysqlDataExtractForCassandraTest() throws SQLException {
		
		ReadJsonDataFromFile readJsonDataFromFile = new ReadJsonDataFromFile();

		boolean fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("mykeyspace","customers", "D:\\Habi\\MS\\Git\\MySQL\\extract.json");

		Assert.assertTrue(fileReadSuccess);

	}


}
