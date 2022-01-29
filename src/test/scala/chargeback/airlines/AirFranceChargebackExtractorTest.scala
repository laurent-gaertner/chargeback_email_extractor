package chargeback.airlines

import chargeback.ChargebackReasonEnum
import org.specs2.mutable.SpecWithJUnit
import org.specs2.specification.core.Fragments
import AirlineChargebackExtractor._
import chargeback.exceptions.AmountExceedLimitException

class AirFranceChargebackExtractorTest extends SpecWithJUnit {

  val airFranceChargebackExtractor = new AirFranceChargebackExtractor

  "AirFranceChargebackExtractor" should {

    "return None if failed to extract ticketNumber" >> {
      val emailSnippet = "Ticket: 123"
      airFranceChargebackExtractor.extractTicketNumber(emailSnippet) must beNone
    }

    "return None if failed to extract amount" >> {
      val amount = "100.00"
      val emailSnippet = "AmountXX: $"+amount
      airFranceChargebackExtractor.extractAmount(emailSnippet) must beNone
    }

    "return None if failed to extract reason" >> {
      val emailSnippet = "Reason: Unknown"
      airFranceChargebackExtractor.extractReason(emailSnippet) must beNone
    }

    "throw AmountExceedLimitException if amount bigger than Max Limit" >> {
      val exceedMaxAmountLimit = AirlineChargebackExtractor.MAX_AMOUNT_LIMIT + 1
      val emailSnippet = "Amount: $"+exceedMaxAmountLimit
      airFranceChargebackExtractor.extractAmountAndValidate(Seq(emailSnippet)) must throwA[AmountExceedLimitException]
    }

    "extract ticketNumber" >> {
      val ticketNumber = "12345678"
      val emailSnippet = s"Ticket Number: $ticketNumber"
      airFranceChargebackExtractor.extractTicketNumber(emailSnippet) must beSome(ticketNumber)
    }

    "extract amount" >> {
      val amount = "100.00"
      val emailSnippet = "Amount: $"+amount
      airFranceChargebackExtractor.extractAmount(emailSnippet) must beSome(BigDecimal(amount))
    }

    Fragments.foreach(Seq(Fraud -> ChargebackReasonEnum.FRAUD,
                          Customer -> ChargebackReasonEnum.CUSTOMER,
                          Duplicate -> ChargebackReasonEnum.DUPLICATE_PROCESSING)) { reason =>
      Fragments {
        s"extract reason ${reason._1}" >> {
          val emailSnippet = s"Reason: ${reason._1}"
          airFranceChargebackExtractor.extractReason(emailSnippet) must beSome(reason._2)
        }
      }
    }


  }

}