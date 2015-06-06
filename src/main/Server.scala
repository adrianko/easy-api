package main

import java.net.{InetAddress, InetSocketAddress}
import java.util.logging.Logger

import com.sun.net.httpserver.HttpServer

object Server {
  
  val nic: String = "0.0.0.0"
  val port: Int = 9090
  private val server: HttpServer = HttpServer.create(new InetSocketAddress(InetAddress.getByName(nic), port), 0)
  private val logger: Logger = Logger.getLogger(this.getClass.toString)
  
  logger.info("Starting server on " + nic + ":" + port)
  server.setExecutor(null)
  server.start()
  
  def stop(): Unit = server.stop(0)
  
  def main(args: Array[String]) = {}
  
}
