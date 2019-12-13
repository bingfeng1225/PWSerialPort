package cn.haier.bio.medical.demo;

import android.app.Application;

import org.apache.log4j.Level;

import java.io.File;

import cn.qd.peiwen.pwlogger.PWConfigurator;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        this.initLogger();
    }

    private void initLogger() {
        new PWConfigurator().level(this.parseLevel())
                .useFileAppender(BuildConfig.PW_LOG_FILE)
                .useLogcatAppender(BuildConfig.PW_LOG_LOGCAT)
                .fileName(this.getExternalCacheDir().getAbsolutePath() + File.separator + "demo.txt")// /sdcard/Android/data/[package]/cache/demo.txt
                .configure();
    }

    private Level parseLevel() {
        String level = BuildConfig.PW_LOG_LEVEL;
        if ("OFF".equals(level)) {
            return Level.OFF;
        } else if ("INFO".equals(level)) {
            return Level.INFO;
        } else if ("WARN".equals(level)) {
            return Level.WARN;
        } else if ("DEBUG".equals(level)) {
            return Level.DEBUG;
        } else if ("ERROR".equals(level)) {
            return Level.ERROR;
        } else if ("FATAL".equals(level)) {
            return Level.FATAL;
        } else if ("TRACE".equals(level)) {
            return Level.TRACE;
        } else {
            return Level.ALL;
        }
    }
}
