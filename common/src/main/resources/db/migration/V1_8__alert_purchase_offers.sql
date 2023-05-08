alter table purchase_offers
    alter column product_name type varchar(200) using product_name::varchar(200);

alter table purchase_offers
    drop constraint purchase_offers_users_id_fk_2;

alter table purchase_offers
    add constraint purchase_offers_users_id_fk_2
        foreign key (customer_id) references users
            on delete set null;



