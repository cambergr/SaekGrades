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
import static java.awt.Component.CENTER_ALIGNMENT;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.*;
import javax.swing.table.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

class AmkInfoDialog extends JDialog {
	private LessonDescription lessonDescription = new LessonDescription();	
	private final int STUDENTS, LESSONS;
	private final boolean isGradeFinal;
	private JPanel topPanel, centralPanel, optionalPanel, bottomPanel;
	private Font fontBig = new Font("Arial", Font.PLAIN, 18);
	private Font font = new Font("Arial", Font.PLAIN, 15);
	private Font fontBold = new Font("Arial", Font.BOLD, 15);
	private Font fontSmall = new Font("Arial", Font.PLAIN, 14);    
	private int width = 870, rowHeight = 22, rowHeightSmall = 14;	
	
	AmkInfoDialog(JFrame parent, StudentRec[] student, int students, int lessons, boolean isGradeFinal, int amk, String semester) {
		super(parent, "Ανάλυση Βαθμολογίας ΑΜΚ", true);
		this.STUDENTS = students;
		this.LESSONS = lessons;
		this.isGradeFinal = isGradeFinal;
		int key = 0;
        for (int i = 0; i < STUDENTS; i++) {
            if (student[i].amk == amk) {
                key = i;
			}
        }
		Map<String, String> dictionary = new HashMap<>();
		dictionary.put("A", "Α'");
		dictionary.put("B", "Β'");
		dictionary.put("C", "Γ'");
		dictionary.put("D", "Δ'");
		
		// Επικεφαλίδες προγράμματος
		JLabel title = new JLabel(dictionary.get(semester) + " ΕΞΑΜΗΝΟ");
		title.setFont(fontBig);
		title.setAlignmentX(CENTER_ALIGNMENT);
		

		JLabel amkLabel = new JLabel("AMK: " + amk);
		amkLabel.setFont(fontBig);
		amkLabel.setForeground(Color.BLUE);
		amkLabel.setAlignmentX(CENTER_ALIGNMENT);
		amkLabel.setBorder(new EmptyBorder(10, 5, 5, 5));
		
		// Οι επικεφαλίδες θα μπούν σε πάνελ στην κορυφή
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		topPanel.add(title);
		topPanel.add(amkLabel);

		String gradeMsg[] = {"ΤΕΛΙΚΗ ΒΑΘΜΟΛΟΓΙΑ", "ΠΡΟΣΩΡΙΝΗ ΒΑΘΜΟΛΟΓΙΑ - ΔΕΝ ΕΓΙΝΕ ΤΕΛΙΚΗ ΕΞΕΤΑΣΗ - ΑΡΙΣΤΑ = 4.00"};
		JLabel gradeLabel;
		if (isGradeFinal) {
			gradeLabel = new JLabel(gradeMsg[0]);
			gradeLabel.setForeground(Color.BLUE);
		} else {
			gradeLabel = new JLabel(gradeMsg[1]);
			gradeLabel.setForeground(Color.RED);
		}
		gradeLabel.setFont(font);
		gradeLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		DefaultTableModel model = new DefaultTableModel();
		// Δημιουργία του πίνακα με απενεργοποιημένη την επεξεργασία
		JTable table = new JTable(model) {
			public boolean isCellEditable(int row, int column) {                
					return false;               
			}
			// και στοίχιση στο κέντρο
			DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
			{ 
				renderRight.setHorizontalAlignment(SwingConstants.CENTER);
			}
			@Override
			public TableCellRenderer getCellRenderer (int arg0, int arg1) {
				return renderRight;
			}				
		};
		table.setRowSelectionAllowed(false);		
		table.setFont(font);
		table.setRowHeight(rowHeight);
		JTableHeader header = table.getTableHeader();
		header.setFont(font);
		//header.setPreferredSize(new Dimension(1100, rowHeight * 2));
		header.setResizingAllowed(false);
		header.setReorderingAllowed(false);
		header.setBackground(Color.LIGHT_GRAY);
		header.setForeground(Color.blue);
	
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollpane.setPreferredSize(new Dimension(width, rowHeight * 3));
		String[] modelHeader = new String[LESSONS + 2];
		modelHeader[0] = "";
		for (int i = 1; i <= LESSONS; i++) {
			modelHeader[i] = "M" + i;
		}
		modelHeader[LESSONS + 1] = "ΤΕΛΙΚΟΣ ΒΑΘΜΟΣ";
		model.setColumnIdentifiers(modelHeader);
		Object[] rows = new Object[LESSONS + 2];
		rows[0] = "ΒΑΘΜΟΣ";
		for (int i = 0; i < LESSONS; i++) {
			rows[i + 1] = student[key].m[i].lessonGrade;
		}
		rows[LESSONS + 1] = new BigDecimal(SaekGrades.student[key].grade).setScale(2, RoundingMode.HALF_UP);
		model.addRow(rows);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(LESSONS + 1).setPreferredWidth(160);
		rows[0] = "ΚΑΤΑΤΑΞΗ";
		for (int i = 0; i < LESSONS; i++) {
			rows[i + 1] = setClassification(student, key, i);
		}
		rows[LESSONS + 1] = setClassification(student, key, -1);
		model.addRow(rows);
		
		JTextArea msg = new JTextArea(getMessage(student, key));
		msg.setForeground(new Color(255, 173, 0));
		msg.setFont(fontBold);
		msg.setEditable(false);
		msg.setLineWrap(true);
		msg.setOpaque(false);
		msg.setHighlighter(null);
		msg.setBorder(new EmptyBorder(5, 0, 2, 0));
		JTextArea classification = new JTextArea("KATATAΞΗ = Η θέση σου σε σχέση με τους βαθμούς των άλλων σπουδαστών");
		classification.setFont(font);
		classification.setEditable(false);
		classification.setLineWrap(true);
		classification.setOpaque(false);
		classification.setHighlighter(null);
		classification.setBorder(new EmptyBorder(0, 0, 10, 0));

		JButton diagramBtn = new JButton("Διάγραμμα Προόδου");
		diagramBtn.setAlignmentX(CENTER_ALIGNMENT);
		diagramBtn.addActionListener((e) -> {
			double[] studentCoord = {0, 0, 0, 0};
			double[] classCoord = {0, 0, 0, 0};
			calcCoord(amk, studentCoord, classCoord);
			new PlotGraph(AmkInfoDialog.this, amk, studentCoord, classCoord);
		});
		
		centralPanel = new JPanel();
		centralPanel.setLayout(new BoxLayout(centralPanel, BoxLayout.Y_AXIS));
		centralPanel.setBorder(new EmptyBorder(10, 30, 10, 30)); // top, left, bottom, right
		centralPanel.add(gradeLabel);
		centralPanel.add(scrollpane);
		centralPanel.add(msg);
		centralPanel.add(classification);
		centralPanel.add(diagramBtn);
		
		// Το πάνελ αυτό εμφανίζεται μόνο όταν δεν έχει γίνει Τελική Εξέταση
		optionalPanel = new JPanel();
		optionalPanel.setLayout(new BoxLayout(optionalPanel, BoxLayout.Y_AXIS));
		optionalPanel.setBorder(new EmptyBorder(30, 10, 10, 10));
		centralPanel.add(optionalPanel);
		showOptionalInfo(student, key);
		
		bottomPanel  = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.setBorder(new EmptyBorder(0, 25, 0, 0)); // top, left, bottom, right		
		bottomPanel.setPreferredSize(new Dimension(width, rowHeight * 5));
		// Μετατρέπω το string semester σε charArray και αφαιρώ το 65 (ASCII τιμή του Α)
		// Έτσι το 'Α' θα γίνει 65-65=0, το 'Β' 66-65=1 κλπ.
		fillBottomPanel(semester.toCharArray()[0]-65);
		
		add(topPanel, BorderLayout.NORTH);
		add(centralPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		pack();
		setLocationRelativeTo(parent);
		setVisible(true);		
	}
	
	// Εμφανίζει επιπλέον πληροφορίες αν δεν έχει γίνει Τελική Εξέταση
	private void showOptionalInfo(StudentRec[] student, int key) {
		if (isGradeFinal) {
			return;
		}
		JLabel passLabel = new JLabel("ΤΙ ΠΡΕΠΕΙ ΝΑ ΓΡΑΨΕΙΣ ΣΤΙΣ ΕΞΕΤΑΣΕΙΣ ΓΙΑ ΝΑ ΠΕΡΑΣΕΙΣ"); 
		passLabel.setFont(font);
		passLabel.setForeground(Color.BLUE);
		passLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		
		JLabel maxLabel = new JLabel("ΜΕΓΙΣΤΟΣ ΤΕΛΙΚΟΣ ΒΑΘΜΟΣ ΑΝ ΓΡΑΨΕΙΣ 10 ΣΤΙΣ ΕΞΕΤΑΣΕΙΣ"); 
		maxLabel.setFont(font);
		maxLabel.setForeground(Color.BLUE);
		maxLabel.setAlignmentX(CENTER_ALIGNMENT);
		maxLabel.setBorder(new EmptyBorder(30, 0, 0, 0));		
		
		optionalPanel.add(passLabel);
		createOptionalTable(student, key, 0);
		optionalPanel.add(maxLabel);
		createOptionalTable(student, key, 1);
	}
	
	// Δημιουργεί τους επιπλέον πίνακες αν δεν έχει γίνει η Τελική Εξέταση
	// key: Η γραμμή του student που βρίσκεται το ΑΜΚ
	// type: 0 = passingGrade, 1 = maxGrade
	private void createOptionalTable(StudentRec[] student, int key, int type) {
		DefaultTableModel model = new DefaultTableModel();
		// Δημιουργία του πίνακα με απενεργοποιημένη την επεξεργασία
		JTable table = new JTable(model) {
			public boolean isCellEditable(int row, int column) {                
					return false;               
			}
			// και στοίχιση στο κέντρο
			DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
			{ 
				renderRight.setHorizontalAlignment(SwingConstants.CENTER);
			}
			@Override
			public TableCellRenderer getCellRenderer (int arg0, int arg1) {
				return renderRight;
			}				
		};
		table.setRowSelectionAllowed(false);		
		table.setFont(font);
		table.setRowHeight(rowHeight);
		JTableHeader header = table.getTableHeader();
		header.setFont(font);
		header.setResizingAllowed(false);
		header.setReorderingAllowed(false);
		header.setBackground(Color.LIGHT_GRAY);
		header.setForeground(Color.blue);
	
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollpane.setPreferredSize(new Dimension(width - 150, rowHeight * 2));
		
		String[] modelHeader = new String[LESSONS];
		for (int i = 0; i < LESSONS; i++) {
			modelHeader[i] = "M" + (i + 1);
		}
		model.setColumnIdentifiers(modelHeader);
		
		Object[] rows = new Object[LESSONS];
		for (int i = 0; i < LESSONS; i++) {
			if (type == 0) {
				rows[i] = student[key].m[i].passingGrade;				
			} else {
				rows[i] = student[key].m[i].maxGrade;								
			}
		}
		model.addRow(rows);
		optionalPanel.add(scrollpane);
	}
	
	// Εμφανίζει τα μαθήματα του εξαμήνου στο κάτω μέρος του παράθυρου
	// semester: 0 = Α Εξάμηνο, 1 = Β Εξάμηνο, 2 = Γ Εξάμηνο, 3 = Δ Εξάμηνο, 
	private void fillBottomPanel(int semester) {
		JLabel[] lessons = new JLabel[LESSONS];
		for (int i = 0; i < LESSONS; i++) {
			lessons[i] = new JLabel(lessonDescription.lessonNames[semester][i]);
			lessons[i].setFont(fontSmall);
			lessons[i].setPreferredSize(new Dimension(430, rowHeightSmall));
			bottomPanel.add(lessons[i]);
		}
	}

	// Υπολογίζει την κατάταξη ενός σπουδαστή σε σχέση με την βαθμολογία των άλλων
	// id = Δείκτης του σπουδαστή στον πίνακα student[]
	// lesson = Το μάθημα, εάν είναι -1 τότε υπολογίζεται η κατάταξη βάσει γενικού βαθμού
	// Ξεκινάμε βάζοντας στον σπουδαστή κατάταξη 1 και μετά αυξάνουμε το
	// classification κάθε φορά που συναντάμε άλλον με μεγαλύτερο βαθμό
	private int setClassification(StudentRec student[], int id, int lesson) {
		int classification = 1;
		if (lesson >= 0) { // Αναζητούμε κατάταξη σε συγκεκριμένο μάθημα
			for (int i = 0; i < STUDENTS; i++)
				if (student[id].m[lesson].lessonGrade < student[i].m[lesson].lessonGrade)
					classification++;
		} else {    // Αναζητούμε την κατάταξη στο γενικό βαθμό
			for (int i = 0; i < STUDENTS; i++) {
				if (student[id].grade < student[i].grade) {
					classification++;
				}
			}
		}
		return classification;
	}

	// Επιστρέφει σχολιασμό για την βαθμολογία του σπουδαστή
	// id = Δείκτης του σπουδαστή στον πίνακα student[]
	private String getMessage(StudentRec student[], int id)
	{
		String msg;
		double score;
		// Αν δεν έχουν γίνει τελικές εξετάσεις κανονικοποίησε
		// τον τελικό βαθμό με άριστα το 10
		if (!isGradeFinal)
			score = student[id].grade * 2.5; // 10 * grade / 4
		else
			score = student[id].grade;
		// Μήνυμα ανάλογο του βαθμού
		if (score >= 9)
			msg = "Aριστα! Τους έσκισες πάλι τσακάλι μου. Μπράβο you are the best, μπράβο έκανες την τύχη σου!";
		else if (score >= 8)
			msg = "Καλάααααααα ε, μπράβο, όοοχι μπράαβα. Brava, bravaaa, bravissima!";
		else if (score >= 7)
			msg = "(insert village xwriatik accent) Πολύ καλό, πολύ καλό!";
		else if (score >= 5)
			msg = "Ικανοποιητικά, αλλά είσαι κωλόφαρδo και μπορείς και καλύτερα. "  
				+ "Παρόλα αυτά, είναι ναι από μένα. It's a yes from me!";
		else
			msg = "Αποτυχία.. Bro μου, βουνό μου, δεν είσαι καλά. Μην πας να τους μιλήσεις, "
				+ " σήκω και πάτα το καλύτερα.\nΣυνέχισε να προσπαθείς, το ξέρω ότι μπορούμε. "
				+ "Πάλεψε το, θα περάσει κι' αυτό.. Θα τα καταφέρουμε, θα δεις!";
		return msg;
	}
	
	private static void calcCoord(int amk, double[] studentCoord, double[] classCoord) {
		String[] course = {"A", "B", "C", "D"};
		// Αποθήκευσε το υπάρχον εξάμηνο για να το επαναφέρεις αργότερα
		String oldSemester = AppWindow.semesterSelected;
		
		for (int i = 0; i < course.length; i++) {
			SaekGrades.readGrades(course[i]);
			// Δες αν έγιναν τελικές εξετάσεις
			boolean isFinal = AppWindow.calcLessonAvg(SaekGrades.student, -1, 2) != 0;
			// Βρες το AMK και πάρε το βαθμό του
			for (int j = 0; j < SaekGrades.STUDENTS; j++) {
				if (SaekGrades.student[j].amk == amk) {
					// Αν δεν έχει γίνει Τελική Εξέταση
					if (!isFinal) {
						studentCoord[i] = SaekGrades.student[j].grade * 2.5;
					} else {
						studentCoord[i] = SaekGrades.student[j].grade;
					}
				}
			}
			// Βρες τη μέση βαθμολογία του εξαμήνου
			if (!isFinal) {
				classCoord[i] = AppWindow.calcLessonAvg(SaekGrades.student, -1, 3) * 2.5;				
			} else {
				classCoord[i] = AppWindow.calcLessonAvg(SaekGrades.student, -1, 3);
			}
		}
		// Επανέφερε το αρχικό εξάμηνο
		SaekGrades.readGrades(oldSemester);
	}
}
