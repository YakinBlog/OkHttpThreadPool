package com.yakin.oktp.simple;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yakin.oktp.ClientException;
import com.yakin.oktp.IRequester;
import com.yakin.oktp.Oktp;
import com.yakin.oktp.ResponseParser;
import com.yakin.oktp.callback.CompletedCallback;
import com.yakin.oktp.callback.ProgressCallback;
import com.yakin.oktp.model.FileRequest;
import com.yakin.oktp.model.FormRequest;
import com.yakin.oktp.model.ObjectRequest;
import com.yakin.oktp.model.ObjectResult;
import com.yakin.rtp.RTPManager;

import java.io.IOException;
import java.net.URI;

public class MainActivity extends AppCompatActivity {

    TextView resultView;
    ProgressBar progressBar;

    IRequester requester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultView = (TextView) findViewById(R.id.result);
        progressBar = (ProgressBar) findViewById(R.id.progress);

        Oktp.Config.setPrintLog(true)
                .setMaxErrorRetry(3);
        requester = Oktp.getRequester();

        RTPManager.getInstance().requestPermissions(this,
                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE});

        getRequest();
        headRequest();
        deleteRequest();
        putRequest();
        postRequest();
    }

    private void postRequest() {
        final FormRequest request = new FormRequest();
        request.setService(URI.create("http://30.77.12.156/okth-server.php"));
        request.addMapBody("key1", "value1");
        request.setStringBody("key2=value2&key3=value3");
        request.setResponseParser(new ResponseParser<StringResult>() {
            @Override
            public StringResult parse(ObjectResult responseResult) throws IOException {
                StringResult result = new StringResult();
                result.setContent(responseResult.getResponse().body().string());
                return result;
            }
        });
        request.setProgressCallback(new ProgressCallback() {
            @Override
            public void onProgress(Object request, long currentSize, long totalSize) {
                progressBar.setProgress((int)(currentSize * 100 / totalSize));
            }
        });
        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(0);
                requester.post(request, new CompletedCallback<ObjectRequest, StringResult>() {
                    @Override
                    public void onSuccess(ObjectRequest request, StringResult result) {
                        resultView.setText(result.getContent());
                    }

                    @Override
                    public void onFailure(ObjectRequest request, ClientException clientException) {
                        resultView.setText(clientException.getMessage());
                    }
                });
                resultView.setText("如果你先看到这条消息代表此次请求是异步的");
            }
        });
        findViewById(R.id.sync_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(0);
                try {
                    StringResult result = (StringResult) requester.post(request);
                    resultView.setText(result.getContent());
                } catch (ClientException e) {
                    resultView.setText(e.getMessage());
                }
                resultView.setText("如果你后看到这条消息代表此次请求是同步的");
            }
        });
    }

    private void putRequest() {
        final ObjectRequest request = new FileRequest("/sdcard/DCIM/20181106070954.jpg");
        request.setService(URI.create("http://30.77.12.156/okth-server.php"));
        request.setResponseParser(new ResponseParser<StringResult>() {
            @Override
            public StringResult parse(ObjectResult responseResult) throws IOException {
                StringResult result = new StringResult();
                result.setContent(responseResult.getResponse().body().string());
                return result;
            }
        });
        request.setProgressCallback(new ProgressCallback() {
            @Override
            public void onProgress(Object request, long currentSize, long totalSize) {
                progressBar.setProgress((int)(currentSize * 100 / totalSize));
            }
        });
        findViewById(R.id.put).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(0);
                requester.put(request, new CompletedCallback<ObjectRequest, StringResult>() {
                    @Override
                    public void onSuccess(ObjectRequest request, StringResult result) {
                        resultView.setText(result.getContent());
                    }

                    @Override
                    public void onFailure(ObjectRequest request, ClientException clientException) {
                        resultView.setText(clientException.getMessage());
                    }
                });
                resultView.setText("如果你先看到这条消息代表此次请求是异步的");
            }
        });
        findViewById(R.id.sync_put).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setProgress(0);
                try {
                    StringResult result = (StringResult) requester.put(request);
                    resultView.setText(result.getContent());
                } catch (ClientException e) {
                    resultView.setText(e.getMessage());
                }
                resultView.setText("如果你后看到这条消息代表此次请求是同步的");
            }
        });
    }

    private void deleteRequest() {
        final ObjectRequest request = new ObjectRequest();
        request.setService(URI.create("http://30.77.12.156/okth-server.php"));
        request.setResponseParser(new ResponseParser<StringResult>() {
            @Override
            public StringResult parse(ObjectResult responseResult) throws IOException {
                StringResult result = new StringResult();
                result.setContent(responseResult.getResponse().body().string());
                return result;
            }
        });
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requester.delete(request, new CompletedCallback<ObjectRequest, StringResult>() {
                    @Override
                    public void onSuccess(ObjectRequest request, StringResult result) {
                        resultView.setText(result.getContent());
                    }

                    @Override
                    public void onFailure(ObjectRequest request, ClientException clientException) {
                        resultView.setText(clientException.getMessage());
                    }
                });
                resultView.setText("如果你先看到这条消息代表此次请求是异步的");
            }
        });
        findViewById(R.id.sync_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    StringResult result = (StringResult) requester.delete(request);
                    resultView.setText(result.getContent());
                } catch (ClientException e) {
                    resultView.setText(e.getMessage());
                }
                resultView.setText("如果你后看到这条消息代表此次请求是同步的");
            }
        });
    }

    private void headRequest() {
        final ObjectRequest request = new ObjectRequest();
        request.setService(URI.create("http://30.77.12.156/okth-server.php"));
        request.setResponseParser(new ResponseParser<StringResult>() {
            @Override
            public StringResult parse(ObjectResult responseResult) throws IOException {
                StringResult result = new StringResult();
                result.setContent(responseResult.getResponse().body().string());
                return result;
            }
        });
        findViewById(R.id.head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requester.head(request, new CompletedCallback<ObjectRequest, StringResult>() {
                    @Override
                    public void onSuccess(ObjectRequest request, StringResult result) {
                        resultView.setText(result.getContent());
                    }

                    @Override
                    public void onFailure(ObjectRequest request, ClientException clientException) {
                        resultView.setText(clientException.getMessage());
                    }
                });
                resultView.setText("如果你先看到这条消息代表此次请求是异步的");
            }
        });
        findViewById(R.id.sync_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    StringResult result = (StringResult) requester.head(request);
                    resultView.setText(result.getContent());
                } catch (ClientException e) {
                    resultView.setText(e.getMessage());
                }
                resultView.setText("如果你后看到这条消息代表此次请求是同步的");
            }
        });
    }

    private void getRequest() {
        final ObjectRequest request = new ObjectRequest();
        request.setService(URI.create("http://30.77.12.156/okth-server.php"));
        request.setResponseParser(new ResponseParser<StringResult>() {
            @Override
            public StringResult parse(ObjectResult responseResult) throws IOException {
                StringResult result = new StringResult();
                result.setContent(responseResult.getResponse().body().string());
                return result;
            }
        });
        findViewById(R.id.get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requester.get(request, new CompletedCallback<ObjectRequest, StringResult>() {
                    @Override
                    public void onSuccess(ObjectRequest request, StringResult result) {
                        resultView.setText(result.getContent());
                    }

                    @Override
                    public void onFailure(ObjectRequest request, ClientException clientException) {
                        resultView.setText(clientException.getMessage());
                    }
                });
                resultView.setText("如果你先看到这条消息代表此次请求是异步的");
            }
        });
        findViewById(R.id.sync_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    StringResult result = (StringResult) requester.get(request);
                    resultView.setText(result.getContent());
                } catch (ClientException e) {
                    resultView.setText(e.getMessage());
                }
                resultView.setText("如果你后看到这条消息代表此次请求是同步的");
            }
        });
    }
}
