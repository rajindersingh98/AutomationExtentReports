package com.jenkins.newtest.CreateRequest;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;




public class CreateRequestFromExcelData {
	public static final String XLS_NAME = "_xls_name.xls";
	public static final String XML_NAME = "_xml_name.xml";
	public static final String XML_PATH = "\\xmlFiles\\";
	public static final String XLS_PATH = "\\ExcelFiles\\";
	String defaultExcelPath = XLS_PATH;
	String defaultXMLPath = XML_PATH;
	String excelName = null;
	String xmlName = null;
	HSSFWorkbook workbook = null;
	HSSFSheet sheet = null;
	private static CreateRequestFromExcelData createXMLRequest = new CreateRequestFromExcelData();
	/*public String prescribe(String drugName){
		String requestXML = createRequest(drugName);
		return requestXML;
		//System.out.println(request);
	}*/
	public static CreateRequestFromExcelData getInstance() {
		return createXMLRequest;
	}

	
	
	public void setXMLXLSNamePath(String api){
		xmlName = System.getProperty("user.dir")+XML_PATH+api+XML_NAME;
		excelName = System.getProperty("user.dir")+XLS_PATH+api+XLS_NAME;
		System.out.println(xmlName);
	//	System.out.println(getAllTestcases().get(2));
		
	}
	
	public ArrayList<String> getAllTestcases(){
		FileInputStream fis = null;
		ArrayList<String> testcases =  new ArrayList<String>();
		//System.out.println(excelName);
		try {
			fis = new FileInputStream(excelName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	util.failMessage("No Data file found at given location"+excelName);
			e.printStackTrace();
		}
        //System.out.println("Fis is "+fis);
       
        try {
			workbook = new HSSFWorkbook(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        sheet = workbook.getSheetAt(0);
        //sheet = workbook.getSheet("10.6");
        Iterator rows = sheet.rowIterator();
        HSSFCell cell = null ;
        while(rows.hasNext()){
        	HSSFRow row = (HSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            if(row.getRowNum() !=0){
            cell = (HSSFCell)row.getCell(0);
            String tescaseName = cell.getStringCellValue();
            testcases.add(tescaseName);
            }
        	
        }
		return testcases;
	}

	public static String getColumnName(HSSFSheet sheet, int col) {
		String columnName = "";
		HSSFRow row = sheet.getRow(0);
		try {
			HSSFCell cell = row.getCell((short)(col));
			columnName = cell.getStringCellValue();
		}
		catch(NullPointerException npe) {}
		return columnName;
	}
	
	private static String readFileAsString(String filePath)
		    throws java.io.IOException{
		        StringBuffer fileData = new StringBuffer(1000);
		        BufferedReader reader = new BufferedReader(new FileReader(filePath));
		        char[] buf = new char[1024];
		        int numRead=0;
		        while((numRead=reader.read(buf)) != -1){
		            String readData = String.valueOf(buf, 0, numRead);
		            fileData.append(readData);
		            buf = new char[1024];
		        }
		        reader.close();
		        return fileData.toString();
		    }
	
	public static String getProperty(String propertyName, String propertFileName) {

		Properties pro = new Properties();

		try {

			FileInputStream in = new FileInputStream(propertFileName);

			pro.load(in);

		} catch (Exception e) {

		}

		String value = pro.getProperty(propertyName);

		return value;

	}

	public Request replaceTags(Map h ,String applicatioName)  {
		Request req = new Request();
		String xml = null;
		try {
			xml = readFileAsString(xmlName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String runautomationOnRegion =  getProperty("runautomationOnRegion", System.getProperty("user.dir")+"\\properties\\instance.properties");
		System.out.println("runautomationOnRegion+++++++"+runautomationOnRegion);
		System.out.println("runautomationOnRegion+++++++"+h.get("DBTOCONNECT"));
		
		Iterator entries = h.entrySet().iterator();
		while (entries.hasNext()) {
			Map.Entry thisEntry = (Map.Entry) entries.next();
			String key = thisEntry.getKey().toString();
			String value = thisEntry.getValue().toString();
			// System.out.println(key +"+++++"+ value);
			String toreplace = "$" + key.trim();
			
			if("pickfromproperty".equals(value.trim())){
			    System.out.println("With:::::" + key); 
				value = getProperty(key.trim(), System.getProperty("user.dir")+"\\"+"properties"+"\\"+applicatioName+"\\"+runautomationOnRegion+".properties");
				System.out.println("With:::::" + value);  
			}
			System.out.println("going to replace::::::" + toreplace+ "With:::::" + value);
			xml = xml.replace(toreplace, value);
			// xml);
		}
		req.setRequest(xml);
		if(h.get("Status") !=null){
			req.setResponse((String)h.get("Status"));
		}
		else{
			//util.failMessage("Status Column is not filled for Testcase");
		}
		if(h.get("StatusNotExpected") !=null){
			req.setResponsenotpresent((String)h.get("StatusNotExpected"));
		}
		//System.out.println(xml);
		return req;
	}
	
	public int getRowIndex(String testCaseNames) {
		System.out.println("testCaseNames is "+testCaseNames);
		FileInputStream fis = null;
		int rowIndex = 0;
		try {
			fis = new FileInputStream(excelName);
			HSSFWorkbook workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = workbook.getSheetAt(0);
			//HSSFSheet sheet = workbook.getSheet("10.6");
			for (Row row : sheet) {
				Cell cell = row.getCell(0);
				//System.out.println(cell.getStringCellValue());
				if (testCaseNames.equals(cell.getStringCellValue())) {
					rowIndex = row.getRowNum();
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		if(rowIndex == 0){
			//util.failMessage("Testcase Not found");
		}
		return rowIndex;
	}
	
	public Request createRequest(String testCaseName ,String applicationName){ 
		Map h = new HashMap();
		try {
			FileInputStream fis = null;
		try {
			fis = new FileInputStream(excelName);
		} catch (IOException e) {
			//util.failMessage("No Data file found at given location"+excelName);
			e.printStackTrace();
		}workbook = new HSSFWorkbook(fis);
        sheet = workbook.getSheetAt(0);
        //sheet = workbook.getSheet("10.6");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        int i =1 ;
        Iterator rows = sheet.rowIterator();
        int tescaseIndexRow = getRowIndex(testCaseName);
        if(tescaseIndexRow == 0){
        	//util.failMessage("No testcase found corresponding to ID mentioned");
        }
        HSSFRow row = (HSSFRow) sheet.getRow(getRowIndex(testCaseName));
        Iterator cells = row.cellIterator();
		while (cells.hasNext()) {
			HSSFCell cell = (HSSFCell) cells.next();
			// System.out.println(cell.getColumnIndex());
			String columnName = getColumnName(sheet, cell.getColumnIndex());
			// System.out.println(columnName);
			String value = null;
			// System.out.println(columnName+":::"+cell.getStringCellValue());
			switch (cell.getCellType()) {
			case BLANK: {
				// System.out.println("Values are blank");
				value = "";
			}
		case STRING: {
				if ("blank".equals(cell.getRichStringCellValue().getString())) {
					value = "";
				}else
					value = cell.getRichStringCellValue().getString();
					break;
				}
			case NUMERIC:{
				if (DateUtil.isCellDateFormatted(cell)) {
					value = df.format(cell.getDateCellValue()).toString();
				}else{
					int cellIntValue = (int) cell.getNumericCellValue();
					value = String.valueOf(cellIntValue);
				}
				break;
			}
			case BOOLEAN:
				value = String.valueOf(cell.getBooleanCellValue());
				break;
			case FORMULA:
				value = cell.getCellFormula().toString();
				break;
			default:
				System.out.println();
			}
			System.out.println(columnName);
	         System.out.println(value);
			h.put(columnName, value);
			//System.out.println(value);
			}
        }
        catch(Exception ae){
        	System.out.println(ae);
        }
		
		Request r = replaceTags(h ,applicationName);
		h.get("DBTOCONNECT");
		r.setH(h);
        return r;
	}

}
