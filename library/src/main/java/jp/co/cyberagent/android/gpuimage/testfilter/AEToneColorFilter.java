package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description AE特效：色调
 */
public class AEToneColorFilter extends GPUImageFilter {

    private float progress = 1.0f;
    private int uWhiteMappingCorLocation;
    private int uTransPercentLocation;

    public AEToneColorFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("aetone.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        uWhiteMappingCorLocation = GLES20.glGetUniformLocation(glProgId, "uWhiteMappingCor");
        uTransPercentLocation = GLES20.glGetUniformLocation(glProgId, "uTransPercent");
    }

    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        Log.d("hj", "SKinNeedlingFilter.onDraw: ");
        setFloatVec3(uWhiteMappingCorLocation, new float[]{0.984f, 1, 0.463f});
        setFloat(uTransPercentLocation,progress);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        setFloat(uTransPercentLocation,progress);
    }
}
