package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 波纹色差效果
 */
public class ChromaticFilter extends GPUImageFilter {

    private float progress = 0;
    private int timerLocation;
    private int GLITCHLocation;
    private int swingLocation;
    private int yStepLocation;
    private int imageWidthLocation;
    private int imageHeightLocation;


    public ChromaticFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("chromatic.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        imageWidthLocation = GLES20.glGetUniformLocation(getProgram(), "imageWidth");
        imageHeightLocation = GLES20.glGetUniformLocation(getProgram(), "imageHeight");
        timerLocation = GLES20.glGetUniformLocation(getProgram(), "timer");
        GLITCHLocation = GLES20.glGetUniformLocation(getProgram(), "GLITCH");
        swingLocation = GLES20.glGetUniformLocation(getProgram(), "swing");
        yStepLocation = GLES20.glGetUniformLocation(getProgram(), "yStep");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        int outputHeight = getOutputHeight();
        int outputWidth = getOutputWidth();
        setInteger(imageHeightLocation, outputHeight);
        setInteger(imageWidthLocation, outputWidth);
        setFloat(timerLocation, progress);
        setFloat(GLITCHLocation, 0.1F);
        setFloat(swingLocation, 5F);
        setFloat(yStepLocation, 5F);
    }


    public void setProgress(final float progress) {
        this.progress = progress;
        setFloat(timerLocation, progress);
    }
}
