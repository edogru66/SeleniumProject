package tr.org.esb.webpagecategorization;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;

import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tr.org.esb.webpagecategorization.categorizationelement.ACategorizationElement;
import tr.org.esb.webpagecategorization.categorizationelement.CategorizationElementFactory;

public class WebpageCategorization {

	private Map<String, Double> map;
	private WebDriver webDriver;
	
	private class Entry implements Map.Entry<String, Double>{

		private String key;
		private Double value;
		
		public Entry(String aKey, Double aValue) {
			this.key = aKey;
			setValue(aValue);
		}
		
		@Override
		public String getKey() {
			return this.key;
		}

		@Override
		public Double getValue() {
			return this.value;
		}

		@Override
		public Double setValue(Double value) {
			this.value = value;
			return value;
		}
	}
	
	private Entry calc(ACategorizationElement e) {
		return new Entry(e.getTag(), e.calculateWeight());
	}

	public WebpageCategorization(String aUrl) throws MalformedURLException, TimeoutException {
		try {
			URL url = new URL(aUrl);
			System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, "bin/chromedriver");
			System.setProperty(ChromeDriverService.CHROME_DRIVER_SILENT_OUTPUT_PROPERTY, "true");
			webDriver = new ChromeDriver();

			//webDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
			webDriver.navigate().to(url);

			/*
			 * For recommendations in Vimeo to load
			 */
			if (webDriver.findElements(By.tagName("video")).size() > 0) {
				List<WebElement> list = webDriver.findElements(By.tagName("video"));
				(new Actions(webDriver)).moveToElement(list.get(list.size() - 1)).build().perform();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

			map = new HashMap<>();
			processWebdriver();

		} catch (MalformedURLException | TimeoutException e) {
			throw e;
		} finally {
			if (webDriver != null)
				webDriver.quit();
		}
	}

	/*
	 * This is a recursive method in order to process all the
	 * iframes (inside an iframe etc)
	 */
	private void processWebdriver() {
		WebElement body = webDriver.findElement(By.tagName("body"));
		if (body == null || body.getAttribute("innerHTML").trim().isEmpty()) {
			return;
		} else {
			for (WebElement e : webDriver.findElements(By.cssSelector("img, video, iframe, body"))) {
				if ("iframe".equals(e.getTagName())) {
					webDriver.switchTo().frame(e);
					processWebdriver();
					webDriver.switchTo().parentFrame();
				} else {
					Entry entry = calc(CategorizationElementFactory.getCategorizationElement(e));
					double weight = entry.getValue() + map.getOrDefault(entry.getKey(), 0.0);
					map.put(entry.getKey(), weight);
				}
			}
		}
	}
	
	
	private double getTotal() {
		return map.entrySet().stream().mapToDouble(Map.Entry::getValue).sum();
	}
	
	private double getCount(String key){
		return map.getOrDefault(key, 0.0);
	}
	
	public double getPercent(String key){
		return getCount(key) / getTotal();
	}

}