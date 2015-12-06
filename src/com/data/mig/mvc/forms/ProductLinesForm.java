package com.data.mig.mvc.forms;

import java.util.Arrays;

public class ProductLinesForm {

	private String productLine;
	private byte[] productLineImage;
	private String productLineImageAsString;
	private String htmlDescription;
	private String textDescription;
	
	public String getProductLine() {
		return productLine;
	}
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	public String getHtmlDescription() {
		return htmlDescription;
	}
	public void setHtmlDescription(String htmlDescription) {
		this.htmlDescription = htmlDescription;
	}
	public String getTextDescription() {
		return textDescription;
	}
	public void setTextDescription(String textDescription) {
		this.textDescription = textDescription;
	}
	public byte[] getProductLineImage() {
		return productLineImage;
	}
	public void setProductLineImage(byte[] productLineImage) {
		this.productLineImage = productLineImage;
	}
	public String getProductLineImageAsString() {
		return productLineImageAsString;
	}
	public void setProductLineImageAsString(String productLineImageAsString) {
		this.productLineImageAsString = productLineImageAsString;
	}
	@Override
	public String toString() {
		return "ProductLinesForm [productLine=" + productLine + ", productLineImage="
				+ Arrays.toString(productLineImage) + ", productLineImageAsString=" + productLineImageAsString
				+ ", htmlDescription=" + htmlDescription + ", textDescription=" + textDescription + "]";
	}


}
