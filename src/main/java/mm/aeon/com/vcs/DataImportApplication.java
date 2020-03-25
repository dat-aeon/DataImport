package mm.aeon.com.vcs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.mybatis.spring.MyBatisSystemException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;

import mm.aeon.com.vcs.common.CommonConstant;
import mm.aeon.com.vcs.common.CommonUtils;
import mm.aeon.com.vcs.common.HeaderConstant;
import mm.aeon.com.vcs.common.MessageConstant;
import mm.aeon.com.vcs.dao.ImportCustomerInfoDao;
import mm.aeon.com.vcs.dao.NRCInfoDao;
import mm.aeon.com.vcs.domain.CustAgreementList;
import mm.aeon.com.vcs.domain.ImportCustomerInfo;
import mm.aeon.com.vcs.domain.NRCInfo;

@SpringBootApplication
@MapperScan("mm.aeon.com.vcs.mapper")
@AutoConfigurationPackage
public class DataImportApplication implements CommandLineRunner {

	@Value(value = "${PRINT_ERROR_TRACE}")
	private String PRINT_ERROR_TRACE;

	@Value(value = "${SOURCE_CSV_PATH}")
	private String SOURCE_CSV_PATH;

	@Value(value = "${SUCCESS_CSV_PATH}")
	private String SUCCESS_CSV_PATH;

	@Value(value = "${FAILED_CSV_PATH}")
	private String FAILED_CSV_PATH;

	private final ImportCustomerInfoDao customerInfoDao;
	private final NRCInfoDao nrcInfoDao;

	public static String JAR_DIR = "/";

	public static File[] sourceCSVFiles;
	private static Map<String, Integer> headerMap;

	private List<Integer> stateDivisionList = new ArrayList<Integer>();
	private List<NRCInfo> nrcInfoList = new ArrayList<NRCInfo>();
	private List<String> nrcCheckList = new ArrayList<>();

	private boolean fileFailed;
	public boolean printStackTrace = true;

	private BufferedWriter writer;
	private CSVPrinter csvPrinter;
	private boolean intializedFailedCSV = false;

	public static void main(String[] args) {

		SpringApplication.run(DataImportApplication.class, args);
	}

	public DataImportApplication(ImportCustomerInfoDao customerInfoDao, NRCInfoDao nrcInfoDao) {
		this.customerInfoDao = customerInfoDao;
		this.nrcInfoDao = nrcInfoDao;
	}

	@Override
	public void run(String... args) {

		ApplicationHome appHome = new ApplicationHome();
		JAR_DIR = appHome.getDir().getAbsolutePath();
		printStackTrace = PRINT_ERROR_TRACE.toString().toLowerCase().equals("true");

		try {

			sourceCSVFiles = CommonUtils.getCSVFileInDir(SOURCE_CSV_PATH);

		} catch (IOException e) {

			e.printStackTrace();
			CommonUtils.log(MessageConstant.PROCESS_END);
			return;
		}

		if (sourceCSVFiles.length <= 0) {
			CommonUtils.log(MessageConstant.NO_CVS_FILE_SOURCE_PATH);
			CommonUtils.log(MessageConstant.PROCESS_END);
			return;
		}

		try {

			this.stateDivisionList = this.nrcInfoDao.getStateIdList();

		} catch (Exception e) {
			if (e instanceof MyBatisSystemException) {
				CommonUtils.log(MessageConstant.DB_CONNECTION_ERROR);
				return;
			} else {
				CommonUtils.log(MessageConstant.SQL_ERROR + "[StateDivison Select]");
			}

			this.printStackTrace(e);
		}

		for (File csvFile : sourceCSVFiles) {

			CommonUtils.currentFileName = csvFile.getName();

			this.fileFailed = false;
			try {

				checkCSVHeader(csvFile);

			} catch (IOException e) {
				CommonUtils.log(MessageConstant.BAD_CSV_FILE_STRUCTURE);
				this.fileFailed = true;
				this.printStackTrace(e);
			}

			String moveDir = (this.fileFailed) ? FAILED_CSV_PATH : SUCCESS_CSV_PATH;

			moveFile(csvFile.getAbsolutePath(), CommonUtils.pathTrailCheck(moveDir) + csvFile.getName());

			savePrintCSV();

		}

		try {

			if (this.nrcInfoList.size() > 0) {
				this.nrcInfoDao.insertUpdate(this.nrcInfoList);
			}

		} catch (Exception e) {

			CommonUtils.log(MessageConstant.SQL_ERROR + "[NRCINFO INSERT]");
			this.printStackTrace(e);

		}

		CommonUtils.currentFileName = "";
		CommonUtils.log(MessageConstant.PROCESS_END);
	}

	private void moveFile(String srcDir, String destDir) {
		try {
			Files.move(Paths.get(srcDir), Paths.get(destDir), StandardCopyOption.REPLACE_EXISTING);
			CommonUtils.log(MessageConstant.SUCCESS_CSV_FILE_MOVING);
		} catch (Exception e) {
			CommonUtils.log(MessageConstant.ERROR_CSV_FILE_MOVING);
			this.printStackTrace(e);
		}
	}

	private void checkCSVHeader(File csvFile) throws IOException {

		CSVParser csvParser = null;
		ImportCustomerInfo importCustomerInfo = null;

		try {
			csvParser = new CSVParser(new FileReader(csvFile),
					CSVFormat.EXCEL.withHeader().withFirstRecordAsHeader().withIgnoreHeaderCase().withAllowMissingColumnNames(false).withTrim());

			headerMap = csvParser.getHeaderMap();

			if (CommonUtils.checkHeader(headerMap)) {

				for (CSVRecord csvRecord : csvParser) {

					importCustomerInfo = getValidImportCustomerInfo(csvRecord);

					if (importCustomerInfo == null) {

						printCSV(csvRecord);

					} else {

						try {

							NRCInfo nrcInfo = CommonUtils.getNRCInfo(importCustomerInfo.getNrcNo());

							if (this.stateDivisionList.contains(nrcInfo.getStateId())) {

								if (!this.nrcCheckList.contains(nrcInfo.getStateId() + nrcInfo.getTownshipCode())) {
									this.nrcCheckList.add(nrcInfo.getStateId() + nrcInfo.getTownshipCode());
									this.nrcInfoList.add(nrcInfo);
								}

							} else {

								CommonUtils.invalidNRCStateErrorLog(String.valueOf(nrcInfo.getStateId()), csvRecord);
								continue;

							}

						} catch (Exception e) {

							CommonUtils.log(MessageConstant.NRC_SPLIT_ERROR);
							this.printStackTrace(e);

						}

						try {

							this.customerInfoDao.insertUpdate(importCustomerInfo);

						} catch (Exception e) {

							if (e instanceof DuplicateKeyException) {

								CommonUtils.duplicatePhoneNumberErrorLog(e, csvRecord);

							} else {

								CommonUtils.log(MessageConstant.SQL_ERROR);
								this.printStackTrace(e);

							}

							printCSV(csvRecord);

						}

					}
				}
			} else {
				this.fileFailed = true;
			}

			csvParser.close();

		} catch (Exception e) {

			if (null != csvParser) {
				csvParser.close();
			}

			CommonUtils.log(MessageConstant.BAD_CSV_FILE_STRUCTURE);
			this.fileFailed = true;
			this.printStackTrace(e);
			throw new IOException();
		}
	}

	private void printCSV(CSVRecord csvRecord) {

		try {

			if (!intializedFailedCSV) {

				List<String> headerList = new ArrayList<>();
				String dir = CommonUtils.pathTrailCheck(FAILED_CSV_PATH).concat(CommonUtils.getCurrentDateString()).concat("_").concat(CommonUtils.currentFileName);

				headerMap.entrySet().stream().sorted(Map.Entry.<String, Integer> comparingByValue()).forEachOrdered(x -> headerList.add(x.getKey()));

				writer = Files.newBufferedWriter(Paths.get(dir));

				csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL.withHeader(headerList.toArray(new String[headerList.size()])));

				intializedFailedCSV = true;
			}

			csvPrinter.printRecord(csvRecord);

		} catch (IOException e) {
			CommonUtils.log(MessageConstant.ERROR_CSV_FILE_CREATION);
			this.printStackTrace(e);
		}
	}

	private void savePrintCSV() {

		try {

			if (intializedFailedCSV) {

				csvPrinter.flush();
				csvPrinter.close();
				CommonUtils.log(MessageConstant.SUCCESS_CSV_FILE_CREATION);
			}

		} catch (IOException e) {
			CommonUtils.log(MessageConstant.ERROR_CSV_FILE_CREATION);
			this.printStackTrace(e);
		}

		intializedFailedCSV = false;

	}

	public static ImportCustomerInfo getValidImportCustomerInfo(CSVRecord csvRecord) throws ParseException {
		ImportCustomerInfo customerInfo = new ImportCustomerInfo();
		List<CustAgreementList> agreementList = new ArrayList<CustAgreementList>();

		String tempStr = "";
		boolean hasInvalidAgreement = false;
		int agCount = 1;
		int agIndex = headerMap.get(HeaderConstant.APPLICATION_FORM_NO_1);

		for (Entry<String, Integer> entry : headerMap.entrySet()) {

			if (entry.getValue() >= csvRecord.size()) {
				continue;
			}

			tempStr = csvRecord.get(entry.getValue());

			if (CommonUtils.isNullOrTrimedEmpty(tempStr)) {
				if (!entry.getKey().toLowerCase().contains(CommonConstant.AGREEMENT_LOWERE_CASE) && !entry.getKey().equals(HeaderConstant.MEMBER_CARD_ID_HEADER)
						&& !entry.getKey().equals(HeaderConstant.MEMBER_CARD_STATUS_HEADER) && !entry.getKey().toLowerCase().contains(HeaderConstant.APPLICATION_FORM_NO_LOWER_CASE)
						&& !entry.getKey().toLowerCase().contains(HeaderConstant.JUDGEMENT_DATE_LOWER_CASE)
						&& !entry.getKey().toLowerCase().contains(HeaderConstant.JUDGEMENT_RESULT_LOWER_CASE)) {
					CommonUtils.recordEmptyErrorLog(entry.getKey(), csvRecord);
					return null;
				}
			}

			switch (entry.getKey()) {

				case HeaderConstant.COMPANY_NAME_HEADER:

					customerInfo.setCompanyName(tempStr);
					break;

				case HeaderConstant.CUSTOMER_NAME_HEADER:

					customerInfo.setName(tempStr);
					break;

				case HeaderConstant.CUSTOMER_NO_HEADER:

					if (!CommonUtils.isValidCustomerNo(tempStr)) {
						CommonUtils.recordErrorLog(HeaderConstant.CUSTOMER_NO_HEADER, csvRecord);
						return null;
					}
					customerInfo.setCustomerNo(tempStr);
					break;

				case HeaderConstant.DOB_HEADER:

					if (!CommonUtils.isValidDOB(tempStr)) {
						CommonUtils.recordErrorLog(HeaderConstant.DOB_HEADER, csvRecord);
						return null;
					}

					Date date = CommonUtils.dobStringToDate(tempStr);
					if (null == date) {
						CommonUtils.recordErrorLog(HeaderConstant.DOB_HEADER, csvRecord);
						return null;
					}

					customerInfo.setDob(date);
					break;

				case HeaderConstant.GENDER_HEADER:
					int i = CommonUtils.checkGender(tempStr);
					if (i == 0) {
						CommonUtils.recordErrorLog(HeaderConstant.GENDER_HEADER, csvRecord);
						return null;
					}
					customerInfo.setGender(i);
					break;

				case HeaderConstant.MEMBER_CARD_ID_HEADER:
					if (!CommonUtils.isValidMemberCardId(tempStr)) {
						CommonUtils.recordErrorLog(HeaderConstant.MEMBER_CARD_ID_HEADER, csvRecord);
						return null;
					}
					customerInfo.setMemberCardId(tempStr);
					break;

				case HeaderConstant.MEMBER_CARD_STATUS_HEADER:
					if (!StringUtils.isEmpty(tempStr)) {
						customerInfo.setMemberCardStatus(Integer.parseInt(tempStr));
					}
					break;

				case HeaderConstant.NRC_HEADER:
					if (!CommonUtils.isValidNRCNo(tempStr)) {
						CommonUtils.recordErrorLog(HeaderConstant.NRC_HEADER, csvRecord);
						return null;
					}

					customerInfo.setNrcNo(tempStr);
					break;
				case HeaderConstant.PHONE_NO_HEADER:

					if (!CommonUtils.isValidNumber(tempStr)) {
						CommonUtils.recordErrorLog(HeaderConstant.PHONE_NO_HEADER, csvRecord);
						return null;
					}
					customerInfo.setPhoneNo(tempStr);
					break;

				case HeaderConstant.SALARY_HEADER:

					if (!CommonUtils.isValidCurrency(tempStr)) {
						CommonUtils.recordErrorLog(HeaderConstant.SALARY_HEADER, csvRecord);
						return null;
					}
					customerInfo.setSalary(Double.valueOf(tempStr.replaceAll(",", "").toString()));
					break;

				case HeaderConstant.TOWNSHIP_ADDRESS_HEADER:

					customerInfo.setTownship(tempStr);
					break;

				case HeaderConstant.DEL_FLAG_HEADER:

					customerInfo.setDelFlag(CommonUtils.checkDelFlag(tempStr));
					break;

			}
		}

		while (csvRecord.size() >= agIndex + 9) {

			if (!isAgreementEmpty(agIndex, csvRecord)) {

				CustAgreementList cusAgr = new CustAgreementList();

				// check agreement no
				if (!!StringUtils.isEmpty(csvRecord.get(agIndex))) {
					CommonUtils.recordErrorLog(CommonConstant.APPLICATION_FORM_NO + agCount, csvRecord);
					hasInvalidAgreement = true;
				} else {

					cusAgr.setApplicationNo(csvRecord.get(agIndex));

					// Check Judgement Result
					if (!CommonUtils.isValidNumber(csvRecord.get(agIndex + 1))) {
						hasInvalidAgreement = true;
						CommonUtils.recordErrorLog(CommonConstant.JUDGEMENT_RESULT + agCount, csvRecord);
					} else {
						cusAgr.setJudgementResult(Integer.valueOf(csvRecord.get(agIndex + 1)));
					}

					// Check Judgement Date
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					if (!CommonUtils.isValidNumber(csvRecord.get(agIndex + 2))) {
						hasInvalidAgreement = true;
						CommonUtils.recordErrorLog(CommonConstant.JUDGEMENT_DATE + agCount, csvRecord);
					} else {
						Date judgementDate = CommonUtils.dobStringToDate(csvRecord.get(agIndex + 2));
						if (null == judgementDate) {
							CommonUtils.recordErrorLog(HeaderConstant.DOB_HEADER, csvRecord);
							return null;
						}
						cusAgr.setJudgementDate(judgementDate);
					}

					if (!StringUtils.isEmpty(csvRecord.get(agIndex + 3))) {
						// Check Agreement No
						if (!CommonUtils.isValidAgreementNumber(csvRecord.get(agIndex + 3))) {
							CommonUtils.recordErrorLog(CommonConstant.AGREEMENT_NO + agCount, csvRecord);
							hasInvalidAgreement = true;
						} else {
							cusAgr.setAgreementNo(csvRecord.get(agIndex + 3));
						}

						// Check QRshow
						if (!CommonUtils.isValidNumber(csvRecord.get(agIndex + 4))) {
							hasInvalidAgreement = true;
							CommonUtils.recordErrorLog(CommonConstant.AGREEMENT_QR_SHOW + agCount, csvRecord);
						} else {
							cusAgr.setQrShow(Integer.valueOf(csvRecord.get(agIndex + 4)));
						}

						// Check FinancialAmount
						if (!CommonUtils.isValidCurrency(csvRecord.get(agIndex + 5))) {
							hasInvalidAgreement = true;
							CommonUtils.recordErrorLog(CommonConstant.AGREEMENT_FINANCIAL_AMT + agCount, csvRecord);
						} else {
							cusAgr.setFinancialAmt(Double.valueOf(csvRecord.get(agIndex + 5)));
						}

						// Check FinancialTerm
						if (!CommonUtils.isValidNumber(csvRecord.get(agIndex + 6))) {
							hasInvalidAgreement = true;
							CommonUtils.recordErrorLog(CommonConstant.AGREEMENT_FINANCIAL_TERM + agCount, csvRecord);
						} else {
							cusAgr.setFinancialTerm(Integer.valueOf(csvRecord.get(agIndex + 6)));
						}

						// Check FinancialStatus
						if (!CommonUtils.isValidNumber(csvRecord.get(agIndex + 7))) {
							hasInvalidAgreement = true;
							CommonUtils.recordErrorLog(CommonConstant.AGREEMENT_FINANCIAL_STATUS + agCount, csvRecord);
						} else {
							cusAgr.setFinancialStatus(Integer.valueOf(csvRecord.get(agIndex + 7)));
						}

						// Check LastPaymentDate
						if (!StringUtils.isEmpty(csvRecord.get(agIndex + 8))) {
							if (!CommonUtils.isValidNumber(csvRecord.get(agIndex + 8))) {
								hasInvalidAgreement = true;
								CommonUtils.recordErrorLog(CommonConstant.PAYMENT_DATE + agCount, csvRecord);
							} else {
								Date date = CommonUtils.paymentDateStringToDate(csvRecord.get(agIndex + 8));
								cusAgr.setPaymentDate(date);
							}
						}

					}
					if (!hasInvalidAgreement) {
						cusAgr.setCreatedTime(CommonUtils.getCurrentDateTime());
						cusAgr.setUpdatedTime(CommonUtils.getCurrentDateTime());

						agreementList.add(cusAgr);
					}
				}

			} else {

				// CommonUtils.recordEmptyErrorLog("Agreement " + agCount,
				// csvRecord);

			}

			agCount += 1;
			agIndex += 9;

		}

		if (hasInvalidAgreement) {
			return null;
		}

		customerInfo.setCustAgreementListList(agreementList);
		customerInfo.setCreatedTime(CommonUtils.getCurrentDateTime());
		customerInfo.setUpdatedTime(CommonUtils.getCurrentDateTime());

		return customerInfo;

	}

	private static boolean isAgreementEmpty(int stratIndex, CSVRecord csvRecord) {

		return CommonUtils.isNullOrTrimedEmpty(csvRecord.get(stratIndex)) && CommonUtils.isNullOrTrimedEmpty(csvRecord.get(stratIndex + 1))
				&& CommonUtils.isNullOrTrimedEmpty(csvRecord.get(stratIndex + 2)) && CommonUtils.isNullOrTrimedEmpty(csvRecord.get(stratIndex + 3))
				&& CommonUtils.isNullOrTrimedEmpty(csvRecord.get(stratIndex + 4));

	}

	private void printStackTrace(Exception e) {
		if (printStackTrace) {
			CommonUtils.log(e.getMessage());
		}
	}

}
