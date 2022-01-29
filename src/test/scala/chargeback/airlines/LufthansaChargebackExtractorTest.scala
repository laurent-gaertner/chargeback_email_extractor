package chargeback.airlines

import chargeback.ChargebackReasonEnum
import AirlineChargebackExtractor._
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.core.Fragments

class LufthansaChargebackExtractorTest extends SpecWithJUnit {

  val lufthansaChargebackExtractor = new LufthansaChargebackExtractor

  "LufthansaChargebackExtractor" should {

    "return None if failed to extract ticketNumber" >> {
      val emailSnippet = "Ticket: 123"
      lufthansaChargebackExtractor.extractTicketNumber(emailSnippet) must beNone
    }

    "return None if failed to extract amount" >> {
      val amount = "100.00"
      val emailSnippet = "AmountXX: $"+amount
      lufthansaChargebackExtractor.extractAmount(emailSnippet) must beNone
    }

    "return None if failed to extract reason" >> {
      val emailSnippet = "Reason: Unknown"
      lufthansaChargebackExtractor.extractReason(emailSnippet) must beNone
    }

    "extract ticketNumber" >> {
      val ticketNumber = "12345678"
      val emailSnippet = s"for ticket_num: $ticketNumber."
      lufthansaChargebackExtractor.extractTicketNumber(emailSnippet) must beSome(ticketNumber)
    }

    "extract amount" >> {
      val amount = "100.00"
      val emailSnippet = "the chargeback is due to fraud and for $" + amount
      lufthansaChargebackExtractor.extractAmount(emailSnippet) must beSome(BigDecimal(amount))
    }

    Fragments.foreach(Seq(Fraud -> ChargebackReasonEnum.FRAUD,
                          Customer -> ChargebackReasonEnum.CUSTOMER,
                          Duplicate -> ChargebackReasonEnum.DUPLICATE_PROCESSING)) { reason =>
      Fragments {
        s"extract reason ${reason._1}" >> {
          val emailSnippet = s"the chargeback is due to ${reason._1} and for ${"$"}50"
          lufthansaChargebackExtractor.extractReason(emailSnippet) must beSome(reason._2)
        }
      }
    }
  }

}