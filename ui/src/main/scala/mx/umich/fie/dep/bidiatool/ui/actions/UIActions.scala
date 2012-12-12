package mx.umich.fie.dep.bidiatool.ui.actions

import java.awt.Toolkit
import java.awt.event.KeyEvent.{ VK_A, VK_D, VK_N, VK_O, VK_Q, VK_S }

import scala.swing.Action

import javax.swing.KeyStroke.getKeyStroke
import mx.umich.fie.dep.bidiatool.ui.UserInterface
import mx.umich.fie.dep.bidiatool.ui.dialogs.NewDiagramDialog

object UIActions {
  val shortcutKey = Toolkit.getDefaultToolkit.getMenuShortcutKeyMask()

  val newDiagramAction = new Action("New Diagram...") {
    accelerator = Some(getKeyStroke(VK_N, shortcutKey))
    def apply() = {
      //      val modelName: Option[String] =
      //        showInput(
      //          BifurcationDiagramContainer,
      //          "Introduce a name for the model",
      //          "New Model",
      //          Message.Plain,
      //          EmptyIcon,
      //          Nil,
      //          "")

      //      modelName match {
      //        case Some(name) ⇒ introduceModel()
      //        case None       ⇒
      //      }
      NewDiagramDialog.open()
    }
  }

  val openModelAction = new Action("Open Model...") {
    accelerator = Some(getKeyStroke(VK_O, shortcutKey))
    def apply() = Unit
  }

  val saveModelAction = new Action("Save") {
    accelerator = Some(getKeyStroke(VK_S, shortcutKey))
    def apply() = Unit
  }

  val saveModelAsAction = new Action("Save Model As...") {
    accelerator = Some(getKeyStroke(VK_A, shortcutKey))
    def apply() = Unit
  }

  def exitAction = new Action("Exit") {
    accelerator = Some(getKeyStroke(VK_Q, shortcutKey))
    def apply() = UserInterface.frame.closeOperation()
  }

  val diagramAction = new Action("New Diagram") {
    accelerator = Some(getKeyStroke(VK_D, shortcutKey))
    def apply() = Unit
  }
}
