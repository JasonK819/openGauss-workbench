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
 * ModelingDataFlowTypeServiceImpl.java
 *
 * IDENTIFICATION
 * base-ops/src/main/java/org/opengauss/admin/plugin/service/modeling/impl/ModelingDataFlowTypeServiceImpl.java
 *
 * -------------------------------------------------------------------------
 */

package org.opengauss.admin.plugin.service.modeling.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.opengauss.admin.plugin.mapper.modeling.ModelingDataFlowTypeMapper;
import org.opengauss.admin.plugin.service.modeling.IModelingDataFlowTypeService;
import org.springframework.stereotype.Service;
import org.opengauss.admin.plugin.domain.entity.modeling.ModelingDataFlowTypeEntity;

/**
* @author LZW
* @description modeling_data_flow_type
* @createDate 2022-08-10 10:00:51
*/
@Service
public class ModelingDataFlowTypeServiceImpl extends ServiceImpl<ModelingDataFlowTypeMapper, ModelingDataFlowTypeEntity>
    implements IModelingDataFlowTypeService {

}




