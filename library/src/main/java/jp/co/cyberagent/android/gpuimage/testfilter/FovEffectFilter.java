package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 模拟视场 fov 变形效果
 */
public class FovEffectFilter extends GPUImageFilter {

    private int imageWidthLocation;
    private int imageHeightLocation;
    private int fovPowerLocation;


    public FovEffectFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("foveffect.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        imageWidthLocation = GLES20.glGetUniformLocation(getProgram(), "inputWidth");
        imageHeightLocation = GLES20.glGetUniformLocation(getProgram(), "inputHeight");
        fovPowerLocation = GLES20.glGetUniformLocation(getProgram(), "fovPower");
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
        setFloat(fovPowerLocation, 0);
    }


    public void setProgress(final float progress) {
        Log.d("hj", "SKinNeedlingFilter.setProgress: " + progress);
        setFloat(fovPowerLocation,  progress);
    }
}
