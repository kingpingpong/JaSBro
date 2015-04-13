package jasbro.gui.objects.div;

import jasbro.gui.pictures.ImageData;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class MyButton extends MyImage {
    private JLabel textLabel;
    private boolean enabled = true;
    private List<ActionListener> actionListeners = new ArrayList<ActionListener>();

    public MyButton(String text, final ImageData iconStandard, final ImageData iconHover) {
        setBackgroundImage(iconStandard);
        setLayout(new GridLayout(1, 1, 0, 0));
        
        textLabel = new JLabel(text);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        add(textLabel);

        textLabel.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mousePressed(MouseEvent e) {
                if (enabled && !e.isConsumed()) {
                    e.consume();
                    ActionEvent actionEvent = new ActionEvent(this, 0, "");
                    for (ActionListener actionListener : actionListeners) {
                        if (actionListener != null) {
                            actionListener.actionPerformed(actionEvent);
                        }
                    }
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                setBackgroundImage(iconStandard);
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (enabled) {
                    setBackgroundImage(iconHover);
                    repaint();
                }
            }
        });
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                MyButton.this.updateFontSize();
            }
        });
    }
    
    @Override
    public synchronized void addMouseListener(MouseListener l) {
        if (textLabel != null) {
            textLabel.addMouseListener(l);
        }
    }
    
    public void updateFontSize() {
        Font font = getFont();
        int maxFontSize = getMaxFittingFontSize(font);

        
        font = font.deriveFont((float)(maxFontSize * 3 / 4));
        
        textLabel.setFont(font);
        repaint();
    }
    
    public int getMaxFittingFontSize(Font font) {
        int minSize = 0;
        int maxSize = 40;
        int curSize = font.getSize();
        int width = getWidth();
        int height = getHeight();
        
        if (width != 0 && height != 0 && textLabel.getText() != null) {
            while (maxSize - minSize > 1) {
                font = font.deriveFont((float) curSize);
                FontMetrics fm = getFontMetrics(font);
                int fontWidth = fm.stringWidth(textLabel.getText());
                int fontHeight = fm.getAscent() + fm.getLeading();
    
                if ((fontWidth >= width) || (fontHeight >= height)) {
                    maxSize = curSize;
                    curSize = (maxSize + minSize) / 2;
                } else {
                    minSize = curSize;
                    curSize = (minSize + maxSize) / 2;
                }
            }
        }

        return minSize;
    }
    
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        setGrayscale(!enabled);
        repaint();
    }

    public boolean isEnabled() {
        return enabled;
    }
    
    public void addActionListener(ActionListener al) {
        actionListeners.add(al);
    }
    
    public void setText(String text) {
        textLabel.setText(text);
    }
}
