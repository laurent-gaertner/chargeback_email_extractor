package chargeback.airlines

import chargeback.ChargebackReasonEnum.ChargebackReasonEnum
import chargeback.AirlinesEnum
import AirlineChargebackExtractor._

class LufthansaChargebackExtractor extends AirlineChargebackExtractor {

  override def airline: AirlinesEnum.Value = AirlinesEnum.LUFTHANSA

  override def extractTicketNumber(email: String): Option[String] = {
    val TicketNumberRegex = "(.*) ticket_num: (\\d{8}).".r
    email match {
      case TicketNumberRegex(_, ticketNumber) => {
        Some(ticketNumber)
      }
      case _ => None
    }
  }

  override def extractAmount(email: String): Option[BigDecimal] = {
    val AmountRegex = "the chargeback is due to (.*) and for \\$(\\d+\\.\\d+)".r
    email match {
      case AmountRegex(_, amount) => Some(BigDecimal(amount))
      case _ => None
    }
  }

  override def extractReason(email: String): Option[ChargebackReasonEnum] = {
    val ReasonRegex = s"the chargeback is due to (?i)($Fraud|$Customer|$Duplicate) (.*)".r
    email match {
      case ReasonRegex(reason, _) => Some(toChargebackReasonEnum(reason))
      case _ => None
    }
  }
}
