package chargeback.exceptions

import chargeback.AirlinesEnum.AirlinesEnum
import chargeback.FieldsEnum.FieldsEnum

case class EmailParsingException(airline: AirlinesEnum, field: FieldsEnum, message: Option[String] = None) extends RuntimeException
