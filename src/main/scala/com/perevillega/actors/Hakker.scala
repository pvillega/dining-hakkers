package com.perevillega.actors

import akka.actor._
import com.perevillega.messages.Messages._
import scala.concurrent.duration._
import scala.util.Random

/**
 * Represents a dinning hakker
 */
class Hakker(name: String, rightFork: ActorRef, leftFork: ActorRef) extends Actor with ActorLogging {
  import context._

  // hakker thinking and wants a fork
  def receive = {
    case Start =>
      log.info(s"$name starting")
      thinkAndWait(receive)

    case TryToEat =>
      log.info(s"$name trying to grab right fork first")
      rightFork ! PickFork(name)
      become(gettingRightFork)

    case ReceiveTimeout =>
      self ! TryToEat
  }

  def gettingRightFork: Receive = {
    case ForkObtained(number) =>
      log.info(s"$name got fork $number")
      // we got right fork, try to get left and start right now (we send timeout to start action)
      become(gettingLeftFork)
      self ! ReceiveTimeout

    case ForkUnavailable(number) =>
      log.info(s"$name could not get fork $number")
      thinkAndWait(receive)
  }

  def gettingLeftFork: Receive = {
    case ForkObtained(number) =>
      log.info(s"$name got fork $number. Eating!")
      //we have both forks, eat!
      context.setReceiveTimeout(2 seconds)
      become(eating)

    case ForkUnavailable(number) =>
      log.info(s"$name could not get fork $number")
      thinkAndWait(gettingLeftFork)

    case ReceiveTimeout =>
      log.info(s"$name trying to grab left fork")
      leftFork ! PickFork(name)
      become(gettingLeftFork)
  }

  // eating, when we get timeout we have been eating for a while so we stop
  def eating: Receive = {
    case ReceiveTimeout =>
      log.info(s"$name is done eating")
      leftFork ! DropFork(name)
      rightFork ! DropFork(name)
      thinkAndWait(receive)
  }

  // sets the hakker in thinking mode
  def thinkAndWait(newState: Actor.Receive) = {
    val some = Random.nextInt(3000)
    log.info(s"$name is thinking for $some time")
    context.setReceiveTimeout(some milliseconds)
    become(newState)
  }
}

object Hakker {
  def props(name: String, rightFork: ActorRef, leftFork: ActorRef) = Props(new Hakker(name, rightFork, leftFork))
}
