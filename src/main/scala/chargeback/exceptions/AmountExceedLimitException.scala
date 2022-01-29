package chargeback.exceptions

import chargeback.AirlinesEnum.AirlinesEnum
import chargeback.FieldsEnum.FieldsEnum

case class AmountExceedLimitException(airline: AirlinesEnum, amount: BigDecimal) extends RuntimeException
