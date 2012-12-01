package mx.umich.fie.dep.bidiatool.stability
import breeze.linalg._
import NumericalDifferentiation._
import math.signum

sealed class Stability
case object AsymptoticallyStable extends Stability // Liapunov Stable & Attracting 
//case object NeutrallyStable extends Stability // Liapunov Stable & NOT Attracting
case object Unstable extends Stability // NOT Liapunov Stable NOR Attracting
case object Undefined extends Stability

sealed class LinearConjugateFlow(val stability: Stability)
case class SaddleNode() extends LinearConjugateFlow(Unstable)
case class Sink() extends LinearConjugateFlow(AsymptoticallyStable)
case class Source() extends LinearConjugateFlow(Unstable)

// These are meaningful in the 2D case only
case class SpiralSink() extends LinearConjugateFlow(AsymptoticallyStable)
//case class Center() extends EquilibriumPoint(NeutrallyStable)
case class SpiralSource() extends LinearConjugateFlow(Unstable)
case class SpiralSaddle() extends LinearConjugateFlow(Unstable)

object StabilityQualifier {

  def oneVariableStability(funEv: FunctionEvaluator): Stability = {
    val f = funEv.OneVariableFunction
    val derF = ridders(f, 1.0)
    val numberOfParams = funEv.numberOfParameters
    val x = funEv.v(numberOfParams)
    signum(derF(x)) match {
      case -1.0 ⇒ AsymptoticallyStable
      case 0.0  ⇒ Undefined
      case 1.0  ⇒ Unstable
    }
  }

  def SeveralVariablesStability(funEv: FunctionEvaluator): Either[Stability, LinearConjugateFlow] = {
    val f = funEv.VectorialFunction
    val numberOfParams = funEv.numberOfParameters
    val numberOfVariables = funEv.namesOfVariables.length
    val Sx = DenseVector.fill(numberOfVariables)(1.0)
    val x = funEv.v(numberOfParams until numberOfParams + numberOfVariables)
    val jacX = jacobianFwdApprox(f, Sx)(x)
    val (eigValuesReal, eigValuesImag, eigVec) = eig(jacX)
    val eigVals = (eigValuesReal.data, eigValuesImag.data).zipped.toList
    stabilityOrLinearConjugateFlow(eigVals)
  }

  def stabilityOrLinearConjugateFlow(eigVals: List[(Double, Double)]): Either[Stability, LinearConjugateFlow] = {
    case class eigInfo(
      val isHyperbolic: Boolean,
      val negativeFound: Boolean,
      val positiveFound: Boolean,
      val complexFound: Boolean)

    val eigInfo0 = eigInfo(
      isHyperbolic = true,
      negativeFound = false,
      positiveFound = false,
      complexFound = false)

    val finalInfo = eigVals.foldLeft(eigInfo0)((info, tuple) ⇒ {
      val realPart = tuple._1
      val imagPart = tuple._2
      val isHyperbolic = info.isHyperbolic && realPart != 0.0 // compare with 0.0 ??? doesn't seem OK
      val negativeFound = info.negativeFound || realPart < 0.0
      val positiveFound = info.positiveFound || realPart > 0.0
      val complexFound = info.complexFound || imagPart != 0.0
      eigInfo(
        isHyperbolic,
        negativeFound,
        positiveFound,
        complexFound)
    })

    val classification: Either[Stability, LinearConjugateFlow] = finalInfo match {
      case eigInfo(false, false, false, _)   ⇒ Left(Undefined) //  1
      case eigInfo(false, false, true, _)    ⇒ Left(Unstable) //  2
      case eigInfo(false, true, false, _)    ⇒ Left(Undefined) //  3
      case eigInfo(false, true, true, _)     ⇒ Left(Unstable) //  4
      case eigInfo(true, false, true, false) ⇒ Right(Source()) //  5
      case eigInfo(true, false, true, true)  ⇒ Right(SpiralSource()) //  6
      case eigInfo(true, true, false, false) ⇒ Right(Sink()) //  7
      case eigInfo(true, true, false, true)  ⇒ Right(SpiralSink()) //  8
      case eigInfo(true, true, true, false)  ⇒ Right(SaddleNode()) //  9
      case eigInfo(true, true, true, true)   ⇒ Right(SpiralSaddle()) // 10
    }
    classification
  }
}

