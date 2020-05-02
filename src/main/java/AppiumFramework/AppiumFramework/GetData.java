package AppiumFramework.AppiumFramework;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFTableColumn;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetData {
	private XSSFWorkbook workBook;
	private FileInputStream filePath;
	private List<String> ColValues;
	private List<String> RowValues;
	private XSSFSheet sheet;
	HashMap<String,String> map;
	String workingDir = System.getProperty("user.dir");

	public GetData(String Address)
	{
		try {
			System.out.println(workingDir +""+Address);
			filePath =new FileInputStream(workingDir +""+Address);
			workBook=new XSSFWorkbook(filePath);
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public List<String> GetColumnValue(XSSFSheet SheetValue){
		ColValues = new LinkedList<String>();
		List<XSSFTable> table = SheetValue.getTables();
		XSSFTable tab = table.get(0);
		List<XSSFTableColumn> Column = tab.getColumns();
		for (int i = 1; i < Column.size(); i++) {
			ColValues.add(Column.get(i).getName());
		}
		return ColValues;
	}

	public List<String> GetRowValue(String SheetValue,String TestcaseName){
		RowValues = new LinkedList<String>();
		XSSFSheet sheet = workBook.getSheet(SheetValue);
		for (Row row : sheet) { // For each Row.
			Cell cell = row.getCell(0); // Get the Cell at the Index / Column you want.
			if(cell != null) {
				if(cell.getStringCellValue().equalsIgnoreCase(TestcaseName)) {
					System.out.println(cell.getRow().getLastCellNum());
					for(int i=1;i<=cell.getRow().getLastCellNum();i++) {
						RowValues.add(String.valueOf(cell.getRow().getCell(i)));
					}
				}
			}
			else {
				break;
			}
		}
		return RowValues;
	}

	public HashMap<String,String> GetDataByTestCase(String SheetName,String TestcaseName) {
		int NumberOfSheet =  workBook.getNumberOfSheets();
		for (int i = 0; i < NumberOfSheet; i++) {
			if(workBook.getSheetName(i).equalsIgnoreCase(SheetName)) {
				sheet = workBook.getSheetAt(i);
				List<String> col = GetColumnValue(sheet);
				List<String> row = GetRowValue(SheetName,TestcaseName);
				map = new HashMap<String,String>();
				for (int j = 0; j < row.size(); j++) {
					map.put(col.get(j),row.get(j));
				}
			}
		}
		return map;
	}
}
