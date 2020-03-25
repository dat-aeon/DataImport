package mm.aeon.com.vcs.common;

/**
 * @author 25-00114
 *
 */

public final class CommonConstant {

	public static final String APPLICATION_FORM_NO = "ApplicationFormNo";
	public static final String JUDGEMENT_DATE = "JudgementDate";
	public static final String JUDGEMENT_RESULT = "JudgementResult";
	public static final String AGREEMENT_NO = "AgreementNo";
	public static final String AGREEMENT_QR_SHOW = "AgreementQRShow";
	public static final String AGREEMENT_FINANCIAL_AMT = "AgreementFinancialAtm";
	public static final String AGREEMENT_FINANCIAL_TERM = "AgreementFinancialTerm";
	public static final String AGREEMENT_FINANCIAL_STATUS = "AgreementFinancialStatus";
	public static final String PAYMENT_DATE = "PaymentDate";

	/**
	 * 
	 */
	public static final String DATASOURCE_URL = "spring.datasource.url";

	/**
	 * 
	 */
	public static final String DATASOURCE_USERNAME = "spring.datasource.username";

	/**
	 * 
	 */
	public static final String DATASOURCE_PASSWORD = "spring.datasource.password";

	/**
	 * 
	 */
	public static final String AGREEMENT_LOWERE_CASE = "agreement";

	/**
	 * 
	 */
	public static final String PROPERTY_FILE_DIR = "/import.properties";

	/**
	 * 
	 */
	public static final String FILE_NAME_DATE_PATTERN = "yyyyMMdd_HHmm";

	/**
	 * 
	 */
	public static final String LOG_FILE_TIME_PATTERN = "HH:mm:ss.S";

	/**
	 * 
	 */
	public static final String LOG_FILENAME_PREFIX = "DataImport_";

	/**
	 * 
	 */
	public static final String LOG_FOLDER = "/Data_Import_Log/";

	/**
	 * 
	 */
	public static final String DOT_LOG = ".log";

	/**
	 * 
	 */
	public static final String SOURCE_CSV_PATH = "SOURCE_CSV_PATH";

	/**
	 * 
	 */
	public static final String SUCCESS_CSV_PATH = "SUCCESS_CSV_PATH";

	/**
	 * 
	 */
	public static final String FAILED_CSV_PATH = "FAILED_CSV_PATH";

	/**
	 * 
	 */
	public static final String NOT_EXIST_CSV_PATH = "NOT_EXIST_CSV_PATH";

	/**
	 * 
	 */
	public static final String PRINT_ERROR_TRACE = "PRINT_ERROR_TRACE";

	/**
	 * 
	 */
	public static final String CSV_EXTENSION = ".csv";

	/**
	 * 
	 */
	public static final String FULLY_PAID_LOWER_CASE = "fully paid";

	/**
	 * 
	 */
	public static final String PAYMENT_ONGOING_LOWER_CASE = "payment ongoing";

	/**
	 * 
	 */
	public static final String FINAL_APPROVE_LOWER_CASE = "final approve";

	/**
	 * 
	 */
	public static final String AG_STATUS_USED = "used";

	/**
	 * 
	 */
	public static final String AG_STATUS_NOT_USED = "not used";

	/**
	 * 
	 */
	public static final String MEMBER = "member";

	/**
	 * E.g. 1709-000271171-5
	 */
	public static final String CUSTOMER_NO_PATTERN = "^([0-9]*-[0-9]*){2}$";

	/**
	 * 
	 */
	public static final String ONLY_NUMBER_PATTERN = "\\d+";

	/**
	 * yyyyMMdd
	 */
	public static final String DOB_PATTERN = "^[0-9]{4}(((0[13578]|(10|12))(0[1-9]|[1-2][0-9]|3[0-1]))|(02(0[1-9]|[1-2][0-9]))|((0[469]|11)(0[1-9]|[1-2][0-9]|30)))$";

	/**
	 * yyyyMMdd
	 */
	public static final String DOB_FORMAT = "yyyyMMdd";

	/**
	 * yyyyMMdd
	 */

	public static final String PAYMENTDATE_PATTEN = "^[0-9]{4}(((0[13578]|(10|12))(0[1-9]|[1-2][0-9]|3[0-1]))|(02(0[1-9]|[1-2][0-9]))|((0[469]|11)(0[1-9]|[1-2][0-9]|30)))$";

	/**
	 * yyyyMMdd
	 */
	public static final String PAYMENTDATE_FORMAT = "yyyyMMdd";

	/**
	 * 
	 */
	public static final String MEMBER_CARD_ID_PATTERN = "\\d*";

	/**
	 * 1810-1-0000504502-5
	 */
	public static final String AGREEMENT_NO_PATTERN = "^([0-9]*-[0-9]*){3}$";

	/**
	 * 
	 */
	public static final String NRC_NO_PATTERN = "^((15\\/[a-zA-Z]{3,})|((([1-9])|([1][0-4]))\\/([a-zA-Z]{6,9})))\\([A-Z]\\)\\d{6}$";

	/**
	 * 
	 */
	public static final String CURRENCY_PATTERN = "^\\$?(\\d{1,3},?(\\d{3},?)*\\d{3}(\\.\\d{1,3})?|\\d{1,3}(\\.\\d+)?)$";

}
