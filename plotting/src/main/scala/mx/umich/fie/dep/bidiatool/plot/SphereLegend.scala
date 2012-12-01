package mx.umich.fie.dep.bidiatool.plot
import org.jzy3d.maths.Coord3d
import org.jzy3d.plot3d.primitives.Sphere
import org.jzy3d.colors.Color

object SphereLegend extends App {
  val pos = new Coord3d(1.0, 2.0, 3.0)
  val radius = 0.1f
  val slices = 8
  val color = Color.BLUE

  val s = new Sphere(pos, radius, slices, color)
  s.setWireframeColor(Color.BLACK)
  s.setWireframeDisplayed(true)
  s.setWireframeWidth(1)
  s.setFaceDisplayed(true)

}