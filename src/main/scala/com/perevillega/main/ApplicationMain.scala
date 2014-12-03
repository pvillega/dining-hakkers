package com.perevillega.main

import akka.actor.{ActorRef, ActorSystem}
import com.perevillega.actors.{Fork, Hakker}
import com.perevillega.messages.Messages.Start


/**
 * Problem is stated as:
 *
 * Five silent philosophers sit at a round table with bowls of spaghetti.
 * Forks are placed between each pair of adjacent philosophers.
 *
 * Each philosopher must alternately think and eat.
 * However, a philosopher can only eat spaghetti when he has both left and right forks.
 * Each fork can be held by only one philosopher and so a philosopher can use the fork only if it's not being used by another philosopher.
 * After he finishes eating, he needs to put down both forks so they become available to others.
 * A philosopher can grab the fork on his right or the one on his left as they become available, but can't start eating before getting both of them.
 *
 * Eating is not limited by the amount of spaghetti left: assume an infinite supply.
 *
 * The problem is how to design a discipline of behavior (a concurrent algorithm) such that each philosopher won't starve;
 * i.e., can forever continue to alternate between eating and thinking assuming that any philosopher cannot know when others may want to eat or think.
 *
 */
object ApplicationMain extends App {
  val system = ActorSystem("AkkaDinner")

  val forks: IndexedSeq[ActorRef] = for (i <- 1 to 5) yield system.actorOf(Fork.props(i), s"fork-$i")
  val hakkers: List[ActorRef] = for ((name, i) <- List("A", "B", "C", "D", "E").zipWithIndex) yield system.actorOf(Hakker.props(name, forks(i), forks((i + 1) % 5)), name)

  //start
  hakkers.foreach(_ ! Start)

  // This example app will ping pong 3 times and thereafter terminate the ActorSystem -
  // see counter logic in PingActor
  //  if (counter == 3) context.system.shutdown()
  //  system.awaitTermination()
}
