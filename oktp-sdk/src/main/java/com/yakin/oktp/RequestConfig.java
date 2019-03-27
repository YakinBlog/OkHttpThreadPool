package com.yakin.oktp;

public class RequestConfig {

    private boolean isPrintLog;
    private int connectionTimeout = 60 * 1000;
    private int writeTimeout = 60 * 1000;
    private int readTimeout = 60 * 1000;
    private int maxErrorRetry = 2;
    private String proxyHost;
    private int proxyPort;

    private int threadPoolSize = 5;

    public RequestConfig setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
        return this;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public RequestConfig setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return this;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public RequestConfig setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public RequestConfig setMaxErrorRetry(int maxErrorRetry) {
        this.maxErrorRetry = maxErrorRetry;
        return this;
    }

    public int getMaxErrorRetry() {
        return maxErrorRetry;
    }

    public RequestConfig setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public RequestConfig setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public RequestConfig setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        return this;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public RequestConfig setPrintLog(boolean printLog) {
        isPrintLog = printLog;
        return this;
    }

    public boolean isPrintLog() {
        return isPrintLog;
    }
}
