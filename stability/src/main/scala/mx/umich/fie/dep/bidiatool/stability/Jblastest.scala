package mx.umich.fie.dep.bidiatool.stability

import org.jblas.DoubleMatrix
import org.jblas.Eigen.eigenvalues

object Jblastest extends App {
  val m1 = new DoubleMatrix(3, 3,
    2., 0., -1.,
    0., 2., 1.,
    -1., -1., 2.)

  println(eigenvalues(m1))

  val m2 = new DoubleMatrix(3, 3,
    2., 1., 0.,
    0., 2., 1.,
    0., 0., 2.)

  println(eigenvalues(m2))
}
