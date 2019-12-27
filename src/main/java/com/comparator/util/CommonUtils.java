package com.comparator.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.comparator.annotation.Index;
import com.comparator.domain.Account;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

public class CommonUtils {

	// T type parameter
	public static <T> List<T> getDataList(String path, Class<T> clazz)
			throws Exception {

		List<T> dataList = new ArrayList<T>();

		// URI absolutePathUri =
		// ClassLoader.getSystemResource(relativePath).toURI();
		Workbook workbook = WorkbookFactory.create(new File(path));
		Sheet sheet = workbook.getSheetAt(0);

		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();

		// skip heading 0
		for (int i = 1; i <= rowCount; i++) {
			T t = clazz.newInstance();
			Row row = sheet.getRow(i);
			Field[] declaredFields = t.getClass().getDeclaredFields();
			for (Field field : declaredFields) {
				if (field.isAnnotationPresent(Index.class)) {
					int index = field.getAnnotation(Index.class).value();
					field.setAccessible(true);
					Cell cell = row.getCell(index);
					cell.setCellType(CellType.STRING);
					field.set(t, cell.getStringCellValue());
				}
			}
			dataList.add(t);
		}

		return dataList;
	}

	private static String encrypt(String value) {
		return new String(Base64.getEncoder().encode(value.getBytes()));

	}

	public static void triggerPoaEndpoint(List<Account> accountList)
			throws Exception {
		accountList.parallelStream().forEach(CommonUtils::trigger);
	}

	public static void trigger(Account account) {
		try {
			System.out.println(Thread.currentThread().getName());
			String url = (account.getEndpoint() + encrypt(account.getAccNo()) + account
					.getResource()).trim();
			HttpResponse<JsonNode> jsonResponse = Unirest.get(url).asJson();
			//ObjectMapper mapper = new ObjectMapper();
			// Response readValue =
			// mapper.readValue(jsonResponse.getBody().toString(),
			// Response.class);
			// System.out.println(account+" : "+readValue.getDecodeValue());
			writeValue(jsonResponse.getBody().toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void writeValue(String jsonMessage) {
		//kali lekhiba
		System.out.println(jsonMessage);
	}

	public static void main(String[] args) throws Exception {
		List<Account> accountList = getDataList(
				"E:\\LIJA'S OFFICE WORK\\tool\\tool\\src\\main\\resources\\templates\\POA.csv",
				Account.class);
		triggerPoaEndpoint(accountList);

	}
}
