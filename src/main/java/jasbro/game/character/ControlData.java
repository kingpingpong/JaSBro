package jasbro.game.character;

public class ControlData {
	private long controlGenerated = 0;
	private long controlUsed = 0;
	private long diff = 0;
	
	public void add(int value) {
		diff += value;
		if (value < 0) {
			controlUsed -= value;
		}
		else {
			controlGenerated += value;
		}
	}
	
	public long getControlGenerated() {
		return controlGenerated;
	}
	public void setControlGenerated(long controlGenerated) {
		this.controlGenerated = controlGenerated;
	}
	public long getControlUsed() {
		return controlUsed;
	}
	public void setControlUsed(long controlUsed) {
		this.controlUsed = controlUsed;
	}
	public long getDiff() {
		return diff;
	}
	public void setDiff(long diff) {
		this.diff = diff;
	}
	
	
}