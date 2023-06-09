package com.nctigba.datastudio.base;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gitee.starblues.annotation.Extract;
import com.gitee.starblues.bootstrap.annotation.AutowiredType;
import com.nctigba.datastudio.config.ConnectionConfig;
import com.nctigba.datastudio.enums.MessageEnum;
import com.nctigba.datastudio.model.entity.OperateStatusDO;
import com.nctigba.datastudio.result.WebDsResult;
import com.nctigba.datastudio.service.OperationInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.opengauss.admin.system.plugin.extract.SocketExtract;
import org.opengauss.admin.system.plugin.facade.WsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.websocket.Session;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static com.nctigba.datastudio.constants.CommonConstants.BEGIN;
import static com.nctigba.datastudio.constants.CommonConstants.END;
import static com.nctigba.datastudio.constants.CommonConstants.FIVE_HUNDRED;
import static com.nctigba.datastudio.constants.CommonConstants.START_RUN;
import static com.nctigba.datastudio.constants.CommonConstants.STOP_DEBUG;
import static com.nctigba.datastudio.constants.CommonConstants.STOP_RUN;
import static com.nctigba.datastudio.constants.CommonConstants.TWO_HUNDRED;
import static com.nctigba.datastudio.constants.CommonConstants.WEBDS_PLUGIN;
import static com.nctigba.datastudio.enums.MessageEnum.window;

@Slf4j
@Service
@Extract(bus = "webds-plugin")
public class WebSocketServer implements SocketExtract {
    @Autowired
    @AutowiredType(AutowiredType.Type.PLUGIN_MAIN)
    private WsFacade wsFacade;

    private final Map<String, Map<String, Object>> paramMap = new HashMap<>();

    private final Map<String, Connection> connectionMap = new HashMap<>();

    private final Map<String, Statement> statementMap = new HashMap<>();

    private final Map<String, OperateStatusDO> operationStatusMap = new HashMap<>();

    private String language = Strings.EMPTY;

    @Override
    public void onOpen(String pluginId, String sessionId, Session session) {
        log.info("onOpen pluginId: " + pluginId + ",sessionId:" + sessionId);
    }

    @Override
    public void processMessage(String sessionId, String message) {
        log.info("sessionId: " + sessionId + ",message:" + message);
        if (StringUtils.isBlank(message)) {
            return;
        }

        boolean isJson = JSONUtil.isJson(message);
        if (!isJson) {
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(message);
        String operation = jsonObject.getString("operation");
        if (StringUtils.isEmpty(operation)) {
            return;
        }
        var aa = SpringApplicationContext.getApplicationContext().getBean(operation, OperationInterface.class);
        ThreadUtil.execAsync(() -> {
            try {
                aa.operate(this, message);
            } catch (Exception e) {
                log.error("method error: ", e);
                try {
                    sendMessage(sessionId, window, FIVE_HUNDRED, e.getMessage(), e.getStackTrace());
                    if (!operation.equals(START_RUN) && !operation.equals(STOP_RUN)) {
                        SpringApplicationContext.getApplicationContext().getBean(STOP_DEBUG, OperationInterface.class)
                                .operate(this, message);
                    }
                } catch (Exception ex) {
                    log.error("method error: ", ex);
                }
            }
        });
    }

    @Override
    public void onClose(String pluginId, String sessionId) {
        log.info("onClose pluginId: " + pluginId + ",sessionId:" + sessionId);
        try {
            if (statementMap.get(sessionId) != null) {
                statementMap.get(sessionId).close();
            }
            if (connectionMap.get(sessionId) != null) {
                connectionMap.get(sessionId).close();
            }
        } catch (Exception e) {
            log.error("method error: ", e);
        }
    }

    /**
     * server send message to client
     *
     * @param type
     * @param code
     * @param message
     * @param obj
     * @throws IOException
     */
    public synchronized void sendMessage(String sessionId, MessageEnum type, String code, String message, Object obj) throws IOException {
        WebDsResult result = WebDsResult.ok(type.toString(), message).addData(obj);
        result.setCode(code);
        wsFacade.sendMessage(WEBDS_PLUGIN, sessionId, JSON.toJSONString(result));
    }

    public void sendMessage(String sessionId, MessageEnum type, String message, Object obj) throws IOException {
        sendMessage(sessionId, type, TWO_HUNDRED, message, obj);
    }

    public Connection getConnection(String sessionId) {
        return this.connectionMap.get(sessionId);
    }

    public void setConnection(String sessionId, Connection connection) {
        this.connectionMap.put(sessionId, connection);
    }

    public Connection createConnection(String uuid, String winName) throws Exception {
        return SpringApplicationContext.getApplicationContext().getBean("connectionConfig",
                ConnectionConfig.class).connectDatabaseMap(uuid, winName);
    }

    public Statement getStatement(String sessionId) {
        return this.statementMap.get(sessionId);
    }

    public void setStatement(String sessionId, Statement statement) {
        this.statementMap.put(sessionId, statement);
    }

    public OperateStatusDO getOperateStatus(String sessionId) {
        OperateStatusDO operateStatusDO = this.operationStatusMap.get(sessionId);
        if (operateStatusDO == null) {
            return new OperateStatusDO();
        }
        return operateStatusDO;
    }

    public void setOperateStatus(String sessionId, OperateStatusDO operateStatus) {
        this.operationStatusMap.put(sessionId, operateStatus);
    }

    public Map<String, Object> getParamMap(String sessionId) {
        Map<String, Object> map = this.paramMap.get(sessionId);
        if (CollectionUtils.isEmpty(map)) {
            return new HashMap<>();
        }
        return map;
    }

    public void setParamMap(String sessionId, String key, Object obj) {
        Map<String, Object> map = getParamMap(sessionId);
        map.put(key, obj);
        this.paramMap.put(sessionId, map);
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    /**
     * check line is in range
     *
     * @param line
     * @return
     */
    public boolean checkLine(String sessionId, int line) {
        int begin = (int) getParamMap(sessionId).get(BEGIN);
        int end = (int) getParamMap(sessionId).get(END);
        return line > begin && line < end;
    }
}
