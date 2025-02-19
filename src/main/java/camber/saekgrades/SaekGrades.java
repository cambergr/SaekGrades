/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package camber.saekgrades;

/**
 *
 * @author Γιώργος Καμπερογιάννης
 */

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder;
import java.math.BigDecimal;
import java.math.RoundingMode;


class AppWindow extends JFrame implements ActionListener {
	private LessonDescription lessonDescription = new LessonDescription();
	private Font fontBig = new Font("Arial", Font.PLAIN, 18);
	private Font font = new Font("Arial", Font.PLAIN, 15);
	private Font fontSmall = new Font("Arial", Font.PLAIN, 14);
	private JLabel title, subtitle;
	private JTable table, footer;
	private DefaultTableModel model, modelAvg;
	private final TableRowSorter<TableModel> sorter;
	private final List<RowSorter.SortKey> sortKeys;
	private JButton homeworkBtn, proodosBtn, examsBtn, finalGradesBtn, aboutBtn;
	private int width = 870, rowHeight = 22, rowHeightSmall = 14;
	private JPanel topPanel, sidePanel, bottomPanel;
	private int examSelected = 3;
	public static String semesterSelected;

	public AppWindow() {
		super("ΣΑΕΚ Βαθμολογία");

		// Επικεφαλίδες προγράμματος
		title = new JLabel("ΤΕΧΝΙΚΟΣ ΕΦΑΡΜΟΓΩΝ ΠΛΗΡΟΦΟΡΙΚΗΣ");
		title.setFont(fontBig);
		
		title.setAlignmentX(CENTER_ALIGNMENT);
		subtitle = new JLabel("ΒΑΘΜΟΛΟΓΙΑ");
		subtitle.setBorder(new EmptyBorder(5, 0, 0, 0));
		subtitle.setFont(font);
		subtitle.setAlignmentX(CENTER_ALIGNMENT);

		// Οι επικεφαλίδες θα μπούν σε πάνελ στην κορυφή
		topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
		topPanel.add(title);
		topPanel.add(subtitle);

		// Δημιουργία κουμπιών
        homeworkBtn = new JButton("Εργασία", new ImageIcon("resources\\homework.png"));
        homeworkBtn.setVerticalTextPosition(AbstractButton.BOTTOM);
        homeworkBtn.setHorizontalTextPosition(AbstractButton.CENTER);
		homeworkBtn.setBackground(new Color(228,228,228));
		homeworkBtn.addActionListener(this);
		proodosBtn = new JButton("Πρόοδος", new ImageIcon("resources\\proodos.png"));
        proodosBtn.setVerticalTextPosition(AbstractButton.BOTTOM);
        proodosBtn.setHorizontalTextPosition(AbstractButton.CENTER);
		proodosBtn.setBackground(new Color(228,228,228));
		proodosBtn.addActionListener(this);
		examsBtn = new JButton("Τελική Εξέταση", new ImageIcon("resources\\exam.png"));
        examsBtn.setVerticalTextPosition(AbstractButton.BOTTOM);
        examsBtn.setHorizontalTextPosition(AbstractButton.CENTER);
		examsBtn.setBackground(new Color(228,228,228));
		examsBtn.addActionListener(this);
		finalGradesBtn = new JButton("Τελ. Βαθμολογία", new ImageIcon("resources\\results.png"));
        finalGradesBtn.setVerticalTextPosition(AbstractButton.BOTTOM);
        finalGradesBtn.setHorizontalTextPosition(AbstractButton.CENTER);
		finalGradesBtn.setBackground(new Color(201,220,242,255));
		finalGradesBtn.addActionListener(this);
		aboutBtn = new JButton("Σχετικά...", new ImageIcon("resources\\about.png"));
        aboutBtn.setVerticalTextPosition(AbstractButton.BOTTOM);
        aboutBtn.setHorizontalTextPosition(AbstractButton.CENTER);
		aboutBtn.setBackground(new Color(228,228,228));
		aboutBtn.addActionListener(this);
		
		// Πλαϊνή στήλη που θα τοποθετηθούν τα κουμπιά
		sidePanel = new JPanel();
		sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
		sidePanel.add(homeworkBtn);
		sidePanel.add(proodosBtn);
		sidePanel.add(examsBtn);
		sidePanel.add(finalGradesBtn);
		sidePanel.add(aboutBtn);

		// Σ' αυτό το πάνελ θα μπουν οι περιγραφές των μαθημάτων
		bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

		// Σ' αυτά τα πάνελ θα μπουν οι πληροφορίες του κάθε tab
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();
		panel1.setPreferredSize(new Dimension(width, rowHeight * SaekGrades.maxRows + (rowHeight * 2 + 10)));
		panel2.setPreferredSize(new Dimension(width, rowHeight * SaekGrades.maxRows + (rowHeight * 2 + 10)));
		panel3.setPreferredSize(new Dimension(width, rowHeight * SaekGrades.maxRows + (rowHeight * 2 + 10)));
		panel4.setPreferredSize(new Dimension(width, rowHeight * SaekGrades.maxRows + (rowHeight * 2 + 10)));
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
		panel4.setLayout(new BoxLayout(panel4, BoxLayout.Y_AXIS));
		
		// Δημιουργία JTabbedPane που θα κρατάει τα tabs 
		JTabbedPane tabPanel = new JTabbedPane();

		// Δημιουργία table	model (το Override δηλώνει τις στήλες σαν αριθμούς ώστε να έχουν σωστή ταξινόμηση)
		model = new DefaultTableModel() {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return Integer.class;
			}
		};
		// Κι ένα table model για να κρατάει τους μέσους όρους
		modelAvg = new DefaultTableModel();
		
		// Δημιουργία του πίνακα με απενεργοποιημένη την επεξεργασία
		table = new JTable(model) {
			public boolean isCellEditable(int row, int column) {                
					return false;               
			}
		};
		// Να μην επιλέγονται πολλές γραμμές του πίνακα
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(font);
		table.setRowHeight(rowHeight);
		// Προσθήκη listener για διπλό κλικ σε γραμμή του πίνακα
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				JTable table =(JTable) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
					// AMK που επιλέχθηκε 
					int AMKselected = (int)table.getValueAt(table.getSelectedRow(), 0);
                    // Δες αν έχει γίνει Τελική Εξέταση οπότε υπάρχει τελική βαθμολογία
                    boolean isGradeFinal = calcLessonAvg(SaekGrades.student, -1, 2) != 0;
					// Εμφάνισε αναλυτικές πληροφορίες σε νέο παράθυρο
					new AmkInfoDialog(AppWindow.this, SaekGrades.student, SaekGrades.STUDENTS, 
							SaekGrades.LESSONS, isGradeFinal, AMKselected, semesterSelected);
				}
			}
		});

		JTableHeader header = table.getTableHeader();
		header.setFont(font);
		header.setPreferredSize(new Dimension(width, rowHeight));
		header.setResizingAllowed(false);
		header.setReorderingAllowed(false);
		header.setBackground(Color.LIGHT_GRAY);
		header.setForeground(Color.blue);

		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setPreferredSize(new Dimension(width, rowHeight * SaekGrades.maxRows + (rowHeight)));
		scrollpane.setMaximumSize(new Dimension(width, rowHeight * SaekGrades.maxRows + (rowHeight)));
		scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

		// Δημιουργία του πίνακα Μέσου όρου με απενεργοποιημένη την επεξεργασία
		footer = new JTable(modelAvg) {
			public boolean isCellEditable(int row, int column) {                
					return false;               
			}
			// και δεξιά στοίχιση
			DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
			{ 
				renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
			}
			@Override
			public TableCellRenderer getCellRenderer (int arg0, int arg1) {
				return renderRight;
			}	
		};
		footer.setRowSelectionAllowed(false);
		footer.setFont(font);
		footer.setRowHeight(rowHeight);
		footer.setPreferredSize(new Dimension(width, rowHeight));
		footer.setMaximumSize(new Dimension(width, rowHeight));
					
		// Προεπιλεγμένη ταξινόμηση κατά την 1η στήλη (ΑΜΚ)
		sorter = new TableRowSorter<TableModel>(model);
		table.setRowSorter(sorter);
		sortKeys = new ArrayList<>();
		sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));

		// Πρόσθεσε 4 tabs στο JTabbedPane 
		tabPanel.setFont(font);
		tabPanel.addTab("Α' Εξάμηνο", panel1);
		tabPanel.addTab("Β' Εξάμηνο", panel2);
		tabPanel.addTab("Γ' Εξάμηνο", panel3);
		tabPanel.addTab("Δ' Εξάμηνο", panel4);
		// Ενέργειες που θα γίνουν όταν επιλέγεται ένα Tab
		tabPanel.addChangeListener((e) -> {
			JTabbedPane obj = (JTabbedPane) e.getSource();
			switch (obj.getSelectedIndex()) {
				case 0:
					semesterSelected = "A";
					updateTab();					
					panel1.add(scrollpane);
					panel1.add(footer);
					fillBottomPanel(0);					
					break;
				case 1:
					semesterSelected = "B";
					updateTab();					
					panel2.add(scrollpane);
					panel2.add(footer);
					fillBottomPanel(1);					
					break;
				case 2:
					semesterSelected = "C";
					updateTab();
					panel3.add(scrollpane);
					panel3.add(footer);
					fillBottomPanel(2);					
					break;
				case 3:
					semesterSelected = "D";
					updateTab();
					panel4.add(scrollpane);
					panel4.add(footer);
					fillBottomPanel(3);
					break;
			}
			// Δώσε ύψος στο container του πίνακα ανάλογα με τις σειρές που περιέχει
			scrollpane.setPreferredSize(new Dimension(width, (model.getRowCount() * rowHeight) + rowHeight));
		});

		tabPanel.setSelectedIndex(3);
		add(topPanel, BorderLayout.NORTH);
		add(sidePanel, BorderLayout.WEST);
		add(tabPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		pack();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	// Ενημερώνει τον πίνακα του επιλεγμένου Tab.
	// Επειδή οι κλάσεις είχαν γραφτεί σε C++ για ένα μόνο εξάμηνο,
	// η διαχείριση όλων των εξαμήνων κάνει αυτή τη συνάρτηση μεγάλη.
	// TODO να διορθώσω τις κλάσεις όταν βρω χρόνο.
	private void updateTab() {
		SaekGrades.readGrades(semesterSelected);
		model.setRowCount(0);
		modelAvg.setColumnIdentifiers(new String[SaekGrades.LESSONS + 2]);
		modelAvg.setRowCount(0);
		Object[] avg = new Object[SaekGrades.LESSONS + 2];
		avg[0] = "Μέσος Όρος";
        
        if (examSelected == 3) {    // Τελικός Βαθμός
            if (calcLessonAvg(SaekGrades.student, -1, 2) == 0) {
                subtitle.setForeground(Color.red);
                subtitle.setText("ΠΡΟΣΩΡΙΝΗ ΒΑΘΜΟΛΟΓΙΑ - ΔΕΝ ΕΓΙΝΕ ΤΕΛΙΚΗ ΕΞΕΤΑΣΗ - ΑΡΙΣΤΑ = 4.00");
            } else {
                subtitle.setForeground(Color.BLUE);
                subtitle.setText("ΤΕΛΙΚΗ ΒΑΘΜΟΛΟΓΙΑ");							
            }		            
        }
		switch (semesterSelected) {
			case "A":
				switch (examSelected) {
					case 0:
						model.setColumnIdentifiers(new String[] {"AMK", "M1", "M2", "M3", "M4", "M5", "M6", "ΒΑΘΜΟΣ ΕΡΓΑΣΙΑΣ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].homework, 
							SaekGrades.student[i].m[1].homework, SaekGrades.student[i].m[2].homework, SaekGrades.student[i].m[3].homework, 
							SaekGrades.student[i].m[4].homework, SaekGrades.student[i].m[5].homework, 
							new BigDecimal(SaekGrades.student[i].homeworkAvg).setScale(2, RoundingMode.HALF_UP)});
						}
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 0)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 0)).setScale(2, RoundingMode.HALF_UP);
						break;
					case 1:
						model.setColumnIdentifiers(new String[] {"AMK", "M1", "M2", "M3", "M4", "M5", "M6", "ΒΑΘΜΟΣ ΠΡΟΟΔΟΥ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].progress, 
							SaekGrades.student[i].m[1].progress, SaekGrades.student[i].m[2].progress, SaekGrades.student[i].m[3].progress, 
							SaekGrades.student[i].m[4].progress, SaekGrades.student[i].m[5].progress, 
							new BigDecimal(SaekGrades.student[i].progressAvg).setScale(2, RoundingMode.HALF_UP)});
						}
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 1)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 1)).setScale(2, RoundingMode.HALF_UP);
						break;
					case 2:
						model.setColumnIdentifiers(new String[] {"AMK", "M1", "M2", "M3", "M4", "M5", "M6", "ΒΑΘΜΟΣ ΕΞΕΤΑΣΗΣ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].exam, 
							SaekGrades.student[i].m[1].exam, SaekGrades.student[i].m[2].exam, SaekGrades.student[i].m[3].exam, 
							SaekGrades.student[i].m[4].exam, SaekGrades.student[i].m[5].exam, 
							new BigDecimal(SaekGrades.student[i].examAvg).setScale(2, RoundingMode.HALF_UP)});
						}
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 2)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 2)).setScale(2, RoundingMode.HALF_UP);
						break;
					case 3:				
						model.setColumnIdentifiers(new String[] {"AMK", "M1", "M2", "M3", "M4", "M5", "M6", "ΤΕΛΙΚΟΣ ΒΑΘΜΟΣ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].lessonGrade, 
							SaekGrades.student[i].m[1].lessonGrade, SaekGrades.student[i].m[2].lessonGrade, SaekGrades.student[i].m[3].lessonGrade, 
							SaekGrades.student[i].m[4].lessonGrade, SaekGrades.student[i].m[5].lessonGrade, 
							new BigDecimal(SaekGrades.student[i].grade).setScale(2, RoundingMode.HALF_UP)});
						}
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 3)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 3)).setScale(2, RoundingMode.HALF_UP);
						break;
				}
				break;
			case "B":
				switch (examSelected) {
					case 0:
						model.setColumnIdentifiers(new String[]{"AMK", "M1", "M2", "M3", "M4", "M5", "ΒΑΘΜΟΣ ΕΡΓΑΣΙΑΣ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].homework, 
							SaekGrades.student[i].m[1].homework, SaekGrades.student[i].m[2].homework, SaekGrades.student[i].m[3].homework, 
							SaekGrades.student[i].m[4].homework, 
							new BigDecimal(SaekGrades.student[i].homeworkAvg).setScale(2, RoundingMode.HALF_UP)});
						}				
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 0)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 0)).setScale(2, RoundingMode.HALF_UP);
						break;
					case 1:
						model.setColumnIdentifiers(new String[]{"AMK", "M1", "M2", "M3", "M4", "M5", "ΒΑΘΜΟΣ ΠΡΟΟΔΟΥ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].progress, 
							SaekGrades.student[i].m[1].progress, SaekGrades.student[i].m[2].progress, SaekGrades.student[i].m[3].progress, 
							SaekGrades.student[i].m[4].progress, 
							new BigDecimal(SaekGrades.student[i].progressAvg).setScale(2, RoundingMode.HALF_UP)});
						}				
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 1)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 1)).setScale(2, RoundingMode.HALF_UP);
						break;
					case 2:
						model.setColumnIdentifiers(new String[]{"AMK", "M1", "M2", "M3", "M4", "M5", "ΒΑΘΜΟΣ ΕΞΕΤΑΣΗΣ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].exam, 
							SaekGrades.student[i].m[1].exam, SaekGrades.student[i].m[2].exam, SaekGrades.student[i].m[3].exam, 
							SaekGrades.student[i].m[4].exam, 
							new BigDecimal(SaekGrades.student[i].examAvg).setScale(2, RoundingMode.HALF_UP)});
						}				
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 2)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 2)).setScale(2, RoundingMode.HALF_UP);
						break;
					case 3:			
						model.setColumnIdentifiers(new String[]{"AMK", "M1", "M2", "M3", "M4", "M5", "ΤΕΛΙΚΟΣ ΒΑΘΜΟΣ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].lessonGrade, 
							SaekGrades.student[i].m[1].lessonGrade, SaekGrades.student[i].m[2].lessonGrade, SaekGrades.student[i].m[3].lessonGrade, 
							SaekGrades.student[i].m[4].lessonGrade, 
							new BigDecimal(SaekGrades.student[i].grade).setScale(2, RoundingMode.HALF_UP)});
						}				
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 3)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 3)).setScale(2, RoundingMode.HALF_UP);
						break;
				}				
				break;
			case "C":
				switch (examSelected) {
					case 0:
						model.setColumnIdentifiers(new String[]{"AMK", "M1", "M2", "M3", "M4", "M5", "M6", "M7", "ΒΑΘΜΟΣ ΕΡΓΑΣΙΑΣ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].homework, 
							SaekGrades.student[i].m[1].homework, SaekGrades.student[i].m[2].homework, SaekGrades.student[i].m[3].homework, 
							SaekGrades.student[i].m[4].homework, SaekGrades.student[i].m[5].homework, SaekGrades.student[i].m[6].homework, 
							new BigDecimal(SaekGrades.student[i].homeworkAvg).setScale(2, RoundingMode.HALF_UP)});
						}				
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 0)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 0)).setScale(2, RoundingMode.HALF_UP);
						break;
					case 1:
						model.setColumnIdentifiers(new String[]{"AMK", "M1", "M2", "M3", "M4", "M5", "M6", "M7", "ΒΑΘΜΟΣ ΠΡΟΟΔΟΥ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].progress, 
							SaekGrades.student[i].m[1].progress, SaekGrades.student[i].m[2].progress, SaekGrades.student[i].m[3].progress, 
							SaekGrades.student[i].m[4].progress, SaekGrades.student[i].m[5].progress, SaekGrades.student[i].m[6].progress, 
							new BigDecimal(SaekGrades.student[i].progressAvg).setScale(2, RoundingMode.HALF_UP)});
						}				
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 1)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 1)).setScale(2, RoundingMode.HALF_UP);
						break;
					case 2:
						model.setColumnIdentifiers(new String[]{"AMK", "M1", "M2", "M3", "M4", "M5", "M6", "M7", "ΒΑΘΜΟΣ ΕΞΕΤΑΣΗΣ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].exam, 
							SaekGrades.student[i].m[1].exam, SaekGrades.student[i].m[2].exam, SaekGrades.student[i].m[3].exam, 
							SaekGrades.student[i].m[4].exam, SaekGrades.student[i].m[5].exam, SaekGrades.student[i].m[6].exam, 
							new BigDecimal(SaekGrades.student[i].examAvg).setScale(2, RoundingMode.HALF_UP)});
						}				
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 2)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 2)).setScale(2, RoundingMode.HALF_UP);
						break;
					case 3:					
						model.setColumnIdentifiers(new String[]{"AMK", "M1", "M2", "M3", "M4", "M5", "M6", "M7", "ΤΕΛΙΚΟΣ ΒΑΘΜΟΣ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].lessonGrade, 
							SaekGrades.student[i].m[1].lessonGrade, SaekGrades.student[i].m[2].lessonGrade, SaekGrades.student[i].m[3].lessonGrade, 
							SaekGrades.student[i].m[4].lessonGrade, SaekGrades.student[i].m[5].lessonGrade, SaekGrades.student[i].m[6].lessonGrade, 
							new BigDecimal(SaekGrades.student[i].grade).setScale(2, RoundingMode.HALF_UP)});
						}				
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 3)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 3)).setScale(2, RoundingMode.HALF_UP);
						break;
				}
				break;
			case "D":
				switch (examSelected) {
					case 0:
						model.setColumnIdentifiers(new String[]{"AMK", "M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "ΒΑΘΜΟΣ ΕΡΓΑΣΙΑΣ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].homework, 
							SaekGrades.student[i].m[1].homework, SaekGrades.student[i].m[2].homework, SaekGrades.student[i].m[3].homework, 
							SaekGrades.student[i].m[4].homework, SaekGrades.student[i].m[5].homework, SaekGrades.student[i].m[6].homework, 
							SaekGrades.student[i].m[7].homework, SaekGrades.student[i].m[8].homework, 
							new BigDecimal(SaekGrades.student[i].homeworkAvg).setScale(2, RoundingMode.HALF_UP)});
						}
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 0)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 0)).setScale(2, RoundingMode.HALF_UP);
						break;
					case 1:
						model.setColumnIdentifiers(new String[]{"AMK", "M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "ΒΑΘΜΟΣ ΠΡΟΟΔΟΥ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].progress, 
							SaekGrades.student[i].m[1].progress, SaekGrades.student[i].m[2].progress, SaekGrades.student[i].m[3].progress, 
							SaekGrades.student[i].m[4].progress, SaekGrades.student[i].m[5].progress, SaekGrades.student[i].m[6].progress, 
							SaekGrades.student[i].m[7].progress, SaekGrades.student[i].m[8].progress, 
							new BigDecimal(SaekGrades.student[i].progressAvg).setScale(2, RoundingMode.HALF_UP)});
						}
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 1)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 1)).setScale(2, RoundingMode.HALF_UP);						
						break;
					case 2:
						model.setColumnIdentifiers(new String[]{"AMK", "M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "ΒΑΘΜΟΣ ΕΞΕΤΑΣΗΣ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].exam, 
							SaekGrades.student[i].m[1].exam, SaekGrades.student[i].m[2].exam, SaekGrades.student[i].m[3].exam, 
							SaekGrades.student[i].m[4].exam, SaekGrades.student[i].m[5].exam, SaekGrades.student[i].m[6].exam, 
							SaekGrades.student[i].m[7].exam, SaekGrades.student[i].m[8].exam, 
							new BigDecimal(SaekGrades.student[i].examAvg).setScale(2, RoundingMode.HALF_UP)});
						}
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 2)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 2)).setScale(2, RoundingMode.HALF_UP);
						break;
					case 3:
						model.setColumnIdentifiers(new String[]{"AMK", "M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "ΤΕΛΙΚΟΣ ΒΑΘΜΟΣ"});
						for (int i = 0; i < SaekGrades.STUDENTS; i++) {
							model.addRow(new Object[]{SaekGrades.student[i].amk, SaekGrades.student[i].m[0].lessonGrade, 
							SaekGrades.student[i].m[1].lessonGrade, SaekGrades.student[i].m[2].lessonGrade, SaekGrades.student[i].m[3].lessonGrade, 
							SaekGrades.student[i].m[4].lessonGrade, SaekGrades.student[i].m[5].lessonGrade, SaekGrades.student[i].m[6].lessonGrade, 
							SaekGrades.student[i].m[7].lessonGrade, SaekGrades.student[i].m[8].lessonGrade, 
							new BigDecimal(SaekGrades.student[i].grade).setScale(2, RoundingMode.HALF_UP)});
						}
						// Footer
						for (int i = 1; i <= SaekGrades.LESSONS; i++) {
							avg[i] = new BigDecimal(calcLessonAvg(SaekGrades.student, i - 1, 3)).setScale(2, RoundingMode.HALF_UP);
						}
						avg[SaekGrades.LESSONS + 1] = new BigDecimal(calcLessonAvg(SaekGrades.student, -1, 3)).setScale(2, RoundingMode.HALF_UP);
						break;
				}
				break;
		}
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(SaekGrades.LESSONS + 1).setPreferredWidth(180);
		footer.getColumnModel().getColumn(0).setPreferredWidth(100);
		footer.getColumnModel().getColumn(SaekGrades.LESSONS + 1).setPreferredWidth(180);
		modelAvg.addRow(avg);
		sorter.setSortKeys(sortKeys);
		sorter.sort();			}
	
	// Υπολογίζει τον Μ.Ο. βαθμού που έγραψε όλη η τάξη σε ένα μάθημα
	// id = Αριθμός μαθήματος, με id = -1 υπολογίζει τον γενικό Μ.Ο.
	// lesson: 0 = Homework, 1 = Progress, 2 = Exam, 3 = Final Grade
	public static double calcLessonAvg(StudentRec student[], int id, int lesson) {
		double average = 0;
		switch (lesson) {
			case 0:
				for (int i = 0; i < SaekGrades.STUDENTS; i++) {
					if (id >= 0)
						average += student[i].m[id].homework;
					else
						average += student[i].homeworkAvg;
				}
				average /= SaekGrades.STUDENTS;
				break;
			case 1:
				for (int i = 0; i < SaekGrades.STUDENTS; i++) {
					if (id >= 0)
						average += student[i].m[id].progress;
					else
						average += student[i].progressAvg;
				}
				average /= SaekGrades.STUDENTS;
				break;
			case 2:
				for (int i = 0; i < SaekGrades.STUDENTS; i++) {
					if (id >= 0)
						average += student[i].m[id].exam;
					else
						average += student[i].examAvg;
				}
				average /= SaekGrades.STUDENTS;
				break;
			case 3:
				for (int i = 0; i < SaekGrades.STUDENTS; i++) {
					if (id >= 0)
						average += student[i].m[id].lessonGrade;
					else
						average += student[i].grade;
				}
				average /= SaekGrades.STUDENTS;
				break;
		}
		return average;
	}

	// Εμφανίζει τα μαθήματα του εξαμήνου στο κάτω μέρος του παράθυρου
	private void fillBottomPanel(int semester) {
		bottomPanel.removeAll();
		// Δημιουργία HorizontalBox πλάτους όσο to SidePanel με τα buttons
		// Έτσι το υποσέλιδο με τις επεξηγήσεις μαθημάτων εμφανίζεαι κάτω από
		// τη βαθμολογία και όχι εντελώς αριστερά
		Box horizontalBox = Box.createHorizontalBox();
		horizontalBox.add(Box.createGlue());
		horizontalBox.setPreferredSize(new Dimension(128,rowHeightSmall));
		horizontalBox.setMinimumSize(new Dimension(128,rowHeightSmall));
		horizontalBox.setMaximumSize(new Dimension(128,rowHeightSmall));
		
		// Δημιουργία εσωτερικού πάνελ που θα περιέχει τις επεξηγήσεις μαθημάτων
		JPanel innerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		innerPanel.setPreferredSize(new Dimension(width, rowHeightSmall * 7));
		innerPanel.setMinimumSize(new Dimension(width, rowHeightSmall * 7));
		
		// Κι όλα αυτά μπαίνουν στο υποσέλιδο πάνελ
		bottomPanel.add(horizontalBox);
		bottomPanel.add(innerPanel);
		
		// Πρόσθεσε τα μαθήματα του εξαμήνου στο εσωτερικό πάνελ
		JLabel[] lessons = new JLabel[SaekGrades.LESSONS];
		for (int i = 0; i < SaekGrades.LESSONS; i++) {
			lessons[i] = new JLabel(lessonDescription.lessonNames[semester][i]);
			lessons[i].setFont(fontSmall);
			lessons[i].setPreferredSize(new Dimension(430, rowHeightSmall));
			innerPanel.add(lessons[i]);
		}
	}

	@Override
	// Ενέργειες που θα γίνουν όταν πατιούνται τα κουμπιά
	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton)e.getSource();
		if (button.getText().equals("Σχετικά...")) {
			// Εμφάνισε νέο παράθυρο με βοηθητικές πληροφορίες
			new AboutDialog(AppWindow.this);
			return;
		}
		// j
		homeworkBtn.setBackground(new Color(228,228,228));
		proodosBtn.setBackground(new Color(228,228,228));
		examsBtn.setBackground(new Color(228,228,228));
		finalGradesBtn.setBackground(new Color(228,228,228));
		button.setBackground(new Color(201,220,242,255));
		switch (button.getText()) {
			case "Εργασία":
				subtitle.setForeground(Color.BLUE);
				subtitle.setText("ΒΑΘΜΟΣ ΕΡΓΑΣΙΑΣ");
				examSelected = 0;
				break;
			case "Πρόοδος":
				subtitle.setForeground(Color.BLUE);
				subtitle.setText("ΒΑΘΜΟΣ ΠΡΟΟΔΟΥ");
				examSelected = 1;
				break;
			case "Τελική Εξέταση":
				subtitle.setForeground(Color.BLUE);
				subtitle.setText("ΒΑΘΜΟΣ ΤΕΛΙΚΗΣ ΕΞΕΤΑΣΗΣ");
				examSelected = 2;
				break;
			case "Τελ. Βαθμολογία":
				// To subtitle καθορίζεται στο updateTab() γιατί μπορεί να μην υπάρχει Τελ.Εξέταση
				examSelected = 3;
				break;
		}
		updateTab();
	}
	
}

// ** ΑΡΧΗ ΠΡΟΓΡΑΜΜΑΤΟΣ **
public class SaekGrades {
	public static int LESSONS;   // Πλήθος μαθημάτων
	public static int STUDENTS;    // Πλήθος καταρτιζόμενων
	public static StudentRec[] student;	// Object με τις καρτέλες των σπουδαστών ενός εξαμήνου
	public static GradeData gradeData;	// Object που διαβάζει τις βαθμολογίες από το δίσκο
	public static int maxRows;	// Μέγιστος αριθμός γραμμών που θα έχουν τα Tabs
	
	public static void main(String[] args) {

		gradeData = new GradeData();
		maxRows = gradeData.maxRows;
		new AppWindow();
	}

	// Διαβάζει τις βαθμολογίες ενός εξαμήνου και τις τοποθετεί στο Object Student
	public static void readGrades(String semester) {
		// Διάβασε τις βαθμολογίες του εξαμήνου από το δίσκο
		int[][] arr = gradeData.getGrades(semester);
		switch (semester) {
			case "A":
				LESSONS = 6;
				break;
			case "B":
				LESSONS = 5;
				break;
			case "C":
				LESSONS = 7;
				break;
			case "D":
				LESSONS = 9;
				break;
		}
		STUDENTS = arr.length;
		student = null;
		// ** Εδώ βάζουμε τους βαθμούς των σπουδαστών **
		// Οι βαθμολογίες δίνονται με τη σειρά
		// {AMK, M1.Εργασία, M2.Εργασία, ..., Μ1.Πρόοδος, Μ2.Πρόοδος, ..., Μ1.Τελική Εξέταση, Μ2.Τελική Εξέταση, ...}
		// Βάζουμε ΥΠΟΧΡΕΩΤΙΚΑ βαθμούς 9 μαθημάτων που έχει το 4ο Εξάμηνο
		// (Αν τα μαθήματα του εξαμήνου είναι λιγότερα ο πίνακας arr έχει 0 σαν βαθμό)
		student = new StudentRec[STUDENTS];
		for (int i = 0; i < STUDENTS; i++) {
			student[i] = new StudentRec(arr[i][0], 
					arr[i][1],  arr[i][2],  arr[i][3],  arr[i][4],  arr[i][5],  arr[i][6],  arr[i][7],  arr[i][8],  arr[i][9], 
					arr[i][10], arr[i][11], arr[i][12], arr[i][13], arr[i][14], arr[i][15], arr[i][16], arr[i][17], arr[i][18], 
					arr[i][19], arr[i][20], arr[i][21], arr[i][22], arr[i][23], arr[i][24], arr[i][25], arr[i][26], arr[i][27]);
		}			
	}

}
