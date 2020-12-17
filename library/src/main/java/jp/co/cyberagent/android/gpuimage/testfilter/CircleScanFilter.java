package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 圆形扫描效果
 */
public class CircleScanFilter extends GPUImageFilter {

    private float progress = 0;
    private int inputHeightLocation;
    private int inputWidthLocation;
    private int progressLocation;
    private int reverseLocation;


    public CircleScanFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("circlescan.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        inputWidthLocation = GLES20.glGetUniformLocation(getProgram(), "inputWidth");
        inputHeightLocation = GLES20.glGetUniformLocation(getProgram(), "inputHeight");
        progressLocation = GLES20.glGetUniformLocation(getProgram(), "progress");
        reverseLocation = GLES20.glGetUniformLocation(getProgram(), "reverse");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        int outputHeight = getOutputHeight();
        int outputWidth = getOutputWidth();
        setInteger(inputHeightLocation, outputHeight);
        setInteger(inputWidthLocation, outputWidth);
        setFloat(progressLocation, progress);
        setInteger(reverseLocation, GLES20.GL_TRUE);
    }


    public void setProgress(final float progress) {
        this.progress = progress;
        setFloat(progressLocation, progress);
    }
}
