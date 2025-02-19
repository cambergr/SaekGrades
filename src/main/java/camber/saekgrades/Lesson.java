/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package camber.saekgrades;

/**
 * 
 * @author Γιώργος Καμπερογιάννης
 */

// Κλάση μαθήματος. Εισάγουμε σαν δεδομένα:
// homework = Βαθμός εργασίας
// progress = Βαθμός προόδου
// exam     = Βαθμός τελικής εξέτασης
// H κλάση υπολογίζει και μας δίνει σαν σταθερές:
// lessonGrade  = Τελικός βαθμός μαθήματος
// passingGrade = Τι πρέπει να γράψουμε στις εξετάσεις για να περάσουμε
// maxGrade     = Ποιός είναι ο μέγιστος βαθμός αν γράψουμε 10 στις εξετάσεις
class Lesson {
    private double prgContribution, hwContribution, exmContribution;
    public final int progress, homework, exam, lessonGrade, passingGrade, maxGrade;

    // Constructor
    Lesson(int homework, int progress, int exam) {
		this.homework = homework;
		this.progress = progress;
		this.exam = exam;
		lessonGrade = calcLessonGrade();
		passingGrade = calcPassingGrade();
		maxGrade = calcMaxGrade();
		
        if (progress < 0 || progress > 10 || homework < 0 || homework > 10 || exam < 0 || exam > 10)
            System.out.println("Σφάλμα αρχικοποίησης! Οι βαθμοί πρέπει να είναι από 0 έως 10");
    }

	// Υπολογίζει την συνεισφορά της κάθε εξέτασης στον τελικό βαθμό
    // Εργασία: 10%, Πρόοδος: 30%, Τελική Εξέταση: 60%
    int calcLessonGrade() {
        hwContribution = homework * 0.1;
        prgContribution = progress * 0.3;
        exmContribution = exam * 0.6;
        return (int)Math.round(prgContribution + hwContribution + exmContribution);
    }

    // Υπολογίζει τον απαιτούμενο βαθμό στην τελική εξέταση για να περάσει το μάθημα
    int calcPassingGrade() {
        double requiredExam = (5 - hwContribution - prgContribution) / 0.6;
        return (int)Math.floor(requiredExam);
    }

    // Υπολογίζει τον μέγιστο βαθμό μαθήματος αν στην τελική εξέταση γράψει 10
    int calcMaxGrade() {
        return (int)Math.round(prgContribution + hwContribution + 6);  // 10 * 0.6
    }

};
