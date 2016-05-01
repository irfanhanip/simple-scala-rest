# --- !Ups

/*TODO: before slick nullable is implemented, use hackist -99 for null*/

insert into "coupons" (
  "id", "code", "coupon_type", "fix_discount", "percent_discount", "validity_start", "validity_end", "currency"
) values
  (1, 'TL2016', 1, 2000.00, -99, 1462035032, 1493571032, 'IDR'),
  (2, 'TL1500', 2, -99, 15.0, 1462035032, 1493571032, 'IDR');

insert into "carts" (
  "id", "status", "coupon_id", "total_amount", "currency"
) values
  (1, 1, -99, 0, 'IDR');

insert into "items" (
  "id", "name", "price", "desc"
) values
  (1, 'Apple', 5000.00, 'Shut up and take my money');


# --- !Downs
;
truncate table "coupons";
truncate table "items";
truncate table "carts";