package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description
 */
public class RotationBlurFilter extends GPUImageFilter {

    private int inputHeightLocation;
    private int inputWidthLocation;
    private int intensityLocation;

    private float intensity = 0;


    public RotationBlurFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("rotationalblur.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        inputHeightLocation = GLES20.glGetUniformLocation(getProgram(), "inputHeight");
        inputWidthLocation = GLES20.glGetUniformLocation(getProgram(), "inputWidth");
        intensityLocation = GLES20.glGetUniformLocation(getProgram(), "intensity");
    }


    @Override
    public void onInitialized() {
        super.onInitialized();
        Log.d("hj", "RotationBlurFilter.onInitialized: ");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        int outputHeight = getOutputHeight();
        int outputWidth = getOutputWidth();
        Log.d("hj", "RotationBlurFilter.onDraw: outputHeight：" + outputHeight + "  outputWidth:" + outputWidth);
        setInteger(inputHeightLocation, outputHeight);
        setInteger(inputWidthLocation, outputWidth);
        setFloat(intensityLocation,intensity);
    }


    public void setIntensity(final float intensity) {
        this.intensity = intensity;
        setFloat(intensityLocation,intensity);
    }
}
