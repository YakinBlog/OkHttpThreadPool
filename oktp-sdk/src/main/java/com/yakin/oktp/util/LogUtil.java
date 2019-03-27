package com.yakin.oktp.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import static com.yakin.oktp.Oktp.Config;

public class LogUtil {

    private static String sTAG = "OktpLog";

    public static void e(String format, Object... args) {
        Log.e(sTAG, buildMessage(format, args));
    }

    public static void e(Throwable e, String format, Object... args) {
        Log.e(sTAG, buildMessage(format, args), e);
    }

    public static void i(String format, Object... args) {
        Log.i(sTAG, buildMessage(format, args));
    }

    public static void i(Throwable e, String format, Object... args) {
        Log.i(sTAG, buildMessage(format, args), e);
    }

    public static void d(String format, Object... args) {
        if(Config.isPrintLog()) {
            Log.d(sTAG, buildMessage(format, args));
        }
    }

    public static void d(Throwable e, String format, Object... args) {
        if(Config.isPrintLog()) {
            Log.d(sTAG, buildMessage(format, args), e);
        }
    }

    public static void w(String format, Object... args) {
        if(Config.isPrintLog()) {
            Log.w(sTAG, buildMessage(format, args));
        }
    }

    public static void w(Throwable e, String format, Object... args) {
        if(Config.isPrintLog()) {
            Log.w(sTAG, buildMessage(format, args), e);
        }
    }

    private static String buildMessage(String format, Object... args) {
        String msg = (args == null || args.length < 1) ? format : String.format(Locale.US, format, args);
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
        for (StackTraceElement stack : trace) {
            String clazzName = stack.getClassName();
            if (!clazzName.equals(LogUtil.class.getName())) {
                clazzName = clazzName.substring(clazzName.lastIndexOf('.') + 1);

                caller = clazzName + "." + stack.getMethodName() + " (" + stack.getLineNumber() + ")";
                break;
            }
        }

        return String.format(Locale.US, "[%d] %s: %s", Thread.currentThread().getId(), caller, msg);
    }

    public static String getStackTrace(Throwable throwable) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        printWriter.close();
        return writer.toString();
    }
}
