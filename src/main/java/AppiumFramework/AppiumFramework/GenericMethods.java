package AppiumFramework.AppiumFramework;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.StaleElementReferenceException;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class GenericMethods{

	public AndroidElement element=null;
	private AndroidElement parentElement=null;
	private By _by=null;
	public String control_name=null;
	private boolean isExistingElement = false;
	AndroidDriver<AndroidElement> driver;
	String Xpath = null;
	GenericReport report=new GenericReport();
	
	public String getControl_name() {
		return control_name;
	}

	public void setControl_name(String control_name) {
		this.control_name = control_name;
	}

	public By get_by() {
		return _by;
	}

	public GenericMethods(String by, String name)
	{
		this.control_name = name;
		this.Xpath = by;
		try
		{
			if (by.contains("/"))
				this._by = By.xpath(by);
			else
				this._by = By.xpath("//*[@*='" + by + "']");
		}
		catch (Exception ex)
		{
			report.Log(status.Fail,"Exception:"+ex.getMessage());
		}
	}

	public AndroidElement GetElement()
	{
		try
		{
			if (isExistingElement)
				return this.element;
			if (this.parentElement == null) {
				element = TestBase.driver.findElement(this._by);   
			}
			else
				element = (AndroidElement) this.parentElement.findElement(this._by);
		}
		catch (Exception ex)
		{
			report.Log(status.Fail,this.control_name + " not found!");
		}
		return element;
	}

	public void TapToElement(){
		element = GetElement();
		boolean IsSuccessfull = false;
		int i = 0;
		while (!IsSuccessfull && i < 300)
		{
			try
			{
				if (!element.isEnabled())
				{
					i++;
					continue;
				}
				element.click();
				IsSuccessfull = true;
				report.Log(status.Pass,"Clicked on " + this.control_name);
				break;
			}
			catch (StaleElementReferenceException st)
			{
				element = GetElement();
				i++;
			}
			catch (ElementNotVisibleException En)
			{
				this.ScrollIntoView();
				element.click();
				break;
			}
			catch (Exception ex)
			{
				i++;
			}
		}
		if (i == 200)
			report.Log(status.Fail,"Not able to click on"+this.control_name+"element.\n\n");
	}

	public void SetText(String text) {
		   element = GetElement();
           try
           {
               element.clear();
               element.sendKeys(text);
               report.Log(status.Pass,"Setting text '" + text + "' in " + this.control_name);
           }
           catch (InvalidElementStateException ex)
           {
               throw new InvalidElementStateException("Element " + this.control_name + " not ready!");
           }
           catch (Exception ex)
           {
               try {
				throw new Exception(String.format("Not able to set text is"+ this.control_name+ "control!\""));
			} catch (Exception e) {
				e.printStackTrace();
			}
               
           }
	}
	
	public void ScrollIntoView() {
		boolean flag=true;
	    int count=1;
	    while(flag){
	        try {
	            driver.findElement(By.xpath(this.Xpath));
	            flag=false;
	            break;
	        }
	        catch(Exception NoSuchElementException) {
	            count=count+1;
	            Map<String, Object> params = new HashMap<>();
	            params.put("start","40%,90%");
	            params.put("end","40%,20%");
	            params.put("duration","2");
	            Object res= driver.executeScript("mobile:touch:swipe",params);
	        if(count==5)
	        {
	            break;
	        }
	        }
	    }
	}

	public List<AndroidElement> GetMatchingElements() {
		List<AndroidElement> matchingElement = null;
		 try
         {
             if (this.parentElement == null)
                 return TestBase.driver.findElements(this._by);
             else {
            	 matchingElement = this.driver.findElements(this._by);
            	 return matchingElement;
             }
         }
         catch (Exception ex)
         {
        	 return matchingElement;
         }
	}
}
