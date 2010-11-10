/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.sftp.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.integration.sftp.outbound.SftpSendingMessageHandler;
import org.springframework.integration.sftp.session.QueuedSftpSessionPool;
import org.springframework.integration.sftp.session.SftpSessionFactory;

/**
 * Supports the construction of a MessagHandler that knows how to take inbound File objects
 * and send them to a remote destination.
 *
 * @author Josh Long
 */
public class SftpMessageSendingConsumerFactoryBean implements FactoryBean<SftpSendingMessageHandler> {

	private String host;

	private String keyFile;

	private String keyFilePassword;

	private String password;

	private String remoteDirectory;

	private String username;

	private int port;

	private String charset;

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public void setHost(final String host) {
		this.host = host;
	}

	public void setKeyFile(final String keyFile) {
		this.keyFile = keyFile;
	}

	public void setKeyFilePassword(final String keyFilePassword) {
		this.keyFilePassword = keyFilePassword;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setPort(final int port) {
		this.port = port;
	}

	public void setRemoteDirectory(final String remoteDirectory) {
		this.remoteDirectory = remoteDirectory;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public SftpSendingMessageHandler getObject() throws Exception {
		SftpSessionFactory sessionFactory = SftpSessionUtils.buildSftpSessionFactory(
				this.host, this.password, this.username, this.keyFile, this.keyFilePassword, this.port);
		QueuedSftpSessionPool queuedSFTPSessionPool = new QueuedSftpSessionPool(15, sessionFactory);
		queuedSFTPSessionPool.afterPropertiesSet();
		SftpSendingMessageHandler sftpSendingMessageHandler = new SftpSendingMessageHandler(queuedSFTPSessionPool);
		sftpSendingMessageHandler.setRemoteDirectory(this.remoteDirectory);
		sftpSendingMessageHandler.setCharset(this.charset);
		sftpSendingMessageHandler.afterPropertiesSet();
		return sftpSendingMessageHandler;
	}

	public Class<? extends SftpSendingMessageHandler> getObjectType() {
		return SftpSendingMessageHandler.class;
	}

	public boolean isSingleton() {
		return false;
	}

}
