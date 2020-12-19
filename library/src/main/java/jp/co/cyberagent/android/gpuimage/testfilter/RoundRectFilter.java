package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 圆角矩形
 */
public class RoundRectFilter extends GPUImageFilter {

    private float radius = 0.1F;
    private int radiusLocation;


    public RoundRectFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("roundrect.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        radiusLocation = GLES20.glGetUniformLocation(getProgram(), "radius");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        setFloat(radiusLocation, radius);
    }


    public void setRadius(final float radius) {
        this.radius = radius;
        setFloat(radiusLocation, radius);
    }
}
