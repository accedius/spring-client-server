package cz.cvut.fit.baklaal1.model.data.helper;

public class Grades {
    public static final int DEFAULT = 0;
    public static final int A = 1;
    public static final int B = 2;
    public static final int C = 3;
    public static final int D = 4;
    public static final int E = 5;
    public static final int F = 6;

    public static boolean isGrade(float value) {
        return value == DEFAULT || (value >= A && value <= F);
    }
}
