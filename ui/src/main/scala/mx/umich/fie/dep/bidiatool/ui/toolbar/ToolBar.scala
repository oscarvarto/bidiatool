package mx.umich.fie.dep.bidiatool.ui.toolbar

import javax.swing.JToolBar
import scala.swing.{ Button, MainFrame, Component }
import java.awt.GridBagConstraints
import javax.swing.ImageIcon
import mx.umich.fie.dep.bidiatool.ui.actions.UIActions._
import java.awt.Dimension

object ToolBar extends JToolBar {
  //val buttonSize = new Dimension(24, 24)

  val newButton = new Button(newDiagramAction)
  newButton.text = ""
  newButton.icon = new ImageIcon("ui/src/main/resources/images/newdocMod.png")
  newButton.tooltip = "New Model..."
  //newButton.preferredSize = buttonSize

  val openButton = new Button(openModelAction)
  openButton.text = ""
  openButton.icon = new ImageIcon("ui/src/main/resources/images/fol.png")
  openButton.tooltip = "Open Model..."
  //openButton.preferredSize = buttonSize

  val saveButton = new Button(saveModelAction)
  saveButton.text = ""
  saveButton.icon = new ImageIcon("ui/src/main/resources/images/action_save.gif")
  saveButton.tooltip = "Save"
  //saveButton.preferredSize = buttonSize

  val diagramButton = new Button(diagramAction)
  diagramButton.text = ""
  diagramButton.icon = new ImageIcon("ui/src/main/resources/images/branch.png")
  diagramButton.tooltip = "New Diagram"
  //diagramButton.preferredSize = buttonSize

  val constraints = new GridBagConstraints()
  constraints.fill = GridBagConstraints.HORIZONTAL
  constraints.weightx = 1.0; constraints.weighty = 0.0
  constraints.gridx = 0; constraints.gridy = 0
  constraints.gridwidth = GridBagConstraints.REMAINDER; constraints.gridheight = 1

  setFloatable(false)
  add(newButton.peer)
  add(openButton.peer)
  add(saveButton.peer)
  add(diagramButton.peer)
}
