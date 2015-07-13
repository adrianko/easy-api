package main.app

import main.core.Endpoint

object Routes {

  object Settings extends Endpoint {
    def main(test: Any): String = "main method"
    def list(): List[Int] = List(1, 2, 3)
  }

  object Another extends Endpoint {
    def hello(): String = "world"
    def map(): Map[String, Int] = Map[String, Int]("abc" -> 1, "def" -> 2)
  }

}