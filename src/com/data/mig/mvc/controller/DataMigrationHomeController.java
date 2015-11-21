package com.data.mig.mvc.controller;

import java.util.ArrayList; 
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.MysqlSchemaDetails;
import com.sun.jersey.core.util.FeaturesAndProperties;
import org.glassfish.jersey.server.spi.Container;

@Controller
@RequestMapping("/dm")
public class DataMigrationHomeController {
	
	
	
	@RequestMapping(path="/home", method = RequestMethod.GET)
	public ModelAndView home() {

		ModelAndView model = new ModelAndView();
		
		
		MysqlSchemaDetails mysqlSchemaDetails = new MysqlSchemaDetails();
		
		
		
		List<String> tableDetailsList = mysqlSchemaDetails.getMysqlTableDetails(null, IApplicationConstants.defaultMySqlSchemaName);
		
		List<String> schemaDetailsList = new ArrayList<String> ();
		
		schemaDetailsList.add("classicmodels");
		schemaDetailsList.add("test");
		
		model.addObject("schemaDetailsList", schemaDetailsList);
		
		model.addObject("tableDetails", tableDetailsList);
		model.setViewName("dmhome");
		model.addObject("msg", "welcome");

		return model;

	}

}
