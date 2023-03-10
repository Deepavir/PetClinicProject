package org.springframework.samples.petclinic.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.samples.petclinic.owner.Owner;

import org.springframework.samples.petclinic.owner.OwnerRepositoryy;
import org.springframework.stereotype.Service;

/**
 * @author deepa
 *
 */

@Service
public class OwnerService {

	@Autowired
	private OwnerRepositoryy ownerepo;
	
	
	
	/**This method provide paginated and sorted owner data orderby createddate.
	 * @param page-This parameter provides page number 
	 * @param pageSize -This parameter gets number of records to be displayed
	 * @return paginated and sorted owner entity by createddate in aescending order.
	 */
	public Page<Owner> getDataByCreatedDate(int page,int pageSize){
		Pageable paging = PageRequest.of(page-1, pageSize);
	Page<Owner>sortedlist =	this.ownerepo.findAllByOrderByCreatedDate(paging);
		return sortedlist;
	}
	
	/** method provide paginated and sorted owner data orderby firstname and createddate if name is similar.
	 * @param page-This parameter provides page number
	 * @param pagesize -This parameter gets number of records to be displayed
	 * @return paginated and sorted owner entity by firstname and createddate in aescending order.
	 */
	public Page<Owner> getDataByFirstNameAndCreatedDated(int page,int pagesize){
		Pageable paging = PageRequest.of(page, pagesize);
		Page<Owner> sortedlist = this.ownerepo.findAllByOrderByFirstNameAscCreatedDateAsc(paging);
		return sortedlist;
	}
	
}
