package matchers

import chargeback.ChargeBackDetails
import chargeback.ChargebackReasonEnum.ChargebackReasonEnum
import org.specs2.matcher.{Matcher, Matchers}

trait ChargeBackDetailsMatcher extends Matchers {

  def beChargeBackDetails(ticketNumber: String, reason: ChargebackReasonEnum, amount: BigDecimal): Matcher[ChargeBackDetails] = {
    be_==(ticketNumber) ^^ {
      (_: ChargeBackDetails).ticketNumber
    } and be_==(reason) ^^ {
      (_: ChargeBackDetails).reason
    } and be_==(amount) ^^ {
      (_: ChargeBackDetails).amount
    }
  }
}
