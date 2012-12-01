package mx.umich.fie.dep.paramInterface

import breeze.linalg.DenseVector
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer
import scalaz._, Scalaz._

object GetSolutions {
  object Solutions {
    def unapplySeq(whole: String): Option[Seq[String]] = {
      val s1 = whole.dropWhile(_ != '[')
      val s2 = s1.slice(2, s1.length - 2)
      val s3 = s2.split("""\],\[""")
      Some(s3)
    }
  }

  object Sol {
    def unapplySeq(sol: String): Option[Seq[String]] = {
      val stringNumbers = sol.split("[ ,]+")
      Some(stringNumbers)
    }
  }

  def getSolutions(s: String): Option[List[DenseVector[Double]]] = s match {
    case Solutions(sol1, sols @ _*) ⇒ {
      val a = (sol1 +: sols).map(s ⇒ getSol(s)).toList
      a.sequence
    }
    case _ ⇒ None
  }

  def getSol(sol: String): Option[DenseVector[Double]] = sol match {
    case Sol(n, nums @ _*) ⇒ {
      val a = (n +: nums).map(_.toDouble)
      val b = DenseVector(a.toArray)
      Some(b)
    }
    case _ ⇒ None
  }

  def getFromFile(file: String): Option[List[DenseVector[Double]]] = {
    import scala.io.Source
    val linesIt = Source.fromFile(file).getLines()
    val lastLine = linesIt.foldLeft("": String)((a, b) ⇒ b)
    val liSols = getSolutions(lastLine)
    liSols
  }
}

