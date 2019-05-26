package jasbro.gui.dnd;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class ToleranceMouseListener implements MouseMotionListener, MouseListener {
	// Our tolerance in both x and y direction
	private static int CLICK_TOLERANCE = 15;
	
	// The point where we pressed the mouse button down
	private Point startPoint;
	private Object initialSource;
	
	// Method for checking the tolerance 
	private boolean checkDragTolerance(Point e) {
		if (startPoint != null) {
			float changesx = Math.abs(e.x - startPoint.x);
			float changesy = Math.abs(e.y - startPoint.y);
			return (changesx < CLICK_TOLERANCE && changesy < CLICK_TOLERANCE);
		}
		else {
			return true;
		}
	}
	
	// Store where we pressed the mouse button down
	@Override
	public void mousePressed(MouseEvent e) {
		startPoint = e.getPoint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (checkDragTolerance(e.getPoint())) {
			e.consume();
		}
		else {
			e.setSource(initialSource);
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		startPoint = null;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
	} 
	
	
}