ALTER TABLE TRANSACTION_REPORT
ADD PERSON_PHONE	VARCHAR;

CREATE OR REPLACE FUNCTION UPDATE_REPORTS() RETURNS void AS $$
    DECLARE
	last_run_date	timestamp;
	current_date_var timestamp;
    BEGIN
	select updated_at into last_run_date from REPORT_TRACK; 
	current_date_var = current_timestamp;
	insert into transaction_report(customer_id, application_id, application_name, person_firstname, 
	       person_lastname, person_addressline1, person_addresscity, person_addressstate, 
	       person_addresszip, person_country, contribution_date, contribution_amount, contribution_discount, contribution_commission, 
	       contribution_onl_transactionid, contribution_paymentinfo, contribution_fund, 
	       contribution_fund_id, contribution_containerid, person_email, person_authorizenetprofileid, 
	       contribution_onl_approvalnumber, contribution_form, transactioninternalid, 
	       transaction_status, transaction_status_id, payment_type, payment_type_id, payment_gw, payment_gw_id, donation_source, donation_source_id,
	       donation_campaing, donation_Campaing_id, transaction_medium, transaction_medium_id, comments, operator_id, operator_email, person_phone)
	select t.customer_id, t.application_id, app.name, ud.name, ud.lastname,
	ud.address, ud.cityText, ud.stateText, ud.postcode, ud.countryText, t.tx_date,
	td.amount, t.discount, t.commission, t.gwAuthId_1, ccpd.credit_card_masked, f.name, f.id,
	f.business_code, ud.email, '', t.gwAuthId_2, 'ONL', t.tx_id, ts.name, ts.id,
	pt.name, pt.id, pgw.name, pgw.id, tsour.name, tsour.id, tcamp.name, tcamp.id,
	tmed.name, tmed.id, t.comments, t.operator_id, t.operator_email, cu.phone_number from transaction t inner join transaction_status ts on ts.id = t.tx_status_id 
	inner join transaction_detail td on td.transaction_id = t.id 
	left join credit_card_payment_data ccpd on t.tx_payment_data_id = ccpd.payment_data_id
	inner join transaction_user_data ud on t.tx_user_data_id = ud.id
	left join customer_user cu on ud.customer_user_id = cu.id 
	inner join application app on t.application_id = app.id 
	inner join payment_data pd on t.tx_payment_data_id = pd.id 
	left join payment_gateway pgw on t.tx_payment_gw_id = pgw.id 
	inner join transaction_source tsour on t.tx_transaction_source_id = tsour.id 
	inner join transaction_campaing tcamp on t.tx_transaction_campaing_id = tcamp.id 
	inner join transaction_medium tmed on t.tx_transaction_medium_id = tmed.id
	inner join fund f on td.fund_id = f.id
	inner join payment_type pt on pd.payment_type_id = pt.id
	where t.tx_date between last_run_date and current_date_var
	order by t.tx_id;
	update REPORT_TRACK set updated_at = current_date_var;
    END;
    $$ LANGUAGE plpgsql;
    
DELETE FROM REPORT_TRACK;

SELECT UPDATE_REPORTS()
