package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 风车效果
 */
public class PinWheellFilter extends GPUImageFilter {

    private float progress = 0;
    private int imageWidthLocation;
    private int imageHeightLocation;
    private int progressLocation;
    private int speedLocation;


    public PinWheellFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("pinwheel.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        imageWidthLocation = GLES20.glGetUniformLocation(getProgram(), "imageWidth");
        imageHeightLocation = GLES20.glGetUniformLocation(getProgram(), "imageHeight");
        progressLocation = GLES20.glGetUniformLocation(getProgram(), "progress");
        speedLocation = GLES20.glGetUniformLocation(getProgram(), "speed");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        int outputHeight = getOutputHeight();
        int outputWidth = getOutputWidth();
        setInteger(imageHeightLocation, outputHeight);
        setInteger(imageWidthLocation, outputWidth);
        setFloat(progressLocation, progress);
        setFloat(speedLocation, 2.0F);
    }


    public void setProgress(final float progress) {
        this.progress = progress;
        setFloat(progressLocation,progress);
    }
}
