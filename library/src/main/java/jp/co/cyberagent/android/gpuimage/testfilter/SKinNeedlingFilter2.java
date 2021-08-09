package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 剪映动感特效：毛刺（移除颜色变化）
 */
public class SKinNeedlingFilter2 extends GPUImageFilter {

    private float progress = 0;
    private int glUniformTexture2;
    private int uScanLineJitter_xLocation;
    private int uScanLineJitter_yLocation;
    private int uTimeStampLocation;


    public SKinNeedlingFilter2(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("skinneedling2.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        glUniformTexture2 = GLES20.glGetUniformLocation(glProgId, "inputImageTexture");
        uScanLineJitter_xLocation = GLES20.glGetUniformLocation(getProgram(), "uScanLineJitter_x");
        uScanLineJitter_yLocation = GLES20.glGetUniformLocation(getProgram(), "uScanLineJitter_y");
        uTimeStampLocation = GLES20.glGetUniformLocation(getProgram(), "uTimeStamp");
    }

    @Override
    protected void onDrawArraysPre() {
        if (textureId != -1) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glUniform1i(glUniformTexture2, 0);
        }
    }

    private int textureId = -1;

    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        this.textureId = textureId;
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        Log.d("hj", "SKinNeedlingFilter.onDraw: ");
        update();
    }


    double[] uScanLineJitter_x = {0.03, 0.01, 0.02, 0.06, 0.065, 0.05, 0.03, 0.04, 0.0, 0.0, 0.0, 0.0, 0.04, 0.07, 0.06, 0.075, 0.03, 0.035, 0.05, 0.065,
            0.0, 0.0, 0.0, 0.0, 0.073, 0.05, 0.08, 0.05, 0.07, 0.035, 0.017, 0.04, 0.0, 0.0, 0.0, 0.0, 0.038, 0.02, 0.025, 0.08, 0.055, 0.025, 0.06, 0.02,
            0.0, 0.0, 0.0, 0.0, 0.022, 0.04, 0.03, 0.016, 0.028, 0.02, 0.045, 0.03, 0.0, 0.0, 0.0, 0.0, 0.014, 0.03, 0.022, 0.034, 0.075, 0.056, 0.012, 0.034, 0.0, 0.0, 0.0, 0.0};
    double[] uScanLineJitter_y = {0.9, 0.77, 0.8, 0.65, 0.45, 0.7, 0.35, 0.65, 0.0, 0.0, 0.0, 0.0, 0.9, 0.68, 0.7, 0.85, 0.44, 0.56, 0.78, 0.89, 0.0, 0.0, 0.0,
            0.0, 0.94, 0.88, 0.65, 0.32, 0.63, 0.85, 0.92, 0.95, 0.0, 0.0, 0.0, 0.0, 0.82, 0.95, 0.82, 0.72, 0.35, 0.25, 0.4, 0.74, 0.0, 0.0, 0.0, 0.0, 0.87, 0.6,
            0.39, 0.24, 0.49, 0.6, 0.88, 0.85, 0.0, 0.0, 0.0, 0.0, 0.95, 0.88, 0.64, 0.28, 0.37, 0.54, 0.66, 0.82, 0.0, 0.0, 0.0, 0.0};
    int uScanLineJitter_x_size = uScanLineJitter_x.length;
    int uScanLineJitter_y_size = uScanLineJitter_y.length;


    private int index = 0;
    double intensity = 1;

    private void update() {
        int cur_id = (index % uScanLineJitter_x_size);
        setFloat(uScanLineJitter_xLocation, (float) uScanLineJitter_x[cur_id]/2.0f);//X 轴横线长度
        cur_id = (index % uScanLineJitter_y_size);
        setFloat(uScanLineJitter_yLocation, (float) uScanLineJitter_y[cur_id]);//Y 轴横线间隔
//        setFloat(intensityLocation, 1);
        setFloat(uTimeStampLocation, progress);
        index++;
    }

    public void setProgress(final float progress) {
        this.progress = progress;
        Log.d("hj", "SKinNeedlingFilter.setProgress: ");
        update();
    }
}
