package ru.nsu.lebedev.examination;

/**
 * Academic discipline enumeration.
 * Represents the various subjects or courses in the curriculum.
 */
public enum AcademicDiscipline {
    PHYSICAL_EDUCATION("Physical Education", DisciplineType.CREDIT),
    IMPERATIVE_PROGRAMMING("Imperative Programming", DisciplineType.EXAM),
    DECLARATIVE_PROGRAMMING("Declarative Programming", DisciplineType.DIFFERENTIATED),
    DIFFERENTIAL_EQUATIONS("Differential Equations", DisciplineType.EXAM),
    PAK("Team Project", DisciplineType.EXAM),
    ARTIFICIAL_INTELLIGENCE("Introduction to Artificial Intelligence", DisciplineType.EXAM),
    OBJECT_ORIENTED_PROGRAMMING("Object-Oriented Programming", DisciplineType.EXAM),
    OPERATING_SYSTEMS("Operating Systems", DisciplineType.EXAM),
    COMPUTATION_MODELS("Computation Models", DisciplineType.EXAM),
    FINAL_THESIS("Final Thesis", DisciplineType.DIFFERENTIATED);

    private final String displayName;
    private final DisciplineType type;

    /**
     * Constructs an AcademicDiscipline.
     *
     * @param displayName name of the discipline
     */
    AcademicDiscipline(String displayName, DisciplineType type) {
        this.displayName = displayName;
        this.type = type;
    }

    /**
     * Retrieves name of the discipline.
     *
     * @return the display name of the discipline
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the type of this academic discipline.
     *
     * @return the type of the discipline, which can be EXAM, DIFFERENTIATED, or CREDIT.
     */
    public DisciplineType getType() {
        return type;
    }

    /**
     * Provides a string representation of the discipline.
     *
     * @return the display name of the discipline
     */
    @Override
    public String toString() {
        return displayName;
    }
}