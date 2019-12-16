package com.demo.back;

import com.demo.common.model.TblBack;

public class BackService {
    private TblBack dao=new TblBack().dao();

    public boolean saveBack(TblBack back){
        return back.save();
    }
}
