package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 自己实现的模糊效果
 */
public class BlurFilter extends GPUImageFilter {

    private float blurIntensity = 0;
    private int blurIntensityLocation;

    public BlurFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("blur.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        blurIntensityLocation = GLES20.glGetUniformLocation(getProgram(), "blurIntensity");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        setFloat(blurIntensityLocation, blurIntensity);
        Log.d("hj", "BlurFilter.onDraw: ");
    }


    public void setProgress(final float blurIntensity) {
        this.blurIntensity = blurIntensity;
    }
}
