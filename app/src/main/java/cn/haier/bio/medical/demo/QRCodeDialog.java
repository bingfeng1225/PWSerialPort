package cn.haier.bio.medical.demo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import cn.qd.peiwen.pwlogger.PWLogger;
import cn.qd.peiwen.pwnetworkinterface.http.ResponseObserver;
import cn.qd.peiwen.pwnetworkinterface.http.transformer.ThreadTransformer;
import cn.qd.peiwen.pwtools.EmptyUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class QRCodeDialog extends Dialog implements DialogInterface.OnDismissListener, DialogInterface.OnShowListener {
    private String mac;
    private Bitmap bitmap;
    private ImageView qr_code_image;
    public QRCodeDialog(@NonNull Context context) {
        super(context);
    }

    public void changeMac(String mac){
        if(!mac.equals(this.mac)){
            this.mac = mac;
            this.createQRCode();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_qr_code);
        this.qr_code_image = findViewById(R.id.qr_code_image);
        this.setCancelable(false);
        this.setOnDismissListener(this);
        this.setOnShowListener(this);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        this.getWindow().setAttributes(lp);
        this.getWindow().getDecorView().setBackgroundColor(Color.parseColor("#22FF0000"));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(EmptyUtils.isNotEmpty(this.bitmap)){
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }


    private void createQRCode(){
        this.createQRCodeObserver()
                .compose(ThreadTransformer.<Bitmap>mainThread())
                .subscribe(new ResponseObserver<Bitmap>() {
                    @Override
                    protected void onStart() {
                        PWLogger.e("开始生成二维码");
                    }

                    @Override
                    protected void onSuccess(Bitmap entity) {
                        PWLogger.e("开始生成二维码成功");
                        bitmap = entity;
                        qr_code_image.setImageBitmap(entity);
                    }

                    @Override
                    protected void onFailure(Throwable e) {
                        PWLogger.e("开始生成二维码失败");
                    }

                    @Override
                    protected void onCompleted() {
                        PWLogger.e("开始生成二维码完毕");
                    }
                });
    }

    public Observable<Bitmap> createQRCodeObserver() {
        return Observable.just(this.mac)
                .flatMap(new Function<String, ObservableSource<Bitmap>>() {
                    @Override
                    public ObservableSource<Bitmap> apply(String entity) throws Exception {
                        Map<EncodeHintType, Object> hints = new HashMap();
                        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                        // 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
                        BitMatrix matrix = new MultiFormatWriter().encode(entity, BarcodeFormat.QR_CODE, 240, 240, hints);
                        int width = matrix.getWidth();
                        int height = matrix.getHeight();
                        // 二维矩阵转为一维像素数组,也就是一直横着排了
                        int[] pixels = new int[width * height];
                        for (int y = 0; y < height; y++) {
                            for (int x = 0; x < width; x++) {
                                if (matrix.get(x, y)) {
                                    pixels[y * width + x] = 0xff000000;
                                }
                            }
                        }
                        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

                        return Observable.just(bitmap);
                    }
                });
    }


}
