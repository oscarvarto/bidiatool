package mx.umich.fie.dep.bidiatool.ui.containers

import swing._
import Orientation.Vertical
import javax.swing.BorderFactory
import mx.umich.fie.dep.bidiatool.ui.Dimensions

object InformationContainer extends BoxPanel(Vertical) {
  border = BorderFactory.createTitledBorder("Information")
  contents.append(EquationPanel, SyntaxPanel, ChartPanel)
  preferredSize = Dimensions.informationContainerPrefSize
}

object EquationPanel extends ScrollPane {
  border = BorderFactory.createTitledBorder("Equations")
}

object SyntaxPanel extends ScrollPane {
  border = BorderFactory.createTitledBorder("Syntax")
}

object ChartPanel extends ScrollPane {
  border = BorderFactory.createTitledBorder("Diagrams")
}
