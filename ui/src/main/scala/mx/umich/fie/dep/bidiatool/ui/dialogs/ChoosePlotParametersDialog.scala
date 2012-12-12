package mx.umich.fie.dep.bidiatool.ui.dialogs

import javax.swing.JComboBox
import java.util.{ Vector ⇒ JVector }
import java.awt.Dimension

import scala.swing.{ BoxPanel, Button, Component, Frame, GridBagPanel, Label, Orientation, TextField }
import scala.swing.GridBagPanel._
import scala.swing.event.ButtonClicked
import scala.swing.Dialog._

import scalaz.syntax.std.string._
import scalaz.syntax.traverse._
import scalaz.std.list._
import scalaz.ValidationNEL

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
  val step = new TextField("", 20)

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
    layout(step) = c
  }

  val applyButton = new Button("Apply") {
    // validate input!!!
    reactions += {
      case ButtonClicked(_) ⇒
        val name = combo.getSelectedItem().asInstanceOf[String]
        val maybeStringNums = List(rangeFrom.text, rangeTo.text, step.text)
        type VNELD[A] = ValidationNEL[NumberFormatException, A]
        val maybeNums = maybeStringNums.traverse[VNELD, Double](_.parseDouble.toValidationNEL)
        maybeNums.fold(
          nelNFE ⇒ showMessage(paramInfo, "From, To and Step must be numbers" + nelNFE),
          nums ⇒ UserInterface.paramsInfo += new ParameterInformation(name, nums(0), nums(1), nums(2)))
      //        try {
      //          val from = rangeFrom.text.toDouble
      //          val to = rangeTo.text.toDouble
      //          val step = stepTextField.text.toDouble
      //          val parInfo = new ParameterInformation(name, from, to, step)
      //          UserInterface.paramsInfo += parInfo
      //        } catch {
      //          case ex: NumberFormatException ⇒ showMessage(paramInfo, "From, To and Step must be numbers")
      //        }
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
    contents ++= List(paramInfo, applyButton, nextButton)
  }
  contents = selectionContainer
}
