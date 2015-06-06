package main.core

import com.sun.net.httpserver.{HttpExchange, HttpHandler}
import main.Server

object Handler extends HttpHandler {
  
  override def handle(httpExchange: HttpExchange): Unit = {
    val url: String = httpExchange.getRequestURI.toString
    val request = url.replaceFirst(Server.path, "").split("\\?")(0).split("/").toList
    
    if (request.nonEmpty) {
      
    }
  }
  
}
