package mx.umich.fie.dep.bidiatool.stability
import StabilityQualifier.stabilityOrLinearConjugateFlow
import breeze.linalg._

/**
  * Examples taken from
  * Differential Equations, Dynamical Systems, and an Introduction to Chaos,
  * Second Edition,
  * from Hirsch, Smale and Devaney
  */

object StabTest2 extends App {
  def classify(m: DenseMatrix[Double]): Either[Stability, LinearConjugateFlow] = {
    val (eigValuesReal, eigValuesImag, eigVec) = eig(m)

    println("\n" + m + "\n===================")
    println("eigValuesReal\n" + eigValuesReal)
    println("eigValuesImag\n" + eigValuesImag)
    println("eigVec\n" + eigVec)

    val eigVals = (eigValuesReal.data, eigValuesImag.data).zipped.toList
    stabilityOrLinearConjugateFlow(eigVals)
  }

  // p. 55, case 1 = Left(Undefined)
  val m11 = DenseMatrix(
    (0., 1.),
    (-4., 0.))

  // p. 97, case 5 = Right(Source())
  val m51 = DenseMatrix(
    (2., 0., -1.),
    (0., 2., 1.),
    (-1., -1., 2.))
  val m52 = DenseMatrix(
    (2., 1., 0.),
    (0., 2., 1.),
    (0., 0., 2.))
  // p. 112, case 10

  val matrices = List(m11, m51, m52)
  val classification = matrices.map(classify)
  println(classification)
}