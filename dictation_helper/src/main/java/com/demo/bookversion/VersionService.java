package com.demo.bookversion;

import com.demo.common.model.TblBookversion;

import java.util.List;

public class VersionService {
    private TblBookversion dao = new TblBookversion().dao();


    /**
     * 通过id查找版本
     * @param bvId
     * @return
     */
    public TblBookversion findOneById(int bvId){
        return dao.findById(bvId);
    }

    /**
     * 找到所有的版本信息
     * @return
     */
    public List<TblBookversion> findAllVersion(){
        return dao.findAll();
    }


}
