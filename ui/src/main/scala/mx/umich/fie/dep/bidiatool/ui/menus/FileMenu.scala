package mx.umich.fie.dep.bidiatool.ui.menus

import scala.swing.Action
import scala.swing.FileChooser
import scala.swing.MainFrame
import scala.swing.Menu
import scala.swing.MenuItem
import scala.swing.Component

import scala.swing.event.Key
import javax.swing.KeyStroke._
import java.awt.event.KeyEvent._
import java.awt.event.InputEvent._
import java.awt.Toolkit

import _root_.mx.umich.fie.dep.bidiatool.ui.actions.UIActions._

object FileMenu extends Menu(title0 = "File") {

  //////////
  // + FileMenu
  //   + newModelItem
  //   + openModelItem
  //   + saveModelItem
  //   + saveModelAsItem
  //   + exitItem
  ////////// 

  val newModelItem = new MenuItem(newModelAction)
  newModelItem.mnemonic = Key.N

  val openModelItem = new MenuItem(openModelAction)
  openModelItem.mnemonic = Key.O

  val saveModelItem = new MenuItem(saveModelAction)
  saveModelItem.mnemonic = Key.S

  val saveModelAsItem = new MenuItem(saveModelAsAction)
  saveModelAsItem.mnemonic = Key.A

  val exitItem = new MenuItem(exitAction)
  exitItem.mnemonic = Key.Q

  contents.append(
    newModelItem,
    openModelItem,
    saveModelItem,
    saveModelAsItem,
    exitItem)

  mnemonic = Key.F
}
