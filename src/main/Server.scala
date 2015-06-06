package main

import java.net.{InetAddress, InetSocketAddress}
import java.util.logging.Logger

import com.sun.net.httpserver.{HttpHandler, HttpServer}
import main.app.Routes
import main.core.Handler

object Server {
  
  val nic: String = "0.0.0.0"
  val port: Int = 9090
  val path = "/"
  
  private val server: HttpServer = HttpServer.create(new InetSocketAddress(InetAddress.getByName(nic), port), 0)
  
  val logger: Logger = Logger.getLogger(this.getClass.toString)
  val routes: Map[String, HttpHandler] = Routes.getClass.getDeclaredClasses.map(m => m.getSimpleName -> 
    m.asInstanceOf[HttpHandler]).toMap
  
  logger.info("Starting server on " + nic + ":" + port)
  server.setExecutor(null)
  server.createContext("/", Handler)
  server.start()
  
  def stop(): Unit = server.stop(0)
  
  def main(args: Array[String]) = {}
  
}