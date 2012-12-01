package mx.umich.fie.dep.bidiatool.stability

import breeze.linalg._
import scala.math.pow

object dynamicProg {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(163); 
	val previousCol = DenseVector(362018.0, 327319.0);System.out.println("""previousCol  : breeze.linalg.DenseVector[Double] = """ + $show(previousCol ));$skip(40); 
	
	val fn = {(x: Double) => 3*pow(x,7)};System.out.println("""fn  : Double => Double = """ + $show(fn ));$skip(15); 
	val con = 1.4;System.out.println("""con  : Double = """ + $show(con ));$skip(21); 
	val h = 1.0/con/con;System.out.println("""h  : Double = """ + $show(h ));$skip(14); 
	val x0 = 5.0;System.out.println("""x0  : Double = """ + $show(x0 ));$skip(42); 
	
	val col = DenseVector.zeros[Double](3);System.out.println("""col  : breeze.linalg.DenseVector[Double] = """ + $show(col ));$skip(35); 
	val num = fn(x0 + h) - fn(x0 - h);System.out.println("""num  : Double = """ + $show(num ));$skip(19); 
	val den = 2.0 * h;System.out.println("""den  : Double = """ + $show(den ));$skip(18); 
	col(0) = num/den;$skip(5); val res$0 = 
	col;System.out.println("""res0: breeze.linalg.DenseVector[Double] = """ + $show(res$0));$skip(265); 
	def nextEstimate(pColElem: Double, colElem: Double, fac: Double, i: Int): Double = {
		val next = (colElem*fac - pColElem)/(fac - 1.0)
		col(i) = next
		if (i == col.length - 1) {
			next
		} else {
			nextEstimate(previousCol(i), col(i), fac * con, i + 1)
		}
	};System.out.println("""nextEstimate: (pColElem: Double, colElem: Double, fac: Double, i: Int)Double""");$skip(53); val res$1 = 
  nextEstimate(previousCol(0), col(0), con * con, 1);System.out.println("""res1: Double = """ + $show(res$1));$skip(7); val res$2 = 
	
	col;System.out.println("""res2: breeze.linalg.DenseVector[Double] = """ + $show(res$2))}
}