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
    success = 0
  }

  def successful(): Unit = success = 1

  def addResponse(r: Any) = response = r

  def send(): Unit = Response(httpExchange, Map[String, Any]("request" -> request, "success" -> success, "response" -> 
    this.response))

  def getURL: String = request

  def getRequest: HttpExchange = httpExchange

}

object Handler extends HttpHandler {

  val routes: List[String] = universe.typeOf[Routes.type].decls.filter(_.isModule).map(_.toString.split(" ")(1)).toList
  val response = APIResponse

  def parse(): Unit = {
    val request: List[String] = response.getURL.replaceFirst(Server.path, "").split("\\?")(0).split("/").toList
    
    if (request.isEmpty) return
    
    val route: List[String] = routes.filter(r => r.toLowerCase.equals(request.head))
    
    if (route.isEmpty) return
    
    val rp1: Endpoint = Class.forName("main.app.Routes$" + route.head + "$").newInstance.asInstanceOf[Endpoint]
    
    if (request.size == 1) return
    
    val subRoute: Option[Method] = rp1.getClass.getDeclaredMethods.find(m => m.getName.toLowerCase.equals(request(1)
      .toLowerCase))
    
    if (subRoute.isEmpty) return
    
    val method: Method = subRoute.orNull
    val args: Array[Any] = request.slice(2, request.size).toArray

    if (args.length != method.getParameterCount) {
      response.addResponse("Path has incorrect number of parameters. Given: " + args.length + ", Required: " +
        method.getParameterCount)
      
      return
    }
    
    response.successful()
    response.addResponse(if (method.getParameterCount == 0) method.invoke(rp1) else method.invoke(rp1, args))
    
  }
  
  override def handle(httpExchange: HttpExchange): Unit = {
    response.create(httpExchange.getRequestURI.toString, httpExchange)
    parse()
    response.send()
  }

}
