package mx.umich.fie.dep.bidiatool.plot

import breeze.linalg._
import java.awt.Dimension
import java.awt.{ BorderLayout ⇒ JBorderLayout, Component ⇒ awtComponent }
import javax.swing.JPanel
import org.jzy3d.chart.Chart
import org.jzy3d.chart.controllers.mouse.camera.CameraMouseController
import org.jzy3d.colors.Color
import org.jzy3d.global.Settings
import org.jzy3d.maths.Coord3d
import org.jzy3d.plot3d.primitives.{ AbstractDrawable, Sphere }
import org.jzy3d.plot3d.rendering.view.modes.ViewBoundMode
import scala.swing.{ BoxPanel, Component, Orientation, Container, Panel }

object PlotFixedPoints {

  def plot2D(sols: Option[List[DenseVector[Double]]]) = sols match {
    case Some(listSols) ⇒ {
      import breeze.plot._
      import breeze.plot.DomainFunction._
      val m = listSols(0).length
      val n = listSols.length
      val mat = new DenseMatrix[Double](n, m)
      for (i ← 0 until n) {
        mat(i, ::) := listSols(i).t
      }
      val x = mat(::, 0)
      println(x)
      val y = mat(::, 1)
      println(y)
      val f = Figure()
      val p = f.subplot(0)
      p += plot(x, y, '.')
    }
    case None ⇒
  }

  def plot3D(sols: Option[List[DenseVector[Double]]]): Component = sols match {
    case Some(listSols) ⇒ {
      val positions = listSols.map { sol ⇒
        val x = sol(0)
        val y = sol(1)
        val z = sol(2)
        val coord = new Coord3d(x, y, z)
        coord
      }
      val fixedPoints = positions.map { pos ⇒
        val radius = 0.01f
        val slices = 8
        val color = Color.BLUE
        val s = new Sphere(pos, radius, slices, color)
        s.setWireframeColor(Color.BLACK)
        s.setWireframeDisplayed(true)
        s.setWireframeWidth(1)
        s.setFaceDisplayed(true)
        s
      }
      Settings.getInstance.setHardwareAccelerated(true)
      chart = new Chart()
      import scala.collection.JavaConversions._
      val fp: java.util.List[AbstractDrawable] = fixedPoints
      chart.getScene().add(fp)
      chart.getView().setBoundMode(ViewBoundMode.AUTO_FIT)
      chart.addController(new CameraMouseController())

      val awtComp = chart.getCanvas().asInstanceOf[awtComponent]
      val panel = new AWTtoSwingComponent(awtComp)
      panel
    }
    case None ⇒ new BoxPanel(Orientation.Vertical)
  }

  class AWTtoSwingComponent(private val c: awtComponent) extends Component with Container.Wrapper {
    override lazy val peer = new JPanel(new JBorderLayout()) with SuperMixin {
      c.setMinimumSize(new Dimension(0, 0))
      add(c, JBorderLayout.CENTER)
    }
  }

  var chart: Chart = _
}
