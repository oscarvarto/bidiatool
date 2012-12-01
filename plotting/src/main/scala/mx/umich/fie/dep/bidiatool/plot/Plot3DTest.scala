package mx.umich.fie.dep.bidiatool.plot

import breeze.linalg._
import scala.swing.Panel
import swing._
import mx.umich.fie.dep.bidiatool.plot.PlotFixedPoints._
import java.awt.GraphicsEnvironment

import scalaz._, Scalaz._

object Plot3DTest extends SimpleSwingApplication {
  def top = new MainFrame { fr ⇒
    title = "Jzy3d Test"
    val liSols = List(
      DenseVector(1.0, 2.0, 3.0),
      DenseVector(0.0, 4.0, 2.0),
      DenseVector(2.5, 3.14, 2.7128)).some
    val b = GraphicsEnvironment.getLocalGraphicsEnvironment.getMaximumWindowBounds
    val centerContainerWidth = b.width * 5 / 8
    val plotPanel = plot3D(liSols)
    contents = plotPanel
    size = new Dimension(b.width, b.height)
    centerOnScreen()

    override def closeOperation() {
      if (PlotFixedPoints.chart != null) {
        PlotFixedPoints.chart.getCanvas().dispose()
      }
      fr.dispose()
    }
  }
}
