package main

import java.net.{InetAddress, InetSocketAddress}
import java.util.logging.Logger

import com.sun.net.httpserver.HttpServer
import main.core.Handler

object Server extends App {
  
  val nic: String = "0.0.0.0"
  val port: Int = 9091
  val path = "/"
  
  private val server: HttpServer = HttpServer.create(new InetSocketAddress(InetAddress.getByName(nic), port), 0)
  
  val logger: Logger = Logger.getLogger(this.getClass.toString)
  
  logger.info("Starting server on " + nic + ":" + port)
  server.setExecutor(null)
  server.createContext("/", Handler)
  server.start()
  
  def stop(): Unit = server.stop(0)
  
}