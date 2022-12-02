import javax.swing.*;
import java.awt.*;

public class KohonenMap implements Runnable {

    private final double[][][] weightsByEpochs;

    private final double[][] dataSet;

    private final JPanel panel;

    private final double[] vectorClasses;

    private final int epochNum;

    public KohonenMap(JPanel panel, double[][][] weightsByEpochs, double[][] dataSet, int epochNum, double[] vectorClasses) {
        this.weightsByEpochs = weightsByEpochs;
        this.dataSet = dataSet;
        this.epochNum = epochNum;
        this.panel = panel;
        this.vectorClasses = vectorClasses;
    }

    private void drawDataset() {
        Graphics2D graphics2D = (Graphics2D) panel.getGraphics();
        graphics2D.translate(245, 245);

        for (int index = 0; index < dataSet.length; ++index) {
            graphics2D.setColor(Color.RED);
            graphics2D.drawOval((int)(dataSet[index][0] * 200), (int)(dataSet[index][1] * 200), 10, 10);
        }
    }

    private void drawLearningStates() throws InterruptedException {
        Graphics2D graphics2D = (Graphics2D) panel.getGraphics();
        graphics2D.translate(245, 245);

        drawDataset();

        for (int index1 = 0; index1 <= epochNum; ++index1) {
            for (int index2 = 0; index2 < weightsByEpochs[index1].length; ++index2) {

                graphics2D.drawOval(
                        (int)(weightsByEpochs[index1][index2][0] * 200),
                        (int)(weightsByEpochs[index1][index2][1] * 200),
                        10, 10
                );
            }
            Thread.sleep(1000);
        }
    }

    private void drawWorkingState() {
        Graphics2D graphics2D = (Graphics2D) panel.getGraphics();
        graphics2D.translate(245, 245);

        double[][] lastEpochWeights = weightsByEpochs[epochNum];

        for (int index1 = 0; index1 < lastEpochWeights.length; ++index1) {
            graphics2D.setColor(getRandomColor());
            graphics2D.drawOval(
                    (int)(lastEpochWeights[index1][0] * 200),
                    (int)(lastEpochWeights[index1][1] * 200),
                    10, 10);

            for (int index2 = 0; index2 < vectorClasses.length; ++index2) {
                if (vectorClasses[index2] == index1) {
                    graphics2D.drawOval(
                                    (int)(dataSet[index2][0] * 200),
                                    (int)(dataSet[index2][1] * 200),
                                    5, 5
                    );
                }
            }
        }
    }

    private Color getRandomColor() {
        return new Color(
                RandomGenerator.generateNum(0, 256),
                RandomGenerator.generateNum(0, 256),
                RandomGenerator.generateNum(0, 256)
        );
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            drawLearningStates();
            Thread.sleep(5000);
            panel.repaint();
            drawWorkingState();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
