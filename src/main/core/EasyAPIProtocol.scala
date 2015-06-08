package main.core

import spray.json._

object EasyAPIProtocol extends DefaultJsonProtocol {

  implicit object MapJsonFormat extends JsonFormat[Map[String, Any]] {
    
    def write(m: Map[String, Any]) = {
      JsObject(m.mapValues {
        case v: String => JsString(v)
        case v: Int => JsNumber(v)
        case v: Map[_, _] => write(v.asInstanceOf[Map[String, Any]])
        case v: Any => JsString(v.toString)
      })
    }

    def read(value: JsValue) = ???
    
  }

}