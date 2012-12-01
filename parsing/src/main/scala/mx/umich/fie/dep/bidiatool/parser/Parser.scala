package mx.umich.fie.dep.bidiatool.parser

import AST.Add
import AST.AlgebraicVar
import AST.App
import AST.ConstantAssign
import AST.Div
import AST.Equation
import AST.DynamicalSystem
import AST.Expr
import AST.Mul
import AST.Neg
import AST.Number
import AST.ParamDecl
import AST.Pow
import AST.StateVar
import AST.Sub
import scala.util.parsing.combinator.ImplicitConversions
import scala.util.parsing.combinator.JavaTokenParsers

object Parser extends JavaTokenParsers with ImplicitConversions {
  def parse(s: String) = parseAll(system, s)

  import AST._
  type PE = Parser[Expr]

  def system = rep(sentence) ^^ DynamicalSystem
  def sentence = equation | constantAssignment | paramDeclaration | normDefinition
  def equation = stateVar ~ ("=" ~> expr) ^^ Equation
  def constantAssignment = algebraicVar ~ ("=" ~> floatingPointNumber) ^^ {
    case v ~ n ⇒ ConstantAssign(v, Number(n.toDouble))
  }
  def paramDeclaration = algebraicVar <~ ("=" ~ "Param") ^^ ParamDecl
  def variable = stateVar | algebraicVar
  def stateVar = ident <~ "'" ^^ { case id ⇒ StateVar("d" + id) }
  def algebraicVar = ident <~ not("'") ^^ AlgebraicVar
  // val cons = { (x: String, y: List[String]) => x :: y }
  // val fp = floatingPointNumber
  // val x = fp ~ (fp ~ fp) ^^ { case a ~ (b ~ c) => cons }
  def expr: PE = chainl1(prod, "+" ^^^ Add | "-" ^^^ Sub)
  def prod = chainl1(signExp, "*" ^^^ Mul | "/" ^^^ Div)
  def signExp = opt("-") ~ power ^^ { case Some(_) ~ e ⇒ Neg(e); case _ ~ e ⇒ e }
  def power = chainr1(
    appExpr,
    "^" ^^^ Pow,
    Pow,
    Number(1.0)) // chainr1 for right associativity
  def appExpr = (
    fun ~ ("[" ~> expr <~ "]") ^^ { case f ~ arg ⇒ App(f, arg) }
    | simpleExpr)

  def fun = "Cos" | "Sin"

  def simpleExpr = (algebraicVar
    | floatingPointNumber ^^ { s ⇒ Number(s.toDouble) } // throws NumberFormatException
    | "(" ~> expr <~ ")")

  def normDefinition = ("Norm1" | "Norm2") ~ ("=" ~> expr) ^^ NormDefinition
}
