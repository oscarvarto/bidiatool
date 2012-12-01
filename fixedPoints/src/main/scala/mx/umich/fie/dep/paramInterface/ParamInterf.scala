package mx.umich.fie.dep.paramInterface

import breeze.linalg.DenseVector
import mx.umich.fie.dep.bidiatool.parser.AST
import mx.umich.fie.dep.bidiatool.parser.AST.DynamicalSystem
import mx.umich.fie.dep.functions.continuous.StateVarVectorNorm
import mx.umich.fie.dep.simulator.Simulation
import net.sourceforge.cilib.algorithm.Algorithm
import net.sourceforge.cilib.measurement.multiple.CompositeMeasurement
import net.sourceforge.cilib.measurement.single.Solution
import net.sourceforge.cilib.problem.FunctionOptimisationProblem
import net.sourceforge.cilib.problem.objective.Minimise
import net.sourceforge.cilib.simulator.MeasurementSuite
import scala.collection.immutable.TreeMap
import scala.collection.mutable.ListBuffer
import scalaz._
import scalaz.Scalaz._
import mx.umich.fie.dep.paramInterface.GetSolutions.getFromFile
import net.sourceforge.cilib.stoppingcondition.MeasuredStoppingCondition
import mx.umich.fie.dep.nichePSO.NPSO
import net.sourceforge.cilib.stoppingcondition.Maximum
import net.sourceforge.cilib.measurement.generic.Iterations

object ParamInterf {
  // Once the ODE system has been successfully parsed, you can get
  // a) A list of variables from that system (with AST.variables)
  // b) A list of parameters (with AST.params)
  // The user then must
  // 1) Select which variables and/or parameters are going to be
  // fixed (and give the corresponding constant values).
  // At least one parameter (and at most two of them) must be allowed to vary
  // 2) Introduce a corresponding norm for the rest of variables that were
  // allowed to vary. NOTE: AT THIS POINT, THERE IS NO CHOICE FOR THE NORM:
  // EUCLIDEAN NORM IS USED BY DEFAULT.

  /**
    * Adds parameters to solutions found by the selected heuristic (nichePSO by default)
    *
    * Example: For the system
    * x' = r + x^2
    * you have to plot
    * x vs r
    * in the bifurcation diagram. Hence, it's necessary to get points to be plotted as
    * vectors [r, x]^T
    *
    * Note that several parameters might be varied at the same time. Therefore, it's
    * necessary to order the parameters by its identifier (to give consistent plots)
    */
  def addParsToSols(pars: Map[String, Double],
                    liSols: List[DenseVector[Double]]): List[DenseVector[Double]] = {
    val sortedPars = new TreeMap[String, Double]() ++ pars
    val vecPars = DenseVector(sortedPars.values.toArray)
    val realSols: List[DenseVector[Double]] = for { sol ← liSols } yield DenseVector.vertcat(vecPars, sol)
    realSols
  }

  /**
    * Finds equilibrium points with the given algorithm
    *
    * @param eqSys The successfully parsed System of First-Order ODEs
    * @param variables The variables that are allowed to vary in the simulation.
    * @param fixedParameters Contains associations of name of parameter → Double value
    * that are going to be used in simulation
    * @param numParticles The number of particles used by nichePSO
    * @param domain The search domain for the variables (excluding the "fixedVariables")
    * @param fixedVariables The state variables that take a constant (given) value.
    */
  def findEquilibriumPoints(
    dsy: DynamicalSystem,
    variables: List[String],
    fixedParameters: DenseVector[Map[String, Double]],
    numParticles: Int,
    domain: String,
    fixedVariables: Map[String, Double] = Map.empty): Option[List[DenseVector[Double]]] = {
    val sols = ListBuffer[Option[List[DenseVector[Double]]]]()
    val outputFile = "data/test.txt"

    fixedParameters.foreach(pars ⇒ {
      val pso = new NPSO(numParticles)
      val stopCond = new MeasuredStoppingCondition(
        new Iterations(),
        new Maximum(),
        100)
      pso.addStoppingCondition(stopCond)

      // Define problem
      val problem = new FunctionOptimisationProblem()
      problem.setDomain(domain)
      problem.setObjective(new Minimise())
      // falta agregar RealFixedParameters al ultimo parametro de StateVarVecorNorm
      // pars ++ fixedVariables ++ RealFixedParameters
      val f = new StateVarVectorNorm(dsy, variables, pars ++ fixedVariables)
      problem.setFunction(f)

      // Measurements
      val meas = new MeasurementSuite()
      meas.setResolution(50) // This shouldn't be a fixed value
      val compMeas = new CompositeMeasurement();
      compMeas.addMeasurement(new Solution())
      meas.addMeasurement(compMeas)
      meas.initialise()

      // Simulation
      val sim = new Simulation(
        pso,
        problem,
        meas,
        outputFile)
      sim.init()
      sim.run()

      val liSols = getFromFile(outputFile)
      sols += liSols ∘ (s ⇒ addParsToSols(pars, s))
    })
    val fixedPoints = sols.foldLeft(Nil.some: Option[List[DenseVector[Double]]])(_ ⊹ _)
    fixedPoints
  }
}

