package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 剪映横线效果（横线毛刺）
 */
public class HorizonalLineFilter extends GPUImageFilter {

    private float progress = 0;
    private int progressLocation;
    private int glUniformTexture2;


    public HorizonalLineFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("horizonal_line.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        progressLocation = GLES20.glGetUniformLocation(getProgram(), "progress");
        glUniformTexture2 = GLES20.glGetUniformLocation(glProgId, "inputImageTexture");
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
        setFloat(progressLocation, progress);
    }


    public void setProgress(final float progress) {
        this.progress = progress;
        setFloat(progressLocation, progress);
    }
}
