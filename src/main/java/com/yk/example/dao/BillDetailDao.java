package com.yk.example.dao;

import com.yk.example.entity.BillDetail;
import com.yk.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by yk on 2018/4/12.
 */
public interface BillDetailDao  extends CrudRepository<BillDetail,String> ,JpaSpecificationExecutor{
    Page<BillDetail> findByUser(User one, Pageable pageable);
}
