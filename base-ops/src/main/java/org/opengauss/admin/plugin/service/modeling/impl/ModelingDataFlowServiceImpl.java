/*
 * Copyright (c) 2022 Huawei Technologies Co.,Ltd.
 *
 * openGauss is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *
 * http://license.coscl.org.cn/MulanPSL2
 *
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FITFOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 * -------------------------------------------------------------------------
 *
 * ModelingDataFlowServiceImpl.java
 *
 * IDENTIFICATION
 * base-ops/src/main/java/org/opengauss/admin/plugin/service/modeling/impl/ModelingDataFlowServiceImpl.java
 *
 * -------------------------------------------------------------------------
 */

package org.opengauss.admin.plugin.service.modeling.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.opengauss.admin.plugin.mapper.modeling.ModelingDataFlowMapper;
import org.opengauss.admin.plugin.service.modeling.IModelingDataFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.opengauss.admin.plugin.domain.entity.modeling.ModelingDataFlowEntity;

import java.util.Arrays;
import java.util.List;

/**
* @author LZW
* @description modeling_data_flow
* @createDate 2022-08-10 11:41:13
*/
@Service
public class ModelingDataFlowServiceImpl extends ServiceImpl<ModelingDataFlowMapper, ModelingDataFlowEntity>
    implements IModelingDataFlowService {

    @Autowired
    private ModelingDataFlowMapper modelingDataFlowMapper;

    @Override
    public IPage<ModelingDataFlowEntity> selectList(IPage<ModelingDataFlowEntity> page, String name) {

        LambdaQueryWrapper<ModelingDataFlowEntity> queryWrapper = Wrappers.lambdaQuery(ModelingDataFlowEntity.class)
                .like(name != null, ModelingDataFlowEntity::getName, name)
                .orderByDesc(ModelingDataFlowEntity::getCreateTime);

        return modelingDataFlowMapper.selectPage(page, queryWrapper);
    }

    /**
     * insert
     *
     * @param dataFlowData dataflow body
     * @return result
     */
    @Override
    public Long insertDataFlow(ModelingDataFlowEntity dataFlowData) {
        modelingDataFlowMapper.insert(dataFlowData);
        return dataFlowData.getId();
    }

    /**
     * batch delete dataflow
     *
     * @param flowIds ids list of dataflow
     * @return result
     */
    @Override
    public int deleteDataFlowByIds(String[] flowIds){
        int row = modelingDataFlowMapper.deleteBatchIds(Arrays.asList(flowIds));
        return row;
    }

    /**
     * save
     *
     * @param dataFlowData dataflow body
     * @return result
     */
    @Override
    public int updateDataFlow(ModelingDataFlowEntity dataFlowData) {
        int row = modelingDataFlowMapper.updateById(dataFlowData);
        return row;
    }

    /**
     * check name
     *
     * @param name dataflow name
     * @return result
     */
    @Override
    public List<ModelingDataFlowEntity> findByName(String name) {
        return modelingDataFlowMapper.findByName(name);
    }
}




