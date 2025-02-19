/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package camber.saekgrades;

/**
 * 
 * @author Γιώργος Καμπερογιάννης
 */
import java.io.File;  // Χειρίζεται αρχεία
import java.io.FileNotFoundException;  // Χειρίζεται τα μηνύματα λάθους αρχείου
import java.util.Scanner; // Διαβάζει αρχεία κειμένου
import java.util.*;
import static javax.swing.JOptionPane.showMessageDialog;

// Διαβάζει από το δίσκο τις βαθμολογίες των σπουδαστών και τις αποθηκεύει σε πίνακες
class GradeData {
	// O φάκελος που βρίσκονται οι βαθμοί
	private final String path = "grades\\";
	// Κρατούν τις βαθμολογίες του κάθε εξαμήνου
	private int[][] gradesA, gradesB, gradesC, gradesD;
	public int maxRows;
	
	GradeData () {
		gradesA =  fileRead("A");
		gradesB =  fileRead("B");
		gradesC =  fileRead("C");
		gradesD =  fileRead("D");
		maxRows = Math.max(Math.max(gradesA.length, gradesB.length), Math.max(gradesC.length, gradesD.length));
	}
	
	public int[][] getGrades(String semester) {
		switch (semester) {
			case "A":
				return gradesA;
			case "B":
				return gradesB;
			case "C":
				return gradesC;
			case "D":
				return gradesD;
			default:
				System.out.println("Error! Invalid semester.");
				return new int[0][0];
		}
	}
	
	// Διαβάζει από το δίσκο τις βαθμολογίες ενός εξαμήνου
	// και επιστρέφει πίνακα μ' αυτές τις βαθμολογίες
	private int[][] fileRead(String semester) {
		// Δυναμικός πίνακας που κρατάει το σύνολο βαθμολογίας ενός εξαμήνου
		// (ΑΜΚ-Εργασία-Πρόοδος-Τελ.Εξέταση)
		List<List<Integer>> semesterGrades = new ArrayList<List<Integer>>();
		
		// Δυναμικός πίνακας ακεραίων 2 διαστάσεων
		List<List<Integer>> twoDim = new ArrayList<List<Integer>>();
		
		// exam: 1 = ΕΡΓΑΣΙΑ, 2 = ΠΡΟΟΔΟΣ, 3 = ΤΕΛΙΚΗ ΕΞΕΤΑΣΗ
		for (int exam = 1; exam <= 3; exam++) {
			// Σύνθεση του ονόματος αρχείου (πχ "grades\\A1.txt")
			String fileLocation = path + semester + exam + ".txt";
			try {
				File myFile = new File(fileLocation);
				Scanner myReader = new Scanner(myFile);
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();
					String[] str = data.split(" ");
					// Add row
					List<Integer> columns = new ArrayList<Integer>();
					twoDim.add(columns);
					// Add column
					for (int i = 0; i < str.length; i++) {
						columns.add(stringToInt(str[i]));
					}
				}
				myReader.close();
				// Αν τα μαθήματα είναι λιγότερα από 9 πρόσθεσε μηδενικά στο τέλος
				for (int i = 0; i < twoDim.size(); i++) {
					// 9 μαθήματα + Α/Α + ΑΜΚ = 11
					for (int j = twoDim.get(i).size(); j < 11; j++) {
						twoDim.get(i).add(0);
					}
				}
			} catch (FileNotFoundException e) {
				//System.out.println("Παρουσιάστηκε σφάλμα.");
				//e.printStackTrace();
				// Aν λείπει η Πρόοδος (2) ή Τελική Εξέταση (3) πρόσθεσε σαν βαθμούς το 0
				// αλλιώς αν λείπει η Εργασία (1) σημαίνει ότι δεν υπάρχουν καθόλου
				// βαθμοί εξαμήνου και επέστρεψε κενό πίνακα
				if (exam > 1) {
					for (int i = 0; i < 11; i++) {
						for (List<Integer> item: semesterGrades) {
							item.add(0);						
						}						
					}
				} else {
					// Αν δεν υπάρχουν βαθμοί εξαμήνου, επιστρέφουμε ένα άδειο πίνακα 
					// (το 1 στο πρώτο μάθημα προόδου χρειάζεται αλλιώς θα πάρουμε null exception)
					return new int[][] {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
				}
			}	
			// Add to main table
			for (int i = 0; i < twoDim.size(); i++) {
				if (exam == 1) {
					semesterGrades.add(twoDim.get(i));
				} else {
					for(int item: twoDim.get(i)) {
						semesterGrades.get(i).add(item);
					}
				}
			}
			twoDim.clear();
		}
		try {
		// Αφαίρεσε τις στήλες 0, 11, 12, 22, 23 που κρατούν Α/Α και ΑΜΚ
		// Θέλουμε το ΑΜΚ μόνο μια φορά στην αρχή και κανένα Α/Α
		int[] removeCol = {23, 22, 12, 11, 0};

			for (int i = 0; i < removeCol.length; i++) {
				for (int j = 0; j < semesterGrades.size(); j++) {
					semesterGrades.get(j).remove(removeCol[i]);
				}
			}			
		} 
		catch (Exception e) {
			showMessageDialog(null, "Σφάλμα στη δομή ενός αρχείου "+ semester + "?.txt");			
		}

		// Μετέτρεψε τον δυναμικό πίνακα semesterGrades σε κανονικό 2D int
		int row = semesterGrades.size();
		int col = semesterGrades.get(0).size();
		int[][] ar = new int[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				ar[i][j] = semesterGrades.get(i).get(j);
			}
		}
		return ar;
	}
	
	// Μετατρέπει ένα string σε ακέραιο και τον επιστρέφει
	// Αν δεν είναι δυνατή η μετατροπή (πχ Α/Φ) επιστρέφει 1 (το χαμηλότερο βαθμό εξέτασης)
	private int stringToInt(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			return 1;
		}
	}	
}
