package com.us.weavx.core.services.tx;

import com.us.weavx.core.data.EventRestrictionServicesTxDAO;
import com.us.weavx.core.exception.InvalidEmailAddressException;
import com.us.weavx.core.model.EventFundSettings;
import com.us.weavx.core.model.RestrictedEventAttendee;
import com.us.weavx.core.model.RestrictedEventAttendeeValidationResult;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("eventRestrictionTxServices")
public class EventRestrictionServicesTx {

    @Autowired
    private EventRestrictionServicesTxDAO dao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public RestrictedEventAttendee addRestrictedEventAttendee(final RestrictedEventAttendee item) {
        return dao.addRestrictedEventAttendee(item);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public RestrictedEventAttendee updateRestrictedEventAttendee(RestrictedEventAttendee item) throws InvalidEmailAddressException {
        return dao.updateRestrictedEventAttendee(item);
    }
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void removeRestrictedEventAttendee(long id, long customerId, long applicationId) {
		dao.removeRestrictedEventAttendee(id, customerId, applicationId);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<RestrictedEventAttendeeValidationResult> validateRestrictedAttendees(List<RestrictedEventAttendee> restrictedEventAttendees) {
		//Validacion
		List<String> currentEmails = new ArrayList<>();
		List<RestrictedEventAttendeeValidationResult> result = new ArrayList<>();
		restrictedEventAttendees.stream().forEach(item -> {
			RestrictedEventAttendeeValidationResult tmp = new RestrictedEventAttendeeValidationResult();
			tmp.setItem(item);
			if (currentEmails.contains(item.getEmail().toLowerCase())) {
				tmp.setValid(false);
				tmp.setMessage("Dupplicated email address");
			} else if (!EmailValidator.getInstance().isValid(item.getEmail())) {
				tmp.setValid(false);
				tmp.setMessage("Invalid email address");
			} else {
				currentEmails.add(item.getEmail().toLowerCase());
				tmp.setValid(true);
				tmp.setMessage("OK");
			}
			result.add(tmp);
		});
		return result;
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public Long getEventRestrictedAttendeesCount(long customerId, long applicationId) {
		return dao.getEventRestrictedAttendeesCount(customerId, applicationId);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public Boolean isRestrictedEvent(long customerId, long applicationId) {
		return dao.getEventRestrictedAttendeesCount(customerId, applicationId) > 0;
	}	
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public RestrictedEventAttendee findEventAttendeeByCustomerIdApplicationIdAndEmail(long customerId, long applicationId, String email) {
		return dao.findEventAttendeeByCustomerIdApplicationIdAndEmail(customerId, applicationId, email);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<RestrictedEventAttendee> listAllRestrictedAttendeesByEvent(long customerId, long applicationId) {
		return dao.listAllRestrictedAttendeesByEvent(customerId, applicationId);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public EventFundSettings findEventFundSettingsByCustomerIdAndApplicationId(long fundId, long customerId, long applicationId) {
		return dao.findEventFundSettingsByCustomerIdAndApplicationId(fundId, customerId, applicationId);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public EventFundSettings findEventFundSettingsByCustomerIdAndApplicationIdAndEventId(long fundId, long customerId, long applicationId, long eventId) {
		return dao.findEventFundSettingsByCustomerIdAndApplicationIdAndEventId(fundId, customerId, applicationId, eventId);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public EventFundSettings addEventFundSettings(final EventFundSettings item) {
		return dao.addEventFundSettings(item);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public EventFundSettings updateEventFundSettings(EventFundSettings item)  {
		return dao.updateEventFundSettings(item);
	}
	

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void removeEventFundSettings(long id) {
		dao.removeEventFundSettings(id);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<Map<String, Object>> getPriceByEventAndApplication(long eventId, long applicationId) {
		return dao.getPriceByApplicationIdAndEventIdAndApplicationId(applicationId, eventId);
	}
	
}
