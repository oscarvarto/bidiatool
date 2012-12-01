package mx.umich.fie.dep.bidiatool.stability

import scala.math.{ abs, max, sqrt, signum }
import breeze.linalg._

object NumericalDifferentiation {

  def ridders(fn: Double ⇒ Double, h: Double): Double ⇒ Double = { x ⇒
    val con = 1.4

    case class DerivativeData(
      val guess: Double,
      val col: Array[Double],
      val error: Double)

    case class Data(
      val guess: Double,
      val colRev: List[Double],
      val err: Double,
      val fac: Double)

    def derivative(
      prevDerData: DerivativeData,
      hh: Double): Double = {

      val derData = nextDerivativeData(prevDerData, hh)

      val higherOrderWorse = {
        val colLast = derData.col.last
        val pColLast = prevDerData.col.last
        val err = derData.error
        val safe = 2.0

        abs(colLast - pColLast) >= safe * err
      }

      if (higherOrderWorse)
        derData.guess
      else
        derivative(derData, hh / con)
    }

    def nextDerivativeData(prevDerData: DerivativeData, hh: Double): DerivativeData = {
      val initialEstimate = (fn(x + hh) - fn(x - hh)) / (2.0 * hh)
      val colRev = List(initialEstimate)

      val factor = con * con
      val initialData = Data(prevDerData.guess, colRev, prevDerData.error, factor)

      val finalData: Data = prevDerData.col.foldLeft(initialData) { (data, n) ⇒
        val colRev = data.colRev
        val fac = data.fac

        val nextGuess = (colRev.head * fac - n) / (fac - 1.0)
        val previousGuess = data.guess

        val nextError = max(
          abs(nextGuess - colRev.head),
          abs(nextGuess - n))
        val previousErr = data.err

        if (nextError <= previousErr) {
          Data(nextGuess, nextGuess :: colRev, nextError, fac * con)
        } else
          Data(previousGuess, nextGuess :: colRev, previousErr, fac * con)
      }

      DerivativeData(
        finalData.guess,
        finalData.colRev.reverse.toArray,
        finalData.err)
    }

    val firstDerData = {
      val firstGuess = (fn(x + h) - fn(x - h)) / (2.0 * h)
      val firstCol = Array(firstGuess)
      val firstErr = Double.MaxValue

      DerivativeData(firstGuess, firstCol, firstErr)
    }

    derivative(firstDerData, h / con)
  }

  def jacobianFwdApprox(
    fvec: DenseVector[Double] ⇒ DenseVector[Double],
    Sx: DenseVector[Double],
    eta: Double = 1e-7)(xc: DenseVector[Double]): DenseMatrix[Double] = {
    val Fc = fvec(xc)
    val sqrtEta = sqrt(eta)
    val n = xc.length
    val Jac = DenseMatrix.zeros[Double](n, n)
    for (j ← 0 until n) {
      val maxj = max(abs(xc(j)), 1.0 / Sx(j))
      val sign = signum(xc(j))
      val signXcj = if (sign != 0) sign else 1.0
      val stepj = sqrtEta * maxj * signXcj
      val tempj = xc(j)
      xc(j) += stepj
      val stepsizej = xc(j) - tempj
      val Fj = fvec(xc)
      Jac(::, j) := (Fj - Fc) :/ stepsizej
      xc(j) = tempj
    }
    Jac
  }
}
