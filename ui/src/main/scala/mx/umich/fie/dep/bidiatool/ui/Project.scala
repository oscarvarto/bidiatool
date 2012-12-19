package mx.umich.fie.dep.bidiatool.ui

import collection.mutable.ListBuffer
import mx.umich.fie.dep.bidiatool.parser.DynamicalSystem

class Project(val name: String) {
  var maybeDynamicalSystem: Option[DynamicalSystem] = None
  var diagrams: ListBuffer[BifurcationDiagram] = ListBuffer.empty
}
