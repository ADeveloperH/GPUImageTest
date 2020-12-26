package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 闪白效果
 */
public class FlashWhiteFilter extends GPUImageFilter {

    private float alpha = 0;
    private int alphaTimeLineLocation;


    public FlashWhiteFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("flashwhite.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        alphaTimeLineLocation = GLES20.glGetUniformLocation(getProgram(), "alphaTimeLine");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        setFloat(alphaTimeLineLocation, 0.5F);
    }


    public void setAlpha(final float alpha) {
        this.alpha = alpha;
        setFloat(alphaTimeLineLocation, alpha);
    }
}
