package mx.umich.fie.dep.bidiatool.ui

import java.awt.GraphicsEnvironment

import scala.swing.{ Component, Dimension, MainFrame, MenuBar, SimpleSwingApplication }
import scala.swing.BorderPanel
import scala.swing.BorderPanel.Position.{ Center, East, North, West }

import Dimensions.b
import containers.{ BifurcationDiagramContainer, InformationContainer, ModelsContainer }
import menus.{ DiagramMenu, FileMenu }
import toolbar.ToolBar
import mx.umich.fie.dep.bidiatool.parser.DynamicalSystem
import scala.collection.mutable.ListBuffer

object Dimensions {
  val b = GraphicsEnvironment.getLocalGraphicsEnvironment.getMaximumWindowBounds
  val centerWidth = b.width * 5 / 8
  val bifDiagramContainerPrefSize = new Dimension(centerWidth, 0)
  val modelContainerPrefSize = new Dimension(centerWidth / 5, 0)
  val informationContainerPrefSize = new Dimension(centerWidth * 2 / 5, 0)
}

object UserInterface extends SimpleSwingApplication {
  var maybeCurrentProject: Option[Project] = None

  var paramsInfo: ListBuffer[ParameterInformation] = new ListBuffer

  // This is the main frame of the application
  val frame = new MainFrame {
    title = "BiDiaTool"

    menuBar = new MenuBar {
      contents.append(FileMenu, DiagramMenu)
    }

    contents = new BorderPanel {
      layout(Component.wrap(ToolBar)) = North
      layout(ModelsContainer) = West // For Models
      layout(BifurcationDiagramContainer) = Center // For Bifurcation Diagrams
      layout(InformationContainer) = East // For information
    }

    import Dimensions.b
    size = new Dimension(b.width, b.height)
    centerOnScreen()
  }

  def top = frame
}
