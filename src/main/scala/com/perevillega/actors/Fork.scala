package com.perevillega.actors

import akka.actor.{Props, ActorLogging, Actor}
import com.perevillega.messages.Messages._

/**
 * Represents one of the forks shared by hakkers
 */
class Fork(number: Int) extends Actor with ActorLogging {
  import context._

  // fork is free and can be picked up
  def receive = {
    case PickFork(name) =>
      log.info(s"$name gets fork $number")
      sender ! ForkObtained(number)
      become(beingUsed)
    case DropFork(name) =>
      log.error(s"$name tries to drop fork $number which is available")
      throw new IllegalStateException(s"[$name -> $number] We can't drop a fork if we don't own it!")
  }

  // fork is being used
  def beingUsed: Receive = {
    case PickFork(name) =>
      log.info(s"$name tries to pick fork $number, but it is being used")
      sender ! ForkUnavailable(number)
    case DropFork(name) =>
      log.info(s"$name has dropped fork $number")
      become(receive)
  }
}

object Fork {
  def props(number: Int) = Props(new Fork(number))
}
