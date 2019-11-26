package com.demo.version;

import com.demo.common.model.Version;

import java.util.List;

public class VersionService {
    private Version dao = new Version().dao();


    /**
     * 通过id查找版本
     * @param bvId
     * @return
     */
    public Version findOneById(int bvId){
        return dao.findById(bvId);
    }

    /**
     * 找到所有的版本信息
     * @return
     */
    public List<Version> findAllVersion(){
        return dao.findAll();
    }


}
