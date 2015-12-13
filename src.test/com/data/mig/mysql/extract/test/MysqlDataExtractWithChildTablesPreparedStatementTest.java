package com.data.mig.mysql.extract.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.data.mig.constants.IApplicationConstants;
import com.data.mig.mysql.db.MysqlDatabaseConnect;

import junit.framework.Assert;

@SuppressWarnings("deprecation")
public class MysqlDataExtractWithChildTablesPreparedStatementTest {
		
	@Test
	public void getMysqlDataExtractWithGivenChildTableTest() throws SQLException {
		MysqlDatabaseConnect mysqlDatabaseConnect = new MysqlDatabaseConnect();

		Connection conn = mysqlDatabaseConnect.getMySqlDBConnection(
				IApplicationConstants.defaultMySqlSchemaName,
				IApplicationConstants.defaultMySqlUserId,
				IApplicationConstants.defaultMySqlPassword);

		Assert.assertNotNull(conn);
		
		PreparedStatement preparedStatement = conn.prepareStatement("select MSRP, productScale, quantityInStock, productDescription ,productlines.productLine as  'productLine', productCode, buyPrice, " + 
				"image, productVendor, htmlDescription, productName, textDescription from  classicmodels.productlines, classicmodels.products " + 
				"where products.productLine = productlines.productLine and productlines.productLine = ? ");
		
		preparedStatement.setString(1, "Classic Cars");
		
		ResultSet rs = preparedStatement.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("productName"));
		}
		
		

		conn.close();

	}
	
}
