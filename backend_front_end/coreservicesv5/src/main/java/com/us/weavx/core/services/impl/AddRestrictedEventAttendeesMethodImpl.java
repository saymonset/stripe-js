package com.us.weavx.core.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.us.weavx.core.constants.ServiceReturnMessages;
import com.us.weavx.core.model.AccessInfo;
import com.us.weavx.core.model.Request;
import com.us.weavx.core.model.Response;
import com.us.weavx.core.model.RestrictedEventAttendee;
import com.us.weavx.core.model.RestrictedEventAttendeeValidationResult;
import com.us.weavx.core.services.ServiceMethod;
import com.us.weavx.core.services.tx.EventRestrictionServicesTx;

@Component
public class AddRestrictedEventAttendeesMethodImpl implements ServiceMethod {
	@Autowired
	private EventRestrictionServicesTx eventRestrictionServices;
	
	public Response executeMethod(Request request) {
		List<RestrictedEventAttendeeValidationResult> resultList = new ArrayList<>();
		try {
			HashMap<String, Object> params = request.getParameters();
			AccessInfo aInfo = (AccessInfo) params.get("accessInfo");
			long customerId = aInfo.getCustomerId();
			long applicationId = aInfo.getApplicationId();
			ArrayList<HashMap<String, Object>> attendees = (ArrayList<HashMap<String, Object>>) params.get("restrictedAttendees");
			//Iterar y crear la coleccion de BulkTransactionItem para las validaciones
			ArrayList<RestrictedEventAttendee> attendeesList = new ArrayList<>();
			attendees.forEach(t -> {
				RestrictedEventAttendee newAttendee = new RestrictedEventAttendee();
				newAttendee.setEmail((String) t.get("email"));
				newAttendee.setCustomerId(customerId);
				newAttendee.setApplicationId(applicationId);
				attendeesList.add(newAttendee);
			});
			//Se valida la lista de attendees
			Collection<RestrictedEventAttendeeValidationResult> verifiedAttendees = eventRestrictionServices.validateRestrictedAttendees(attendeesList);
			boolean hasErrors = false;
			for (RestrictedEventAttendeeValidationResult verifiedItem : verifiedAttendees) {
				if (!verifiedItem.isValid()) {
					hasErrors = true;
					break;
				}
			}
			if (hasErrors) {
				Response resp = new Response();
				HashMap<String, Object> result = new HashMap<>();
				result.put("verifiedAttendeesResult", verifiedAttendees);
				resp.setReturnCode(ServiceReturnMessages.TRANSACTION_ABORTED_CODE);
				resp.setReturnMessage("Transaction aborted: At least one attendee has errors on his data.");
				resp.setResult(result);
				return resp;
			}
			//Al no haber errores se procesa la lista de attendees y se van generando las transacciones correspondientes.
			for (RestrictedEventAttendee attendee : attendeesList) {
				RestrictedEventAttendeeValidationResult currentResult = new RestrictedEventAttendeeValidationResult();
				try {
					attendee = eventRestrictionServices.addRestrictedEventAttendee(attendee);
					currentResult.setItem(attendee);
					currentResult.setMessage("OK");
					currentResult.setValid(true);
				} catch (Exception e) {
					currentResult.setItem(attendee);
					currentResult.setValid(false);
					currentResult.setMessage("ERROR: "+e.getMessage());
				}
				resultList.add(currentResult);
			}
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.SUCCESS_CODE);
			resp.setReturnMessage(ServiceReturnMessages.SUCCESS);
			HashMap<String, Object> result = new HashMap<>();
			result.put("restrictedEventAttendeesResult", resultList);
			resp.setResult(result);
			return resp;
		} catch (Exception e) {
			Response resp = new Response();
			resp.setReturnCode(ServiceReturnMessages.GENERAL_ERROR_CODE);
			resp.setReturnMessage(ServiceReturnMessages.GENERAL_ERROR+" "+e.getMessage());
			HashMap<String, Object> result = new HashMap<>();	
			result.put("restrictedEventAttendeesResult", resultList);
			resp.setResult(result);			
			return resp;
		}
	}
}
