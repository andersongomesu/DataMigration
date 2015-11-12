package com.data.mig.read.json.extract.test;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Test;

import com.data.mig.read.json.extract.ReadJsonDataFromFile;

@SuppressWarnings("deprecation")
public class ReadJsonDataFromFileTest {
	
	@Test
	public void getMysqlDataExtractTest() throws SQLException {
		
		ReadJsonDataFromFile readJsonDataFromFile = new ReadJsonDataFromFile();

		boolean fileReadSuccess = readJsonDataFromFile.readJsonDataFromFile("D:\\Sampath\\MS\\Dissertation\\MySQL\\extract.json");

		Assert.assertTrue(fileReadSuccess);

	}

}
