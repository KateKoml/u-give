alter table users
    add unique (id);

alter table user_balance
    add unique (id);

alter table roles
    add unique (id);

alter table purchase_offers
    add unique (id);

alter table payments
    add unique (id);

alter table messages
    add unique (id);

alter table l_users_roles
    add unique (id);

alter table favourites
    add unique (id);

alter table chats
    add unique (id);

alter table c_product_conditions
    add unique (id);

alter table c_product_categories
    add unique (id);

alter table c_payment_type
    add unique (id);

alter table c_offer_status
    add unique (id);

