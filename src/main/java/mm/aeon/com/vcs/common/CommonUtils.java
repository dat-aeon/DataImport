package mm.aeon.com.vcs.common;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVRecord;

import mm.aeon.com.vcs.domain.NRCInfo;

public class CommonUtils {

	public static String currentFileName = "";

	public static String getLogFileName(String parentDir) {

		String folder = pathTrailCheck(parentDir + CommonConstant.LOG_FOLDER);
		dirNotExists(folder, true);
		return folder + CommonConstant.LOG_FILENAME_PREFIX + getCurrentDateString() + CommonConstant.DOT_LOG;
	}

	public static String getCurrentDateString() {
		DateFormat dateFormat = new SimpleDateFormat(CommonConstant.FILE_NAME_DATE_PATTERN);
		return dateFormat.format(getCurrentDateTime());
	}

	public static String getCurrentTimeString() {
		DateFormat dateFormat = new SimpleDateFormat(CommonConstant.LOG_FILE_TIME_PATTERN);
		return dateFormat.format(getCurrentDateTime());
	}

	public static Date getCurrentDateTime() {
		return new Date(System.currentTimeMillis());
	}

	public static void log(String str) {
		System.out.println(getCurrentTimeString() + "\t" + ((!currentFileName.isEmpty()) ? "[" + currentFileName + "]\t" : "") + str);
	}

	public static File[] getCSVFileInDir(String dir) throws IOException {
		File[] csvFiles = new File(dir.trim()).listFiles((file) -> {
			return file.getName().endsWith(".csv");
		});
		return csvFiles;
	}

	public static List<String> getRequiredHeaderList() {
		List<String> headerList = new ArrayList<>();

		headerList.add(HeaderConstant.AGREEMENT_NO_1);
		headerList.add(HeaderConstant.AGREEMENT_QR_SHOW_1);
		headerList.add(HeaderConstant.AGREEMENT_FINANCIAL_AMT_1);
		headerList.add(HeaderConstant.AGREEMENT_FINANCIAL_STATUS_1);
		headerList.add(HeaderConstant.AGREEMENT_FINANCIAL_TERM_1);
		headerList.add(HeaderConstant.COMPANY_NAME_HEADER);
		headerList.add(HeaderConstant.CUSTOMER_NAME_HEADER);
		headerList.add(HeaderConstant.CUSTOMER_NO_HEADER);
		headerList.add(HeaderConstant.DOB_HEADER);
		headerList.add(HeaderConstant.GENDER_HEADER);
		headerList.add(HeaderConstant.MEMBER_CARD_ID_HEADER);
		headerList.add(HeaderConstant.NRC_HEADER);
		headerList.add(HeaderConstant.PHONE_NO_HEADER);
		headerList.add(HeaderConstant.SALARY_HEADER);
		headerList.add(HeaderConstant.DEL_FLAG_HEADER);
		headerList.add(HeaderConstant.TOWNSHIP_ADDRESS_HEADER);

		return headerList;
	}

	public static List<String> getRequiredPaymentHeaderList() {
		List<String> headerList = new ArrayList<>();

		headerList.add(HeaderConstant.AGREEMENT_CD);
		headerList.add(HeaderConstant.PAYMENT_DATE);

		return headerList;
	}

	public static String pathTrailCheck(String path) {
		if (path.charAt(path.length() - 1) != File.separatorChar) {
			path += File.separator;
		}
		return path;
	}

	public static int checkDelFlag(String text) {
		text = text.toLowerCase();
		return (text.equals("yes") || text.equals("1")) ? 1 : 0;
	}

	public static int checkMemberFlag(String text) {
		text = text.toLowerCase();
		return (text.equals("member") || text.equals("yes") || text.equals("1")) ? 1 : 0;
	}

	public static int checkGender(String text) {

		// int num = 0;
		//
		// switch (text.toLowerCase()) {
		// case "male":
		// num = 1;
		// break;
		// case "female":
		// num = 2;
		// break;
		// case "1":
		// num = 1;
		// break;
		// case "2":
		// num = 2;
		// break;
		// }
		//
		// return num;

		return text.equals("1") ? 1 : 2;
	}

	public static int checkAgreementStatus(String text) {
		text = text.toLowerCase();
		return (text.equals(CommonConstant.AG_STATUS_USED) || text.equals("1")) ? 1 : 0;
	}

	public static int checkStatus(String text) {

		int num = 0;

		switch (text.toLowerCase()) {
			case CommonConstant.FULLY_PAID_LOWER_CASE:
				num = 1;
				break;

			case CommonConstant.PAYMENT_ONGOING_LOWER_CASE:
				num = 2;
				break;

			case CommonConstant.FINAL_APPROVE_LOWER_CASE:
				num = 3;
				break;

		}

		return num;
	}

	public static boolean isNullOrTrimedEmpty(Object object) {
		if (null == object) {
			return true;
		} else if (object.toString().trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean checkHeader(Map<String, Integer> headerMap) {
		boolean isValid = true;

		for (String listHeader : CommonUtils.getRequiredHeaderList()) {
			if (!headerMap.containsKey(listHeader)) {
				isValid = false;
				CommonUtils.log("\"" + listHeader + "\" " + MessageConstant.COLUMN_HEADER_NOT_FOUND);
			}
		}

		return isValid;
	}

	public static boolean checkPaymentHeader(Map<String, Integer> headerMap) {
		boolean isValid = true;

		for (String listHeader : CommonUtils.getRequiredPaymentHeaderList()) {
			if (!headerMap.containsKey(listHeader)) {
				isValid = false;
				CommonUtils.log("\"" + listHeader + "\" " + MessageConstant.COLUMN_HEADER_NOT_FOUND);
			}
		}

		return isValid;
	}

	public static Date dobStringToDate(String strDate) {
		return convertStringToDate(strDate, CommonConstant.DOB_FORMAT);
	}

	public static Date paymentDateStringToDate(String strDate) {
		return convertStringToDate(strDate, CommonConstant.PAYMENTDATE_FORMAT);
	}

	public static boolean isValidNRCNo(String text) {
		return isStringMatchPattern(text, CommonConstant.NRC_NO_PATTERN);
	}

	public static boolean isValidMemberCardId(String text) {
		return isStringMatchPattern(text, CommonConstant.MEMBER_CARD_ID_PATTERN);
	}

	public static boolean isValidDOB(String text) {
		return isStringMatchPattern(text, CommonConstant.DOB_PATTERN);
	}

	public static boolean isPaymentDate(String text) {
		return isStringMatchPattern(text, CommonConstant.PAYMENTDATE_PATTEN);
	}

	public static boolean isValidAgreementNumber(String text) {
		return isStringMatchPattern(text, CommonConstant.AGREEMENT_NO_PATTERN);
	}

	public static boolean isValidNumber(String text) {
		return isStringMatchPattern(text, CommonConstant.ONLY_NUMBER_PATTERN);
	}

	public static boolean isValidCustomerNo(String text) {
		return isStringMatchPattern(text, CommonConstant.CUSTOMER_NO_PATTERN);
	}

	public static boolean isValidCurrency(String text) {
		return isStringMatchPattern(text, CommonConstant.CURRENCY_PATTERN);
	}

	public static boolean isStringMatchPattern(String text, String patternString) {
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}

	public static Date convertStringToDate(String strDate, String dateFormat) {
		Date date = null;
		DateFormat formatter = new SimpleDateFormat(dateFormat);
		try {
			date = formatter.parse(strDate);
		} catch (ParseException e) {
			CommonUtils.log("date convert error.");
			e.printStackTrace();
		}
		return date;
	}

	public static String listToDoubleQuotedAndCommaSeperatedString(List<String> stringList) {
		return String.join(",", stringList.stream().map(string -> ("\"" + string + "\"")).collect(Collectors.toList()));
	}

	public static void recordErrorLog(String header, CSVRecord csvRecord) {
		CommonUtils.log("[Record No. " + csvRecord.getRecordNumber() + "] Invalid [" + header + " = " + ((!"AGREEMENT_NO".equals(header)) ? csvRecord.get(header) : "") + "] in "
				+ csvRecord.toMap().toString());
	}

	public static void recordEmptyErrorLog(String header, CSVRecord csvRecord) {
		CommonUtils.log("[Record No. " + csvRecord.getRecordNumber() + "] Empty [" + header + "] in " + csvRecord.toMap().toString());
	}

	public static void invalidNRCStateErrorLog(String stateId, CSVRecord csvRecord) {
		CommonUtils.log("[Record No. " + csvRecord.getRecordNumber() + "] Invalid NRC's state [" + stateId + "] in " + csvRecord.toMap().toString());
	}

	public static void duplicatePhoneNumberErrorLog(Exception e, CSVRecord csvRecord) {
		CommonUtils.log("[Record No. " + csvRecord.getRecordNumber() + "] Duplicate [" + e.getCause().getMessage().split("Detail: Key ")[1] + "] " + csvRecord.toMap().toString());
	}

	public static void notExistAgreementErrorLog(CSVRecord csvRecord) {
		CommonUtils.log("[Record No. " + csvRecord.getRecordNumber() + "] Not Exist - " + csvRecord.toMap().toString());
	}

	public static boolean dirNotExists(String dir, boolean createIfNot) {

		File file = new File(dir);

		if (createIfNot) {
			if (!file.exists()) {
				file.mkdir();
			}
		}

		return !file.exists();
	}

	public static NRCInfo getNRCInfo(String nrcString) {
		NRCInfo nrcInfo = new NRCInfo();

		String[] tempStrArr = nrcString.split("/");

		nrcInfo.setStateId(Integer.valueOf(tempStrArr[0]));
		nrcInfo.setTownshipCode(tempStrArr[1].split("\\(")[0].toUpperCase());

		return nrcInfo;
	}
}
