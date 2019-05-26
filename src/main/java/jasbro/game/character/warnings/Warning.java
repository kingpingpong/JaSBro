package jasbro.game.character.warnings;

import jasbro.gui.pictures.ImageData;

public class Warning implements Comparable<Warning> {
	private Severity severity;
	private String message;
	
	public Warning(Severity severity, String message) {
		super();
		this.severity = severity;
		this.message = message;
	}
	
	public Severity getSeverity() {
		return severity;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public int compareTo(Warning o) {
		return severity.compareTo(o.severity);
	}
	
	public ImageData getIcon() {
		return severity.getIcon();
	}
	
	
}