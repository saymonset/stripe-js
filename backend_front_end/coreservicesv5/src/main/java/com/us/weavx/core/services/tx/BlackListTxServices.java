package com.us.weavx.core.services.tx;

import com.us.weavx.core.data.BlackListServicesTxDAO;
import com.us.weavx.core.exception.AlreadyCleanedException;
import com.us.weavx.core.exception.BLackListItemNotFoundException;
import com.us.weavx.core.model.BlackListItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("blackListTxServices")
public class BlackListTxServices {

    @Autowired
    private BlackListServicesTxDAO dao = null;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public BlackListItem addBlacklistItem(final BlackListItem item) {
        return dao.addBlacklistItem(item);
    }
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void cleanBLackListData(int dataTypeId, String data) throws BLackListItemNotFoundException, AlreadyCleanedException {
		BlackListItem itemToBeCleaned = dao.findBlackListItemByDataTypeIdAndData(dataTypeId, data);	
		if (itemToBeCleaned != null) {
			if (!itemToBeCleaned.isActive()) {
				throw new AlreadyCleanedException();
			}
			dao.cleanBLackListItem(itemToBeCleaned);
			List<BlackListItem> blockedItemsByItemCleaned = dao.findItemsBlockedByCleanedItem(itemToBeCleaned.getId());
			blockedItemsByItemCleaned.forEach(t -> {
				try {
					cleanBLackListData(t.getDataTypeId(), t.getData());
				} catch (BLackListItemNotFoundException e) {
					//Ignore
				} catch (AlreadyCleanedException e) {
					//Ignore
				}
			});
		} else {
			throw new BLackListItemNotFoundException();
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void cleanBlackListData(long blackListItemToBeCleanedId, List<BlackListItem> result) {
		BlackListItem itemToBeCleaned = dao.findBlackListItemById(blackListItemToBeCleanedId);
		if (itemToBeCleaned != null && itemToBeCleaned.isActive()) {
			dao.cleanBLackListItem(itemToBeCleaned);
			result.add(itemToBeCleaned);
			List<BlackListItem> blockedItemsByItemCleaned = dao.findItemsBlockedByCleanedItem(blackListItemToBeCleanedId);
			blockedItemsByItemCleaned.forEach(t -> {
				cleanBlackListData(t.getId(),result);
			});
		}
	}
	
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public BlackListItem findBlackListItemById(long id) {
		return dao.findBlackListItemById(id);
	}
	
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public BlackListItem findBlackListItemByDataTypeIdAndData(int dataTypeId, String data) {
		return dao.findBlackListItemByDataTypeIdAndData(dataTypeId, data);
	}
	
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
	public List<BlackListItem> listAllBlackListItems() {
		return dao.listAllBlackListItems();
	}
	
	

}
