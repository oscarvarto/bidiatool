package mx.umich.fie.dep.bidiatool
package object parser {

  import scala.collection.immutable.{ TreeMap, TreeSet }
  import scala.collection.mutable
  import scala.util.parsing.combinator.JavaTokenParsers

  private[parser]type Num = Double

  private[parser] object Num {
    def addition(lhs: Num, rhs: Num) = lhs + rhs
    def subtraction(lhs: Num, rhs: Num) = lhs - rhs
    def product(lhs: Num, rhs: Num) = lhs * rhs
    def division(lhs: Num, rhs: Num) = lhs / rhs
    def pow(lhs: Num, rhs: Num) = math.pow(lhs, rhs)
  }

  type TableVals = Map[String, Num]

  // TreeSet is used to preserve a lexicographic order with Strings
  private[parser] case class AST(equations: List[Equation] = List.empty,
                                 namesVariables: TreeSet[String] = TreeSet.empty,
                                 constants: TableVals = Map.empty,
                                 namesParams: TreeSet[String] = TreeSet.empty,
                                 norms: List[NormDefinition] = List.empty,
                                 namesVariablesInNorm: TreeSet[String] = TreeSet.empty)

  private[parser] case class MutableAST(
      equations: mutable.ListBuffer[Equation] = mutable.ListBuffer.empty,
      namesVariables: mutable.Set[String] = mutable.Set.empty,
      constants: mutable.Map[String, Double] = mutable.Map.empty,
      namesParams: mutable.Set[String] = mutable.Set.empty,
      norms: mutable.ListBuffer[NormDefinition] = mutable.ListBuffer.empty,
      namesVariablesInNorm: mutable.Set[String] = mutable.Set.empty) {

    def toImmutable = AST(
      equations = this.equations.toList,
      namesVariables = TreeSet.empty[String] ++ this.namesVariables,
      constants = Map.empty ++ this.constants,
      namesParams = TreeSet.empty[String] ++ this.namesParams,
      norms = this.norms.toList,
      namesVariablesInNorm = TreeSet.empty[String] ++ this.namesVariablesInNorm)
  }

  private[parser] implicit def exprToCanEvaluate(e: Expr) = e.asInstanceOf[CanEvaluate]
}
