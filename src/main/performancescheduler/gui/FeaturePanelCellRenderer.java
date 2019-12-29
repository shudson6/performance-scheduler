package performancescheduler.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import performancescheduler.data.Feature;

public class FeaturePanelCellRenderer extends JPanel implements ListCellRenderer<Feature> {
	private final String WIDE_STRING = "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM";
	
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
		
		setBackground(isSelected ? Color.cyan : Color.white);
		return this;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(200, 80);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString(title, getInsets().left, getInsets().top + g.getFontMetrics().getHeight());
		g.drawString(rating, getInsets().left, getHeight() - getInsets().bottom);
		g.drawString(runtime, (getWidth() - g.getFontMetrics().stringWidth(runtime)) / 2, getHeight() - getInsets().bottom);
		g.drawString(amenity, getWidth() - getInsets().right - g.getFontMetrics().stringWidth(amenity), getHeight() - getInsets().bottom);
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
