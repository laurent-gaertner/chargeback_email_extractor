package chargeback

import chargeback.ChargebackReasonEnum.ChargebackReasonEnum

case class ChargeBackDetails(ticketNumber: String, reason: ChargebackReasonEnum, amount: BigDecimal)
