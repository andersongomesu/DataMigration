package com.data.mig.mvc.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.data.mig.cassandra.online.load.MysqlToCassandraBatchLoad;
import com.data.mig.cassandra.online.load.MysqlToCassandraOnlineLoad;
import com.data.mig.cassandra.online.load.OracleToCassandraOnlineLoad;
import com.data.mig.constants.IApplicationConstants;
import com.data.mig.db.MongoCollectionFind;
import com.data.mig.db.MongoDatabaseCollection;
import com.data.mig.db.MongoDatabaseConnect;
import com.data.mig.mongo.online.load.MysqlToMongoOnlineLoad;
import com.data.mig.mongo.online.load.OracleToMongoOnlineLoad;
import com.data.mig.mvc.forms.OnlineLoadForm;
import com.data.mig.mvc.forms.ProductLinesForm;
import com.data.mig.mysql.db.MysqlSchemaDetails;
import com.data.mig.mysql.extract.MysqlDataExtract;
import com.data.mig.mysql.extract.MysqlDataExtractWithChildTables;
import com.data.mig.oracle.db.OracleSchemaDetails;
import com.data.mig.oracle.extract.OracleDataExtract;
import com.data.mig.oracle.extract.OracleDataExtractWithChildTables;
import com.data.mig.read.json.extract.ReadJsonDataFromFile;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/dm")
public class DataMigrationHomeController {

	@RequestMapping(path = "/home", method = RequestMethod.GET)
	public ModelAndView home() {

		OnlineLoadForm onlineLoadForm = new OnlineLoadForm();

		MysqlSchemaDetails mysqlSchemaDetails = new MysqlSchemaDetails();
		OracleSchemaDetails oracleSchemaDetails = new OracleSchemaDetails();

		List<String> tableNameList = mysqlSchemaDetails.getMysqlTableDetails(null,
				IApplicationConstants.defaultMySqlSchemaName);

		/*
		 * List<String> oracleTableNameList =
		 * oracleSchemaDetails.getOracleTableDetails(null,
		 * IApplicationConstants.defaultOracleSchemaName);
		 * 
		 * // Adding the oracle table list to table list if (oracleTableNameList
		 * != null && oracleTableNameList.size() > 0) {
		 * tableNameList.addAll(oracleTableNameList); }
		 */

		List<String> sourceSchemaList = new ArrayList<String>();

		sourceSchemaList.add("classicmodels");
		sourceSchemaList.add("HR");
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

		OracleSchemaDetails oracleSchemaDetails = new OracleSchemaDetails();

		List<String> tableNameList = mysqlSchemaDetails.getMysqlTableDetails(null,
				IApplicationConstants.defaultMySqlSchemaName);

		/*
		 * List<String> oracleTableNameList =
		 * oracleSchemaDetails.getOracleTableDetails(null,
		 * IApplicationConstants.defaultOracleSchemaName);
		 * 
		 * // Adding the oracle table list to table list if (oracleTableNameList
		 * != null && oracleTableNameList.size() > 0) {
		 * tableNameList.addAll(oracleTableNameList); }
		 */
		List<String> sourceSchemaList = new ArrayList<String>();

		sourceSchemaList.add("classicmodels");
		sourceSchemaList.add("HR");
		// schemaDetailsList.add("test");

		ModelAndView model = new ModelAndView("batchload", "command", onlineLoadForm);
		model.addObject("schemaDetailsList", sourceSchemaList);

		model.addObject("tableNameList", tableNameList);

		return model;

	}

	@RequestMapping(path = "/submitOnlineLoad", method = RequestMethod.POST)
	public ModelAndView loadOnline(@ModelAttribute("SpringWeb") OnlineLoadForm onlineLoadForm, ModelMap model) {

		System.out.println("Source database :" + onlineLoadForm.getSourceDatabase());
		MysqlToMongoOnlineLoad mysqlToMongoOnlineLoad = null;
		OracleToMongoOnlineLoad oracleToMongoOnlineLoad = null;
		MysqlToCassandraOnlineLoad mysqlToCassandraOnlineLoad = null;
		OracleToCassandraOnlineLoad oracleToCassandraOnlineLoad = null;

		String sourceTableName = null;
		if (onlineLoadForm.getSourceTableName() != null) {
			sourceTableName = onlineLoadForm.getSourceTableName()
					.substring(onlineLoadForm.getSourceTableName().indexOf(".") + 1);
		}

		System.out.println("Source table name is : " + sourceTableName);

		Boolean successFlag = false;

		if (onlineLoadForm.getSourceDatabase() != null && onlineLoadForm.getTargetDatabase() != null
				&& onlineLoadForm.getSourceDatabase().equalsIgnoreCase(IApplicationConstants.mysqlDatabase)
				&& onlineLoadForm.getTargetDatabase().equalsIgnoreCase(IApplicationConstants.mongoDatabase)) {
			System.out.println("MySQL to Mongo Load .........");

			mysqlToMongoOnlineLoad = new MysqlToMongoOnlineLoad();
			successFlag = mysqlToMongoOnlineLoad.loadDataFromMysqlToMongo(onlineLoadForm.getSourceSchema(),
					sourceTableName, onlineLoadForm.getSourceSchema(),
					// onlineLoadForm.getTargetCollectionOrColumnFamilyName(),
					sourceTableName, onlineLoadForm.getNoOfRecordsToBeExtracted(),
					Boolean.valueOf(onlineLoadForm.getChildTableExtractRequired()));

		} else if (onlineLoadForm.getSourceDatabase() != null && onlineLoadForm.getTargetDatabase() != null
				&& onlineLoadForm.getSourceDatabase().equalsIgnoreCase(IApplicationConstants.oracleDatabase)
				&& onlineLoadForm.getTargetDatabase().equalsIgnoreCase(IApplicationConstants.mongoDatabase)) {
			System.out.println("Oracle to Mongo Load .........");

			oracleToMongoOnlineLoad = new OracleToMongoOnlineLoad();
			successFlag = oracleToMongoOnlineLoad.loadDataFromOracleToMongo(onlineLoadForm.getSourceSchema(),
					sourceTableName, onlineLoadForm.getSourceSchema(),
					// onlineLoadForm.getTargetCollectionOrColumnFamilyName(),
					sourceTableName, onlineLoadForm.getNoOfRecordsToBeExtracted(),
					Boolean.valueOf(onlineLoadForm.getChildTableExtractRequired()));

		} else if (onlineLoadForm.getSourceDatabase() != null && onlineLoadForm.getTargetDatabase() != null
				&& onlineLoadForm.getSourceDatabase().equalsIgnoreCase(IApplicationConstants.mysqlDatabase)
				&& onlineLoadForm.getTargetDatabase().equalsIgnoreCase(IApplicationConstants.cassandralDatabase)) {
			System.out.println("MySQL to Cassandra Load .........");

			mysqlToCassandraOnlineLoad = new MysqlToCassandraOnlineLoad();
			successFlag = mysqlToCassandraOnlineLoad.loadDataFromMysqlToCassandra(onlineLoadForm.getSourceSchema(),
					sourceTableName, onlineLoadForm.getSourceSchema(),
					// onlineLoadForm.getTargetCollectionOrColumnFamilyName(),
					sourceTableName, onlineLoadForm.getNoOfRecordsToBeExtracted(),
					Boolean.valueOf(onlineLoadForm.getChildTableExtractRequired()));

		} else if (onlineLoadForm.getSourceDatabase() != null && onlineLoadForm.getTargetDatabase() != null
				&& onlineLoadForm.getSourceDatabase().equalsIgnoreCase(IApplicationConstants.oracleDatabase)
				&& onlineLoadForm.getTargetDatabase().equalsIgnoreCase(IApplicationConstants.cassandralDatabase)) {
			System.out.println("Oracle to Cassandra Load .........");

			oracleToCassandraOnlineLoad = new OracleToCassandraOnlineLoad();
			successFlag = oracleToCassandraOnlineLoad.loadDataFromOracleToCassandra(onlineLoadForm.getSourceSchema(),
					sourceTableName, onlineLoadForm.getSourceSchema(),
					// onlineLoadForm.getTargetCollectionOrColumnFamilyName(),
					sourceTableName, onlineLoadForm.getNoOfRecordsToBeExtracted(),
					Boolean.valueOf(onlineLoadForm.getChildTableExtractRequired()));
		} else {
			onlineLoadForm.setMessage(IApplicationConstants.invalidSelectionMessage);
			return new ModelAndView("failure", "command", onlineLoadForm);
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

	@RequestMapping(path = "/submitBatchLoad", method = RequestMethod.POST)
	public ModelAndView batchLoad(@ModelAttribute("SpringWeb") OnlineLoadForm onlineLoadForm, ModelMap model) {

		System.out.println("Source database :" + onlineLoadForm.getSourceDatabase());
		Boolean successFlag = null;
		MysqlDataExtract mysqlDataExtract = null;
		MysqlDataExtractWithChildTables mysqlDataExtractWithChildTables = null;

		MysqlToCassandraBatchLoad mysqlToCassandraBatchLoad = null;

		OracleDataExtract oracleDataExtract = null;
		OracleDataExtractWithChildTables oracleDataExtractWithChildTables = null;

		ReadJsonDataFromFile readJsonDataFromFile = null;

		String sourceTableName = null;
		if (onlineLoadForm.getSourceTableName() != null) {
			sourceTableName = onlineLoadForm.getSourceTableName()
					.substring(onlineLoadForm.getSourceTableName().indexOf(".") + 1);
		}

		System.out.println("Source table name is : " + sourceTableName);

		try {

			if (onlineLoadForm.getSourceDatabase() != null && onlineLoadForm.getTargetDatabase() != null
					&& onlineLoadForm.getSourceDatabase().equalsIgnoreCase(IApplicationConstants.mysqlDatabase)
					&& onlineLoadForm.getTargetDatabase().equalsIgnoreCase(IApplicationConstants.mongoDatabase)) {
				System.out.println("MySQL to Mongo Extract .........");

				if (onlineLoadForm.getChildTableExtractRequired() != null
						&& !Boolean.valueOf(onlineLoadForm.getChildTableExtractRequired())) {

					System.out.println("MySQL Extract without child tables .........");
					mysqlDataExtract = new MysqlDataExtract();
					successFlag = mysqlDataExtract.extractMysqlDataIntoJsonFile(onlineLoadForm.getSourceSchema(),
							sourceTableName, onlineLoadForm.getNoOfRecordsToBeExtracted(),
							onlineLoadForm.getFilePath());

				} else {
					System.out.println("MySQL Extract with child tables .........");

					mysqlDataExtractWithChildTables = new MysqlDataExtractWithChildTables();
					successFlag = mysqlDataExtractWithChildTables.extractMysqlDataIntoJsonFile(
							onlineLoadForm.getSourceSchema(), sourceTableName,
							onlineLoadForm.getNoOfRecordsToBeExtracted(), onlineLoadForm.getFilePath());

				}

			} else if (onlineLoadForm.getSourceDatabase() != null && onlineLoadForm.getTargetDatabase() != null
					&& onlineLoadForm.getSourceDatabase().equalsIgnoreCase(IApplicationConstants.mysqlDatabase)
					&& onlineLoadForm.getTargetDatabase().equalsIgnoreCase(IApplicationConstants.cassandralDatabase)) {

				System.out.println("MySQL to Cassandra Extract .........");
				mysqlToCassandraBatchLoad = new MysqlToCassandraBatchLoad();
				successFlag = mysqlToCassandraBatchLoad.writeDataFromMysqlToJsonFile(onlineLoadForm.getSourceSchema(),
						sourceTableName, onlineLoadForm.getTargetCollectionOrColumnFamilyName(),
						onlineLoadForm.getNoOfRecordsToBeExtracted(), onlineLoadForm.getFilePath(),
						Boolean.valueOf(onlineLoadForm.getChildTableExtractRequired()));

			} else if (onlineLoadForm.getSourceDatabase() != null && onlineLoadForm.getTargetDatabase() != null
					&& onlineLoadForm.getSourceDatabase().equalsIgnoreCase(IApplicationConstants.oracleDatabase)
					&& onlineLoadForm.getTargetDatabase().equalsIgnoreCase(IApplicationConstants.mongoDatabase)) {

				System.out.println("Oracle to Mongo Extract .........");
				if (onlineLoadForm.getChildTableExtractRequired() != null
						&& !Boolean.valueOf(onlineLoadForm.getChildTableExtractRequired())) {

					System.out.println("Oracle to Mongo Extract without child tables .........");
					oracleDataExtract = new OracleDataExtract();
					successFlag = oracleDataExtract.extractOracleDataIntoJsonFile(onlineLoadForm.getSourceSchema(),
							sourceTableName, onlineLoadForm.getNoOfRecordsToBeExtracted(),
							onlineLoadForm.getFilePath());

				} else {

					System.out.println("Oracle to Mongo Extract with child tables .........");
					oracleDataExtractWithChildTables = new OracleDataExtractWithChildTables();
					successFlag = oracleDataExtractWithChildTables.extractOracleDataIntoJsonFile(
							onlineLoadForm.getSourceSchema(), sourceTableName,
							onlineLoadForm.getNoOfRecordsToBeExtracted(), onlineLoadForm.getFilePath());

				}

			}

			if (successFlag) {
				System.out.println("Extract has been successful ...");
				readJsonDataFromFile = new ReadJsonDataFromFile();

				if (onlineLoadForm.getTargetDatabase() != null) {
					if (onlineLoadForm.getTargetDatabase().equalsIgnoreCase(IApplicationConstants.mongoDatabase)) {

						System.out.println("Mongo database load ..........");
						successFlag = readJsonDataFromFile.readJsonDataFromFile(onlineLoadForm.getSourceSchema(),
								onlineLoadForm.getTargetCollectionOrColumnFamilyName(), onlineLoadForm.getFilePath(),
								sourceTableName);

					} else if (onlineLoadForm.getTargetDatabase()
							.equalsIgnoreCase(IApplicationConstants.cassandralDatabase)) {

						System.out.println("Cassandra database load .........");
						successFlag = readJsonDataFromFile.readJsonDataFromFileintoCassandraDatabase(
								onlineLoadForm.getSourceSchema(), sourceTableName,
								onlineLoadForm.getTargetCollectionOrColumnFamilyName(), sourceTableName,
								onlineLoadForm.getFilePath(),
								Boolean.valueOf(onlineLoadForm.getChildTableExtractRequired()));

					}
				}

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

		DB mongodb = mangoDatabaseConnect.getMongoDBConnection("classicmodels");

		DBCollection dbCollection = mangoDatabaseCollection.getMongoCollection(mongodb, "productlines");

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

		DB mongodb = mangoDatabaseConnect.getMongoDBConnection("classicmodels");

		DBCollection dbCollection = mangoDatabaseCollection.getMongoCollection(mongodb, "productlines");

		MongoCollectionFind mongoCollectionFind = new MongoCollectionFind();

		DBObject productLineDbObject = mongoCollectionFind.findProductLinesByProductLineFromCollection(dbCollection,
				productLinesForm.getProductLine());

		if (productLineDbObject != null) {
			// productLinesForm.setProductLineImageAsString(Base64.encode(productLineDbObject.get("image").toString().getBytes()));
			// productLinesForm.setProductLineImageAsString(Base64.encodeBase64String(productLineDbObject.get("image").toString().getBytes()));
			productLinesForm.setProductLineImageAsString(productLineDbObject.get("image").toString());
			productLinesForm.setProductLineImage(Base64.decodeBase64(productLineDbObject.get("image").toString()));

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
		modelAndView.addObject("productLineImage", productLinesForm.getProductLineImage());
		modelAndView.addObject("productLinesList", productLinesList);
		modelAndView.addObject("productLinesForm", productLinesForm);
		return modelAndView;

	}

}
