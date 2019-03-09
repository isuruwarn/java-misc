package org.warn.misc.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


public class GenerateCSV {

	public static final String SCHEMA_NAME = "C2A0EB2141A24C43AFA0A46B5F42E5A1";

	static String driver = "com.sap.db.jdbc.Driver";

	//Connection parameters
	static String CONNECTURL = "jdbc:sap://10.253.86.191:30015/?currentschema=C2A0EB2141A24C43AFA0A46B5F42E5A1";
	static String USERNAME = "SBSS_48883904544050306765640558243497335947639161296749407721419640184";
	static String PASSWORD = "Aa_98954832971947542439078588709780353777752427769510190315993454585";

	//files location both input and output
	static String INPUTTABLES_FILELOC = "/Users/i802850/Desktop/planmodeltables.txt";
	static String OUTPUTXLS_FILELOC = "/Users/i802850/Desktop/output2.xls";

	private static Connection conn = null;
	public static Connection getConnection() throws Exception {
		if (conn != null)
			return conn;
		Class.forName(driver);
		System.out.println("trying to fetch connection");
		conn = DriverManager.getConnection(CONNECTURL, USERNAME, PASSWORD);
		return conn;
	}
	static  List <String> TABLE_NAMES = null;
	public static void main(String[] args) throws IOException {
		try {
			String sqlStatmentStr = "SELECT COLUMN_NAME FROM table_columns where schema_name = ? and table_name = ? order by position";
			PreparedStatement pstmt = getConnection().prepareStatement(sqlStatmentStr);
			TABLE_NAMES = readFile(INPUTTABLES_FILELOC);
			WritableWorkbook workbook = Workbook.createWorkbook(new File(OUTPUTXLS_FILELOC));
			workbook.createSheet("data", 0);
			WritableSheet sheet = workbook.getSheet(0);
			int rowCtr = -1;

			for (int i = 0; i < TABLE_NAMES.size(); i++) {
				rowCtr++;
				List<String> columnNames = new ArrayList<String>();
				String [] schemaAndTabNames = getSchemaAndTableName(TABLE_NAMES.get(i));
//				System.out.println(schemaAndTabNames[0]);
//				System.out.println(schemaAndTabNames[1]);
				pstmt.setString(1,schemaAndTabNames[0]);
				pstmt.setString(2, schemaAndTabNames[1] );
				ResultSet rset = pstmt.executeQuery();
				String whereClause = "";
				while (rset.next()) {
					String columnName = rset.getString(1);
					columnNames.add(columnName);
					
				}
//				System.out.println(columnNames.size());
				if(columnNames.size() == 0) continue;
				sqlStatmentStr = "SELECT  * FROM " + schemaAndTabNames[0] + ".\"" + schemaAndTabNames[1] + "\" " + whereClause ;//+ ORDERBYCOLS[i];
				System.out.println(sqlStatmentStr);
				rset = conn.createStatement().executeQuery(sqlStatmentStr);
				String columnHeader = "";
				Label label = new Label(0, rowCtr,TABLE_NAMES.get(i) );
				sheet.addCell(label);

				while(rset.next()) {
					String columnVal = "";
					if(columnHeader.length()==0){
						for (int j = 0; j < columnNames.size(); j++) {
							columnHeader=columnHeader + "," +columnNames.get(j);
							label = new Label(j+2, rowCtr,columnNames.get(j) );
							sheet.addCell(label);
						}
						rowCtr++;
//						outputStr += "\n" + TABLE_NAMES.get(i)+","+""+columnHeader +"\n";
					}
					for (int j = 0; j < columnNames.size(); j++) {
						String val = rset.getString(columnNames.get(j));
						columnVal = columnVal +"," + (val ==null?"":"\""+ val +"\"");
						label = new Label(j+2, rowCtr,val );
						sheet.addCell(label);
					}
//					outputStr = outputStr+","+""+columnVal +"\n";
					rowCtr++;
				}
				rowCtr++;
			}
//			writeFile(outputStr, "result.csv", false);
			workbook.write();
			workbook.close();

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static List<String> readFile(String fileName) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str;
			List<String> returnList = new ArrayList<String>();
			StringBuffer strBuff = new StringBuffer();
			while ((str = in.readLine()) != null) {
				returnList.add(str);
			}
			in.close();
			return returnList;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static String[] getSchemaAndTableName(String fqnTable) {
		String[] retArray = new String[2];
		if (fqnTable == null)
			return retArray;
		int index = fqnTable.indexOf('.');
		if (index > -1) {
			retArray[0] = fqnTable.substring(0, index);
			retArray[1] = fqnTable.substring(index + 1);
		} else {
			retArray[0] = SCHEMA_NAME;
			retArray[1] = fqnTable;

		}
		return retArray;
	}

}
