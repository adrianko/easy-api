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
    val request: List[String] = response.getURL.replaceFirst(Server.path, "").split("\\?")(0).split("/").toList
    
    if (request.nonEmpty) {
      val route: List[String] = routes.filter(r => r.toLowerCase.equals(request.head))
      
      if (route.nonEmpty) {
        val routeObj: Endpoint = Class.forName("main.app.Routes$" + route.head + "$").newInstance.asInstanceOf[Endpoint]
        
        if (request.size > 1) {
          val subRoute: Option[Method] = routeObj.getClass.getDeclaredMethods.find(m => m.getName.toLowerCase.equals(
            request(1).toLowerCase))
          
          val method: Method = subRoute.get
          val args: Array[Any] = request.slice(2, request.size).toArray
          
          if (args.length == method.getParameterCount) {
            response.addResponse(method.invoke(routeObj, args))
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
