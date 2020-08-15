package jp.co.cyberagent.android.gpuimage.testfilter;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.FloatBuffer;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * @author huangjian
 * @create 2020/8/15
 * @Description
 */
public class SoulScaleFilter extends GPUImageFilter {

    public static final String SOULSCALE_FRAGMENT_SHADER =
            "varying highp vec2 textureCoordinate;\n" +
                    "\n" +
                    "uniform sampler2D inputImageTexture;\n" +
                    "uniform sampler2D inputImageTexture2;\n" +
                    "uniform lowp float mixturePercent;\n" +
                    "uniform highp float scalePercent;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                    "    \n" +
                    "    highp vec2 textureCoordinateToUse = textureCoordinate;\n" +
                    "    highp vec2 center = vec2(0.5, 0.5);\n" +
                    "    textureCoordinateToUse -= center;\n" +
                    "    textureCoordinateToUse = textureCoordinateToUse / scalePercent;\n" +
                    "    textureCoordinateToUse += center;\n" +
                    "    lowp vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinateToUse);\n" +
                    "    \n" +
                    "    gl_FragColor = mix(textureColor, textureColor2, mixturePercent);\n" +
                    "}\n";
    private int mixturePercentLocation;
    private int scalePercentLocation;

    private float[] scalePercentArray = new float[]{
            1.084553f,
            1.173257f,
            1.266176f,
            1.363377f,
            1.464923f,
            1.570877f,
            1.681300f,
            1.796254f,
            1.915799f,
            2.039995f,
            2.168901f,
            2.302574f,
            2.302574f,
            2.302574f,
            2.302574f,
            2.302574f};
    private float[] mixturePercentArray = new float[]{
            0.411498f,
            0.340743f,
            0.283781f,
            0.237625f,
            0.199993f,
            0.169133f,
            0.143688f,
            0.122599f,
            0.037117f,
            0.028870f,
            0.022595f,
            0.017788f,
            0.010000f,
            0.010000f,
            0.010000f,
            0.010000f
    };

    private int scalePercentIndex = 0;
    private int mixturePercentIndex = 0;
    public SoulScaleFilter() {
        super(NO_FILTER_VERTEX_SHADER, SOULSCALE_FRAGMENT_SHADER);
    }

    @Override
    public void onInit() {
        super.onInit();
        Log.d("hj", "SoulScaleFilter.onInit: ");
        mixturePercentLocation = GLES20.glGetUniformLocation(getProgram(), "mixturePercent");
        scalePercentLocation = GLES20.glGetUniformLocation(getProgram(), "scalePercent");
    }


    @Override
    public void onInitialized() {
        super.onInitialized();
        Log.d("hj", "SoulScaleFilter.onInitialized: ");
    }


    @Override
    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        super.onDraw(textureId, cubeBuffer, textureBuffer);
        Log.d("hj", "SoulScaleFilter.onDraw: ");

        if (scalePercentIndex >= scalePercentArray.length) {
            scalePercentIndex = 0;
        }
        if (mixturePercentIndex >= mixturePercentArray.length) {
            mixturePercentIndex = 0;
        }
        setFloat(scalePercentLocation,scalePercentArray[scalePercentIndex]);
        setFloat(mixturePercentLocation,mixturePercentArray[mixturePercentIndex]);

        scalePercentIndex++;
        mixturePercentIndex++;
    }

    @Override
    public boolean needAutoRefresh() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("hj", "SoulScaleFilter.onDestroy: ");
    }

}
