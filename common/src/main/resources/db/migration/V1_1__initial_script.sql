create table if not exists public.users
(
    id            bigserial
        primary key
        unique,
    user_name     varchar(20)  not null,
    surname       varchar(30)  not null,
    gender        varchar(20)  not null,
    e_mail        varchar(50)  not null,
    phone         varchar(12)  not null,
    user_login    varchar(30)  not null
        constraint users_login_key
            unique,
    user_password varchar(20)  not null,
    created       timestamp(6) not null,
    changed       timestamp(6) not null,
    is_deleted    boolean      not null
);

create index users_gender_index
    on public.users (gender);

create index users_name_index
    on public.users (user_name);

create index users_surname_gender_index
    on public.users (surname, gender);

CREATE SEQUENCE payment_type_id_seq;

create table if not exists public.c_payment_type
(
    id         integer default nextval('payment_type_id_seq'::regclass) not null
        constraint payment_type_pkey
            primary key
        unique,
    type       varchar(15)                                              not null,
    created    timestamp(6)                                             not null,
    changed    timestamp(6)                                             not null,
    is_deleted boolean                                                  not null
);

create index c_payment_type_type_index
    on public.c_payment_type (type);

CREATE SEQUENCE categories_id_seq;

create table if not exists public.c_product_categories
(
    id            integer default nextval('categories_id_seq'::regclass) not null
        constraint categories_pkey
            primary key
        constraint c_categories_id_key
            unique,
    category_name varchar(100)                                           not null
        constraint categories_name_key
            unique,
    created       timestamp(6)                                           not null,
    changed       timestamp(6)                                           not null,
    is_deleted    boolean                                                not null
);

CREATE SEQUENCE conditions_id_seq;

create table if not exists public.c_product_conditions
(
    id             integer default nextval('conditions_id_seq'::regclass) not null
        constraint conditions_pkey
            primary key
        constraint c_conditions_id_key
            unique,
    condition_name varchar(4)                                             not null
        constraint conditions_name_key
            unique,
    created        timestamp(6)                                           not null,
    changed        timestamp(6)                                           not null,
    is_deleted     boolean                                                not null
);

CREATE SEQUENCE status_id_seq;

create table if not exists public.c_offer_status
(
    id          integer default nextval('status_id_seq'::regclass) not null
        constraint status_pkey
            primary key
        unique,
    status_name varchar(20)                                        not null
        constraint status_name_key
            unique,
    created     timestamp(6)                                       not null,
    changed     timestamp(6)                                       not null,
    is_deleted  boolean                                            not null
);

create table if not exists public.purchase_offers
(
    id                   bigserial
        primary key
        unique,
    seller_id            bigint       not null
        constraint purchase_offers_users_id_fk
            references public.users
            on update cascade on delete cascade,
    customer_id          bigint
        constraint purchase_offers_users_id_fk_2
            references public.users,
    status_id            integer      not null
        constraint purchase_offers_status_id_fk
            references public.c_offer_status,
    product_name         varchar(200)  not null,
    product_category_id  integer      not null
        constraint purchase_offers_categories_id_fk
            references public.c_product_categories,
    product_condition_id integer      not null
        constraint purchase_offers_conditions_id_fk
            references public.c_product_conditions,
    price                numeric      not null,
    created              timestamp(6) not null,
    changed              timestamp(6) not null,
    is_deleted           boolean      not null
);

create index purchase_offers_created_index
    on public.purchase_offers (created desc);

create index purchase_offers_product_condition_id_index
    on public.purchase_offers (product_condition_id);

create index purchase_offers_status_id_index
    on public.purchase_offers (status_id);

create index purchase_offers_product_category_id_index
    on public.purchase_offers (product_category_id);

create index purchase_offers_price_index
    on public.purchase_offers (price);

create table if not exists public.favourites
(
    id                bigserial
        primary key
        unique,
    user_id           bigint       not null
        constraint favourites_users_id_fk
            references public.users,
    purchase_offer_id bigint       not null
        constraint favourites_purchase_offers_id_fk
            references public.purchase_offers,
    created           timestamp(6) not null,
    changed           timestamp(6) not null,
    is_deleted        boolean      not null
);

create index favourites_created_index
    on public.favourites (created desc);

create index favourites_purchase_offer_id_index
    on public.favourites (purchase_offer_id);

create index favourites_user_id_index
    on public.favourites (user_id);



create table if not exists public.chats
(
    id             bigserial
        primary key
        unique,
    first_user_id  bigint       not null
        constraint chat_users_id_fk_2
            references public.users
            on delete cascade,
    second_user_id bigint       not null
        constraint chat_users_id_fk
            references public.users
            on delete cascade,
    created        timestamp(6) not null,
    changed        timestamp(6) not null,
    is_deleted     boolean      not null
);

create index chat_first_user_id_index
    on public.chats (first_user_id);

create index chat_second_user_id_index
    on public.chats (second_user_id);

create index chats_created_index
    on public.chats (created desc);

create table if not exists public.messages
(
    id         bigserial
        primary key
        unique,
    chat_id    bigint       not null
        constraint messages_chat_id_fk
            references public.chats,
    user_id    bigint       not null
        constraint messages_users_id_fk
            references public.users,
    text       varchar(200) not null,
    is_read    boolean      not null,
    created    timestamp(6) not null,
    changed    timestamp(6) not null,
    is_deleted boolean      not null
);

create index messages_user_id_index
    on public.messages (user_id);

create index messages_chat_id_index
    on public.messages (chat_id);

create index messages_text_index
    on public.messages (text);

create index messages_created_index
    on public.messages (created desc);

create table if not exists public.user_balance
(
    id         bigserial
        primary key
        unique,
    user_id    bigint       not null
        unique,
    balance    numeric      not null,
    created    timestamp(6) not null,
    changed    timestamp(6) not null,
    is_deleted boolean      not null
);

create index user_balance_balance_index
    on public.user_balance (balance desc);

create table if not exists public.payments
(
    id              bigserial
        primary key
        unique,
    offer_id        bigint    not null
        constraint payments_purchase_offers_id_fk
            references public.purchase_offers,
    payment_type_id integer   not null
        constraint payments_c_payment_type_id_fk
            references public.c_payment_type,
    created         timestamp(6) not null,
    changed         timestamp(6) not null,
    is_deleted      boolean   not null
);

create index payments_created_index
    on public.payments (created desc);

create index payments_offer_id_index
    on public.payments (offer_id);

create table if not exists public.roles
(
    id         serial
        primary key
        unique,
    role_name  varchar(100) not null,
    created    timestamp(6) not null,
    changed    timestamp(6) not null,
    is_deleted boolean      not null
);

create index roles_role_name_index
    on public.roles (role_name);

create table if not exists public.l_users_roles
(
    id         bigserial
        primary key
        unique,
    user_id    bigint       not null,
    role_id    integer      not null,
    created    timestamp(6) not null,
    changed    timestamp(6) not null,
    is_deleted boolean      not null
);

create index l_users_roles_id_index
    on public.l_users_roles (id);

create index l_users_roles_role_id_index
    on public.l_users_roles (role_id);

create index l_users_roles_user_id_index
    on public.l_users_roles (user_id);

create index l_users_roles_user_id_role_id_index
    on public.l_users_roles (user_id, role_id);

alter table public.users
    owner to kate;

alter table public.c_payment_type
    owner to kate;

alter table public.c_product_categories
    owner to kate;

alter table public.c_product_conditions
    owner to kate;

alter table public.c_offer_status
    owner to kate;

alter table public.purchase_offers
    owner to kate;

alter table public.favourites
    owner to kate;

alter table public.chats
    owner to kate;

alter table public.messages
    owner to kate;

alter table public.user_balance
    owner to kate;

alter table public.payments
    owner to kate;

alter table public.roles
    owner to kate;

alter table public.l_users_roles
    owner to kate;