package main.app

import main.core.Endpoint

object Routes {

  object Settings extends Endpoint {
    def main(): String = "main"
  }

  object Another extends Endpoint {
    def hello(): String = "world"
  }

}