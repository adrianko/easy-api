package main.core

class Endpoint {

  protected var get: collection.mutable.Map[String, String] = collection.mutable.Map[String, String]()
  protected var post: collection.mutable.Map[String, String] = collection.mutable.Map[String, String]()

  def clearParams() {
    get.empty
    post.empty
  }

  def setGetParams(params: Map[String, String]) = get.++=(params)

  def setPostParams(params: Map[String, String]) = post.++=(params)

}
