package mx.umich.fie.dep.bidiatool.parser

import scala.collection.immutable.TreeMap
import scala.collection.mutable.ListBuffer
import scala.sys.error

object AST {
  var env: Map[String, Double] = Map.empty
  var evaluation = TreeMap.newBuilder[String, Double]
  var equations: List[Expr] = List.empty
  var variables: List[String] = List.empty
  var params: List[String] = List.empty

  var norms: List[Expr] = List.empty
  var normEvaluation = TreeMap.newBuilder[String, Double]
  var varsInNorm: Set[String] = Set.empty

  case class DynamicalSystem(exprs: List[Expr]) {
    override def toString = exprs mkString "\n"

    val eqs = new ListBuffer[Expr]
    val varNames = new ListBuffer[String]
    val paramNames = new ListBuffer[String]

    val normsAcc = new ListBuffer[Expr]

    exprs foreach { e ⇒
      e match {
        case Equation(variable, expr) ⇒ {
          varNames += variable.name.drop(1)
          eqs += e
        }
        case ParamDecl(param) ⇒ {
          paramNames += param.name
        }
        case NormDefinition(norm, expr) ⇒ {
          normsAcc += e

        }
        case _ ⇒ e.eval
      }

      equations = eqs.toList
      variables = varNames.sorted.toList
      params = paramNames.toList

      norms = normsAcc.toList
    }

    def eval(data: Map[String, Double] = Map.empty) {
      if (data.keySet == (variables ++ params).toSet) {
        env ++= data
        equations.foreach(_.eval)
      } else {
        println("eval didn't receive enough data")
      }
    }

    def evalNorm(data: Map[String, Double] = Map.empty) {
      if (data.keySet == (variables ++ params).toSet) {
        env ++= data
        norms.foreach(_.eval)
      } else {
        println("evalNorm didn't receive enough data")
      }
    }
  }

  sealed abstract class Expr {
    def eval: Double
  }

  case class ConstantAssign(variable: AlgebraicVar, c: Number) extends Expr {
    def eval = {
      env += (variable.name -> c.eval)
      c.eval
    }
  }

  case class ParamDecl(variable: AlgebraicVar) extends Expr {
    def eval = env(variable.name)
  }

  case class Equation(stVar: StateVar, e: Expr) extends Expr {
    def eval = {
      evaluation += (stVar.name -> e.eval)
      e.eval
    }
  }

  trait CanSearchAlgVarsInNorm {
    def search(): Unit
  }
  
  implicit def ToCanSearchAlVarsInNorm(e: Expr) = e.asInstanceOf[CanSearchAlgVarsInNorm]
  
  case class NormDefinition(norm: String, e: Expr) extends Expr with CanSearchAlgVarsInNorm {
    def eval = {
      normEvaluation += (norm -> e.eval)
      e.eval
    }
    def search() = e.search()
  }

  sealed abstract class BinaryOp extends Expr {
    val e1, e2: Expr
    def eval = (e1.eval, e2.eval) match {
      case (x: Double, y: Double) ⇒ this match {
        case _: Add ⇒ x + y
        case _: Sub ⇒ x - y
        case _: Mul ⇒ x * y
        case _: Div ⇒ x / y
        case _: Pow ⇒ math.pow(x, y)
      }
      case _ ⇒ error(this.getClass.getSimpleName + " requires two numeric values")
    }
    //    def search() = {
    //      e1.search()
    //      e2.search()
    //    }
  }

  case class Add(e1: Expr, e2: Expr) extends BinaryOp
  case class Sub(e1: Expr, e2: Expr) extends BinaryOp
  case class Mul(e1: Expr, e2: Expr) extends BinaryOp
  case class Div(e1: Expr, e2: Expr) extends BinaryOp
  case class Pow(e1: Expr, e2: Expr) extends BinaryOp

  case class Neg(e: Expr) extends Expr {
    def eval = e.eval match {
      case x: Double ⇒ -x
      case _         ⇒ error("Neg requires a numeric argument")
    }
    //  def search() = e.search()
  }

  /** Function application. */
  case class App(fun: String, arg: Expr) extends Expr {
    def eval = (fun, arg.eval) match {
      case ("Sin", x: Double) ⇒ math.sin(x)
      case ("Cos", x: Double) ⇒ math.cos(x)
      // ...
      case (f, x)             ⇒ error(f + " cannot be applied as a function to the argument " + x)
    }
    //  def search() = arg.search()
  }

  /*
  sealed abstract class Fun extends Expr 
  case object Sin extends Fun
  case object Cos extends Fun
  // ...
  */
  case class Number(v: Double) extends Expr {
    def eval = v
  }

  // eval method never gets used for StateVar!!!
  sealed abstract class Var extends Expr {
    val name: String
    def eval = env.get(name) match {
      case Some(x) ⇒ x
      case None    ⇒ error("Variable " + name + " is unknown")
    }
  }

  case class StateVar(name: String) extends Var
  case class AlgebraicVar(name: String) extends Var {
    //override def eval = env(name)
    //    override def search() {
    //      varsInNorm += name
    //    }
  }
}
