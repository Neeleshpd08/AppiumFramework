package AppiumFramework.AppiumFramework;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class AppiumTest extends TestBase{	
	@Test(priority=1)
	public void FirstTest(){
		ControlProperties.NameInput.SetText(data.get("Name"));
		driver.hideKeyboard();
		ControlProperties.FemaleCheck.TapToElement();
		ControlProperties.DropDownBtn.TapToElement();
		assertTrue(false,"Not Found");
	}
	
	@Test(priority=2)
	public void SecondTest() {
		ControlProperties.NameInput.SetText(data.get("Name"));
		driver.hideKeyboard();
		ControlProperties.FemaleCheck.TapToElement();
		ControlProperties.DropDownBtn.TapToElement();
	}
	
	@Test(priority=3)
	public void ThirdTest() {
		ControlProperties.NameInput.SetText(data.get("Name"));
		driver.hideKeyboard();
		ControlProperties.FemaleCheck.TapToElement();
		ControlProperties.DropDownBtn.TapToElement();
	}
	
	@Test(priority=4)
	public void ForthTest() {
		ControlProperties.NameInput.SetText(data.get("Name"));
		driver.hideKeyboard();
		ControlProperties.FemaleCheck.TapToElement();
		ControlProperties.DropDownBtn.TapToElement();
	}
	
}
