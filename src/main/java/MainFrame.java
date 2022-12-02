import javax.swing.*;

public class MainFrame extends JFrame {
    public MainFrame(double[][][] weightsByEpochs, double[][] dataSet, int epochNum, double[] vectorClasses) {
        JPanel panel = new JPanel();

        Thread thread = new Thread(new KohonenMap(panel, weightsByEpochs, dataSet, epochNum, vectorClasses));
        thread.start();

        setSize(515,545);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
    }

    public static void main(String[] args) {
        int dataSetSize = 20;

        double[][] dataSet = new double[dataSetSize][KohonenNetwork.INPUT_NUM];

        for (int index = 0; index < dataSetSize; ++index) {
            dataSet[index] = RandomGenerator.generateVector(KohonenNetwork.INPUT_NUM, -1, 1);
        }

        KohonenNetwork kohonenNetwork = new KohonenNetwork(4);
        kohonenNetwork.learn(dataSet);

        double[] vectorClasses = kohonenNetwork.classifyAll(dataSet);

        new MainFrame(kohonenNetwork.getWeightsByEpochs(), dataSet, kohonenNetwork.getEpochNum(), vectorClasses);
    }
}
