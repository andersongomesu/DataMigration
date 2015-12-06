package com.data.mig.mvc.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.db.MongoCollectionFind;
import com.data.mig.db.MongoDatabaseCollection;
import com.data.mig.db.MongoDatabaseConnect;
import com.data.mig.mongo.online.load.MysqlToMongoOnlineLoad;
import com.data.mig.mvc.forms.OnlineLoadForm;
import com.data.mig.mvc.forms.ProductLinesForm;
import com.data.mig.mysql.db.MysqlSchemaDetails;
import com.data.mig.mysql.extract.MysqlDataExtract;
import com.data.mig.mysql.extract.MysqlDataExtractWithChildTables;
import com.data.mig.read.json.extract.ReadJsonDataFromFile;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sun.org.apache.xml.internal.security.utils.Base64;

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

	@RequestMapping(path = "/batchHome", method = RequestMethod.POST)
	public ModelAndView batchLoad() {

		OnlineLoadForm onlineLoadForm = new OnlineLoadForm();

		MysqlSchemaDetails mysqlSchemaDetails = new MysqlSchemaDetails();

		List<String> tableNameList = mysqlSchemaDetails.getMysqlTableDetails(null,
				IApplicationConstants.defaultMySqlSchemaName);

		List<String> sourceSchemaList = new ArrayList<String>();

		sourceSchemaList.add("classicmodels");
		// schemaDetailsList.add("test");

		ModelAndView model = new ModelAndView("batchload", "command", onlineLoadForm);
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

		if (successFlag) {
			onlineLoadForm.setMessage(IApplicationConstants.onlineLoadSuccessMessage);
			ModelAndView modelAndView = new ModelAndView("success", "command", onlineLoadForm);
			modelAndView.addObject("onlineLoadForm", onlineLoadForm);
			return modelAndView;
		} else {
			onlineLoadForm.setMessage(IApplicationConstants.onlineLoadFailureMessage);
			return new ModelAndView("failure", "command", onlineLoadForm);
		}
	}

	@RequestMapping(path = "/submitBatchLoad", method = RequestMethod.POST)
	public ModelAndView batchLoad(@ModelAttribute("SpringWeb") OnlineLoadForm onlineLoadForm, ModelMap model) {

		System.out.println("Source database :" + onlineLoadForm.getSourceDatabase());
		Boolean successFlag = null;
		MysqlDataExtract mysqlDataExtract = null;
		MysqlDataExtractWithChildTables mysqlDataExtractWithChildTables = null;

		ReadJsonDataFromFile readJsonDataFromFile = null;

		try {
			if (onlineLoadForm.getChildTableExtractRequired() != null
					&& Boolean.valueOf(onlineLoadForm.getChildTableExtractRequired())) {
				mysqlDataExtract = new MysqlDataExtract();
				successFlag = mysqlDataExtract.extractMysqlDataIntoJsonFile(onlineLoadForm.getSourceSchema(),
						onlineLoadForm.getSourceTableName(), onlineLoadForm.getNoOfRecordsToBeExtracted(),
						onlineLoadForm.getFilePath());

			} else {
				mysqlDataExtractWithChildTables = new MysqlDataExtractWithChildTables();
				successFlag = mysqlDataExtractWithChildTables.extractMysqlDataIntoJsonFile(
						onlineLoadForm.getSourceSchema(), onlineLoadForm.getSourceTableName(),
						onlineLoadForm.getNoOfRecordsToBeExtracted(), onlineLoadForm.getFilePath());

			}

			if (successFlag) {
				System.out.println("Ëxtract has been successful ...");
				readJsonDataFromFile = new ReadJsonDataFromFile();

				successFlag = readJsonDataFromFile.readJsonDataFromFile(onlineLoadForm.getTargetDatabase(),
						onlineLoadForm.getTargetCollectionOrColumnFamilyName(), onlineLoadForm.getFilePath(),
						onlineLoadForm.getSourceTableName());

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (successFlag) {
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
	public ModelAndView onlineLoadSuccess(@ModelAttribute("SpringWeb") OnlineLoadForm onlineLoadForm, ModelMap model) {

		return new ModelAndView("dmhome", "command", onlineLoadForm);
	}

	@RequestMapping(path = "/failure", method = RequestMethod.POST)
	public ModelAndView onlineLoadFailure(@ModelAttribute("SpringWeb") OnlineLoadForm onlineLoadForm, ModelMap model) {

		return new ModelAndView("dmhome", "command", onlineLoadForm);
	}

	@RequestMapping(path = "/getProductLine", method = RequestMethod.POST)
	public ModelAndView getProductLines(@ModelAttribute("SpringWeb") ProductLinesForm productLinesForm,
			ModelMap model) {

		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection();

		DB mongodb = mangoDatabaseConnect.getMongoDBConnection("test");

		DBCollection dbCollection = mangoDatabaseCollection.getMongoCollection(mongodb, "mycol111");

		MongoCollectionFind mongoCollectionFind = new MongoCollectionFind();

		DBCursor dbProductLinesCursor = mongoCollectionFind.findAllProductLinesFromCollection(dbCollection);

		List<String> productLinesList = null;

		if (dbProductLinesCursor != null) {

			productLinesList = new ArrayList<String>();
			while (dbProductLinesCursor.hasNext()) {
				DBObject productLinesDbObject = dbProductLinesCursor.next();
				System.out.println(productLinesDbObject.get("productLine").toString());
				productLinesList.add(productLinesDbObject.get("productLine").toString());
			}

		}

		ModelAndView modelAndView = new ModelAndView("productline", "command", productLinesForm);
		modelAndView.addObject("productLineImageAsString", "");
		modelAndView.addObject("productLinesList", productLinesList);
		modelAndView.addObject("productLinesForm", productLinesForm);
		return modelAndView;

	}

	@RequestMapping(path = "/showProductLine", method = RequestMethod.POST)
	public ModelAndView showProductLines(@ModelAttribute("SpringWeb") ProductLinesForm productLinesForm,
			ModelMap model) {

		System.out.println("Product line :" + productLinesForm.getProductLine());
		MongoDatabaseConnect mangoDatabaseConnect = new MongoDatabaseConnect();
		MongoDatabaseCollection mangoDatabaseCollection = new MongoDatabaseCollection();

		DB mongodb = mangoDatabaseConnect.getMongoDBConnection("test");

		DBCollection dbCollection = mangoDatabaseCollection.getMongoCollection(mongodb, "mycol111");

		MongoCollectionFind mongoCollectionFind = new MongoCollectionFind();

		DBObject productLineDbObject = mongoCollectionFind.findProductLinesByProductLineFromCollection(dbCollection,
				productLinesForm.getProductLine());

		if (productLineDbObject != null) {
			productLinesForm.setProductLineImageAsString(Base64.encode(productLineDbObject.get("image").toString().getBytes()));

		}
		
		DBCursor dbProductLinesCursor = mongoCollectionFind.findAllProductLinesFromCollection(dbCollection);

		List<String> productLinesList = null;

		if (dbProductLinesCursor != null) {

			productLinesList = new ArrayList<String>();
			while (dbProductLinesCursor.hasNext()) {
				DBObject productLinesDbObject = dbProductLinesCursor.next();
				System.out.println(productLinesDbObject.get("productLine").toString());
				productLinesList.add(productLinesDbObject.get("productLine").toString());
			}

		}
		

		ModelAndView modelAndView = new ModelAndView("productline", "command", productLinesForm);
		modelAndView.addObject("productLineImageAsString", productLinesForm.getProductLineImageAsString());
		modelAndView.addObject("productLinesList", productLinesList);
		modelAndView.addObject("productLinesForm", productLinesForm);
		return modelAndView;

	}

}
