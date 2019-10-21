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
import cn.qd.peiwen.demo.serialport.mainboard.tools.MainBoardTools;
import cn.qd.peiwen.demo.serialport.rfid.RFIDReaderManager;
import cn.qd.peiwen.demo.serialport.rfid.listener.RFIDReaderListener;
import cn.qd.peiwen.logger.PWLogger;
import cn.qd.peiwen.pwtools.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class MainActivity extends AppCompatActivity implements FingerPrintListener, RFIDReaderListener, MainBoardListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RFIDReaderManager.getInstance().init(this);
        FingerPrintManager.getInstance().init(this);
        MainBoardManager.getInstance().init(this);
    }
    private static int indexOf(ByteBuf haystack, byte[] needle) {
        //遍历haystack的每一个字节
        for (int i = haystack.readerIndex(); i < haystack.writerIndex(); i++) {
            int needleIndex;
            int haystackIndex = i;
            /*haystack是否出现了delimiter，注意delimiter是一个ChannelBuffer（byte[]）
            例如对于haystack="ABC\r\nDEF"，needle="\r\n"
            那么当haystackIndex=3时，找到了“\r”，此时needleIndex=0
            继续执行循环，haystackIndex++，needleIndex++，
            找到了“\n”
            至此，整个needle都匹配到了
            程序然后执行到if (needleIndex == needle.capacity())，返回结果
            */
            for (needleIndex = 0; needleIndex < needle.length; needleIndex++) {
                if (haystack.getByte(haystackIndex) != needle[needleIndex]) {
                    break;
                } else {
                    haystackIndex++;
                    if (haystackIndex == haystack.writerIndex() && needleIndex != needle.length - 1) {
                        return -1;
                    }
                }
            }

            if (needleIndex == needle.length) {
                // Found the needle from the haystack!
                return i - haystack.readerIndex();
            }
        }
        return -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RFIDReaderManager.getInstance().release();
        FingerPrintManager.getInstance().release();
    }

    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.regist:
                FingerPrintManager.getInstance().regist();
                break;
            case R.id.download:
                FingerPrintManager.getInstance().download("/sdcard");
                break;
            case R.id.upload:
                List<String> files = new ArrayList<>();
                files.add("/sdcard/finger.1");
                files.add("/sdcard/finger.2");
                files.add("/sdcard/finger.3");
                FingerPrintManager.getInstance().uplaod(files);
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
        PWLogger.e("onCardRecognized ID：" +id + "，卡号：" + card);
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
