package com.mckinsey.sf.io;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ExcelWrite {
	public static void main(String[] args) {
		excelExp("src/main/resources/result.xls");
	}

	/** * 传入文件的绝对路径 * * @param filePath */
	public static void excelExp(String filePath) {
		Workbook wb = null;
		OutputStream out = null;
		try {
			wb = new HSSFWorkbook();
			Sheet sheet = wb.createSheet("test");
			sheet.setColumnWidth(0, 18 * 256);
			sheet.setColumnWidth(1, 18 * 256);
			Row r = sheet.createRow(0);
			r.createCell(0).setCellValue("ip");
			r.createCell(1).setCellValue("community");
			r.createCell(2).setCellValue("result");
			out = new FileOutputStream(filePath);
			wb.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
