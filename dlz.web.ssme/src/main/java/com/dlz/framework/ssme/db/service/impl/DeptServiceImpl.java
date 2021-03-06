package com.dlz.framework.ssme.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dlz.framework.ssme.base.service.impl.BaseServiceImpl;
import com.dlz.framework.ssme.db.dao.DeptMapper;
import com.dlz.framework.ssme.db.model.Dept;
import com.dlz.framework.ssme.db.service.DeptService;

@Service
@Transactional(rollbackFor=Exception.class)
public class DeptServiceImpl extends BaseServiceImpl<Dept, Long> implements DeptService {
	void doNothing(){new java.util.ArrayList<>().forEach(a->{});}

    @Autowired
    public void setMapper(DeptMapper mapper) {
        this.mapper=mapper;
    }
}