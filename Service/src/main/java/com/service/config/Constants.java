package com.service.config;

public class Constants {
	
	//File path
	public static final String UPLOAD_DIR = "/uploads/";
	
	//Defaults for pagination
	public static final int DEFAULT_PAGE = 1;
	public static final int DEFAULT_PAGE_SIZE = 10;
	public static final String DEFAULT_SORT_COLUMN = "name";
	public static final String DEFAULT_SORT_ORDER= "asc";
	
	
	//Excel export
	public static final String EXCEL_CONTENT_TYPE= "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String EXCEL_HEADER="Content-Disposition";
	public static final String EXCEL_FILENAME = "attachment; filename=student.xlsx";
	
	
	private Constants() {
		
	}
	

}
