package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 色差放大效果
 */
public class ChromaticScaleFilter extends GPUImageFilter {

    private int scaleRLocation;
    private int scaleGLocation;
    private int scaleBLocation;

    private float scaleR = 1.0F;
    private float scaleG = 1.0F;
    private float scaleB = 1.0F;

    public ChromaticScaleFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("chromaticscale.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        scaleRLocation = GLES20.glGetUniformLocation(getProgram(), "scaleR");
        scaleGLocation = GLES20.glGetUniformLocation(getProgram(), "scaleG");
        scaleBLocation = GLES20.glGetUniformLocation(getProgram(), "scaleB");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        setFloat(scaleRLocation, scaleR);
        setFloat(scaleGLocation, scaleG);
        setFloat(scaleBLocation, scaleB);
    }


    public void setProgress(final float progress) {
        this.scaleR = progress;
        this.scaleB = 1 / progress;
        setFloat(scaleRLocation, scaleR);
        setFloat(scaleGLocation, scaleG);
        setFloat(scaleBLocation, scaleB);
    }
}
