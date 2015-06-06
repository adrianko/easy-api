package main.core

import java.lang.reflect.Method

import com.sun.net.httpserver.{HttpExchange, HttpHandler}
import main.Server
import main.app.Routes

object Handler extends HttpHandler {
  
  override def handle(httpExchange: HttpExchange): Unit = {
    val url: String = httpExchange.getRequestURI.toString
    val request = url.replaceFirst(Server.path, "").split("\\?")(0).split("/").toList

    if (request.nonEmpty) {
      val route: Option[Class[_]] = Routes.getClass.getDeclaredClasses.find(r => r.getSimpleName.toLowerCase.equals(
          request(0).toLowerCase))

      if (route.nonEmpty) {
        val rp1: Endpoint = route.get.newInstance().asInstanceOf[Endpoint]
        rp1.clearParams()

        if (request.size > 1) {
          val subRoute: Option[Method] = rp1.getClass.getDeclaredMethods.find(m => m.getName.toLowerCase.equals(
              request(1).toLowerCase))
          
        }

      }

    }
  }

}
