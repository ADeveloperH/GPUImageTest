package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 模拟波形变形效果
 */
public class WaveDeformFilter extends GPUImageFilter {

    private int imageWidthLocation;
    private int imageHeightLocation;
    private int waveHeightLocation;
    private int progressLocation;


    public WaveDeformFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("wavedeform.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        imageWidthLocation = GLES20.glGetUniformLocation(getProgram(), "inputWidth");
        imageHeightLocation = GLES20.glGetUniformLocation(getProgram(), "inputHeight");
        waveHeightLocation = GLES20.glGetUniformLocation(getProgram(), "waveHeight");
        progressLocation = GLES20.glGetUniformLocation(getProgram(), "progress");
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
    }

    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        Log.d("hj", "SKinNeedlingFilter.onDraw: ");
        int outputHeight = getOutputHeight();
        int outputWidth = getOutputWidth();
        setFloat(imageHeightLocation, outputHeight);
        setFloat(imageWidthLocation, outputWidth);
        setFloat(progressLocation, 0);
        setFloat(waveHeightLocation, 0);
    }


    public void setProgress(final float progress) {
        Log.d("hj", "SKinNeedlingFilter.setProgress: " + progress);
        setFloat(progressLocation,  progress * 10);
        setFloat(waveHeightLocation,0.03f / 10);
    }
}
