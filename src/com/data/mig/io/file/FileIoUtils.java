package com.data.mig.io.file;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

public class FileIoUtils {

	public Boolean write(Map<String, Object> targetObject, String filePath) {

		Boolean writeToFileSuccessFlag = false;

		ObjectMapper objectMapper = new ObjectMapper();

		System.out.println("File write operation ...");

		try {
			// Write the target object into file
			if (filePath != null) {

				objectMapper.writeValue(new File(filePath), targetObject);

				writeToFileSuccessFlag = true;

			} else {
				System.out.println("Extract file path is null. So didn't write the target object to file.");
			}

		} catch (IOException ioe) {

			ioe.printStackTrace();
		}

		System.out.println("File write operation is successful ...");

		return writeToFileSuccessFlag;
	}

}
