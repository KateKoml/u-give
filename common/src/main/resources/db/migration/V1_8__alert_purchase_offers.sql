alter table purchase_offers
    alter column product_name type varchar(200) using product_name::varchar(200);

