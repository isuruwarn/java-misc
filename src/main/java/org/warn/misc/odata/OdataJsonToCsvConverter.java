package org.warn.misc.odata;

import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;



/**
 * Takes an OData results array as input and produces the results in CSV format. The OData results
 * are usually in the following format:
 * 
 * 
 * {
 * 	"d": {
 * 		"results": [{
 * 				"__metadata": {
 * 					"uri": "https://sample.domain.com/...",
 * 					"type": "..."
 * 				},
 * 				"COL_1": "abc",
 * 				"COL_2": "def",
 * 				...
 * 			}
 * 		]
 * 	}
 * }
 * 
 */
public class OdataJsonToCsvConverter {
	
	
	private static final String DELIMITER = ",";
	
	
	
	public void convert( String resultsJSON ) {
	
		JSONArray arrResults = new JSONArray( resultsJSON );
		boolean isHeaderRow = true;
		StringBuilder headerRow = new StringBuilder();
		StringBuilder dataRow = new StringBuilder();
		
		
		for( int i=0; i<arrResults.length(); i++ ) {
			
			dataRow = new StringBuilder();
			JSONObject resultRowObj = arrResults.getJSONObject(i);
			Set<String> keys = resultRowObj.keySet();
			
			for( String key: keys ) {
				
				if( key != null && !key.contains("__metadata") ) {
					
					if( isHeaderRow ) {
						headerRow.append( key + DELIMITER );
					}
					
					Object obj = resultRowObj.get(key);
					String value = String.valueOf( obj );
					
					if( "null".equalsIgnoreCase(value) ) {
						value = "";
					}
					
					dataRow.append( value + DELIMITER );
				}
				
			}
			dataRow = dataRow.deleteCharAt( dataRow.length()-1 ); // remove last delimiter
			
			if( isHeaderRow ) {
				headerRow = headerRow.deleteCharAt( headerRow.length()-1 ); // remove last delimiter
				System.out.println( headerRow.toString() );
				isHeaderRow = false;
			}
			
			System.out.println( dataRow.toString() );
			
		}
		
		
	}

}
