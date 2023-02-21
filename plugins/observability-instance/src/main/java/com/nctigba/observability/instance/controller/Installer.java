package com.nctigba.observability.instance.controller;

import java.io.IOException;

import javax.websocket.Session;

import org.opengauss.admin.common.core.domain.model.ops.WsSession;
import org.opengauss.admin.common.core.handler.ops.cache.TaskManager;
import org.opengauss.admin.common.core.handler.ops.cache.WsConnectorManager;
import org.opengauss.admin.system.plugin.extract.SocketExtract;
import org.opengauss.admin.system.plugin.facade.WsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gitee.starblues.annotation.Extract;
import com.gitee.starblues.bootstrap.annotation.AutowiredType;
import com.gitee.starblues.bootstrap.annotation.AutowiredType.Type;
import com.nctigba.observability.instance.listener.PluginListener;
import com.nctigba.observability.instance.service.ExporterService;
import com.nctigba.observability.instance.service.PrometheusService;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Extract(bus = PluginListener.pluginId)
public class Installer implements SocketExtract {
	@Autowired
	@AutowiredType(Type.PLUGIN_MAIN)
	private WsConnectorManager wsConnectorManager;
	@Autowired
	private PrometheusService prometheusService;
	@Autowired
	private ExporterService exporterService;
	@Autowired
	@AutowiredType(Type.PLUGIN_MAIN)
	private WsFacade wsFacade;

	@Override
	public void onOpen(String pluginId, String sessionId, Session session) {
		wsConnectorManager.register(sessionId, new WsSession(session, sessionId));
		System.out.println("连接成功。。。。。。。。");
	}

	@Override
	public void processMessage(String sessionId, String message) {
		ThreadUtil.execute(() -> {
			try {
				var obj = JSONUtil.parseObj(message);
				var session = wsConnectorManager.getSession(sessionId)
						.orElseThrow(() -> new RuntimeException("websocket session not exist"));
				switch (obj.getStr("key")) {
				case "prometheus":
					prometheusService.install(session, obj.getStr("hostId"), obj.getStr("rootPassword"));
					break;
				case "exporter":
					exporterService.install(session, obj.getStr("nodeId"), obj.getStr("rootPassword"));
				}
			} catch (Exception e) {
				e.printStackTrace();
				wsFacade.sendMessage(PluginListener.pluginId, sessionId, e.toString());
			}
		});
		System.out.println("接收到消息并处理。。。。。。。。" + message);
	}

	@Override
	public void onClose(String pluginId, String sessionId) {
		wsConnectorManager.getSession(sessionId).ifPresent(wsSession -> {
			try {
				wsSession.getSession().close();
			} catch (IOException e) {
				log.error("close websocket session fail", e);
			}
		});
		TaskManager.remove(sessionId).ifPresent(future -> future.cancel(true));
		wsConnectorManager.remove(sessionId);
	}
}