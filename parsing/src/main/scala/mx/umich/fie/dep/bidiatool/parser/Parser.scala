package mx.umich.fie.dep.bidiatool.parser

import scala.util.parsing.combinator.{ ImplicitConversions, JavaTokenParsers }

//import AST.{Add, AlgebraicVar, App, ConstantAssign, Div, DynamicalSystem, Equation, Expr, Mul, Neg, NormDefinition, Number, ParamDecl, Pow, StateVar, Sub}

object Parser extends JavaTokenParsers with ImplicitConversions {
  def parse(s: String) = parseAll(system, s)

  def system = rep(sentence) ^^ DynamicalSystem
  def sentence = equation | paramDeclaration | constantDefinition | normDefinition
  def equation = dotStateVar ~ ("=" ~> expr) ^^ Equation
  def paramDeclaration = "param" ~> parameter ^^ ParamDeclaration //~ rep("," ~> parameter ^^ ParamDeclaration)
  def constantDefinition = stateVar ~ ("=" ~> floatingPointNumber) ^^ {
    case v ~ n ⇒ ConstantDefinition(v, Number(n.toDouble))
  }
  def normDefinition = ("Norm1" | "Norm2") ~ ("=" ~> expr) ^^ NormDefinition
  //def variable: Parser[Expr] = dotStateVar | stateVar
  def dotStateVar = ident <~ "'" ^^ { case id ⇒ DotStateVar("d" + id) }
  def stateVar = ident <~ not("'") ^^ StateVar
  def parameter = ident <~ not("'") ^^ Parameter
  def expr: Parser[Expr] = chainl1(prod, "+" ^^^ Add | "-" ^^^ Sub)
  def prod = chainl1(signExp, "*" ^^^ Mul | "/" ^^^ Div)
  def signExp = opt("-") ~ power ^^ { case Some(_) ~ e ⇒ Neg(e); case _ ~ e ⇒ e }
  def power = chainr1(
    appExpr,
    "^" ^^^ Pow,
    Pow,
    Number(1.0)) // chainr1 for right associativity
  def appExpr = (
    ("Cos" | "Sin")
    ~ ("[" ~> expr <~ "]") ^^ { case f ~ arg ⇒ App(f, arg) }
    | simpleExpr)

  //def fun: Parser[Expr] = literal("Cos") | literal("Sin")

  def simpleExpr = (stateVar
    | floatingPointNumber ^^ { s ⇒ Number(s.toDouble) } // throws NumberFormatException
    | "(" ~> expr <~ ")")

}
