package mx.umich.fie.dep.bidiatool.ui.containers

import scala.swing.ScrollPane

import javax.swing.BorderFactory
import mx.umich.fie.dep.bidiatool.ui.Dimensions

object ModelsContainer extends ScrollPane {
  border = BorderFactory.createTitledBorder("Models")

  preferredSize = Dimensions.modelContainerPrefSize
}
