package com.hanip.ssr.models

/**
 * Created by hanip on 4/30/16.
 */
case class Coupon(id: Int, code: String, couponType: Int, fixDiscount: BigDecimal, percentDiscount: BigDecimal,
                  validityStart: Long, validityEnd: Long, currency: String) extends BaseModel