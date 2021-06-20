package cz.cvut.fit.baklaal1.model.data.helper;

public class Grades {
    public static final int DEFAULT = 0;
    public static final int A = 1;
    public static final int B = 2;
    public static final int C = 3;
    public static final int D = 4;
    public static final int E = 5;
    public static final int F = 6;

    /**
     * Returns true if value can be considered a grade value or a placeholder for it (Grades.DEFAULT), otherwise false
     * @param value
     * @return
     */
    public static boolean isGrade(int value) {
        return value == DEFAULT || (value >= A && value <= F);
    }

    /**
     * Returns true if value can be derived from grades by any arithmetical operations, otherwise false
     * @param value
     * @return
     */
    public static boolean isGradeDerived(float value) {
        return value == DEFAULT || (value >= A && value <= F);
    }

    /**
     * Returns true if grade value is actually a grade and can be considered final: is not Grades.DEFAULT, otherwise false
     * @param grade
     * @return
     */
    public static boolean isFinalGrade(int grade) {
        return isGrade(grade) && grade != DEFAULT;
    }
}
