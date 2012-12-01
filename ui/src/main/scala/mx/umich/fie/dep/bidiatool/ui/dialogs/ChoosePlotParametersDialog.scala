package mx.umich.fie.dep.bidiatool.ui.dialogs

import javax.swing.JComboBox
import java.util.{ Vector ⇒ JVector }
import java.awt.Dimension

import scala.swing.{ BoxPanel, Button, Component, Frame, GridBagPanel, Label, Orientation, TextField }
import scala.swing.GridBagPanel._
import scala.swing.event.ButtonClicked
import scala.swing.Dialog._

import mx.umich.fie.dep.bidiatool.ui.UserInterface
import mx.umich.fie.dep.bidiatool.ui.ParameterInformation

class ChoosePlotParametersDialog extends Frame { fr ⇒
  //var params: List[String] = L
  val instructions = new Label("Choose one parameter, its range, and a step")
  import collection.JavaConverters._

  val options = new JVector(List[String]().asJava)
  val combo = new JComboBox(options)

  val rangeFrom = new TextField("", 10)
  val rangeTo = new TextField("", 10)
  val stepTextField = new TextField("", 20)

  lazy val paramInfo = new GridBagPanel {
    val c = new Constraints

    c.fill = Fill.Horizontal
    c.weightx = 0.0
    c.gridx = 0
    c.gridy = 0
    layout(new Label("From")) = c

    c.fill = Fill.Horizontal
    c.weightx = 0.35
    c.gridx = 1
    c.gridy = 0
    layout(rangeFrom) = c

    c.fill = Fill.Horizontal
    c.weightx = 0.1
    c.gridx = 2
    c.gridy = 0
    layout(new Label("To")) = c

    c.fill = Fill.Horizontal
    c.weightx = 0.35
    c.gridx = 3
    c.gridy = 0
    layout(rangeTo) = c

    c.fill = Fill.Horizontal
    //c.anchor = Anchor.PageEnd
    c.weightx = 0.0
    c.gridx = 0
    c.gridwidth = 1
    c.gridy = 1
    layout(new Label("Step")) = c

    c.fill = Fill.Horizontal
    c.weightx = 1.0
    c.gridx = 1
    c.gridwidth = 3
    c.gridy = 1
    layout(stepTextField) = c
  }

  val applyButton = new Button("Apply") {
    // validate input!!!
    reactions += {
      case ButtonClicked(_) ⇒
      	val name = combo.getSelectedItem().asInstanceOf[String]
        try {
          val from = rangeFrom.text.toDouble
          val to = rangeTo.text.toDouble
          val step = stepTextField.text.toDouble
          val parInfo = new ParameterInformation(name, from, to, step)
          println(parInfo)
          UserInterface.CurrentModel.paramsInfo += parInfo
        } catch {
          case ex: NumberFormatException => showMessage(paramInfo, "From, To and Step must be numbers")
        }
    }
  }

  val nextButton = new Button("Next") {
    reactions += {
      case ButtonClicked(_) ⇒
        fr.dispose()
    }
  }

  val selectionContainer = new BoxPanel(Orientation.Vertical) {
    contents += instructions
    contents += Component.wrap(combo)
    contents += paramInfo
  }
  contents = selectionContainer

  // Crear un frame que contenga
  // una forma de escoger a lo más dos parámetros
  // Si puso dos normas sólo puede escoger un parámetro
  // Si puso una norma puede escoger hasta dos parámetros

  //    val onlyOneParameter = if (AST.norms.length == 2) true else false
  //    if (onlyOneParameter || AST.params.length == 1) {
  //      val fr = chooseOneParameterFrame()
  //      println("Chosen: " + UserInterface.holo)
  //    }
  //size = new Dimension(400, 600)
}