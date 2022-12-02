import java.util.Random;

public class RandomGenerator {
    public static double generateNum(double min, double max) {
        Random random = new Random();

        return random.doubles(min, max)
                .findFirst()
                .getAsDouble();
    }

    public static int generateNum(int min, int max) {
        Random random = new Random();

        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }

    public static double[] generateVector(int vectorSize, double min, double max) {
        double[] vector = new double[vectorSize];

        for (int index = 0; index < vectorSize; ++index) {
            vector[index] = generateNum(min, max);
        }

        return vector;
    }
}
