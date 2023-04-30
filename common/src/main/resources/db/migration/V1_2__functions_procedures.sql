create function public.get_product_by_name(search character varying)
    returns TABLE(id bigint, product_name character varying, product_category_id integer, product_condition_id integer, price numeric)
    language plpgsql
as
$$
BEGIN
    return query select purchase_offers.id, purchase_offers.product_name, purchase_offers.product_category_id,
                        purchase_offers.product_condition_id, purchase_offers.price
                 from purchase_offers
                 where purchase_offers.product_name ILIKE '%' || search || '%';
END;
$$;

alter function public.get_product_by_name(varchar) owner to postgres;

create procedure public.update_offer_price(offer_id bigint, new_price numeric)
    language plpgsql
as
$$
BEGIN
    update purchase_offers set price = new_price where id = offer_id;
END;
$$;

alter procedure public.update_offer_price(bigint, numeric) owner to postgres;

