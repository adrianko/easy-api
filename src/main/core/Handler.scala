package main.core

import com.sun.net.httpserver.{HttpExchange, HttpHandler}
import main.Server

object Handler extends HttpHandler {
  
  override def handle(httpExchange: HttpExchange): Unit = {
    val request = httpExchange.getRequestURI.toString.replaceFirst(Server.path, "").split("/").toList
    
    if (request.nonEmpty) {
      Server.routes.get(request.head) 
    }
  }
  
}
