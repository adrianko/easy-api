package main.app


class Endpoint {
  
  protected var get: collection.mutable.Map[String, String] = null
  protected var post: collection.mutable.Map[String, String] = null
  
  def clearParams() {
    get = collection.mutable.Map[String, String]()
    post = collection.mutable.Map[String, String]()
  }

  def setGetParams(params: Map[String, String]) {
    get.++=(params)
  }

  def setPostParams(params: Map[String, String]) {
    post.++=(params)
  }
  
}

object Routes {
  
  class settings extends Endpoint {
    
  }
  
}