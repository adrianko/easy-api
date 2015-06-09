package main.core

import java.io.IOException

import com.sun.net.httpserver.HttpExchange
import spray.json._
import main.core.EasyAPIProtocol._
import main.Server

object Response {
  
  private def send(t: HttpExchange, response: String, code: Int) {
    Server.logger.info(t.getRequestMethod + ": " + t.getRequestURI.toString)
    val responseBytes = response.getBytes
    
    try {
      t.getResponseHeaders.add("Content-Type", "application/json")
      t.sendResponseHeaders(code, responseBytes.length)
      t.getResponseBody.write(responseBytes)
      t.getResponseBody.close()
    } catch {
      case e: IOException => e.printStackTrace()
    }
  }

  def apply(t: HttpExchange, json: Map[String, Any]): Unit = {
    println(json) //debug
    val jsonString = json.toJson.toString()
    println(jsonString) //debug
    send(t, jsonString, 200)
  }
  
}
