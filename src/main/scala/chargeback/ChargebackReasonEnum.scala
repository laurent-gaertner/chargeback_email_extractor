package chargeback

import play.api.libs.json.{Reads, Writes}

object ChargebackReasonEnum extends Enumeration {
  type ChargebackReasonEnum = Value
  val UNKNOWN = Value(0, "Unknown")
  val FRAUD = Value(1, "Fraud")
  val CUSTOMER = Value(2, "Customer")
  val DUPLICATE_PROCESSING = Value(3, "Duplicate Processing")

  implicit val myEnumReads = Reads.enumNameReads(ChargebackReasonEnum)//Needed for Serialization
  implicit val myEnumWrites = Writes.enumNameWrites
}
