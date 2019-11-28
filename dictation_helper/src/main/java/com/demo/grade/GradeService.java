package com.demo.grade;

import com.demo.common.model.TblGrade;

import java.util.List;

public class GradeService {
    private TblGrade dao=new TblGrade().dao();

    public List<TblGrade> findAll(){
        return dao.findAll();
    }
}
