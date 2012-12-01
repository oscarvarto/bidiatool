package mx.umich.fie.dep.bidiatool.ui

class ParameterInformation (
  val name: String,
  val from: Double,
  val to: Double,
  val step: Double) {
  require(from <= to, "It should be obvious that from <= to")
  def range: String = "R(" + from + ":" + to + ")"
}