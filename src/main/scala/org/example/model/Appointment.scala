package org.example.model

case class Appointment(email: String, address: String, slot: String)

object Appointment {
  val appointments: Map[String, Appointment] = Map(
    "1" -> Appointment("example1@d.com", "your address here 1", "2020-10-15"),
    "2" -> Appointment("example2@d.com", "your address here 2", "2020-10-16"),
    "3" -> Appointment("example3@d.com", "your address here 3", "2020-10-17")
  )

}