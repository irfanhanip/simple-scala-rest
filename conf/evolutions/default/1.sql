# --- !Ups

create table "coupons" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "code" VARCHAR(64) NOT NULL,
  "coupon_type" INT,
  "fix_discount" DECIMAL,
  "percent_discount" DECIMAL,
  "validity_start" BIGINT,
  "validity_end" BIGINT,
  "currency" VARCHAR(3)
);

create table "carts" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "status" INT,
  "coupon_id" INT,
  "total_amount" DECIMAL NOT NULL,
  "currency" VARCHAR(3)
);

create table "items" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "name" VARCHAR(254) NOT NULL,
  "price" DECIMAL NOT NULL,
  "desc" VARCHAR(254) NOT NULL
);

create table "item_carts" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "item_id" INT NOT NULL,
  "cart_id" INT NOT NULL
);
# --- !Downs
;
drop table "item_carts";
drop table "items";
drop table "carts";
drop table "coupons";