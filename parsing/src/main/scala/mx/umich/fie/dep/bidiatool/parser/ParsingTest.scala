package mx.umich.fie.dep.bidiatool.parser

import scala.collection.immutable.TreeMap

object ParsingTest {
  def main(args: Array[String]): Unit = {
    val code =
      """
        |x2' = -w^2*x1 - 2*Mu*x2 + F*Cos[Omega]
    	|x1' = x2
        |w = Param
        |F = 10
        |Mu = 2
        |Omega = 2
        |Norm1 = x1^2 + x2^2
      """.stripMargin

    import math.cos
    val x1 = 10.0 * cos(2) / 9.0
    val data = TreeMap("x1" -> x1, "x2" -> 0.0, "w" -> 3.0)
    // val ast = new MyAST
    val res = Parser.parse(code)

    val map =
      if (res.successful) {
        val eqSystem = res.get
        //eqSystem.init()
        //println(AST.variables)
        Some(eqSystem.eval(data))
        //eqSystem.evalNorm(data)
        //Some(AST.evaluation.result, AST.normEvaluation.result)
        //
      } else
        None

    println(map)
  }
}
