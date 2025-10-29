package com.geobudget.geobudget.repository;

import com.geobudget.geobudget.entity.ReceiptItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptItemRepository extends CrudRepository<ReceiptItem, Long> {

}
