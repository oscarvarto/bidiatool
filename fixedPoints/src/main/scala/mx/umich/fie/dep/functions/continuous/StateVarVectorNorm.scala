package mx.umich.fie.dep.functions.continuous

import net.sourceforge.cilib.functions.ContinuousFunction
import java.lang.{ Double ⇒ JDouble }
import net.sourceforge.cilib.`type`.types.container.{ Vector ⇒ CilibVector }
import mx.umich.fie.dep.bidiatool.parser.DynamicalSystem
import breeze.linalg.DenseVector
import scala.collection.mutable.ArrayBuffer

class StateVarVectorNorm(
  dsy: DynamicalSystem,
  keys: List[String], // cambiar keys por variables que debe ser una lista de nombres ordenados
  pars: Map[String, Double])
    extends ContinuousFunction {
  implicit def toArrayDouble(a: Array[JDouble]) = a.asInstanceOf[Array[Double]]

  override def apply(input: CilibVector): JDouble = {
    val inp = new ArrayBuffer[Double]
    val it = input.iterator()
    while (it.hasNext) {
      inp += it.next().doubleValue
    }

    val data: Map[String, Double] = (keys, inp).zipped.toMap ++ pars

    val a = dsy.eval(data)
    val g = DenseVector(a).norm(2)
    return g
  }
}
