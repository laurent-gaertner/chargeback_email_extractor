package chargeback.airlines

import chargeback.ChargebackReasonEnum
import chargeback.exceptions.EmailParsingException
import matchers.ChargeBackDetailsMatcher
import org.specs2.mutable.SpecWithJUnit
import scala.io.Source

class ChargebackEmailExtractorTest extends SpecWithJUnit with ChargeBackDetailsMatcher {

  "ChargebackEmailExtractor::extractChargeBackDetails" should {

    "return extractChargeBackDetails for Air France" >> {
      //Data to be found in /resources/airFrance_Chargeback_Email.txt
      val ticketNumber = "12345678"
      val reason = ChargebackReasonEnum.FRAUD
      val amount = BigDecimal(100.00)

      val email = Source.fromResource("airFrance_Chargeback_Email.txt").mkString
      ChargebackEmailExtractor.extractChargeBackDetails(email) must beChargeBackDetails(ticketNumber, reason, amount)
    }

    "return extractChargeBackDetails for Lufthansa" >> {
      //Data to be found in /resources/lufthansa_Chargeback_Email.txt
      val ticketNumber = "87654321"
      val reason = ChargebackReasonEnum.CUSTOMER
      val amount = BigDecimal(50.00)

      val email = Source.fromResource("lufthansa_Chargeback_Email.txt").mkString
      ChargebackEmailExtractor.extractChargeBackDetails(email) must beChargeBackDetails(ticketNumber, reason, amount)
    }

    "throw EmailParsingException" >> {
      val email = Source.fromResource("unknown_Chargeback_Email.txt").mkString
      ChargebackEmailExtractor.extractChargeBackDetails(email) must throwA[EmailParsingException]
    }
  }

  "ChargebackEmailExtractor::extractChargeBackDetails" should {

    "return extractChargeBackDetails Json" >> {
      //Data to be found in /resources/airFrance_Chargeback_Email.txt
      val expectedJson = """{"ticketNumber":"12345678","reason":"Fraud","amount":100}"""
      val email = Source.fromResource("airFrance_Chargeback_Email.txt").mkString
      ChargebackEmailExtractor.extractChargeBackDetailsToJson(email) must beEqualTo(expectedJson)
    }

  }
}
