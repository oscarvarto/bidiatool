package mx.umich.fie.dep.bidiatool.stability
import scala.collection.mutable.ArrayBuffer

object Recursion1 {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(145); 
  val fn = { (x: Double) => x * x };System.out.println("""fn  : Double => Double = """ + $show(fn ));$skip(15); 
  val x0 = 5.0;System.out.println("""x0  : Double = """ + $show(x0 ));$skip(119); 

  def nextElem(pColElem: Double, colElem: Double, fac: Double): Double =
    (colElem * fac - pColElem) / (fac - 1.0);System.out.println("""nextElem: (pColElem: Double, colElem: Double, fac: Double)Double""");$skip(107); 

  /*
 	def nextCol(pCol: Array[Double], hh: Double): Array[Double] ={
 		
 	}
*/
  val a = Array(1, 2, 3);System.out.println("""a  : Array[Int] = """ + $show(a ));$skip(49); 

  val bBuf = new ArrayBuffer[Int](a.length + 1);System.out.println("""bBuf  : scala.collection.mutable.ArrayBuffer[Int] = """ + $show(bBuf ));$skip(13); val res$0 = 
  bBuf += 10;System.out.println("""res0: mx.umich.fie.dep.bidiatool.stability.Recursion1.bBuf.type = """ + $show(res$0));$skip(101); 
  for ((aElem, i) <- a.zipWithIndex) {
    val nextBElem = aElem + bBuf(i)
    bBuf += nextBElem
  };$skip(22); 
  val b = bBuf.result;System.out.println("""b  : scala.collection.mutable.ArrayBuffer[Int] = """ + $show(b ));$skip(13); 
  println(b);$skip(35); 

  val pCol = Array(1.0, 2.0, 3.0);System.out.println("""pCol  : Array[Double] = """ + $show(pCol ));$skip(54); 
  val cBuf = new ArrayBuffer[Double](pCol.length + 1);System.out.println("""cBuf  : scala.collection.mutable.ArrayBuffer[Double] = """ + $show(cBuf ));$skip(15); val res$1 = 
  cBuf += 10.0;System.out.println("""res1: mx.umich.fie.dep.bidiatool.stability.Recursion1.cBuf.type = """ + $show(res$1));$skip(16); 
 	val con = 1.4;System.out.println("""con  : Double = """ + $show(con ));$skip(25); 
 	val factor = con * con;System.out.println("""factor  : Double = """ + $show(factor ));$skip(199); 
  val (nColBuf, finalFac) = pCol.foldLeft((cBuf, factor)){(bufAndFac, n) => {
 			val (buf, fac) = bufAndFac
 			val next = (buf.last*fac - n)/(fac - 1.0)
 			buf += next
 			(buf, fac*con)
  	}
  };System.out.println("""nColBuf  : scala.collection.mutable.ArrayBuffer[Double] = """ + $show(nColBuf ));System.out.println("""finalFac  : Double = """ + $show(finalFac ))}
}