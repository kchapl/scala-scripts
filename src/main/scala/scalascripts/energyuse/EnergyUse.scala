package scalascripts.energyuse

import java.time.ZonedDateTime

case class Reading(start: ZonedDateTime, end: ZonedDateTime, units: Double)

object Reading:
  given decoder: CsvDecoder[Reading] = fields =>
    val units = fields(0).toDouble
    val start = ZonedDateTime.parse(fields(1))
    val end   = ZonedDateTime.parse(fields(2))
    Reading(start, end, units)

def unitsConsumed(from: ZonedDateTime, to: ZonedDateTime)(using readings: Seq[Reading]): Double =
  readings
    .dropWhile(_.start.isBefore(from))
    .takeWhile(!_.end.isAfter(to))
    .foldLeft(0d)((acc, reading) => acc + reading.units)

@main def usage(): Unit =
  given readings: Seq[Reading] = CsvReader.read(sys.env("GAS_USE"))
  val consumed = unitsConsumed(
    from = ZonedDateTime.parse("2022-12-02T03:00:00+00:00"),
    to = ZonedDateTime.parse("2022-12-02T07:00:00+00:00")
  )
  println(consumed)
