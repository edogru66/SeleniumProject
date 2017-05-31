package tr.org.esb.webpagecategorization.categorizationelement;

import org.openqa.selenium.WebElement;

public class ImageCategorizationElement extends ACategorizationElement {

	public ImageCategorizationElement(WebElement element) {
		super("img", element);
	}

	@Override
	public double calculateWeight() {
		int size = Integer.parseInt(e.getAttribute("width"))
				* Integer.parseInt(e.getAttribute("height"));
		if (size > 48 * 48) {
			return 200;
		}
		return 0;
	}
}
