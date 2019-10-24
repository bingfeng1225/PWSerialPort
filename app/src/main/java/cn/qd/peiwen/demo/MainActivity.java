package cn.qd.peiwen.demo;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.apache.log4j.chainsaw.Main;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import cn.qd.peiwen.demo.serialport.finger.FingerPrintManager;
import cn.qd.peiwen.demo.serialport.finger.listener.FingerPrintListener;
import cn.qd.peiwen.demo.serialport.mainboard.MainBoardManager;
import cn.qd.peiwen.demo.serialport.mainboard.listener.MainBoardListener;
import cn.qd.peiwen.demo.serialport.rfid.RFIDReaderManager;
import cn.qd.peiwen.demo.serialport.rfid.listener.RFIDReaderListener;
import cn.qd.peiwen.pwtools.logger.PWLogger;


public class MainActivity extends AppCompatActivity implements FingerPrintListener, RFIDReaderListener, MainBoardListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RFIDReaderManager.getInstance().init(this);
        FingerPrintManager.getInstance().init(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RFIDReaderManager.getInstance().release();
        FingerPrintManager.getInstance().release();
        MainBoardManager.getInstance().release();
    }

    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.regist:
                if (!FingerPrintManager.getInstance().isBusy()) {
                    FingerPrintManager.getInstance().regist();
                }
                break;
            case R.id.download:
                if (!FingerPrintManager.getInstance().isBusy()) {
                    FingerPrintManager.getInstance().download("/sdcard");
                }
                break;
            case R.id.upload:
                if (!FingerPrintManager.getInstance().isBusy()) {
                    List<String> files = new ArrayList<>();
                    files.add("/sdcard/finger.1");
                    files.add("/sdcard/finger.2");
                    files.add("/sdcard/finger.3");
                    FingerPrintManager.getInstance().uplaod(files);
                }
                break;
            case R.id.open_finger:
                FingerPrintManager.getInstance().enable();
                break;
            case R.id.close_finger:
                FingerPrintManager.getInstance().disable();
                break;
            case R.id.open_rfid:
                RFIDReaderManager.getInstance().enable();
                break;
            case R.id.close_rfid:
                RFIDReaderManager.getInstance().disable();
                break;
            case R.id.open_main:
                MainBoardManager.getInstance().init(this);
                break;
            case R.id.close_main:
                MainBoardManager.getInstance().release();
                break;
        }
    }

    @Override
    public void onRegistTimeout() {
        PWLogger.e("onRegistTimeout");
    }

    @Override
    public void onRegistFailured() {
        PWLogger.e("onRegistFailured");
    }

    @Override
    public void onFingerAlreadyExists() {
        PWLogger.e("onFingerAlreadyExists");
    }

    @Override
    public void onRegistSuccessed(int finger) {
        PWLogger.e("onRegistSuccessed:" + finger);
    }

    @Override
    public void onUploadFailured() {
        PWLogger.e("onUploadFailured");
    }

    @Override
    public void onUploadSuccessed() {
        PWLogger.e("onUploadSuccessed");
    }

    @Override
    public void onNoFingerExist() {
        PWLogger.e("onNoFingerExist");
    }

    @Override
    public void onDownloadFailured() {
        PWLogger.e("onDownloadFailured");
    }

    @Override
    public void onDownloadSuccessed() {
        PWLogger.e("onDownloadSuccessed");
    }

    @Override
    public boolean isFingerValid(int finger) {
        return true;
    }

    @Override
    public void onFingerUNRegistered() {
        PWLogger.e("onFingerUNRegistered");
    }

    @Override
    public void onFingerRecognized(int finger) {
        PWLogger.e("onFingerRecognized" + finger);
    }

    @Override
    public void onFingerPrintReady() {

    }

    @Override
    public void onFingerPrintException() {

    }

    @Override
    public void onRFIDReaderReady() {

    }

    @Override
    public void onRFIDReaderException() {

    }

    @Override
    public void onRFIDReaderRecognized(long id, String card) {
        PWLogger.e("onCardRecognized ID：" + id + "，卡号：" + card);
    }

    @Override
    public void onRegistStated() {

    }

    @Override
    public void onRegistStepChanged(int step) {

    }

    @Override
    public void onUploadStated() {

    }

    @Override
    public void onDownloadStated() {

    }

    @Override
    public void onMainBoardReady() {

    }

    @Override
    public void onMainBoardException() {

    }

    @Override
    public void onSystemTypeChanged(int type) {

    }

    @Override
    public void onStateDataReceived(byte[] data) {

    }

    @Override
    public byte[] requestStateReply(byte[] data) {
        return new byte[0];
    }

    @Override
    public byte[] requestCommandReply(int type) {
        return new byte[0];
    }
}
