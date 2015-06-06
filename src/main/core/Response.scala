package main.core

import java.io.IOException

import com.sun.net.httpserver.HttpExchange
import main.Server
import org.json.JSONObject

object Response {
  
  private def send(t: HttpExchange, response: Array[Byte], code: Int, contentType: String) {
    Server.logger.info(t.getRequestMethod + ": " + t.getRequestURI.toString)
    
    try {
      if (contentType != null) {
        t.getResponseHeaders.add("Content-Type", contentType)
      }
      t.sendResponseHeaders(code, response.length)
      t.getResponseBody.write(response)
      t.getResponseBody.close()
    }
    catch {
      case e: IOException => e.printStackTrace()
    }
  }

  def send(t: HttpExchange, json: Map[String, AnyRef]) {
    send(t, new JSONObject(json).toString.toArray[Byte], 200, "application/json")
  }
  
}
