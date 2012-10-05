package pl.jedenpies.android.tracker.list;

public abstract class JPListItem {

	protected String title;
	protected String details;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	
	/**
	 * Typ widoku dla tego konkretnego typu itemu.
	 * Resource id z katalogu layout
	 * @return
	 */
	public abstract int getItemViewType();
}
