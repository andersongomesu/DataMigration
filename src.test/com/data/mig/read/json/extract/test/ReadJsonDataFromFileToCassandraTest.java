package com.data.mig.read.json.extract.test;

import java.sql.SQLException;

import org.junit.Test;

import com.data.mig.read.json.extract.ReadJsonDataFromFile;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class ReadJsonDataFromFileToCassandraTest {

	@Test
	public void getMysqlDataExtractForCassandraTest() throws SQLException {

		ReadJsonDataFromFile readJsonDataFromFile = new ReadJsonDataFromFile();

		boolean fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("classicmodels","mykeyspace",
				"customers", "D:\\Habi\\MS\\Git\\MySQL");

		/*boolean fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("classicmodels","mykeyspace",
				"customers", "D:\\Sampath\\MS\\Dissertation\\MySQL\\");*/
		
		Assert.assertTrue(fileReadSuccess);

	}

}
