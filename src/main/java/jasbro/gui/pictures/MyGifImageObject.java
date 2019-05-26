package jasbro.gui.pictures;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

public class MyGifImageObject extends BufferedImage {
	private List<ImageFrame> imageFrames = new ArrayList<ImageFrame>();
	private int currentImage = 0;
	private Set<ImageObserver> observers = new HashSet<ImageObserver>();
	private boolean active = false;
	private long lastActive = 0;
	private RenderThread renderThread;
	
	public MyGifImageObject(List<ImageFrame> imageFrames) {
		super(imageFrames.get(0).getImage().getWidth(), imageFrames.get(0).getImage().getHeight(), BufferedImage.TRANSLUCENT);
		this.imageFrames = imageFrames;
		update();
	}
	
	private class RenderThread extends Thread {
		@Override
		public void run() {
			lastActive = System.currentTimeMillis();
			long timeDiff = 0;
			do {
				try {
					if (!observers.isEmpty() && active && imageFrames.size() > 1) {
						int delay = imageFrames.get(currentImage).getDelay();
						if (delay <= 0) {
							delay = 5;
						}
						Thread.sleep(delay * 10);
						currentImage++;
						if (currentImage >= imageFrames.size()) {
							currentImage = 0;
						}
						
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								update();
								
								for (final ImageObserver observer : observers) {
									observer.imageUpdate(getCurrentImage(), ImageObserver.ALLBITS, 0, 0, getCurrentImage().getWidth(),
											getCurrentImage().getHeight());
								}
							}
						});
					}
					else {
						Thread.sleep(500);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				timeDiff = System.currentTimeMillis() - lastActive;
				if (timeDiff > 1000) {
					active = false;
				}
			}
			while (timeDiff < 10000);
		}
	}
	
	public synchronized void update() {
		Graphics g = MyGifImageObject.super.createGraphics();
		g.clearRect(0, 0, getCurrentImage().getWidth(), getCurrentImage().getHeight());
		g.drawImage(getCurrentImage(), 0, 0, getCurrentImage().getWidth(), getCurrentImage().getHeight(), null);
		g.dispose();
	}
	
	public void setActive() {
		active = true;
		lastActive = System.currentTimeMillis();
		if ((renderThread == null || !renderThread.isAlive())) {
			AccessController.doPrivileged(new PrivilegedAction<Void>() {
				@Override
				public Void run() {
					renderThread = new RenderThread();
					renderThread.start();
					return null;
				}
			});
		}
	}
	
	public BufferedImage getCurrentImage() {
		return imageFrames.get(currentImage).getImage();
	}
	
	@Override
	public int getWidth(ImageObserver observer) {
		if (observer != null) {
			observers.add(observer);
		}
		setActive();
		return super.getWidth(observer);
	}
	
	@Override
	public int getHeight(ImageObserver observer) {
		if (observer != null) {
			observers.add(observer);
		}
		setActive();
		return super.getHeight(observer);
	}
	
	public List<ImageFrame> getImageFrames() {
		return imageFrames;
	}
}