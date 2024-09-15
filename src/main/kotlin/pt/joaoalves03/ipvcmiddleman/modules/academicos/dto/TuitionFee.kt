package pt.joaoalves03.ipvcmiddleman.modules.academicos.dto

import java.math.BigDecimal

data class TuitionFee(
  val name: String,
  val expiryDate: String,
  val bankReference: String?,
  val value: BigDecimal,
  val paymentDate: String?,
  val paid: BigDecimal,
  val owed: BigDecimal,
  val fine: BigDecimal
)
