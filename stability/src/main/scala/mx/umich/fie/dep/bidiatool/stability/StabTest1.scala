package mx.umich.fie.dep.bidiatool.stability
import StabilityQualifier.stabilityOrLinearConjugateFlow
import breeze.linalg._

object StabTest1 extends App {
  def classify(m: DenseMatrix[Double]) = {
    //(DenseVector[Double], DenseVector[Double], DenseMatrix[Double]) = {
    val (eigValuesReal, eigValuesImag, eigVec) = eig(m)
    val eigVals = (eigValuesReal.data, eigValuesImag.data).zipped.toList
    val classification = stabilityOrLinearConjugateFlow(eigVals)
    println(classification)
  }

  // case 1  
  val m11 = DenseMatrix.zeros[Double](4, 4)
  val m12 = DenseMatrix((0., 1.), (-1., 0.))
  val m13 = DenseMatrix(
    (0., 1., 0., 0.),
    (-1., 0., 0., 0.),
    (0., 0., 0., -100.0),
    (0., 0., 100.0, 0.))
  val case1 = List(m11, m12, m13)

  // case 2
  val m21 = diag(DenseVector(1., 2., 0.))
  val m22 = DenseMatrix(
    (1., 3., 0.),
    (-3., 1., 0.),
    (0., 0., 0.))
  val case2 = List(m21, m22)

  // case 3
  val m31 = diag(DenseVector(0., -1., -100.))
  val m32 = DenseMatrix(
    (-1., 3., 0., 0., 0.),
    (-3., -1., 0., 0., 0.),
    (0., 0., -1., 3., 0.),
    (0., 0., -3., -1., 0.),
    (0., 0., 0., 0., 0.))
  val case3 = List(m31, m32)

  // case 4
  val m41 = diag(DenseVector(-1., 0., 1.))
  val m42 = DenseMatrix(
    (-1., 3., 0., 0., 0.),
    (-3., -1., 0., 0., 0.),
    (0., 0., 1., 3., 0.),
    (0., 0., -3., 1., 0.),
    (0., 0., 0., 0., 0.))
  val case4 = List(m41, m42)

  // case 5
  val m51 = diag(DenseVector(1., 2., 3.))
  val m52 = diag(DenseVector.tabulate(10) { _ + 1.0 })
  val case5 = List(m51, m52)

  // case 6
  val m61 = DenseMatrix(
    (1., 3., 0., 0., 0.),
    (-3., 1., 0., 0., 0.),
    (0., 0., 1., 3., 0.),
    (0., 0., -3., 1., 0.),
    (0., 0., 0., 0., 5.))
  val m62 = DenseMatrix(
    (1., 3., 0., 0., 0.),
    (-3., 1., 0., 0., 0.),
    (0., 0., 9., 3., 0.),
    (0., 0., -3., 9., 0.),
    (0., 0., 0., 0., 0.1))
  val case6 = List(m61, m62)

  // case 7
  val m71 = diag(DenseVector.tabulate(10) { i ⇒ -(i + 1.0) })
  val case7 = List(m71)

  // case 8
  val m81 = DenseMatrix(
    (-1., 3., 0., 0., 0.),
    (-3., -1., 0., 0., 0.),
    (0., 0., -1., 3., 0.),
    (0., 0., -3., -1., 0.),
    (0., 0., 0., 0., -5.))
  val m82 = DenseMatrix(
    (-1., 3., 0., 0., 0.),
    (-3., -1., 0., 0., 0.),
    (0., 0., -9., 3., 0.),
    (0., 0., -3., -9., 0.),
    (0., 0., 0., 0., -0.1))
  val case8 = List(m81, m82)

  // case 9
  val m91 = diag(DenseVector(-100., 100, 0.1))
  val case9 = List(m91)

  // case 10
  val m101 = DenseMatrix(
    (1., 3., 0., 0., 0.),
    (-3., 1., 0., 0., 0.),
    (0., 0., -1., 3., 0.),
    (0., 0., -3., -1., 0.),
    (0., 0., 0., 0., 5.))
  val m102 = DenseMatrix(
    (1., 3., 0., 0., 0.),
    (-3., 1., 0., 0., 0.),
    (0., 0., -9., 3., 0.),
    (0., 0., -3., -9., 0.),
    (0., 0., 0., 0., -0.1))
  val case10 = List(m101, m102)

  val matrices = case7
  for (m ← matrices)
    classify(m)
}