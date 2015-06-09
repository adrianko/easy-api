package main.app

import main.core.Endpoint

object Routes {

  object Settings extends Endpoint {
    def main(test: Any): String = "main method"
  }

  object Another extends Endpoint {
    def hello(): String = "world"
  }

}