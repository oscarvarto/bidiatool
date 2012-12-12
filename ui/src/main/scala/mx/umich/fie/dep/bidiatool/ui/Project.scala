package mx.umich.fie.dep.bidiatool.ui

import collection.mutable.ListBuffer

class Project(val name: String) {
  var diagrams: ListBuffer[BifurcationDiagram] = new ListBuffer
}
