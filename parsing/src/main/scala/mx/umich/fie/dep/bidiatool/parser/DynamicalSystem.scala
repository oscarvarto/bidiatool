package mx.umich.fie.dep.bidiatool.parser

import scala.sys.error
import scalaz._, Scalaz._

sealed abstract class Expr

sealed trait CanEvaluate {
  def eval: Reader[TableVals, Num]
}

case class DynamicalSystem(exprs: List[Expr]) extends Expr {
  override def toString = exprs mkString "\n"

  val ast = exprs.foldLeft(MutableAST()) { (ast, expr) ⇒
    expr match {
      case eq @ Equation(dotStVar, e) ⇒
        ast.equations += eq
        ast.namesVariables += dotStVar.name.drop(1)
        ast
      case ConstantDefinition(stVar, number) ⇒
        ast.constants += (stVar.name → number.value)
        ast
      case ParamDeclaration(param) ⇒
        ast.namesParams += param.name
        ast
      case normDef @ NormDefinition(norm, e) ⇒
        ast.norms += normDef
        // Find names variables in e !!!!
        ast
      case _ ⇒ ast
    }
  }.toImmutable

  def eval(input: TableVals) = {
    require(input.keySet == ast.namesVariables ++ ast.namesParams,
      "eval didn't receive enough data")

    val evaluation: List[Num] = for (eq ← ast.equations) yield eq.expr.eval(input)
    evaluation.toArray
  }
  
  def namesParameters: List[String] = ast.namesParams.toList  // Already sorted!
  
}

private[parser] case class Equation(dotStVar: DotStateVar, expr: Expr)
    extends Expr with CanEvaluate {
  def eval = for {
    num ← expr.eval
  } yield num

  //implicit def eqToCanEvaluate(e: Equation) = eq.asInstanceOf[CanEvaluate]
}

private[parser] case class ConstantDefinition(stVar: StateVar, c: Number) extends Expr

private[parser] case class ParamDeclaration(param: Parameter) extends Expr

//
//
//  trait CanSearchAlgVarsInNorm {
//    def search(): Unit
//  }
//
//  implicit def ToCanSearchAlVarsInNorm(e: Expr) = e.asInstanceOf[CanSearchAlgVarsInNorm]
//
private[parser] case class NormDefinition(norm: String, expr: Expr)
    extends Expr with CanEvaluate { //CanSearchAlgVarsInNorm {
  def eval = Reader { input ⇒ expr.eval(input) }
  //def search() = e.search()
}

private[parser] sealed abstract class BinaryOp(lhs: Expr, rhs: Expr)(f: (Num, Num) ⇒ Num)
    extends Expr with CanEvaluate {
  def eval = for {
    numlhs ← lhs.eval
    numrhs ← rhs.eval
  } yield f(numlhs, numrhs)
}

private[parser] case class Add(lhs: Expr, rhs: Expr) extends BinaryOp(lhs, rhs)(Num.addition)
private[parser] case class Sub(lhs: Expr, rhs: Expr) extends BinaryOp(lhs, rhs)(Num.subtraction)
private[parser] case class Mul(lhs: Expr, rhs: Expr) extends BinaryOp(lhs, rhs)(Num.product)
private[parser] case class Div(lhs: Expr, rhs: Expr) extends BinaryOp(lhs, rhs)(Num.division)
private[parser] case class Pow(lhs: Expr, rhs: Expr) extends BinaryOp(lhs, rhs)(Num.pow)

private[parser] case class Neg(expr: Expr) extends Expr with CanEvaluate {
  def eval = for {
    num ← expr.eval
  } yield (-num)
}

/** Function application. */
private[parser] case class App(f: String, arg: Expr) extends Expr with CanEvaluate {
  def eval = {
    val fReader = for {
      argNum ← arg.eval
    } yield argNum

    (f, fReader) match {
      case ("Sin", _) ⇒ Reader { (fReader >=> Reader { math.sin _ }) apply (_: TableVals) }
      //case ("Cos", _) ⇒ math.cos(x)
      // ...
      case (f, x)     ⇒ error(f + " cannot be applied as a function to the argument " + x)
    }
    //  def search() = arg.search()
  }
}

private[parser] case class Number(value: Double) extends Expr with CanEvaluate {
  def eval = Reader { input ⇒ value }
}

//private[parser] sealed abstract class Var(name: String) extends Expr
private[parser] case class StateVar(name: String) extends Expr with CanEvaluate {
  def eval = Reader { input ⇒ input(name) }
}
private[parser] case class DotStateVar(name: String) extends Expr
private[parser] case class Parameter(name: String) extends Expr

