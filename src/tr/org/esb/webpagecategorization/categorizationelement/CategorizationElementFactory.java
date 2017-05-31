package tr.org.esb.webpagecategorization.categorizationelement;

import org.openqa.selenium.WebElement;

public class CategorizationElementFactory {

	private CategorizationElementFactory() {}

	public static ACategorizationElement getCategorizationElement(WebElement e) {
		switch (e.getTagName()) {
		case "img":
			return new ImageCategorizationElement(e);
		case "video":
			return new VideoCategorizationElement(e);
		default:
			return new TextCategorizationElement(e);
		}
	}
}
