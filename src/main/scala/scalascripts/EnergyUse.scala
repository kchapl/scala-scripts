package scalascripts

import java.time.LocalDateTime
import scala.io.Source

object CSVReader:

  private def parsedLine(line: String): Reading =
    val fields = line.split(",")
    val start  = LocalDateTime.parse(fields(0))
    val end    = LocalDateTime.parse(fields(1))
    val units  = fields(2).toInt
    Reading(start, end, units)

  def parsedFile(filePath: String): List[Reading] =
    val source = Source.fromFile(filePath)
    val readings = source
      .getLines()
      .drop(1)
      .map(parsedLine)
      .toList
    source.close()
    readings

case class Reading(start: LocalDateTime, end: LocalDateTime, units: Int)

def unitsConsumed(from: LocalDateTime, to: LocalDateTime)(using readings: Seq[Reading]): Int =
  readings
    .dropWhile(_.start.isBefore(from))
    .takeWhile(!_.end.isAfter(to))
    .foldLeft(0)((acc, reading) => acc + reading.units)

@main def usage(): Unit =
  given readings: Seq[Reading] = CSVReader.parsedFile(sys.env("GAS_USE"))
  val consumed = unitsConsumed(
    from = LocalDateTime.of(2024, 8, 1, 10, 0),
    to = LocalDateTime.of(2024, 8, 2, 11, 30)
  )
  println(consumed)
