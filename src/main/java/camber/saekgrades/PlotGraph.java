/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package camber.saekgrades;

/**
 *
 * @author Γιώργος Καμπερογιάννης
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.geom.*;

public class PlotGraph extends JPanel {
	// Βαθμολογίες σπουδαστή και τάξης (θα χρησιμοποιηθούν σαν συντεταγμένες στο διάγραμμα)
	double[] studentCoord;
	double[] classCoord;
	int margin = 50;	// Περιθώριο από τα άκρα του παραθύρου
	
	public PlotGraph(JDialog parent, int amk, double[] studentCoord, double[] classCoord) {
		this.studentCoord = studentCoord;
		this.classCoord = classCoord;
		
		JDialog dialog = new JDialog(parent, "Διάγραμμα προόδου", true);
		
		JLabel title = new JLabel("AMK " + amk);
		title.setAlignmentX(CENTER_ALIGNMENT);
		JLabel subtitle = new JLabel("Σύγκριση Βαθμολογίας Σπουδαστή με το Μέσο Όρο Τάξης");
		subtitle.setAlignmentX(CENTER_ALIGNMENT);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.add(title);
		topPanel.add(subtitle);
	
		Box studentLine = Box.createHorizontalBox();
		studentLine.setBackground(Color.red);
		studentLine.setOpaque(true);
		studentLine.setPreferredSize(new Dimension(50, 2));
		
		Box classLine = Box.createHorizontalBox();
		classLine.setBackground(Color.blue);
		classLine.setOpaque(true);
		classLine.setPreferredSize(new Dimension(50, 2));
		
		JLabel studentLabel = new JLabel("Βαθμολογία Σπουδαστή");
		JLabel classLabel = new JLabel("Μέσος Όρος τάξης");
		
		JPanel innerPanel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		innerPanel1.add(studentLine);
		innerPanel1.add(studentLabel);
		
		JPanel innerPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		innerPanel2.add(classLine);
		innerPanel2.add(classLabel);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		bottomPanel.setBorder(new EmptyBorder(0, margin, 20, 0));
		bottomPanel.add(innerPanel1);
		bottomPanel.add(innerPanel2);
		
		dialog.add(topPanel, BorderLayout.NORTH);
		dialog.add(this, BorderLayout.CENTER);
		dialog.add(bottomPanel, BorderLayout.SOUTH);
		dialog.pack();
		dialog.setSize(400, 400);
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
	}

	protected void paintComponent(Graphics grf) {
		//create instance of the Graphics to use its methods
		super.paintComponent(grf);
		Graphics2D graph = (Graphics2D) grf;

		//Sets the value of a single preference for the rendering algorithms.
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Πλάτος και Ύψος παράθυρου
		int width = getWidth();
		int height = getHeight();
		// Ετικέτες αξόνων X, Y
		String[] labelX = {"Α", "Β", "Γ", "Δ"};
		double[] labelY = {10, 5};
		int maxGrade = 10;	// Μέγιστη δυνατή βαθμολογία
				
		// Ζωγράφισε τους άξονες X, Y
		graph.draw(new Line2D.Double(margin, height - margin, width - margin, height - margin)); // X
		graph.draw(new Line2D.Double(margin, margin, margin, height - margin));	// Y
		
		// Βρες την τιμή x (απόσταση τιμών x) και την κλίμακα που θα σχεδιαστούν οι τιμές Υ
		double x = (double) (width - 2 * margin) / (labelX.length - 1);
		double scale = (double) (height - 2 * margin) / maxGrade;

		// Βαθμονόμηση και ετικέτες άξονα Χ
		for (int i = 0; i < labelX.length; i++) {
			graph.draw(new Line2D.Double(margin + (i * x), height - margin, margin + (i *x), height -margin + 5));
			int labelDim[] = findStringSize(labelX[i], grf);
			graph.drawString(labelX[i], margin + (int)(i * x) - (labelDim[0] / 2), height - margin + labelDim[1] + 5);			
		}
		int lblDim[] = findStringSize("ΕΞΑΜΗΝA", grf);
		graph.drawString("ΕΞΑΜΗΝA", (width - margin + lblDim[0]) / 2 - lblDim[0] / 2, height - margin + lblDim[1] * 2 + 5);			
		
		// Βαθμονόμηση και ετικέτες άξονα Υ
		for (int i = 0; i < labelY.length; i++) {
			graph.draw(new Line2D.Double(margin, height - margin - scale * labelY[i], margin - 5, height - margin - scale * labelY[i]));
			int labelDim[] = findStringSize(Double.toString(labelY[i]), grf);
			graph.drawString(Double.toString(labelY[i]), margin - labelDim[0] - 5, 
					height - margin - (int)(scale * maxGrade / (i + 1)) - labelDim[1] / 2 + labelDim[2]);			
		}

		// Βαθμολογία σπουδαστή
		// Χρώμα σχεδίασης
		graph.setPaint(Color.RED);
		// Σχεδίασε τα σημεία με τους βαθμούς
		for (int i = 0; i < studentCoord.length; i++) {
			double x1 = margin + i * x;
			double y1 = height - margin - scale * studentCoord[i];
			graph.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
			// Τράβηξε γραμμές μεταξύ των σημείων
			if (i > 0) {
				double x0 = margin + (i - 1) * x;
				double y0 = height - margin - scale * (studentCoord[i - 1]);
				graph.draw(new Line2D.Double(x0, y0, x1, y1));
			}
		}

		// Βαθμολογία Μέσου Όρου Τάξης
		// Χρώμα σχεδίασης
		graph.setPaint(Color.BLUE);
		// Σχεδίασε τα σημεία με τους βαθμούς
		for (int i = 0; i < classCoord.length; i++) {
			double x1 = margin + i * x;
			double y1 = height - margin - scale * classCoord[i];
			graph.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
			// Τράβηξε γραμμές μεταξύ των σημείων
			if (i > 0) {
				double x0 = margin + (i - 1) * x;
				double y0 = height - margin - scale * (classCoord[i - 1]);
				graph.draw(new Line2D.Double(x0, y0, x1, y1));
			}
		}		
	}

	// Επιστρέφει σε πίνακα ακεραίων το μήκος, ύψος, ascent ενός string
	public int[] findStringSize(String str, Graphics grf) {
		Graphics2D graph = (Graphics2D) grf;
		FontMetrics fontMetrics = graph.getFontMetrics();
		Rectangle2D rect = fontMetrics.getStringBounds(str, graph);
		//int x = point.x + (width - (int) rect.getWidth()) / 2;
		//int y = point.y + (height - (int) rect.getHeight()) / 2 + fontMetrics.getAscent();
		int x = (int)rect.getWidth();
		int y = (int)rect.getHeight();
		//graph.drawString(str, x, y);
		int arr[] = {x, y, fontMetrics.getAscent()};
		return arr;
	}
}
