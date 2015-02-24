import java.io.*;
import java.util.*;

class RecordHandler{
	Map<String, String> record = new HashMap<String, String>();
	int dbSize;
	String[] columns;
}
public class Main {
	public static String readFile(String filename){
		String file = new String();
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			
			while (line != null) {
			    sb.append(line);
			    sb.append('\n');
			    line = br.readLine();
			}
			file = sb.toString();
			return file;
			
		} catch (IOException e) {
			System.err.println("Error opening file");
		}
		return file;
	}
	public static RecordHandler[] parseDB(String file){
		String[] lines = file.split("\n");
		int lineCount = lines.length, columnsCount=0;
		columnsCount = lines[0].split(",").length;
		String[] columns = lines[0].split(",");
		RecordHandler[] db = new RecordHandler[12];
		for (int i=1; i<lineCount; i++) {
			String[] values = lines[i].split(",");
			db[i-1] = new RecordHandler();
			db[i-1].dbSize = lineCount;
			db[i-1].columns = columns;
			for (int j = 0; j < columnsCount; j++) {
				db[i-1].record.put(columns[j], values[j]);
			}
		}
		return db;
	}
	public static void drawTable(RecordHandler[] db, int limit, String[] fields){
		int padding=20;
		for (String field : fields) {
			System.out.format("%"+(field.length()+(Math.abs(padding-field.length())))+"s", field);
		}
		System.out.print("\n");
		for (int i = 0; i < fields.length*padding; i++) {
			System.out.print("_");
		}
		System.out.print("\n");
		for (int i = 0; i < limit; i++) {
			for (String field : fields) {
				System.out.format("%"+(db[i].record.get(field).length()+
						(Math.abs(padding-db[i].record.get(field).length())))+"s", db[i].record.get(field));
			}
			System.out.print("\n");
			for (int j = 0; j < fields.length*padding; j++) {
				System.out.print("-");
			}
			System.out.print("\n");
		}
	}
	public static boolean validateFields(String[] fields, String[] selectedFields){
		boolean valide = true;
		for (String field:selectedFields) {
			if(!Arrays.asList(fields).contains(field)){
				valide = false;
				break;
			}
		}
		return valide;
	}
	public static boolean validateField(String[] fields, String selectedField){
		return Arrays.asList(fields).contains(selectedField);
	}
	public static void executeQuery(String query, RecordHandler[] db){
		int limit = db[0].dbSize-1;
		query = query.replace(", ", ",");
		String[] fields = db[0].columns, queryParts = query.split(" ");
		switch (queryParts[0].toLowerCase()) {
		case "select":
			if(queryParts.length > 1){
				String[] selectedFields = queryParts[1].split(",");
				if(validateFields(fields, selectedFields)){
					if(queryParts.length > 3){
						if(queryParts[2].equalsIgnoreCase("limit")){
							try {
								if(limit > Integer.parseInt(queryParts[3])+1){
									limit = Integer.parseInt(queryParts[3]);
								}
							} catch (NumberFormatException e) {
								System.err.println("Invalid query: limit must be number");
							}
						}
					}
					drawTable(db, limit, selectedFields);
				}else{
					System.err.println("Incorrect query: incorrect parameter [columns]");
				}
			}else{
				System.err.println("Incorrect query: missing parameter [columns]");
			}
			break;
		case "show":
			for (int i = 0; i < fields.length; i++) {
				System.out.print(fields[i]);
				if(i<fields.length-1){
					System.out.print(",");
				}else{
					System.out.print("\n");
				}
			}
			break;
		case "sum":
			int sum = 0;
			if(queryParts.length > 1){
				String field = queryParts[1];
				if(validateField(fields, field)){
					for (int i = 0; i < limit; i++) {
						try {
							sum += Integer.parseInt(db[i].record.get(field));
						} catch (NumberFormatException e) {
							System.err.println("Invalid query: the selected column is not numerical type");
						}
					}
					System.out.println(sum);
				}else{
					System.err.println("Incorrect query: incorrect parameter");
				}
			}else{
				System.err.println("Incorrect query: missing parameter");
			}
			break;
		case "find":
			RecordHandler[] results = new RecordHandler[limit];
			int resultCount=0;
			String needle = queryParts[1];
			if(queryParts.length > 1){
				for (int i = 0; i < limit; i++) {
					for (String field : fields) {
						if(needle.startsWith("\"") && needle.endsWith("\"")){
							String searchString = needle.substring(1, needle.length()-1);
							if(db[i].record.get(field).contains(searchString)){
								results[resultCount] = db[i];
								resultCount++;
							}
						}else{
							if(needle.equals(db[i].record.get(field))){
								results[resultCount] = db[i];
								resultCount++;
							}
						}
					}
				}
				if(resultCount>0){
					drawTable(results, resultCount, fields);
				}
			}else{
				System.err.println("Incorrect query: missing parameter");
			}
			break;
		default:
			System.err.println("Invalid query");
			break;
		}
	}
	public static void main(String[] args){
		RecordHandler[] db = parseDB(readFile("db.csv"));
		String query = "";
		Scanner scan = new Scanner(System.in);
		while(!query.equals("exit")){
			if(query != ""){
				try {
					executeQuery(query, db);
				} catch (Exception e) {
					System.err.println("Error executing query. Check your file");
				}
			}
			System.out.println("query->");
			query = scan.nextLine();
		}
		System.out.println(db[0].record.get("name"));
	}
}
