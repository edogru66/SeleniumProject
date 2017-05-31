package tr.org.esb.webpagecategorization.categorizationelement;

import org.openqa.selenium.WebElement;

public abstract class ACategorizationElement {

	protected WebElement e;
	private String tag;

	public ACategorizationElement(String tag, WebElement element) {
		setTag(tag);
		this.e = element;
	}

	public abstract double calculateWeight();
	
	public String getTagName() {
		return e.getTagName();
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
