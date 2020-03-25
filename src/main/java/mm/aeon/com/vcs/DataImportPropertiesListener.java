package mm.aeon.com.vcs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import mm.aeon.com.vcs.common.CommonConstant;
import mm.aeon.com.vcs.common.CommonUtils;
import mm.aeon.com.vcs.common.MessageConstant;

public class DataImportPropertiesListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

	Properties properties;

	public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
		ConfigurableEnvironment environment = event.getEnvironment();

		try {
			PrintStream out = new PrintStream(new FileOutputStream(CommonUtils.getLogFileName(new ApplicationHome().getDir().getAbsolutePath())));
			System.setOut(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		CommonUtils.log(MessageConstant.PROCESS_START);

		if (!isPropertiesFileExist()) {
			CommonUtils.log(MessageConstant.PROCESS_END);
			System.exit(0);
		}

		if (!isValidProperties()) {
			CommonUtils.log(MessageConstant.PROCESS_END);
			System.exit(0);
		}

		environment.getPropertySources().addFirst(new PropertiesPropertySource("myProps", properties));

	}

	private boolean isValidProperties() {

		boolean stopExecution = false;

		if (properties.isEmpty()) {
			CommonUtils.log(MessageConstant.PROPERTY_FILE_EMPTY);
			return false;
		}

		if (!properties.containsKey(CommonConstant.SOURCE_CSV_PATH)) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.SOURCE_CSV_PATH_PROP_NOT_FOUND);
		} else if (CommonUtils.isNullOrTrimedEmpty(properties.get(CommonConstant.SOURCE_CSV_PATH))) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.SOURCE_CSV_PATH_VALUE_EMPTY);
		} else if (CommonUtils.dirNotExists(properties.getProperty(CommonConstant.SOURCE_CSV_PATH), false)) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.SOURCE_CSV_PATH_NOT_EXISTS);
		}

		if (!properties.containsKey(CommonConstant.SUCCESS_CSV_PATH)) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.SUCCESS_CSV_PATH_PROP_NOT_FOUND);
		} else if (CommonUtils.isNullOrTrimedEmpty(properties.get(CommonConstant.SUCCESS_CSV_PATH))) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.SUCCESS_CSV_PATH_VALUE_EMPTY);
		} else if (CommonUtils.dirNotExists(properties.getProperty(CommonConstant.SUCCESS_CSV_PATH), false)) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.SUCCESS_CSV_PATH_NOT_EXISTS);
		}

		if (!properties.containsKey(CommonConstant.FAILED_CSV_PATH)) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.FAILED_CSV_PATH_PROP_NOT_FOUND);
		} else if (CommonUtils.isNullOrTrimedEmpty(properties.get(CommonConstant.FAILED_CSV_PATH))) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.FAILED_CSV_PATH_VALUE_EMPTY);
		} else if (CommonUtils.dirNotExists(properties.getProperty(CommonConstant.FAILED_CSV_PATH), false)) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.FAILED_CSV_PATH_NOT_EXISTS);
		}

		if (!properties.containsKey(CommonConstant.NOT_EXIST_CSV_PATH)) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.NOT_EXIST_CSV_PATH_PROP_NOT_FOUND);
		} else if (CommonUtils.isNullOrTrimedEmpty(properties.get(CommonConstant.NOT_EXIST_CSV_PATH))) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.NOT_EXIST_CSV_PATH_VALUE_EMPTY);
		} else if (CommonUtils.dirNotExists(properties.getProperty(CommonConstant.NOT_EXIST_CSV_PATH), false)) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.NOT_EXIST_CSV_PATH_NOT_EXISTS);
		}

		if (!properties.containsKey(CommonConstant.DATASOURCE_URL)) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.DATASOURCE_URL_NOT_DEFINE);
		} else if (CommonUtils.isNullOrTrimedEmpty(properties.get(CommonConstant.DATASOURCE_URL))) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.DATASOURCE_URL_IS_EMPTY);
		}

		if (!properties.containsKey(CommonConstant.DATASOURCE_PASSWORD)) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.DATASOURCE_PASSWORD_NOT_DEFINE);
		} else if (CommonUtils.isNullOrTrimedEmpty(properties.get(CommonConstant.DATASOURCE_PASSWORD))) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.DATASOURCE_PASSWORD_IS_EMPTY);
		}

		if (!properties.containsKey(CommonConstant.DATASOURCE_USERNAME)) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.DATASOURCE_USERNAME_NOT_DEFINE);
		} else if (CommonUtils.isNullOrTrimedEmpty(properties.get(CommonConstant.DATASOURCE_USERNAME))) {
			stopExecution = true;
			CommonUtils.log(MessageConstant.DATASOURCE_USERNAME_IS_EMPTY);
		}

		if (!properties.containsKey(CommonConstant.PRINT_ERROR_TRACE) && CommonUtils.isNullOrTrimedEmpty(properties.get(CommonConstant.DATASOURCE_USERNAME))) {
			properties.put(CommonConstant.PRINT_ERROR_TRACE, "FALSE");
		}

		return !stopExecution;

	}

	private boolean isPropertiesFileExist() {
		try {
			properties = new Properties();

			InputStream stream;

			File f = new File(CommonUtils.pathTrailCheck(new ApplicationHome().getDir().getAbsolutePath()) + CommonConstant.PROPERTY_FILE_DIR);
			stream = new FileInputStream(f);

			properties.load(stream);

			return true;
		} catch (Exception e) {
			CommonUtils.log(MessageConstant.PROPERTY_FILE_NOT_FOUND);

			return false;
		}
	}

}
