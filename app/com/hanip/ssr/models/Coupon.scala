package com.hanip.ssr.models

import org.joda.time.DateTime

/**
 * Created by hanip on 4/30/16.
 */
case class Coupon (id: Int, code: String, couponType: Int, fixDiscount: BigDecimal, percentDiscount: BigDecimal,
                   validityStart: DateTime, validityEnd: DateTime, currency: String)