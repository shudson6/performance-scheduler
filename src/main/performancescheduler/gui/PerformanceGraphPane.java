package performancescheduler.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class PerformanceGraphPane extends JScrollPane {
    private PerformanceGraph graph;
    
    public PerformanceGraphPane(PerformanceGraph graphView) {
        Objects.requireNonNull(graphView);
        graph = graphView;
        setViewportView(graphView);
        setRowHeaderView(new RowHeader());
        setColumnHeaderView(new TimelinePanel());
    }
    
    public class RowHeader extends JComponent {        
        public RowHeader() {
            super();
            setPreferredSize(new Dimension(80,
                    graph.getAuditoriumHeight() * graph.getPerformanceManager().getAuditoriumCount()));
            setOpaque(true);
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            for (int aud = 1; aud <= graph.getPerformanceManager().getAuditoriumCount(); aud++) {
                g.drawLine(0, aud * graph.getAuditoriumHeight(), getWidth(), aud * graph.getAuditoriumHeight());
                g.drawString(Integer.toString(aud),
                        (getWidth() - g.getFontMetrics().stringWidth(Integer.toString(aud))) / 2, 
                        aud * graph.getAuditoriumHeight() - (graph.getAuditoriumHeight() - g.getFontMetrics().getHeight())
                        / 2);
            }
        }
    }
    
    class TimelinePanel extends JPanel
    {

        /**
         * the preferred height of the timeline.
         */
        public static final int PREFERRED_HEIGHT = 42;

        /**
         * Creates a new instance of TimelinePanel
         */
        public TimelinePanel()
        {
            super();
            // set the preferred size so there is room for 24 hours.
            setPreferredSize(new Dimension((int) (24 * 60 * graph.getPixelsPerMinute()), PREFERRED_HEIGHT));
        }

        /**
         * Draws the timeline.
         */
        @Override
        public void paintComponent(Graphics g)
        {
            // let JPanel handle the background painting
            super.paintComponent(g);
            // set the color to draw
            g.setColor(Color.BLACK);
            // get font metrics to measure the size of the strings
            FontMetrics fontMetrics = g.getFontMetrics();
            // pixels per minute
            double ppm = graph.getPixelsPerMinute();
            // get offsets for 15 and 30 and 60 minute intervals
            int offset15 = (int) (15 * ppm);
            int offset30 = (int) (30 * ppm);
            int offset60 = (int) (60 * ppm);
            // get the height of this component
            int height = getHeight();
            // and the heights of the lines that represent hours, half hours, and quarter hours
            int hourHeight = height / 2;
            int halfHeight = height / 3;
            int quarterHeight = height / 4;
            // a time value to keep up with the time to display
            LocalTime time = graph.getModel().getRangeStart().toLocalTime();
            // loop through 24 hours, incrementing time with each pass
            for (int hour = 0; hour < 24; hour++, time = time.plusHours(1))
            {
                // the x location of this hour
                int hourX = hour * offset60;
                // the time
                String timeText = time.format(DateTimeFormatter.ofPattern("h:a"));
                // the width of the time string
                int timeWidth = fontMetrics.stringWidth(timeText);
                // x location of time string
                int timeX = hourX - timeWidth / 2;
                // as long as the time string doesn't fall off the screen at either end, draw it
                if (!(timeX < 0) && !(timeX + timeWidth > getWidth()))
                {
                    g.drawString(timeText, timeX, fontMetrics.getHeight());
                }
                // the lines
                int lineX = hourX;
                // draw the big line for the hour
                g.drawLine(lineX, height, lineX, height - hourHeight);
                // draw a littler line for the half hour
                lineX += offset30;
                g.drawLine(lineX, height, lineX, height - halfHeight);
                // draw 2 littler lines for the quarter hours
                lineX -= offset15;
                g.drawLine(lineX, height, lineX, height - quarterHeight);
                lineX += offset30;
                g.drawLine(lineX, height, lineX, height - quarterHeight);
            }
            // draw a border across the bottom
            g.drawLine(0, height - 1, getWidth(), height - 1);
        }
    } // inner class TimelinePanel
}
