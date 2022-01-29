package chargeback.airlines

import chargeback.AirlinesEnum.AirlinesEnum
import chargeback.exceptions.EmailParsingException
import chargeback.{AirlinesEnum, ChargeBackDetails, FieldsEnum}

object ChargebackEmailExtractor {

  //TODO:Not correct place to create those classes
  val airFranceChargebackExtractor = new AirFranceChargebackExtractor
  val lufthansaChargebackExtractor = new LufthansaChargebackExtractor

  private val AirFrance = "Airline AF"
  private val Lufthansa = "Airline LH"

  def extractChargeBackDetails(email: String): ChargeBackDetails = {
    email match {
      case isAirFrance(_) => airFranceChargebackExtractor.extractChargeBackDetails(email)
      case isLufthansa(_) => lufthansaChargebackExtractor.extractChargeBackDetails(email)
      case _ => throw EmailParsingException(airline = AirlinesEnum.UNKNOWN, field = FieldsEnum.AIRLINE)
    }
  }

  object isAirFrance {
    def unapply(text: String): Option[AirlinesEnum] =
      if (text.contains(AirFrance)) Some(AirlinesEnum.AIR_FRANCE) else None
  }

  object isLufthansa {
    def unapply(text: String): Option[AirlinesEnum] =
      if (text.contains(Lufthansa)) Some(AirlinesEnum.LUFTHANSA) else None
  }
}


