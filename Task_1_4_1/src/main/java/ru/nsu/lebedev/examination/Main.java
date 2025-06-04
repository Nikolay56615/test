package ru.nsu.lebedev.examination;

/**
 * Main class.
 */
public class Main {
    /**
     * Main function.
     *
     * @param args for specified parameters.
     */
    public static void main(String[] args) {
        Student student = new Student("Floria", "Fernandina", "Test");
        student.getCreditBook().addGrade(5, AcademicDiscipline.PHYSICAL_EDUCATION);
        student.getCreditBook().addGrade(4, AcademicDiscipline.IMPERATIVE_PROGRAMMING);
        student.getCreditBook().addGrade(5, AcademicDiscipline.OBJECT_ORIENTED_PROGRAMMING);
        student.getCreditBook().addGrade(5, AcademicDiscipline.DIFFERENTIAL_EQUATIONS);
        student.getCreditBook().addGrade(5, AcademicDiscipline.ARTIFICIAL_INTELLIGENCE);
        System.out.println("Average grade: " + student.getCreditBook().averageGrade());
        System.out.println("Eligible for increased scholarship: "
                +
                student.getCreditBook().canObtainIncreasedScholarship());
        System.out.println("Eligible for red diploma: "
                + student.getCreditBook().canObtainRedDiploma());
        student.finishSemester();
        student.getCreditBook().addGrade(5, AcademicDiscipline.FINAL_THESIS);
        student.getCreditBook().addGrade(4, AcademicDiscipline.DECLARATIVE_PROGRAMMING);
        System.out.println("Average grade: " + student.getCreditBook().averageGrade());
        System.out.println("Eligible for increased scholarship: "
                +
                student.getCreditBook().canObtainIncreasedScholarship());
        System.out.println("Eligible for red diploma: "
                + student.getCreditBook().canObtainRedDiploma());
        student.finishSemester();
        student.getCreditBook().addGrade(5, AcademicDiscipline.PAK);
        student.getCreditBook().addGrade(5, AcademicDiscipline.COMPUTATION_MODELS);
        System.out.println("Average grade: " + student.getCreditBook().averageGrade());
        System.out.println("Eligible for increased scholarship: "
                +
                student.getCreditBook().canObtainIncreasedScholarship());
        System.out.println("Eligible for red diploma: "
                + student.getCreditBook().canObtainRedDiploma());
        System.out.println("Grades: " + student.getCreditBook().getGrades());
        student.finishSemester();
        System.out.println("Eligible for increased scholarship: "
                +
                student.getCreditBook().canObtainIncreasedScholarship());
        System.out.println("Eligible for red diploma: "
                + student.getCreditBook().canObtainRedDiploma());
    }
}