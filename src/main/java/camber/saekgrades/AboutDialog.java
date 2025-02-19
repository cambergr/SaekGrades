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
import static java.awt.Component.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

class AboutDialog extends JDialog {
	private Font fontBig = new Font("Arial", Font.PLAIN, 18);
	private Font font = new Font("Arial", Font.PLAIN, 15);
	private Font fontBold = new Font("Arial", Font.BOLD, 15);
	
	AboutDialog(JFrame parent) {
		super(parent, "Πληροφορίες εφαρμογής", true);

		JLabel l = new JLabel("ΣΑΕΚ Βαθμολογία v.1.0");
		l.setFont(fontBold);
		l.setBorder(new EmptyBorder(0, 0, 10, 0));
		JLabel l1 = new JLabel("Προγραμματιστής: Γιώργος Καμπερογιάννης");
		l1.setFont(font);
		JLabel l2 = new JLabel("Σχολιασμός by Anastasia Crosszeria");
		l2.setFont(font);
		

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBorder(new EmptyBorder(20, 30, 0, 30)); // top, left, bottom, right

		topPanel.add(l);
		topPanel.add(l1);
		topPanel.add(l2);		
		
		String str = "Η εφαρμογή προσφέρει πολλές δυνατότητες ταξινόμησης και ανάλυσης της βαθμολογίας των σπουδαστών.\n" +
					"- Με κλικ στην επικεφαλίδα οποιασδήποτε στήλης ταξινομούνται οι βαθμοί κατά αύξουσα σειρά της στήλης.\n" +
					"- Με δεύτερο κλικ στην ίδια επικεφαλίδα γίνεται ταξινόμηση κατά φθίνουσα σειρά.\n" +
					"- Με διπλό κλικ σε οποιαδήποτε γραμμή εμφανίζονται αναλυτικές πληροφορίες για το ΑΜΚ της γραμμής.\n" +
					"- Στις αναλυτικές πληροφορίες μπορούμε να πάρουμε Διάγραμμα Προόδου σπουδαστή για όλα τα εξάμηνα.\n" + 
					"\n" +
					"Τα αρχεία με τις βαθμολογίες βρίσκονται στον υποφάκελο grades του project.\n" +
					"Το αρχείο ReadMe.pdf που βρίσκεται σ' αυτόν τον υποφάκελο περιέχει αναλυτικές οδηγίες " + 
					"για την προσθήκη νέων βαθμολογιών που θα υπάρξουν στο μέλλον (πχ. Τελική Εξέταση).";
		JTextArea msg = new JTextArea(str);
		msg.setFont(font);
		msg.setEditable(false);
		msg.setLineWrap(true);
		msg.setOpaque(false);
		msg.setHighlighter(null);
		//msg.setBorder(new EmptyBorder(0, 0, 0, 0));
		msg.setPreferredSize(new Dimension(710, 170));
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(0, 25, 0, 30)); // top, left, bottom, right

		panel.add(msg);

		GridLayout gr = new GridLayout();
		gr.setRows(2);
		setLayout(gr);

		add(topPanel);
		add(panel);
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);				
	}
}
