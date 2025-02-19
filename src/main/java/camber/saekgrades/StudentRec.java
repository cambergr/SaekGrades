/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package camber.saekgrades;

/**
 * 
 * @author Γιώργος Καμπερογιάννης
 */

// Κλάση σπουδαστή. Εμπεριέχει ένα πίνακα κλάσης μαθήματος
// Εισάγουμε σαν δεδομένα:
// amk  = Αριθμός Μητρώου Καταρτιζόμενου
// m[9] = Πίνακας κλάσης μαθήματος 9 στοιχείων (μέγιστος αριθμός στο Δ εξάμηνο)
//     .homework = Βαθμός εργασίας
//     .progress = Βαθμός προόδου
//     .exam     = Βαθμός τελικής εξέτασης
// H κλάση υπολογίζει και μας δίνει σαν σταθερά:
// grade  = Τελικός βαθμός σπουδαστή
// Χρησιμοποιούμε την μεταβλητή
// index = Σαν ευρετήριο για να ταξινομήσουμε την κλάση βάσει βαθμών
class StudentRec {
    public final int amk;
    public Lesson[] m = new Lesson[9];    // Μέγιστος αριθμός μαθημάτων για όλα τα 6μηνα είναι 9
    public final double grade;
    private int index = 0;
	public final double homeworkAvg, progressAvg, examAvg;
	
    StudentRec(int amk, int m1h, int m2h, int m3h, int m4h, int m5h, int m6h, int m7h, int m8h, int m9h,
               int m1p, int m2p, int m3p, int m4p, int m5p, int m6p, int m7p, int m8p, int m9p,
               int m1e, int m2e, int m3e, int m4e, int m5e, int m6e, int m7e, int m8e, int m9e) {
		this.amk = amk;
		m[0] = new Lesson(m1h, m1p, m1e);
		m[1] = new Lesson(m2h, m2p, m2e);
		m[2] = new Lesson(m3h, m3p, m3e);
		m[3] = new Lesson(m4h, m4p, m4e);
		m[4] = new Lesson(m5h, m5p, m5e);
		m[5] = new Lesson(m6h, m6p, m6e);
		m[6] = new Lesson(m7h, m7p, m7e);
		m[7] = new Lesson(m8h, m8p, m8e);
		m[8] = new Lesson(m9h, m9p, m9e);
		this.grade = calcAverage();
		this.homeworkAvg = calcHomeworkAvg();
		this.progressAvg = calcProgressAvg();
		this.examAvg = calcExamAvg();
	}
		
    double calcAverage()
    {
        double average = 0;
        boolean passed = true;
        int numLessons = 0;
        // Δες πόσα μαθήματα υπάρχουν. Μόλις συναντήσεις 0 στον βαθμό προόδου
        // σημαίνει ότι έχουν τελειώσει τα μαθήματα εξαμήνου αλλιώς σταμάτα στο μέγιστο 9
        while (numLessons < 9 && m[numLessons].progress > 0) {
            numLessons++;
        }
        for (int i = 0; i < numLessons; i++) {
            // Εάν έχουν βγει βαθμοί τελικής εξέτασης και
            // εάν έχει κάτω από 5 έστω σε 1 μάθημα τότε έχει κοπεί
            // και δεν μπορεί να βγει τελικός βαθμός
            if (m[i].exam > 0 && m[i].lessonGrade < 5) {
                passed = false;
            }
            average += m[i].lessonGrade;
        }
        average /= numLessons;
        // Αν κόπηκε σε κάποιο μάθημα μηδένισε τον τελικό βαθμό
        if (!passed)
            average = 0;
        return average;
    }
	
	double calcHomeworkAvg() {
        double average = 0;		
        int numLessons = 0;
        // Δες πόσα μαθήματα υπάρχουν. Μόλις συναντήσεις 0 στον βαθμό προόδου
        // σημαίνει ότι έχουν τελειώσει τα μαθήματα εξαμήνου αλλιώς σταμάτα στο μέγιστο 9
        while (numLessons < 9 && m[numLessons].progress > 0) {
            numLessons++;
        }
        for (int i = 0; i < numLessons; i++) {
            average += m[i].homework;
        }
        average /= numLessons;		
		return average;
	}
	
	double calcProgressAvg() {
        double average = 0;		
        int numLessons = 0;
        // Δες πόσα μαθήματα υπάρχουν. Μόλις συναντήσεις 0 στον βαθμό προόδου
        // σημαίνει ότι έχουν τελειώσει τα μαθήματα εξαμήνου αλλιώς σταμάτα στο μέγιστο 9
        while (numLessons < 9 && m[numLessons].progress > 0) {
            numLessons++;
        }
        for (int i = 0; i < numLessons; i++) {
            average += m[i].progress;
        }
        average /= numLessons;		
		return average;		
	}
	
	double calcExamAvg() {
        double average = 0;		
        int numLessons = 0;
        // Δες πόσα μαθήματα υπάρχουν. Μόλις συναντήσεις 0 στον βαθμό προόδου
        // σημαίνει ότι έχουν τελειώσει τα μαθήματα εξαμήνου αλλιώς σταμάτα στο μέγιστο 9
        while (numLessons < 9 && m[numLessons].progress > 0) {
            numLessons++;
        }
        for (int i = 0; i < numLessons; i++) {
            average += m[i].exam;
        }
        average /= numLessons;		
		return average;		
	}

};
