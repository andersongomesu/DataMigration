package com.data.mig.rest.service.impl;

import com.data.mig.rest.service.IRestDataMigrationTestService;

public class RestDataMigrationTestServiceImpl implements IRestDataMigrationTestService{

	@Override
	public String getHelloWorld() {
		
		
		String message = "Test rest service is working !!!";
		
		return message;
	}

}
