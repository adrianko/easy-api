package main.app

import com.sun.net.httpserver.{HttpExchange, HttpHandler}

abstract class Endpoint extends HttpHandler

object Routes {
  
  class settings extends Endpoint {
    override def handle(httpExchange: HttpExchange): Unit = {
      
    }
  }
  
}