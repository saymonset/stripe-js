package com.us.weavx.core.services.tx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.us.weavx.core.data.AssistantServicesTxDAO;
import com.us.weavx.core.model.Assistant;

@Service("assistantServicesTX")
public class AssistantServicesTx {

	@Autowired
	private AssistantServicesTxDAO dao;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public Assistant addAssistant(Assistant assistant) {
		return dao.addAssistant(assistant);
	}
}
