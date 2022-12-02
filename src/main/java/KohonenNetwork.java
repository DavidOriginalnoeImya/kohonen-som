public class KohonenNetwork {

    private final double[][][] weightsByEpochs;

    private final double[][] weights, prevWeights;

    public static final int INPUT_NUM = 2;

    public static final double LEARN_RATE = 0.3;

    public static final double RADIUS = 0.05;

    private int epochNum;

    public KohonenNetwork(int clusterNum) {
        weights = new double[clusterNum][INPUT_NUM];
        prevWeights = new double[clusterNum][INPUT_NUM];
        weightsByEpochs = new double[100][weights.length][];
        fillWeights();
    }

    public double[][][] getWeightsByEpochs() {
        return weightsByEpochs;
    }

    public int getEpochNum() {
        return epochNum;
    }

    public double[] classifyAll(double[][] dataSet) {
        double[] classes = new double[dataSet.length];

        for (int index = 0; index < dataSet.length; ++index) {
            classes[index] = classify(dataSet[index]);
        }

        return classes;
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
        int epochNum = 0; double learnRate = LEARN_RATE, radius = RADIUS, radiusDiff = 0.5;

        saveEpochWeights(epochNum);

        do {
            copyWeights();

            for (int index1 = 0; index1 < dataSet.length; ++index1) {
                int neuronWinnerIndex = 0;
                double minDistance = Double.MAX_VALUE;

                for (int index2 = 0; index2 < weights.length; ++index2) {
                    double distance = getVectorsDistance(dataSet[index1], weights[index2]);

                    if (distance < minDistance) {
                        minDistance = distance; neuronWinnerIndex = index2;
                    }
                }

                double[] winnerWeights = weights[neuronWinnerIndex];
                correctNeuronsWeights(
                        winnerWeights[0] - radius, winnerWeights[0] + radius,
                        winnerWeights[1] - radius, winnerWeights[1] + radius,
                            dataSet[index1], learnRate);
            }
            learnRate -= 0.01; ++epochNum; saveEpochWeights(epochNum);
            if (radius - radiusDiff >= 0) radius -= radiusDiff;
        } while (isWeightsChange());

        this.epochNum = epochNum;

        System.out.println("Epoch num: " + epochNum);
    }

    private void correctNeuronsWeights(double x1, double x2, double y1, double y2, double[] dataVec, double learnRate) {
        for (int index = 0; index < weights.length; ++index) {
            if (weights[index][0] >= x1 && weights[index][0] <= x2 &&
                    weights[index][1] >= y1 && weights[index][1] <= y2) {
                correctNeuronWeights(index, dataVec, learnRate);
            }
        }
    }

    private void correctNeuronWeights(int neuronNum, double[] dataVec, double learnRate) {
        if (neuronNum >= weights.length || neuronNum < 0) throw new RuntimeException("Incorrect neuron index");

        if (weights[neuronNum].length != dataVec.length) throw new RuntimeException("Different vectors size");

        for (int index = 0; index < weights[neuronNum].length; ++index) {
            weights[neuronNum][index] += learnRate * (dataVec[index] - weights[neuronNum][index]);
        }
    }

    private void saveEpochWeights(int epoch) {
        if (epoch > weightsByEpochs.length) throw new RuntimeException("Epoch num too big");

        for (int index = 0; index < weights.length; ++index) {
            weightsByEpochs[epoch][index] = weights[index].clone();
        }
    }

    private void copyWeights() {
        for (int index = 0; index < weights.length; ++index) {
            prevWeights[index] = weights[index].clone();
        }
    }

    private boolean isWeightsChange() {
        for (int index1 = 0; index1 < weights.length; ++index1) {
            for (int index2 = 0; index2 < weights[index1].length; ++index2) {
                if (weights[index1][index2] - prevWeights[index1][index2] > 0.01) {
                    return true;
                }
            }
        }

        return false;
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
            weights[index] = RandomGenerator.generateVector(weights[index].length, -0.1, 0.1);
        }
    }

    public static void main(String[] args) {
        int dataSetSize = 10;

        double[][] dataSet = new double[dataSetSize][KohonenNetwork.INPUT_NUM];

        for (int index = 0; index < dataSetSize; ++index) {
            dataSet[index] = RandomGenerator.generateVector(KohonenNetwork.INPUT_NUM, -1, 1);
        }

        KohonenNetwork kohonenNetwork = new KohonenNetwork(4);
        System.out.println("Source network weights:");

        for (int index = 0; index < kohonenNetwork.weights.length; ++index) {
            printVector(kohonenNetwork.weights[index]);
            System.out.println();
        }

        kohonenNetwork.learn(dataSet);

        System.out.println("Network weights after learning:");

        for (int index = 0; index < kohonenNetwork.weights.length; ++index) {
            printVector(kohonenNetwork.weights[index]);
            System.out.println();
        }

        for (int index = 0; index < dataSetSize; ++index) {
            System.out.print("Vector " + (index + 1) + ": "); printVector(dataSet[index]);
            System.out.println(" - class: " + kohonenNetwork.classify(dataSet[index]));
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
