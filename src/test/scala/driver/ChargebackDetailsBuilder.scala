package driver

import chargeback.{ChargeBackDetails, ChargebackReasonEnum}

object ChargebackDetailsBuilder {

  def aChargeBackDetails(): ChargeBackDetails = {
    ChargeBackDetails(ticketNumber = "12345", reason = ChargebackReasonEnum.FRAUD, amount = BigDecimal(10))
  }
}
