package com.demo.unit;

import com.demo.common.model.TblUnit;

import java.util.List;

public class UnitService {
    private TblUnit dao=new  TblUnit().dao();

    public List<TblUnit> findAll(){
        return dao.findAll();
    }
}
