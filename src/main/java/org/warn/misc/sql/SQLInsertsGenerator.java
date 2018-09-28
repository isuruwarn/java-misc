package org.warn.misc.sql;

import java.util.ArrayList;
import java.util.List;

import org.warn.utils.file.FileOperations;



/**
 * Generates SQL insert statements based on provided CSV file. If you have an MS Excel sheet,
 * just copy the content into a plain text file and set the delimiter as \t.
 * 
 */
public class SQLInsertsGenerator {

	
	private static final String DELIMITER = "\t";
	
	
	public static void print( String path, String tableName, String columnNames ) {
		
		List<String> lines = FileOperations.readLines( path );
		
		ArrayList<Integer> integerCols = new ArrayList<Integer>();
		integerCols.add(0);
		integerCols.add(3);
		integerCols.add(6);
		integerCols.add(7);
		integerCols.add(13);
		integerCols.add(22);
		
		ArrayList<Integer> timestampCols = new ArrayList<Integer>();
		timestampCols.add(4);
		timestampCols.add(5);
		
		StringBuilder sb = null;
		for( String line: lines ) {
			
			//System.out.println(line);
			
			sb = new StringBuilder();
			String [] columns = line.split(DELIMITER);
			//System.out.println(columns.length);
			
			for( int i=0; i<columns.length; i++ ) {
				
				String col = columns[i];
				//System.out.println(col);
				
				if( col.equals(" ") ) {
					col = "null";
				}
				
				String comma = ", ";
				if( i == columns.length - 1) {
					comma = "";
				}
				
				String prefix = "'";
				String sufix = "'";
				if( integerCols.contains(i) || col.equalsIgnoreCase("null") ) {
					prefix = "";
					sufix = "";
					
				} else if ( timestampCols.contains(i) ) {
					String [] date = col.split("/");
					String year = date[2]; 
					String month = date[0].length()==1?"0"+date[0]:date[0];
					String day = date[1].length()==1?"0"+date[1]:date[1];
					col = year + "-" + month + "-" + day;
					prefix = "'";
					sufix = "T00:00:00.000Z'";
				}
				
				sb.append( prefix + col + sufix + comma );
				
			}
			
			System.out.println( "INSERT INTO " + tableName + " "
					+ columnNames + " "
					+ " VALUES ( " + sb.toString() + " ); " );
		}
		
		
		
	}
	
	
	

}
