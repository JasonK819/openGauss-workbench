package com.nctigba.datastudio.service.impl.sql;

import com.nctigba.datastudio.base.WebSocketServer;
import com.nctigba.datastudio.config.ConnectionConfig;
import com.nctigba.datastudio.model.dto.DatabaseCreateSequenceDTO;
import com.nctigba.datastudio.model.dto.DatabaseDropSequenceDTO;
import com.nctigba.datastudio.model.dto.DatabaseFunctionSPDTO;
import com.nctigba.datastudio.model.dto.DatabaseSequenceDdlDTO;
import com.nctigba.datastudio.service.DatabaseFunctionSPService;
import com.nctigba.datastudio.service.DatabaseSequenceService;
import com.nctigba.datastudio.util.DebugUtils;
import lombok.extern.slf4j.Slf4j;
import org.opengauss.admin.common.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static com.nctigba.datastudio.constants.CommonConstants.*;
import static com.nctigba.datastudio.constants.CommonConstants.PRO_RET_SET;
import static com.nctigba.datastudio.constants.SqlConstants.*;
import static org.apache.commons.lang3.StringUtils.isNumeric;

@Slf4j
@Service
public class DatabaseFunctionSPServiceImpl implements DatabaseFunctionSPService {
    @Autowired
    private ConnectionConfig connectionConfig;

    @Override
    public void dropFunctionSP(DatabaseFunctionSPDTO request) {
        log.info("dropFunctionSP request is: " + request);
        try {
            Connection connection = connectionConfig.connectDatabase(request.getConnectionName(), request.getWebUser());
            Statement statement = connection.createStatement();

            ResultSet funcResult = statement.executeQuery(getFuncSql(request.getFunctionSPName(), statement));
            String proKind = "";
            while (funcResult.next()) {
                proKind = funcResult.getString(PRO_KIND);
            }
            String sql = "";
            if ("f".equals(proKind)) {
                sql = DROP_SQL + FUNCTION_KEYWORD_SQL + request.getSchema() + POINT + request.getFunctionSPName() + SEMICOLON;
            } else if ("p".equals(proKind)) {
                sql = DROP_SQL + PROCEDURE_KEYWORD_SQL + request.getSchema() + POINT + request.getFunctionSPName() + SEMICOLON;
            }
            statement.execute(sql);
            log.info("dropFunctionSP sql is: " + sql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(e.getMessage());
        }
    }

    public static String getFuncSql(String name, Statement statement) throws SQLException {
        List<String> oidList = new ArrayList<>();
        List<String> paramTypeList = DebugUtils.getParamTypeList(name);
        for (int i = 0; i < paramTypeList.size(); i++) {
            ResultSet typeNameResult = statement.executeQuery(GET_TYPE_OID_SQL
                    + paramTypeList.get(i) + QUOTES_SEMICOLON);
            while (typeNameResult.next()) {
                oidList.add(typeNameResult.getString(OID));
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(GET_PROC_SQL + DebugUtils.getFuncName(name) + GET_PROC_PARAM_SQL);

        if (oidList.size() == 0) {
            sb.append(SPACE);
        } else {
            for (int i = 0; i < oidList.size(); i++) {
                sb.append(oidList.get(i));
                if (i != oidList.size() - 1) {
                    sb.append(SPACE);
                }
            }
        }

        sb.append(QUOTES_SEMICOLON);
        log.info("func sql is: " + sb);
        return sb.toString();
    }

}