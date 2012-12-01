package mx.umich.fie.dep.bidiatool.stability

import mx.umich.fie.dep.bidiatool.parser.{ AST, Parser }
import AST.DynamicalSystem
import breeze.linalg._

/**
  * Creates a VectorialFuntion f, using parameters included in a solution.
  *
  * @param eqSys The successfully parsed Dynamical System
  * @param namesOfVariables The names of those variables that can take
  *  different values in the search space
  * @param namesOfParameters The names of those parameters that vary in a
  *  quasi-static fashion
  * @param valuesAssignedToFixedParameters A relation that assigns a value to
  *  each fixed parameter
  * @param valuesAssignedToFixedVariables A relation that assigns a value to
  *  each fixed variable
  */
class FunctionEvaluator(
  val v: DenseVector[Double],
  val dsy: DynamicalSystem,
  val namesOfVariables: List[String],
  val namesOfParameters: List[String],
  val valuesAssignedToFixedParameters: Map[String, Double] = Map.empty,
  val valuesAssignedToFixedVariables: Map[String, Double] = Map.empty) {

  val numberOfParameters = namesOfParameters.length
  val valuesOfParameters = v(0 until numberOfParameters).data
  val valuesAssignedToParameters =
    (namesOfParameters, valuesOfParameters).zipped.toMap
  val dataIncomplete: Map[String, Double] =
    valuesAssignedToParameters ++
      valuesAssignedToFixedParameters ++
      valuesAssignedToFixedVariables

  def VectorialFunction: DenseVector[Double] ⇒ DenseVector[Double] = { x ⇒
    val valuesAssignedToVariables = (namesOfVariables, x.data).zipped.toMap
    val data = dataIncomplete ++ valuesAssignedToVariables
    dsy.eval(data)
    val a = AST.evaluation.result.values.toArray
    DenseVector(a)
  }

  def OneVariableFunction: Double ⇒ Double = { x ⇒
    require(namesOfVariables.length == 1, "This is a one variable function")
    val data = dataIncomplete + (namesOfVariables(0) → x)
    dsy.eval(data)
    val a = AST.evaluation.result.values.head
    a
  }
}

