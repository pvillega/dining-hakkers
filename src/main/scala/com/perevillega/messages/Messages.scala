package com.perevillega.messages

/**
 * Messages used by actors
 */
object Messages {

  case class PickFork(name: String)
  case class ForkObtained(number: Int)
  case class ForkUnavailable(number: Int)
  case class DropFork(name: String)

  case class Start()
  case class TryToEat()
}
