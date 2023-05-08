alter table l_users_roles
    alter column created set default now();

alter table l_users_roles
    alter column changed set default now();

alter table l_users_roles
    alter column is_deleted set default false;

