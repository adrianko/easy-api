package main.core

import java.lang.reflect.Method

import scala.reflect.runtime.universe
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

  def getURL: String = request

  def getRequest: HttpExchange = httpExchange

}

object Handler extends HttpHandler {

  val routes: List[String] = universe.typeOf[Routes.type].decls.filter(_.isModule).map(_.toString.split(" ")(1)).toList
  val response = APIResponse

  def parse(): Unit = {
    println("-------------------------") //debug
    val request: List[String] = response.getURL.replaceFirst(Server.path, "").split("\\?")(0).split("/").toList
    println("-request") //debug
    println(request) //debug
    if (request.nonEmpty) {
      val route: List[String] = routes.filter(r => r.toLowerCase.equals(request.head))
      println("-route") //debug
      println(route) //debug
      if (route.nonEmpty) {
        val routeObj: Endpoint = Class.forName("main.app.Routes$" + route.head + "$").newInstance.asInstanceOf[Endpoint]
        println("-routeObj") //debug
        println(routeObj) //debug
        if (request.size > 1) {
          val subRoute: Option[Method] = routeObj.getClass.getDeclaredMethods.find(m => m.getName.toLowerCase.equals(
            request(1).toLowerCase))
          println("-subRoute") //debug
          println(subRoute) //debug
          val method: Method = subRoute.orNull
          val args: Array[Any] = request.slice(2, request.size).toArray
          println("-method") //debug
          println(method) //debug
          println("-args") //debug
          println(args) //debug
          if (args.length == method.getParameterCount) {
            val resp = method.invoke(routeObj, args)
            println("-resp") //debug
            println(resp) //debug
            response.addResponse(resp)
          }
        }
      }
      
    }
  }
  
  override def handle(httpExchange: HttpExchange): Unit = {
    response.create(httpExchange.getRequestURI.toString, httpExchange)
    parse()
    response.send()
  }

}
