package com.data.mig.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mongo.online.load.MysqlToMongoOnlineLoad;
import com.data.mig.mvc.forms.OnlineLoadForm;
import com.data.mig.mysql.db.MysqlSchemaDetails;

@Controller
@RequestMapping("/dm")
public class DataMigrationHomeController {

	@RequestMapping(path = "/home", method = RequestMethod.GET)
	public ModelAndView home() {

		OnlineLoadForm onlineLoadForm = new OnlineLoadForm();

		MysqlSchemaDetails mysqlSchemaDetails = new MysqlSchemaDetails();

		List<String> tableNameList = mysqlSchemaDetails.getMysqlTableDetails(null,
				IApplicationConstants.defaultMySqlSchemaName);

		List<String> sourceSchemaList = new ArrayList<String>();

		sourceSchemaList.add("classicmodels");
		// schemaDetailsList.add("test");

		ModelAndView model = new ModelAndView("dmhome", "command", onlineLoadForm);
		model.addObject("schemaDetailsList", sourceSchemaList);

		model.addObject("tableNameList", tableNameList);

		return model;

	}

	@RequestMapping(path = "/submitOnlineLoad", method = RequestMethod.POST)
	public ModelAndView loadOnline(@ModelAttribute("SpringWeb") OnlineLoadForm onlineLoadForm, ModelMap model) {

		System.out.println("Source database :" + onlineLoadForm.getSourceDatabase());
		MysqlToMongoOnlineLoad mysqlToMongoOnlineLoad = new MysqlToMongoOnlineLoad();

		Boolean successFlag = mysqlToMongoOnlineLoad.loadDataFromMysqlToMongo(onlineLoadForm.getSourceSchema(),
				onlineLoadForm.getSourceTableName(), onlineLoadForm.getTargetDatabase(),
				onlineLoadForm.getTargetCollectionOrColumnFamilyName(), onlineLoadForm.getNoOfRecordsToBeExtracted(),
				Boolean.valueOf(onlineLoadForm.getChildTableExtractRequired()));
		
		
		if(successFlag) {
			onlineLoadForm.setMessage(IApplicationConstants.onlineLoadSuccessMessage);
			ModelAndView modelAndView = new ModelAndView("success", "command", onlineLoadForm);
			modelAndView.addObject("onlineLoadForm", onlineLoadForm);
			return modelAndView;
		} else {
			onlineLoadForm.setMessage(IApplicationConstants.onlineLoadFailureMessage);
			return new ModelAndView("failure", "command", onlineLoadForm);
		}
	}
	
	@RequestMapping(path = "/success", method = RequestMethod.POST)
	public ModelAndView onlineLoadSuccess (@ModelAttribute("SpringWeb") OnlineLoadForm onlineLoadForm, ModelMap model) {
		
		
		
		return new ModelAndView("dmhome", "command", onlineLoadForm);
	}
	
	@RequestMapping(path = "/failure", method = RequestMethod.POST)
	public ModelAndView onlineLoadFailure (@ModelAttribute("SpringWeb") OnlineLoadForm onlineLoadForm, ModelMap model) {

		
		return new ModelAndView("dmhome", "command", onlineLoadForm);
	}		

}
