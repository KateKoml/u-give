CREATE FUNCTION insert_payments() RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO payments (offer_id, payment_type_id, created, changed, is_deleted)
    VALUES (NEW.id, 2, NOW(), NOW(), false);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER insert_payments
    AFTER INSERT ON purchase_offers
    FOR EACH ROW
    WHEN (NEW.status_id = 2)
EXECUTE FUNCTION insert_payments();