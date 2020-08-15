package jp.co.cyberagent.android.gpuimage.testfilter;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description
 */
public class Fake3dFilter extends GPUImageFilter {

    public static final String FAKE3D_FRAGMENT_SHADER =
            "precision highp float;\n" +
                    "\n" +
                    "varying highp vec2 textureCoordinate;\n" +
                    "uniform sampler2D inputImageTexture;\n" +
                    "uniform float scale;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec2 newTextureCoordinate = vec2((scale - 1.0) * 0.5 + textureCoordinate.x / scale,\n" +
                    "                                     (scale - 1.0) * 0.5 + textureCoordinate.y / scale);\n" +
                    "    vec4 textureColor = texture2D(inputImageTexture, newTextureCoordinate);\n" +
                    "    \n" +
                    "    // shift color\n" +
                    "    vec4 shiftColor1 = texture2D(inputImageTexture, newTextureCoordinate + vec2(-0.05 * (scale - 1.0), -0.05 * (scale - 1.0)));\n" +
                    "    vec4 shiftColor2 = texture2D(inputImageTexture, newTextureCoordinate + vec2(-0.1 * (scale - 1.0), -0.1 * (scale - 1.0)));\n" +
                    "    \n" +
                    "    // 3d blend color\n" +
                    "    vec3 blendFirstColor = vec3(textureColor.r, textureColor.g, shiftColor1.b);\n" +
                    "    vec3 blend3DColor = vec3(shiftColor2.r, blendFirstColor.g, blendFirstColor.b);\n" +
                    "    gl_FragColor = vec4(blend3DColor, textureColor.a);\n" +
                    "}\n";
    private int scaleLocation;

    private float[] scaleArray = new float[]{
            1,
            1.07f,
            1.1f,
            1.13f,
            1.17f,
            1.2f,
            1.2f,
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1};

    private int scaleIndex = 0;

    public Fake3dFilter(Context context) {
        super(NO_FILTER_VERTEX_SHADER, loadShader("fake3d.fsh", context));
    }

    @Override
    public void onInit() {
        super.onInit();
        Log.d("hj", "Fake3dFilter.onInit: ");
        scaleLocation = GLES20.glGetUniformLocation(getProgram(), "scale");
    }


    @Override
    public void onInitialized() {
        super.onInitialized();
        Log.d("hj", "Fake3dFilter.onInitialized: ");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        Log.d("hj", "Fake3dFilter.onDraw: ");

        if (scaleIndex >= scaleArray.length) {
            scaleIndex = 0;
        }
        setFloat(scaleLocation, scaleArray[scaleIndex]);
        scaleIndex++;
    }

    @Override
    public boolean needAutoRefresh() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("hj", "Fake3dFilter.onDestroy: ");
    }

}
