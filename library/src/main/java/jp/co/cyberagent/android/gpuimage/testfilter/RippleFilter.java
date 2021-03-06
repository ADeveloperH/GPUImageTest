package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 水波纹效果
 */
public class RippleFilter extends GPUImageFilter {

    private float progress = 0;
    private int imageWidthLocation;
    private int imageHeightLocation;
    private int rippleProgressLocation;


    public RippleFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("ripple.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        imageWidthLocation = GLES20.glGetUniformLocation(getProgram(), "inputWidth");
        imageHeightLocation = GLES20.glGetUniformLocation(getProgram(), "inputHeight");
        rippleProgressLocation = GLES20.glGetUniformLocation(getProgram(), "rippleProgress");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        int outputHeight = getOutputHeight();
        int outputWidth = getOutputWidth();
        setFloat(imageHeightLocation, outputHeight);
        setFloat(imageWidthLocation, outputWidth);
        setFloat(rippleProgressLocation, progress);
    }


    public void setProgress(final float progress) {
        this.progress = progress;
        setFloat(rippleProgressLocation, progress);
    }
}
