package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 运镜转场拉远模糊效果
 */
public class ZoomFarFilter extends GPUImageFilter {

    private float blurSize = 0;
    private int blurSizeLocation;


    public ZoomFarFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("zoomfar.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        blurSizeLocation = GLES20.glGetUniformLocation(getProgram(), "blurSize");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        setFloat(blurSizeLocation, blurSize);
    }


    public void setBlurSize(final float blurSize) {
        Log.d("hj", "ZoomFarFilter.setProgress: progress:" + blurSize);
        this.blurSize = blurSize;
        setFloat(blurSizeLocation, blurSize);
    }
}
