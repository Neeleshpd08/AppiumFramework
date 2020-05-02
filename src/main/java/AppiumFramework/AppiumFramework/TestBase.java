package AppiumFramework.AppiumFramework;
import java.io.*;
import java.net.ServerSocket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.aventstack.extentreports.Status;
import java.lang.Runtime;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;

public class TestBase extends GenericReport {

	Properties p=new Properties();
	public static AndroidDriver<AndroidElement> driver = null;
	AppiumDriverLocalService service=null;
	DesiredCapabilities cap=null;
	public HashMap<String,String> data;
	private String Excelpath = "\\Resources\\TestData.xlsx";
	GetData ExcelData=new GetData(Excelpath);

	public DesiredCapabilities InitializeCapabilities() {
		try {
			FileReader reader=new FileReader(".\\Resources\\global.properties");
			p.load(reader);
			File f=new File("Resources");
			File fs=new File(f,p.getProperty("AppName"));
			cap=new DesiredCapabilities();
			cap.setCapability(MobileCapabilityType.DEVICE_NAME,p.getProperty("DeviceName"));
			cap.setCapability(MobileCapabilityType.AUTOMATION_NAME,"uiautomator2");
			cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,14);
			System.out.println(fs.getAbsolutePath());
			cap.setCapability(MobileCapabilityType.APP,fs.getAbsolutePath());
		}
		catch(IOException ex) {
			Log(status.Fail,ex.getMessage());
		}
		return cap;
	}

	public AndroidDriver<AndroidElement> InitializeDriver() {
		try {
			cap = InitializeCapabilities();
			driver=new AndroidDriver<AndroidElement>(new URL("http://127.0.0.1:4723/wd/hub"),cap);
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		}catch(Exception ex) {
			Log(status.Fail,ex.getMessage());
		}
		return driver;
	}

	public void StartServer() throws InterruptedException {
		Boolean flag = CheckServerIsRunnning(4723);
		if(!flag) {
			service = AppiumDriverLocalService.buildDefaultService();
			service.start();
			Log(status.Pass,"Appium server launched");
		}
		else {
			Log(status.Pass,"Server is in running mode");
		}
	}

	public boolean CheckServerIsRunnning(int port) {
		boolean isServerRunning = false;
		ServerSocket serverSocket;
		try {
			serverSocket=new ServerSocket(port);
			serverSocket.close();
		}
		catch(IOException e) {
			isServerRunning = true;
		}
		finally {
			serverSocket = null;
		}
		return isServerRunning;
	}

	@BeforeSuite
	public void BeforeSuite() throws IOException {
		GenerateReport();
	}

	@BeforeClass
	public void BeforeClass() throws IOException, InterruptedException {
		String[] command = {"cmd.exe", "/c","E:\\AppiumProject\\AppiumFramework\\Resources\\RobotEmulator.bat"};
		Runtime.getRuntime().exec(command);
		Thread.sleep(2000);
		System.out.println("Emulator launched");
	}

	@BeforeMethod
	public void BeforeTest(ITestResult result) throws InterruptedException, IOException {
		String testName = result.getMethod().getMethodName();
		data = ExcelData.GetDataByTestCase("Data", testName);
		CreateTest(result.getMethod().getMethodName());
		try {
			Process Pkill = Runtime.getRuntime().exec("taskkill /F /IM node.exe");
			Thread.sleep(1000);
			Pkill.destroy();
			System.out.println("Destroy all Nodejs task/server");
		}
		catch(Exception ex) {
			Log(status.Fail,"Appium Server is running"+ex.getMessage());
		}
		Thread.sleep(2000);
		StartServer();
		InitializeDriver();
	}

	@AfterMethod
	public void AfterTest(ITestResult result) throws IOException {
		if(result.getStatus() == ITestResult.FAILURE)
		{
			GenericReport.test.log(Status.FAIL,"Test Case failed is"+result.getName());
			GenericReport.test.log(Status.FAIL,"Test Case failed is"+result.getThrowable());
			String screenshotPath = getScreenshot(driver, result.getName());

			GenericReport.test.addScreenCaptureFromPath(screenshotPath);

		}else if(result.getStatus() == ITestResult.SKIP) {
			GenericReport.test.log(Status.SKIP, "TestCase Skipped is:"+result.getName());
		}
		else if(result.getStatus() == ITestResult.SUCCESS) {
			GenericReport.test.log(Status.PASS, "TestCase Passed is:"+result.getName());
		}
		driver.closeApp();
		Log(status.Pass,"Appium server stop");
		service.stop();
	}

	public String getScreenshot(AndroidDriver<AndroidElement> driver,String screenshotName) {
		String destination = null;
		try {
			String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
			TakesScreenshot ts=(TakesScreenshot) driver;
			File source = ts.getScreenshotAs(OutputType.FILE);

			destination = System.getProperty("user.dir") + "/Screenshots/" + screenshotName + dateName +".png";
			File finalDestination = new File(destination);
			FileUtils.copyFile(source,finalDestination);
		}
		catch(Exception ex) {
			Log(status.Fail,"Screenshot is not generated "+ex.getMessage());
		}
		return destination;
	}

	@AfterClass
	public void AfterClass() throws IOException {
		System.out.println("Emulator is closed");
		Runtime.getRuntime().exec("adb -e emu kill");
	}

	@AfterSuite()
	public void AfterSuite() {
		FlushReport();
	}
}