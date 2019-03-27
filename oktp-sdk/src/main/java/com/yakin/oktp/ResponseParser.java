package com.yakin.oktp;

import com.yakin.oktp.model.ObjectResult;

import java.io.IOException;

public interface ResponseParser<Result extends ObjectResult> {

    Result parse(ObjectResult responseResult) throws IOException;
}
