import java.util.Arrays;

public class KohonenMap {
    private final double[][] weights;

    private double learnRate;

    public KohonenMap(int clusterNum, int inputNum, double learnRate) {
        this.learnRate = learnRate;
        weights = new double[clusterNum][inputNum];
        fillWeights();
    }

    public int classify(double[] vector) {
        double minDistance = Double.MAX_VALUE;

        int vectorClassNum = 0;

        for (int index = 0; index < weights.length; ++index) {
            double distance = getVectorsDistance(vector, weights[index]);

            if (distance < minDistance) {
                minDistance = distance;
                vectorClassNum = index;
            }
        }

        return vectorClassNum;
    }


    public void learn(double[][] dataSet) {
        double networkError;

        do {
            networkError = 0;

            for (int index1 = 0; index1 < dataSet.length; ++index1) {
                int neuronWinnerIndex = 0;
                double minDistance = Double.MAX_VALUE;

                for (int index2 = 0; index2 < weights.length; ++index2) {
                    double distance = getVectorsDistance(dataSet[index1], weights[index2]);

                    if (distance < minDistance) {
                        minDistance = distance;
                        neuronWinnerIndex = index2;
                    }
                }
                correctNeuronWeights(neuronWinnerIndex, dataSet[index1], learnRate);
                networkError += getVectorsDistance(dataSet[index1], weights[neuronWinnerIndex]);
            }
            learnRate -= 0.01; networkError /= dataSet.length;
        } while (networkError >= 0.9);
    }

    private void correctNeuronWeights(int neuronNum, double[] dataVec, double learnRate) {
        if (neuronNum >= weights.length || neuronNum < 0) throw new RuntimeException("Incorrect neuron index");

        if (weights[neuronNum].length != dataVec.length) throw new RuntimeException("Different vectors size");

        for (int index = 0; index < weights[neuronNum].length; ++index) {
            weights[neuronNum][index] += learnRate * (dataVec[index] - weights[neuronNum][index]);
        }
    }

    private double getVectorsDistance(double[] vec1, double[] vec2) {
        if (vec1.length != vec2.length) throw new RuntimeException("Different vectors size");

        double distance = 0;

        for (int index = 0; index < vec1.length; ++index) {
            distance += Math.pow(vec1[index] - vec2[index], 2);
        }

        return Math.sqrt(distance);
    }

    private void fillWeights() {
        for (int index = 0; index < weights.length; ++index) {
            weights[index] = RandomVectorGenerator.generateVector(weights[index].length);
        }
    }

    public static void main(String[] args) {
        int dataSetSize = 10;

        double[][] dataSet = new double[dataSetSize][];

        for (int index = 0; index < dataSetSize; ++index) {
            dataSet[index] = RandomVectorGenerator.generateVector(dataSetSize);
        }

        KohonenMap kohonenMap = new KohonenMap(4, dataSetSize, 0.3);
        System.out.println("Source network weights:");

        for (int index = 0; index < kohonenMap.weights.length; ++index) {
            printVector(kohonenMap.weights[index]);
            System.out.println();
        }

        kohonenMap.learn(dataSet);

        System.out.println("Network weights after learning:");

        for (int index = 0; index < kohonenMap.weights.length; ++index) {
            printVector(kohonenMap.weights[index]);
            System.out.println();
        }

        for (int index = 0; index < dataSetSize; ++index) {
            System.out.print("Vector " + (index + 1) + ": "); printVector(dataSet[index]);
            System.out.println(" - class: " + kohonenMap.classify(dataSet[index]));
        }
    }

    public static void printVector(double[] vector) {
        StringBuilder stringBuilder = new StringBuilder("[");

        for (double component: vector) {
            stringBuilder.append(String.format("%.2f", component));//System.out.printf("%.2f", component);
            stringBuilder.append(", ");
        }

        System.out.print(stringBuilder.toString().trim() + "]");
    }
}
