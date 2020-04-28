package AppiumFramework.AppiumFramework;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
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
			filePath =new FileInputStream(workingDir +""+Address);
			workBook=new XSSFWorkbook(filePath);
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public List<String> GetColumnValue(XSSFSheet SheetValue){
		ColValues = new ArrayList<String>();
		List<XSSFTable> table = SheetValue.getTables();
		XSSFTable tab = table.get(0);
		List<XSSFTableColumn> Column = tab.getColumns();
		for (int i = 1; i < Column.size(); i++) {
			ColValues.add(Column.get(i).getName());
		}
		return ColValues;
	}

	public List<String> GetRowValue(XSSFSheet SheetValue,String TestcaseName){
		RowValues = new ArrayList<String>();
		List<XSSFTable> table = SheetValue.getTables();
		XSSFTable tab = table.get(0);
		int rowCount = tab.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			XSSFRow row = SheetValue.getRow(i);
			Iterator<Cell> r =  row.iterator();
			while(r.hasNext()) {
				if(r.next().toString().contains(TestcaseName))
				{
					while(r.hasNext()) {
						RowValues.add(r.next().toString());
					}
				}
				else
				{
					break;
				}
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
				List<String> row = GetRowValue(sheet,TestcaseName);
				map = new HashMap<String,String>();
				for (int j = 0; j < row.size(); j++) {
					map.put(col.get(j),row.get(j));
				}
			}
		}
		return map;
	}
}
