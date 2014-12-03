The Dining Philosophers in Scala Akka
=========================

Based on http://en.wikipedia.org/wiki/Dining_philosophers_problem

Implementation just for fun.

I discovered later on this one from Typesafe itself: http://www.typesafe.com/activator/template/akka-sample-fsm-scala

Glad to see mine wasn't so different after all! The main difference is their use of

system.scheduler.scheduleOnce(duration, self, Eat)

versus mine of Timeouts. And the estates defined in the hakker actor.

Theirs is obviously more elegant, but in a pinch both work ;)


