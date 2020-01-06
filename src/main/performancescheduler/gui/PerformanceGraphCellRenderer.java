package performancescheduler.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.time.format.DateTimeFormatter;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import performancescheduler.data.Performance;

@SuppressWarnings("serial")
public class PerformanceGraphCellRenderer extends JComponent {
    private final Border normalBorder = new LineBorder(Color.BLACK, 1);
    private final Border selectedBorder = new LineBorder(Color.BLUE, 2);
    private final Color regularColor = Color.CYAN;
    private final Color threeDColor = Color.YELLOW;
    private final Color fontColor = Color.BLACK;
    private final Insets insets = new Insets(4, 4, 4, 4);
    
    private Performance p;
    private boolean selected;
    private boolean focused;
    
    public Component getCellRendererComponent(PerformanceGraph graph, Performance value, boolean isSelected,
            boolean cellHasFocus) {
        p = value;
        selected = isSelected;
        focused = cellHasFocus;
        setBounds(graph);
        setBorder(selected ? selectedBorder : normalBorder);
        setOpaque(true);
        return this;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(p.getFeature().is3d() ? threeDColor : regularColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        // now to lay on some text
        g.setColor(fontColor);
        // TODO make the pattern selectable and get it from somewhere else
        g.drawString(p.getTime().format(DateTimeFormatter.ofPattern("HH:mm")), insets.left,
                insets.top + g.getFontMetrics().getAscent());
        g.drawString(p.getFeature().getTitle(), insets.left,
                insets.top + g.getFontMetrics().getHeight() + g.getFontMetrics().getAscent());
    }
    
    private void setBounds(PerformanceGraph g) {
        setBounds(new Rectangle(g.convertTimeToCoordinateX(p.getDateTime()),
                g.convertAudNumToCoordinateY(p.getAuditorium()) + 2,
                g.getPixelsPerMinute() * p.getFeature().getRuntime(),
                g.getAuditoriumHeight() - 3));
    }
}
