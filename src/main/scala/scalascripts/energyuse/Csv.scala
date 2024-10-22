package scalascripts.energyuse

import scala.io.Source

trait CsvDecoder[A]:
  def decode(fields: List[String]): A

object CsvReader:

  private def read[A](rows: List[List[String]])(using decoder: CsvDecoder[A]): List[A] =
    rows.foldRight(List.empty[A]) { (fields, acc) =>
      decoder.decode(fields) :: acc
    }

  def read[A](filePath: String)(using decoder: CsvDecoder[A]): List[A] =
    val source = Source.fromFile(filePath)
    val fields = source
      .getLines()
      .drop(1)
      .map(_.split(",").toList)
      .map(_.map(_.trim))
      .toList
    source.close()
    read(fields)
