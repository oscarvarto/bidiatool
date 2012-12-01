package mx.umich.fie.dep.bidiatool.ui.dialogs

import java.awt.Dimension

import scala.swing.{ Frame, Label, SplitPane, Swing, TextArea }
import scala.swing.BorderPanel
import scala.swing.BorderPanel.Position.{ Center, North, South }
import scala.swing.Button
import scala.swing.Dialog.{ Message, showMessage }
import scala.swing.event.ButtonClicked

import mx.umich.fie.dep.bidiatool.parser.AST
import mx.umich.fie.dep.bidiatool.parser.AST.DynamicalSystem
import mx.umich.fie.dep.bidiatool.parser.Parser
import mx.umich.fie.dep.bidiatool.ui.Dimensions.b
import mx.umich.fie.dep.bidiatool.ui.UserInterface
import collection.JavaConverters._

object NewModelDialog extends Frame { fr ⇒
  val instructions = new Label("Type your dynamical system, please.")

  val dynSys = new TextArea(20, 50)
  val log = new Label("(Developer, add logging here!)")
  val dsyAndLogContainer = new SplitPane(
    swing.Orientation.Horizontal,
    dynSys,
    log)
  val choose1 = new ChoosePlotParametersDialog()

  val nextDialogButton = new Button("Read dynSys") {
    reactions += {
      case ButtonClicked(_) ⇒
        val code = dynSys.text
        val res = Parser.parse(code)
        val maybeDSy: Option[DynamicalSystem] = if (res.successful) {
          val eqSystem = res.get
          Some(eqSystem)
        } else
          None

        maybeDSy match {
          case Some(dsy) ⇒
            val params = AST.params
            choose1.options.addAll(params.asJava)
            choose1.open()
            fr.dispose()
          case None ⇒ showMessage(
            dsyAndLogContainer, code, "Check your syntax, please.", Message.Info, Swing.EmptyIcon)
        }
    }
  }

  contents = new BorderPanel {
    import BorderPanel.Position._

    layout(instructions) = North
    layout(dsyAndLogContainer) = Center
    layout(nextDialogButton) = South
  }

  val choose2 = new ChoosePlotParametersDialog()

  listenTo(choose1.nextButton)
  reactions += {
    case ButtonClicked(choose1.nextButton) ⇒
      val parInfo = UserInterface.CurrentModel.paramsInfo
      val maybeTwoParameters = if (AST.params.length >= 1 && AST.norms.length < 2) true else false 
      if (parInfo.length == 1 && maybeTwoParameters) {
        val alreadyChosen = parInfo(0).name
        val params = AST.params.filterNot(alreadyChosen contains)
        choose2.options.addAll(params.asJava)
        choose2.open()
      }
  }
  size = new Dimension(b.width / 2, b.height * 5 / 6)
  dsyAndLogContainer.dividerLocation = b.height * 2 / 3
}