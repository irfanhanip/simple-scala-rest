package models

import play.api.libs.json.Json

/**
 * Created by hanip on 4/30/16.
 */
object Book {

  case class Book(name: String, author: String)

  implicit val bookWrites = Json.writes[Book]
  implicit val bookReads = Json.reads[Book]

  var books = List(Book("TAOCP", "Knuth"), Book("SICP", "Sussman, Abelson"))

  def addBook(b: Book) = books = books ::: List(b)
}
