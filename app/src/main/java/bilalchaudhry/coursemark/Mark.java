package bilalchaudhry.coursemark;


/**
 * Mark information
 */
public class Mark {

    private int numerator;
    private int denominator;
    private int weight;
    private String name;

    public Mark(String name, int numerator, int denominator, int weight) {
        this.name = name;
        this.numerator = numerator;
        this.denominator = denominator;
        this.weight = weight;
    }

    public int getNumerator() {
        return this.numerator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public int getDenominator() {
        return this.denominator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
