package com.data.mig.read.json.extract.test;

import java.sql.SQLException;

import org.junit.Test;

import com.data.mig.read.json.extract.ReadJsonDataFromFile;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class ReadJsonDataFromFileToCassandraTest {

	@Test
	public void getMysqlDataExtractWithChildForCassandraTest() throws SQLException {

		ReadJsonDataFromFile readJsonDataFromFile = new ReadJsonDataFromFile();

/*		boolean fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("classicmodels","productlines","mykeyspace",
				"productlines", "D:\\Habi\\MS\\Git\\MySQL",true);*/

		boolean fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("classicmodels","customers","mykeyspace",
				"customers", "D:\\Habi\\MS\\Git\\MySQL\\CassandraExtracts\\",true);
		
		Assert.assertTrue(fileReadSuccess);

	}
	@Test
	public void getMysqlDataExtractForCassandraTest() throws SQLException {

		ReadJsonDataFromFile readJsonDataFromFile = new ReadJsonDataFromFile();

		boolean fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("classicmodels","customers","mykeyspace",
				"customers", "D:\\Habi\\MS\\Git\\MySQL\\CassandraExtracts\\customers.json",false);
	
		 fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("classicmodels","payments","mykeyspace",
				"payments", "D:\\Habi\\MS\\Git\\MySQL\\CassandraExtracts\\payments.json",false);
		 
		 fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("classicmodels","products","mykeyspace",
				"products", "D:\\Habi\\MS\\Git\\MySQL\\CassandraExtracts\\products.json",false);
		 
		 fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("classicmodels","productlines","mykeyspace",
				"productlines", "D:\\Habi\\MS\\Git\\MySQL\\CassandraExtracts\\productlines.json",false);
		 
		 fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("classicmodels","orders","mykeyspace",
					"orders", "D:\\Habi\\MS\\Git\\MySQL\\CassandraExtracts\\productlines.json",false);

		 
		 fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("classicmodels","employees","mykeyspace",
					"employees", "D:\\Habi\\MS\\Git\\MySQL\\CassandraExtracts\\employees.json",false);
			 
			
			 
			 fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("classicmodels","orderdetails","mykeyspace",
					"productlines", "D:\\Habi\\MS\\Git\\MySQL\\CassandraExtracts\\orderdetails.json",false);
			 
			 fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("classicmodels","office","mykeyspace",
						"office", "D:\\Habi\\MS\\Git\\MySQL\\CassandraExtracts\\office.json",false);
						

	//	boolean fileReadSuccess = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase("classicmodels","customers","mykeyspace",
		//		"customers", "D:\\Sampath\\MS\\Dissertation\\MySQL\\extract.json",false);
	
		
		Assert.assertTrue(fileReadSuccess);

	}

}
