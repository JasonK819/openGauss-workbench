package org.opengauss.admin.plugin.domain.model.ops.node;

import cn.hutool.core.util.StrUtil;
import org.opengauss.admin.common.exception.ops.OpsException;
import org.opengauss.admin.plugin.domain.entity.ops.OpsClusterNodeEntity;
import org.opengauss.admin.plugin.enums.ops.ClusterRoleEnum;
import lombok.Data;

import java.util.Objects;

/**
 * Enterprise Edition Installation Node Configuration
 *
 * @author lhf
 * @date 2022/8/4 23:16
 **/
@Data
public class EnterpriseInstallNodeConfig {
    /**
     * Cluster role
     */
    private ClusterRoleEnum clusterRole;
    /**
     * host ID
     */
    private String hostId;

    private String rootPassword;
    /**
     * Public IP
     */
    private String publicIp;
    /**
     * Intranet IP
     */
    private String privateIp;
    /**
     * hostname
     */
    private String hostname;

    private String installUserId;

    private String installUsername;

    private Boolean isInstallCM;

    private Boolean isCMMaster;

    private String cmDataPath;

    private Integer cmPort;

    private Integer dcfPort;

    private String dataPath;

    private String xlogPath;

    public void checkConfig() {
        if (Objects.isNull(clusterRole)) {
            throw new OpsException("Cluster role error");
        }
        if (StrUtil.isEmpty(hostId)) {
            throw new OpsException("wrong host id");
        }

        if (StrUtil.isEmpty(rootPassword)){
            throw new OpsException("wrong root password");
        }

        if (StrUtil.isEmpty(publicIp)) {
            throw new OpsException("public IP error");
        }

        if (StrUtil.isEmpty(privateIp)) {
            throw new OpsException("Intranet IP error");
        }

        if (StrUtil.isEmpty(hostname)) {
            throw new OpsException("wrong hostname");
        }

        if (StrUtil.isEmpty(installUserId)) {
            throw new OpsException("install user error");
        }

        if (Objects.isNull(isInstallCM)) {
            throw new OpsException("Is it wrong to install CM");
        }

        if (Objects.isNull(isCMMaster)) {
            throw new OpsException("Whether it is the CM master node error");
        }

        if (isInstallCM && StrUtil.isEmpty(cmDataPath)) {
            throw new OpsException("cm data path error");
        }

        if (isInstallCM && Objects.isNull(cmPort)) {
            throw new OpsException("cm port error");
        }

        if (StrUtil.isEmpty(dataPath)) {
            throw new OpsException("[" + hostname + "]data path error");
        }

        if (StrUtil.isEmpty(xlogPath)) {
            throw new OpsException("[" + hostname + "]xlog path error");
        }
    }

    public OpsClusterNodeEntity toOpsClusterNodeEntity() {
        OpsClusterNodeEntity opsClusterNodeEntity = new OpsClusterNodeEntity();
        opsClusterNodeEntity.setClusterRole(clusterRole);
        opsClusterNodeEntity.setHostId(hostId);
        opsClusterNodeEntity.setInstallUserId(installUserId);
        opsClusterNodeEntity.setDataPath(dataPath);
        opsClusterNodeEntity.setIsInstallCM(isInstallCM);
        opsClusterNodeEntity.setIsCMMaster(isCMMaster);
        opsClusterNodeEntity.setCmDataPath(cmDataPath);
        opsClusterNodeEntity.setCmPort(cmPort);
        opsClusterNodeEntity.setXlogPath(xlogPath);
        return opsClusterNodeEntity;
    }
}