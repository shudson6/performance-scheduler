package performancescheduler.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import performancescheduler.data.Feature;

public class FeaturePanelCellRenderer extends JPanel implements ListCellRenderer<Feature> {
	private final String WIDE_STRING = "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM";
	private final Border unselBorder = BorderFactory.createLineBorder(Color.darkGray, 2);
	private final Border selBorder = BorderFactory.createLineBorder(Color.blue, 2);
	
	private String title;
	private String rating;
	private String runtime;
	private String amenity;

	@Override
	public Component getListCellRendererComponent(JList<? extends Feature> list, Feature value, int index,
			boolean isSelected, boolean cellHasFocus) {
		title = value.getTitle();
		rating = value.getRating().toString();
		runtime = Integer.toString(value.getRuntime()) + " min";
		amenity = amenityString(value);
		
		if (isSelected) {
			setBorder(selBorder);
			setBackground(Color.cyan);
		} else {
			setBorder(unselBorder);
			setBackground(Color.white);
		}
		return this;
	}
	
	public Dimension getPreferredSize() {
		if (isVisible()) {
			return new Dimension(getGraphics().getFontMetrics().stringWidth(WIDE_STRING) + getInsets().left
					+ getInsets().right,
					getGraphics().getFontMetrics().getHeight() * 2 + getInsets().top + getInsets().bottom);
		}
		return new Dimension(200, 80);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString(title, left(), topRowBase(g));
		g.drawString(rating, left(), bottomRowBase(g));
		g.drawString(runtime, runtimeX(g), bottomRowBase(g));
		g.drawString(amenity, amenityX(g), bottomRowBase(g));
	}
	
	private int left() {
		return getInsets().left;
	}
	
	private int amenityX(Graphics g) {
		return left() + g.getFontMetrics().stringWidth("MMMMMMMMMMMMM");
	}
	
	private int bottomRowBase(Graphics g) {
		return getHeight() - getInsets().bottom - g.getFontMetrics().getDescent();
	}
	
	private int runtimeX(Graphics g) {
		return left() + g.getFontMetrics().stringWidth("MMMMM");
	}
	
	private int topRowBase(Graphics g) {
		return getInsets().top + g.getFontMetrics().getHeight();
	}
	
	private String amenityString(Feature f) {
		StringBuilder sb = new StringBuilder(8);
		if (f.hasClosedCaptions()) {
			sb.append("CC");
		}
		if (f.hasOpenCaptions()) {
			sb.append("OC");
		}
		if (f.hasDescriptiveAudio()) {
			sb.append("DA");
		}
		if (sb.length() > 2) {
			sb.insert(2, " ");
		}
		if (sb.length() > 5) {
			sb.insert(5, " ");
		}
		return sb.toString();
	}
}
