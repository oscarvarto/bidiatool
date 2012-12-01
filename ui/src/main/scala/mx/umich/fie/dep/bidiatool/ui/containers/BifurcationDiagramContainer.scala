package mx.umich.fie.dep.bidiatool.ui.containers

import scala.swing.FlowPanel

import javax.swing.BorderFactory
import mx.umich.fie.dep.bidiatool.ui.Dimensions

object BifurcationDiagramContainer extends FlowPanel {
  border = BorderFactory.createTitledBorder("Diagram")

  preferredSize = Dimensions.bifDiagramContainerPrefSize
}
