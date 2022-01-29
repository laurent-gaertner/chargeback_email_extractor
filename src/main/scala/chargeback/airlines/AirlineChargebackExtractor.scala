package chargeback.airlines

import chargeback.ChargebackReasonEnum.ChargebackReasonEnum
import chargeback.FieldsEnum.FieldsEnum
import chargeback.exceptions.{AmountExceedLimitException, EmailParsingException}
import chargeback.{AirlinesEnum, ChargeBackDetails, ChargebackReasonEnum, FieldsEnum}
import AirlineChargebackExtractor._

trait AirlineChargebackExtractor {

  def airline: AirlinesEnum.Value
  def extractTicketNumber(line: String): Option[String]
  def extractAmount(line: String): Option[BigDecimal]
  def extractReason(line: String): Option[ChargebackReasonEnum]

  def extractChargeBackDetails(email: String): ChargeBackDetails = {
    val lastLines: Seq[String] = email.split("\n").toList.takeRight(3)//Read only the last three lines of the email
    val ticketNumber = extractField(lastLines, FieldsEnum.TICKET_NUMBER, extractTicketNumber)
    val reason = extractField(lastLines, FieldsEnum.REASON, extractReason)
    val amount = extractAmountAndValidate(lastLines)

    ChargeBackDetails(ticketNumber, reason, amount)
  }

  def extractAmountAndValidate(lastLines: Seq[String]): BigDecimal = {
    val amount = extractField(lastLines, FieldsEnum.AMOUNT, extractAmount)
    if (amount.compare(MAX_AMOUNT_LIMIT) == 1) throw AmountExceedLimitException(airline, amount)
    amount
  }

  private def extractField[T](lastLines: Seq[String], field: FieldsEnum, fieldExtractor: String => Option[T]): T = {
    val ticketNumber = lastLines.flatMap(fieldExtractor)
    if (ticketNumber.size == 1)
      ticketNumber.head
    else
      throw EmailParsingException(airline, field)
  }

  def toChargebackReasonEnum(reason: String): ChargebackReasonEnum = {
    reason.toLowerCase match {
      case Fraud => ChargebackReasonEnum.FRAUD
      case Customer => ChargebackReasonEnum.CUSTOMER
      case Duplicate => ChargebackReasonEnum.DUPLICATE_PROCESSING
      case _ => throw EmailParsingException(airline, FieldsEnum.REASON, message = Some(s"Reason $reason unknown"))
    }
  }
}
object AirlineChargebackExtractor {
  val Fraud = "fraud"
  val Customer = "customer"
  val Duplicate = "duplicate"

  val MAX_AMOUNT_LIMIT = 10000.00//There shouldn't be a Chargeback bigger than $10.000
}