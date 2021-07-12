/*
 * Copyright (C) 2018 CyberAgent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.cyberagent.android.gpuimage.filter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.PointF;
import android.opengl.GLES20;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils;


/**
 * 所有 filter 的基类
 */
public class GPUImageFilter {
    public static final String NO_FILTER_VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "}";
    public static final String NO_FILTER_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            "uniform sampler2D inputImageTexture;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "}";

    private final LinkedList<Runnable> runOnDraw;
    private final String vertexShader;
    private final String fragmentShader;
    protected int glProgId;
    private int glAttribPosition;
    private int glUniformTexture;
    private int glAttribTextureCoordinate;
    private int outputWidth;
    private int outputHeight;
    private boolean isInitialized;

    public GPUImageFilter() {
        this(NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);
    }

    public GPUImageFilter(final String vertexShader, final String fragmentShader) {
        runOnDraw = new LinkedList<>();
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    private final void init() {
        onInit();
        onInitialized();
    }

    public void onInit() {
        glProgId = OpenGlUtils.loadProgram(vertexShader, fragmentShader);
        /**
         * 返回属性变量的位置
         * GLint glGetAttribLocation（GLuint program,const GLchar *name）;
         *
         * 参数
         * program：指定要查询的程序对象。
         * name：要查询其位置的属性变量的名称。
         *
         * 描述
         * glGetAttribLocation查询由program指定的先前链接的程序对象，用于name指定的属性变量，并返回绑定到该属性变量的通用顶点属性的索引。
         * 如果name是矩阵属性变量，则返回矩阵的第一列的索引。 如果指定的属性变量不是指定程序对象中的活动属性，或者名称以保留前缀“gl_”开头，则返回-1。
         * 可以通过调用glBindAttribLocation随时指定属性变量名和通用属性索引之间的关联。 在调用glLinkProgram之前，属性绑定不会生效。 成功链接程序对象后，
         * 属性变量的索引值将保持固定，直到发生下一个链接命令。 如果链接成功，则只能在链接后查询属性值。
         * glGetAttribLocation返回上次为指定程序对象调用glLinkProgram时实际生效的绑定。
         * glGetAttribLocation不返回自上次链接操作以来指定的属性绑定。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667174
         */
        glAttribPosition = GLES20.glGetAttribLocation(glProgId, "position");

        /**
         * 返回统一变量的位置
         * GLint glGetUniformLocation（GLuint program,const GLchar *name）;
         *
         * 参数
         * program：指定要查询的程序对象。
         * name：要查询其位置的统一变量的名称。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667229
         *
         */
        glUniformTexture = GLES20.glGetUniformLocation(glProgId, "inputImageTexture");
        glAttribTextureCoordinate = GLES20.glGetAttribLocation(glProgId, "inputTextureCoordinate");
        isInitialized = true;
    }

    public void onInitialized() {
    }

    public void ifNeedInit() {
        if (!isInitialized) init();
    }

    public final void destroy() {
        isInitialized = false;
        /**
         * 删除一个程序对象
         * void glDeleteProgram（GLuint program）;
         *
         * 参数
         * program：指定要删除的程序对象。
         *
         * 描述
         * glDeleteProgram释放内存并使与着色器指定的着色器对象关联的ID无效。 这个命令有效地撤消了对glCreateProgram的调用的影响。
         * 如果程序对象正在被用作当前渲染状态的一部分，则它将被标记为删除，但在它不再是任何渲染上下文的当前状态的一部分之前不会被删除。
         * 如果要删除的程序对象附加了着色器对象，那么这些着色器对象将自动分离但不会被删除，除非它们已被标记为先前调用glDeleteShader而被删除。 程序的值0为0将被忽视。
         * 要确定对象是否已标记为删除，请使用参数program和GL_DELETE_STATUS调用glGetProgramiv。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82666987
         *
         */
        GLES20.glDeleteProgram(glProgId);
        onDestroy();
    }

    public void onDestroy() {
    }

    public void onOutputSizeChanged(final int width, final int height) {
        outputWidth = width;
        outputHeight = height;
    }

    public void onDraw(final int textureId, final FloatBuffer cubeBuffer,
                       final FloatBuffer textureBuffer) {
        GLES20.glUseProgram(glProgId);
        runPendingOnDrawTasks();
        if (!isInitialized) {
            return;
        }

        cubeBuffer.position(0);
        /**
         *  定义通用顶点属性数据的数组
         *  void glVertexAttribPointer（GLuint index,GLint size,GLenum type,GLboolean normalized,GLsizei stride,const GLvoid * pointer）;
         *
         * 参数
         * index：指定要修改的通用顶点属性的索引。
         * size：指定每个通用顶点属性的组件数。 必须为1,2,3或4.初始值为4。
         * type：指定数组中每个组件的数据类型。 接受符号常量GL_BYTE，GL_UNSIGNED_BYTE，GL_SHORT，GL_UNSIGNED_SHORT，GL_FIXED或GL_FLOAT。 初始值为GL_FLOAT。
         * normalized：指定在访问定点数据值时是应将其标准化（GL_TRUE）还是直接转换为定点值（GL_FALSE）。
         * stride：指定连续通用顶点属性之间的字节偏移量。 如果stride为0，则通用顶点属性被理解为紧密打包在数组中的。 初始值为0。
         * pointer：指定指向数组中第一个通用顶点属性的第一个组件的指针。 初始值为0。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667374
         *
         */
        GLES20.glVertexAttribPointer(glAttribPosition, 2, GLES20.GL_FLOAT, false, 0, cubeBuffer);
        /**
         * 启用或禁用通用顶点属性数组
         * void glEnableVertexAttribArray（GLuint index）;
         *
         * void glDisableVertexAttribArray（GLuint index）;
         *
         * 参数
         * index：指定要启用或禁用的通用顶点属性的索引。
         *
         * 描述
         * glEnableVertexAttribArray启用index指定的通用顶点属性数组。 glDisableVertexAttribArray禁用index指定的通用顶点属性数组。
         * 默认情况下，禁用所有客户端功能，包括所有通用顶点属性数组。
         * 如果启用，将访问通用顶点属性数组中的值，并在调用顶点数组命令（如glDrawArrays或glDrawElements）时用于呈现。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667052
         *
         */
        GLES20.glEnableVertexAttribArray(glAttribPosition);
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(glAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0,
                textureBuffer);
        GLES20.glEnableVertexAttribArray(glAttribTextureCoordinate);
        if (textureId != OpenGlUtils.NO_TEXTURE) {
            /**
             *  激活纹理单元
             *  void glActiveTexture（GLenum texture）;
             *
             * 参数
             * texture：指定要激活的纹理单元，纹理单元的数量依赖于实现，但必须至少为8。texture必须是GL_TEXTUREi之一，
             * 其中i的范围从0到（GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS - 1）。初始值是GL_TEXTURE0。
             *
             * 描述
             * 被glActiveTexture 激活的纹理将会影响其后续的纹理调用状态。
             *
             * 参考：https://blog.csdn.net/flycatdeng/article/details/82595253
             */
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            /**
             * 将一个指定的纹理ID绑定到一个纹理目标上
             *
             * void glBindTexture(GLenum target, GLuint texture);
             *
             * 参数
             * target：指定之前激活了的纹理要绑定到的一个目标。必须是GL_TEXTURE_2D 或GL_TEXTURE_CUBE_MAP。
             * texture：指定纹理ID。
             *
             * 参考：https://blog.csdn.net/flycatdeng/article/details/82664549
             *
             */
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            /**
             * 指定当前程序对象的统一变量的值
             * 参考：https://blog.csdn.net/flycatdeng/article/details/82667357
             *
             */
            GLES20.glUniform1i(glUniformTexture, 0);
        }
        onDrawArraysPre();
        /**
         * 从数组数据中渲染图元
         * void glDrawArrays（GLenum mode，GLint first,GLsizei count）;
         *
         * 参数
         * mode：指定要渲染的图元类型。 接受符号常量GL_POINTS，GL_LINE_STRIP，GL_LINE_LOOP，GL_LINES，GL_TRIANGLE_STRIP，GL_TRIANGLE_FAN和GL_TRIANGLES。
         * first：指定已启用阵列中的起始索引。
         * count：指定要渲染的索引数。
         *
         * 描述
         * glDrawArrays指定了几个子例程调用的几何图元。你可以使用glVertexAttribPointer预先指定单独的顶点，法线和颜色数组，
         * 而不是调用GL过程来传递每个单独的顶点属性并使用它们通过单次调用glDrawArrays来构造图元序列。
         * 当调用glDrawArrays时，它使用每个启用数组中的计数顺序元素来构造几何图元序列，从元素first开始。mode指定构造什么类型的图元以及数组元素如何构造这些图元。
         * 要启用和禁用通用顶点属性数组，请调用glEnableVertexAttribArray和glDisableVertexAttribArray。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667044
         */
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        /**
         * 启用或禁用通用顶点属性数组
         * void glEnableVertexAttribArray（GLuint index）;
         * void glDisableVertexAttribArray（GLuint index）;
         *
         * 参数
         * index：指定要启用或禁用的通用顶点属性的索引。
         *
         * 描述
         * glEnableVertexAttribArray启用index指定的通用顶点属性数组。 glDisableVertexAttribArray禁用index指定的通用顶点属性数组。
         * 默认情况下，禁用所有客户端功能，包括所有通用顶点属性数组。
         * 如果启用，将访问通用顶点属性数组中的值，并在调用顶点数组命令（如glDrawArrays或glDrawElements）时用于呈现。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667052
         */
        GLES20.glDisableVertexAttribArray(glAttribPosition);
        GLES20.glDisableVertexAttribArray(glAttribTextureCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    protected void onDrawArraysPre() {
    }

    protected void runPendingOnDrawTasks() {
        synchronized (runOnDraw) {
            while (!runOnDraw.isEmpty()) {
                runOnDraw.removeFirst().run();
            }
        }
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public int getOutputWidth() {
        return outputWidth;
    }

    public int getOutputHeight() {
        return outputHeight;
    }

    public int getProgram() {
        return glProgId;
    }

    public int getAttribPosition() {
        return glAttribPosition;
    }

    public int getAttribTextureCoordinate() {
        return glAttribTextureCoordinate;
    }

    public int getUniformTexture() {
        return glUniformTexture;
    }

    protected void setInteger(final int location, final int intValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniform1i(location, intValue);
            }
        });
    }

    protected void setFloat(final int location, final float floatValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniform1f(location, floatValue);
            }
        });
    }

    protected void setFloatVec2(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniform2fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatVec3(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniform3fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatVec4(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniform4fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatArray(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniform1fv(location, arrayValue.length, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setPoint(final int location, final PointF point) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                ifNeedInit();
                float[] vec2 = new float[2];
                vec2[0] = point.x;
                vec2[1] = point.y;
                GLES20.glUniform2fv(location, 1, vec2, 0);
            }
        });
    }

    protected void setUniformMatrix3f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {

            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniformMatrix3fv(location, 1, false, matrix, 0);
            }
        });
    }

    protected void setUniformMatrix4f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {

            @Override
            public void run() {
                ifNeedInit();
                GLES20.glUniformMatrix4fv(location, 1, false, matrix, 0);
            }
        });
    }

    protected void runOnDraw(final Runnable runnable) {
        synchronized (runOnDraw) {
            runOnDraw.addLast(runnable);
        }
    }

    public static String loadShader(String file, Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream ims = assetManager.open(file);

            String re = convertStreamToString(ims);
            ims.close();
            return re;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }


    /**
     * 是否需要自动刷新（用于动画）
     * @return
     */
    public boolean needAutoRefresh() {
        return false;
    }
}
