package main.core

import scala.reflect.runtime.{universe => u}
import com.sun.net.httpserver.{HttpExchange, HttpHandler}
import main.Server
import main.app.Routes

object APIResponse {

  private var success: Int = 0
  private var request: String = null
  private var response: Any = null
  private var httpExchange: HttpExchange = null

  def create(req: String, he: HttpExchange) {
    request = req
    httpExchange = he
    response = null
  }

  def successful():Unit = {
    success = 1
  }

  def fail(): Unit = {
    success = 0
  }

  def addResponse(r: Any) {
    response = r
  }

  def send(): Unit = {
    val response: collection.mutable.Map[String, Any] = collection.mutable.Map[String, Any]()
    response.put("request", request)
    response.put("success", success)
    response.put("response", this.response)
    Response(httpExchange, response.toMap)
  }

  def getURL: String = {
    request
  }

  def getRequest: HttpExchange = {
    httpExchange
  }
}

object Handler extends HttpHandler {

  val response = APIResponse

  def parse(): Unit = {
    val request = response.getURL.replaceFirst(Server.path, "").split("\\?")(0).split("/").toList
    println(request)

    if (request.nonEmpty) {
      val routes: List[String] = u.typeOf[Routes.type].decls.filter(_.isModule).map(_.toString.split(" ")(1)).toList
    }
  }
  
  override def handle(httpExchange: HttpExchange): Unit = {
    response.create(httpExchange.getRequestURI.toString, httpExchange)
    parse()
    response.send()
  }

}
