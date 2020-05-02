package AppiumFramework.AppiumFramework;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.*;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class GenericReport {

	private ExtentHtmlReporter htmlReporter=null;
	private ExtentReports extent=null;
	public static ExtentTest test = null;
	private String dateName = new SimpleDateFormat("yyyyMMdd_hhmmss").format(new Date());
	private String destination = System.getProperty("user.dir") + "/Reports/" +"Report_"+ dateName +".html";
	
	public void GenerateReport() {
		htmlReporter = new ExtentHtmlReporter(destination);
		htmlReporter.config().setDocumentTitle("Automation Report");
		htmlReporter.config().setReportName("Appium Report");
		htmlReporter.config().setTheme(Theme.DARK);
		
		extent=new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("OS", System.getProperty("os.name"));
		extent.setSystemInfo("Tester Name", System.getProperty("user.name"));
	}
	
	public void CreateTest(String testCaseName) {
		test = extent.createTest(testCaseName);
	}
	
	public void Log(Object status,String description) {
		if(status.toString() == "Fail") {		
			test.log(Status.FAIL, description);
		}else if(status.toString() == "Skip") {
			test.log(Status.SKIP, description);
		}
		else {
			test.log(Status.PASS, description);
		}
	}
	
	public void FlushReport() {
		extent.flush();
	}
}
