alter table purchase_offers
    alter column status_id set default 1;

alter table purchase_offers
    alter column product_category_id set default 18;

alter table purchase_offers
    alter column product_condition_id set default 3;

alter table payments
    alter column payment_type_id set default 2;

