package chargeback

object ChargebackReasonEnum extends Enumeration {
  type ChargebackReasonEnum = Value
  val UNKNOWN, FRAUD, CUSTOMER, DUPLICATE_PROCESSING = Value
}
