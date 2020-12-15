package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 立方体效果
 */
public class CubeFilter extends GPUImageFilter {

    private float progress = 0;
    private int imageWidthLocation;
    private int imageHeightLocation;
    private int progressLocation;
    private int perspLocation;
    private int unzoomLocation;
    private int reflectionLocation;
    private int floatingLocation;


    public CubeFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("cube.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        imageWidthLocation = GLES20.glGetUniformLocation(getProgram(), "imageWidth");
        imageHeightLocation = GLES20.glGetUniformLocation(getProgram(), "imageHeight");
        progressLocation = GLES20.glGetUniformLocation(getProgram(), "progress");
        perspLocation = GLES20.glGetUniformLocation(getProgram(), "persp");
        unzoomLocation = GLES20.glGetUniformLocation(getProgram(), "unzoom");
        reflectionLocation = GLES20.glGetUniformLocation(getProgram(), "reflection");
        floatingLocation = GLES20.glGetUniformLocation(getProgram(), "floating");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        int outputHeight = getOutputHeight();
        int outputWidth = getOutputWidth();
        setInteger(imageHeightLocation, outputHeight);
        setInteger(imageWidthLocation, outputWidth);
        setFloat(progressLocation, progress);
        setFloat(perspLocation, 0.7F);
        setFloat(unzoomLocation, 0.3F);
        setFloat(reflectionLocation, 0.4F);
        setFloat(floatingLocation, 3.0F);
    }


    public void setProgress(final float progress) {
        this.progress = progress;
        setFloat(progressLocation,progress);
    }
}
