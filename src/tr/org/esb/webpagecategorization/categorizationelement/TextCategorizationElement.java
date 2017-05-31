package tr.org.esb.webpagecategorization.categorizationelement;

import org.openqa.selenium.WebElement;

public class TextCategorizationElement extends ACategorizationElement {

	public TextCategorizationElement(WebElement element) {
		super("text", element);
	}

	@Override
	public double calculateWeight() {
		return e.getAttribute("innerText").trim().length();
	}
}
