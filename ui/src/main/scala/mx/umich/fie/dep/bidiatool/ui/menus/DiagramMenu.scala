package mx.umich.fie.dep.bidiatool.ui.menus

import scala.swing.Menu
import _root_.mx.umich.fie.dep.bidiatool.ui.actions.UIActions.diagramAction
import scala.swing.MenuItem
import scala.swing.event.Key

object DiagramMenu extends Menu(title0 = "Diagram") {
  val newDiagram = new MenuItem(diagramAction)
  newDiagram.mnemonic = Key.D

  contents += newDiagram
}
