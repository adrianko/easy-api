package main.core

import java.io.IOException

import com.sun.net.httpserver.HttpExchange
import main.Server
import org.json.JSONObject

object Response {
  
  private def send(t: HttpExchange, response: String, code: Int, contentType: String) {
    Server.logger.info(t.getRequestMethod + ": " + t.getRequestURI.toString)
    val responseBytes = response.getBytes
    
    try {
      if (contentType != null) {
        t.getResponseHeaders.add("Content-Type", contentType)
      }
      t.sendResponseHeaders(code, responseBytes.length)
      t.getResponseBody.write(responseBytes)
      t.getResponseBody.close()
    }
    catch {
      case e: IOException => e.printStackTrace()
    }
  }

  def send(t: HttpExchange, json: Map[String, Any]) {
    send(t, new JSONObject(json).toString, 200, "application/json")
  }
  
}
