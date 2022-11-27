import java.util.Random;

public class RandomVectorGenerator {
    private static double generateNum(double min, double max) {
        Random random = new Random();

        return random.doubles(min, max)
                .findFirst()
                .getAsDouble();
    }

    public static double[] generateVector(int vectorSize) {
        double[] vector = new double[vectorSize];

        for (int index = 0; index < vectorSize; ++index) {
            vector[index] = generateNum(0, 1);
        }

        return vector;
    }
}
