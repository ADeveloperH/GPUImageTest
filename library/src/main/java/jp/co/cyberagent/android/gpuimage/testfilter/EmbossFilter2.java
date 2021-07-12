package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 浮雕效果
 */
public class EmbossFilter2 extends GPUImageFilter {

    private float strength = 0;
    private int strengthLocation;


    public EmbossFilter2(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("emboss.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        strengthLocation = GLES20.glGetUniformLocation(getProgram(), "strength");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        setFloat(strengthLocation, strength);
    }


    public void setProgress(final float progress) {
        this.strength = progress;
        setFloat(strengthLocation, strength);
    }
}
