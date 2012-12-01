package mx.umich.fie.dep.simulator;

import java.io.File;
import java.io.IOException;

import net.sourceforge.cilib.algorithm.AbstractAlgorithm;
import net.sourceforge.cilib.algorithm.Algorithm;
import net.sourceforge.cilib.algorithm.AlgorithmEvent;
import net.sourceforge.cilib.algorithm.AlgorithmListener;
import net.sourceforge.cilib.problem.Problem;
import net.sourceforge.cilib.simulator.MeasurementSuite;

import com.google.common.io.Files;

public class Simulation implements AlgorithmListener, Runnable {

    private static final long serialVersionUID = 1L;
    private final Algorithm algorithm;
    private final Problem problem;
    private final MeasurementSuite measurementSuite;
    private final String outputFileName;

    /**
     * Create a Simulation with the required dependencies.
     */
    public Simulation(
                      Algorithm algorithm,
                      Problem problem,
                      MeasurementSuite measurementSuite,
                      String outputFileName) {
        this.algorithm = algorithm;
        this.problem = problem;
        this.measurementSuite = measurementSuite;
        this.outputFileName = outputFileName;
    }

    /**
     * Prepare for execution. The simulation is prepared for execution by
     * setting the provided problem on the current algorithm,
     * followed by the required initialization for the algorithm itself.
     */
    public void init() {
        AbstractAlgorithm alg = (AbstractAlgorithm) algorithm;
        alg.addAlgorithmListener(this);
        alg.setOptimisationProblem(problem);
        alg.performInitialisation();
    }

    /**
     * Execute the simulation.
     */
    @Override
    public void run() {
        algorithm.run();
    }

    /**
     * Terminate the current simulation.
     */
    public void terminate() {
        ((AbstractAlgorithm) algorithm).terminate();
    }

    @Override
    public void algorithmStarted(AlgorithmEvent event) {
        measurementSuite.initialise(); // Initialise the temporary data store
    }

    @Override
    public void algorithmFinished(AlgorithmEvent event) {
        measurementSuite.measure(event.getSource());
        //simulator.updateProgress(this, ((AbstractAlgorithm) event.getSource()).getPercentageComplete());

        try {
            measurementSuite.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        File output = measurementSuite.getFile();
        try {
            Files.copy(output, new File(outputFileName));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void iterationCompleted(AlgorithmEvent event) {
        Algorithm alg = event.getSource();
        if (alg.getIterations() % measurementSuite.getResolution() == 0) {
            measurementSuite.measure(alg);
            //simulator.updateProgress(this, ((AbstractAlgorithm) alg).getPercentageComplete());
        }
    }

    @Override
    public AlgorithmListener getClone() {
        return this;
    }

    public MeasurementSuite getMeasurementSuite() {
        return measurementSuite;
    }

    public Problem getProblem() {
        return problem;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
