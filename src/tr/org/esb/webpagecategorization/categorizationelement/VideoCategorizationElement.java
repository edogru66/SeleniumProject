package tr.org.esb.webpagecategorization.categorizationelement;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class VideoCategorizationElement extends ACategorizationElement {

	public VideoCategorizationElement(WebElement element) {
		super("video", element);
	}

	@Override
	public double calculateWeight() {
		if (!e.getAttribute("src").isEmpty() || e.findElements(By.tagName("source")).size()>0) {
			return 6000;
		}
		return 0;
	}
}
