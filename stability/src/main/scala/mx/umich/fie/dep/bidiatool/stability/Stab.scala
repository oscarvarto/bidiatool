package mx.umich.fie.dep.bidiatool.stability

import NumericalDifferentiation.{ ridders, jacobianFwdApprox }
import math.{ exp, abs }
import breeze.linalg._

object Stab extends App {
  /*
  val f1 = { (x: Double) => 3*exp(0.5*x) }
  val f1Der = ridders(f1, 1.0)
  println(f1Der(5.0))
*/

  /*
  //This works ok
  
  val f2: DenseVector[Double] => DenseVector[Double] = { xVec =>
    require(xVec.length == 2, "f2 maps R2 to R2")
    val x = xVec(0)
    val y = xVec(1)
    DenseVector(
      x + y * y,
      -y)
  }
  
  val Sx = DenseVector(0.5, 0.5).map { v => 1 / abs(v) }

  val jacf2 = jacobianFwdApprox(f2, Sx, 1e-9)_
  val jEP = jacf2(DenseVector(0.0, 0.0))
  println("jac:\n" + jEP + "\n")
  val (realEigVal, imagEigVal, eigVec) = eig(jEP)
  println(realEigVal)
  println(imagEigVal)
  println(eigVec)
*/

  val f3: DenseVector[Double] ⇒ DenseVector[Double] = { xVec ⇒
    require(xVec.length == 2, "f3 maps R2 to R2")
    val x = xVec(0)
    val y = xVec(1)

    DenseVector(
      0.5 * x - y - 0.5 * (x * x * x + y * y * x),
      x + 0.5 * y - 0.5 * (y * y * y + x * x * y))
  }

  val Sx = DenseVector(2.0, 2.0).map { v ⇒ 1 / abs(v) }

  val jacf3 = jacobianFwdApprox(f3, Sx)_
  val jEP = jacf3(DenseVector(0.0, 0.0))
  println("jac:\n" + jEP + "\n")
  val (realEigVal, imagEigVal, eigVec) = eig(jEP)
  println(realEigVal)
  println(imagEigVal)
  println(eigVec)
}
