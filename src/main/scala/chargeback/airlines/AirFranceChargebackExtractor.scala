package chargeback.airlines

import chargeback.ChargebackReasonEnum.ChargebackReasonEnum
import chargeback.AirlinesEnum
import AirlineChargebackExtractor._

class AirFranceChargebackExtractor extends AirlineChargebackExtractor {

  override def airline: AirlinesEnum.Value = AirlinesEnum.AIR_FRANCE

  override def extractTicketNumber(email: String): Option[String] = {
    val TicketNumberRegex = "Ticket Number: (\\d{8})".r
    email match {
      case TicketNumberRegex(ticketNumber) => Some(ticketNumber)
      case _ => None
    }
  }

  override def extractAmount(email: String): Option[BigDecimal] = {
    val AmountRegex = "Amount: \\$(\\d+\\.\\d+)".r
    email match {
      case AmountRegex(amount) => Some(BigDecimal(amount))
      case _ => None
    }
  }

  override def extractReason(email: String): Option[ChargebackReasonEnum] = {
    val ReasonRegex = s"Reason: (?i)($Fraud|$Customer|$Duplicate)".r
    email match {
      case ReasonRegex(reason) => Some(toChargebackReasonEnum(reason))
      case _ => None
    }
  }
}
