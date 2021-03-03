package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description 热成像效果（彩色负片）
 */
public class HotImgFilter extends GPUImageFilter {

    private float progress = 0;
    private Bitmap bitmap;
    private int filterSourceTexture2 = OpenGlUtils.NO_TEXTURE;
    private int filterInputTextureUniform2;

    private float threshold = 0;
    private int thresholdLocation;

    public HotImgFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("hotimg.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        filterInputTextureUniform2 = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture2");
        thresholdLocation = GLES20.glGetUniformLocation(getProgram(), "value");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        if (bitmap != null && !bitmap.isRecycled()) {
            setBitmap(bitmap);
        }
    }

    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        int outputHeight = getOutputHeight();
        int outputWidth = getOutputWidth();
        setFloat(thresholdLocation, threshold);
    }


    public void setBitmap(final Bitmap bitmap) {
        if (bitmap != null && bitmap.isRecycled()) {
            return;
        }
        this.bitmap = bitmap;
        if (this.bitmap == null) {
            return;
        }
        runOnDraw(new Runnable() {
            public void run() {
                if (filterSourceTexture2 == OpenGlUtils.NO_TEXTURE) {
                    if (bitmap == null || bitmap.isRecycled()) {
                        return;
                    }
                    GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
                    filterSourceTexture2 = OpenGlUtils.loadTexture(bitmap, OpenGlUtils.NO_TEXTURE, false);
                }
            }
        });
    }


    @Override
    protected void onDrawArraysPre() {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE3);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, filterSourceTexture2);
        GLES20.glUniform1i(filterInputTextureUniform2, 3);
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void recycleBitmap() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }


    public void setProgress(final float threshold) {
        this.threshold = threshold;
    }
}
