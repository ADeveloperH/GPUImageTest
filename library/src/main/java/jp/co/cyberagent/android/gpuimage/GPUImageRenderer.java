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

package jp.co.cyberagent.android.gpuimage;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.util.OpenGlUtils;
import jp.co.cyberagent.android.gpuimage.util.Rotation;
import jp.co.cyberagent.android.gpuimage.util.TextureRotationUtil;

import static jp.co.cyberagent.android.gpuimage.util.TextureRotationUtil.TEXTURE_NO_ROTATION;

public class GPUImageRenderer implements GLSurfaceView.Renderer, GLTextureView.Renderer, PreviewCallback {
    private static final int NO_IMAGE = -1;
    public static final float CUBE[] = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f,
    };

    private GPUImageFilter filter;

    public final Object surfaceChangedWaiter = new Object();

    private int glTextureId = NO_IMAGE;
    private SurfaceTexture surfaceTexture = null;
    private final FloatBuffer glCubeBuffer;
    private final FloatBuffer glTextureBuffer;
    private IntBuffer glRgbBuffer;

    private int outputWidth;
    private int outputHeight;
    private int imageWidth;
    private int imageHeight;
    private int addedPadding;

    private final Queue<Runnable> runOnDraw;
    private final Queue<Runnable> runOnDrawEnd;
    private Rotation rotation;
    private boolean flipHorizontal;
    private boolean flipVertical;
    private GPUImage.ScaleType scaleType = GPUImage.ScaleType.CENTER_CROP;

    private float backgroundRed = 0;
    private float backgroundGreen = 0;
    private float backgroundBlue = 0;

    private GPUImage gpuImage;

    public GPUImageRenderer(final GPUImageFilter filter,GPUImage gpuImage) {
        this.gpuImage = gpuImage;
        this.filter = filter;
        runOnDraw = new LinkedList<>();
        runOnDrawEnd = new LinkedList<>();

        glCubeBuffer = ByteBuffer.allocateDirect(CUBE.length * 4)//float 类型 4 字节
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        glCubeBuffer.put(CUBE).position(0);

        glTextureBuffer = ByteBuffer.allocateDirect(TEXTURE_NO_ROTATION.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        setRotation(Rotation.NORMAL, false, false);
    }

    @Override
    public void onSurfaceCreated(final GL10 unused, final EGLConfig config) {
        Log.w("hj", "GPUImageRenderer.onSurfaceCreated: ");
        /**
         * 为颜色缓冲区指定清除值
         * void glClearColor(GLclampf red, GLclampf green, GLclampf blue, GLclampf alpha);
         *
         * 参数
         * red,green,blue,alpha
         * 指定颜色缓冲区清除时的RGBA值，默认都是0
         *
         * 描述
         * glClearColor为glClear清除颜色缓冲区时指定RGBA值（也就是所有的颜色都会被替换成指定的RGBA值）。每个值的取值范围都是0.0~1.0，超出范围的将被截断。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82664971
         */
        GLES20.glClearColor(backgroundRed, backgroundGreen, backgroundBlue, 1);
        /**
         * 启用或禁用服务器端GL功能
         * void glEnable（GLenum cap）;
         * void glDisable（GLenum cap）;
         * 参数
         * cap:指定表示GL功能的符号常量。
         * GL_DEPTH_TEST
         * 如果启用，进行深度比较并更新深度缓冲区。 注意，即使存在深度缓冲区且深度掩码不为零，如果禁用深度测试，也将不会更新深度缓冲区。 请参阅glDepthFunc和glDepthRangef。
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667050
         *
         */
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        filter.ifNeedInit();
    }

    @Override
    public void onSurfaceChanged(final GL10 gl, final int width, final int height) {
        Log.w("hj", "GPUImageRenderer.onSurfaceChanged: ");
        Log.d("hj", "GPUImageRenderer.onSurfaceChanged: width:" + width + "  height:" + height);
        outputWidth = width;
        outputHeight = height;
        /**
         * 设置视口
         * void glViewport（GLint x,GLint y,GLsizei width,GLsizei height）;
         *
         * 参数
         * x, y：指定视口矩形的左下角坐标，以像素为单位，初始值为（0，0）。
         * width, height：指定视口的宽高，当一个GLContext第一次绑定到一个窗口时，width, height就会被指定为该窗口的宽高。
         *
         * 视口宽高会被默认限制到一定的范围内，视具体实现而定，可以通过glGet变量GL_MAX_VIEWPORT_DIMS得到
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667381
         */
        GLES20.glViewport(0, 0, width, height);
        /**
         * 使用程序对象作为当前渲染状态的一部分
         * void glUseProgram（GLuint program）;
         *
         * 参数
         * program：指定程序对象的句柄，该程序对象的可执行文件将用作当前渲染状态的一部分。
         * 如果program为0，则当前呈现状态引用无效的程序对象，并且会使得任何glDrawArrays或glDrawElements命令的顶点和片段着色器执行的结果未定义。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667360
         *
         */
        GLES20.glUseProgram(filter.getProgram());
        filter.onOutputSizeChanged(width, height);
        adjustImageScaling();
        synchronized (surfaceChangedWaiter) {
            surfaceChangedWaiter.notifyAll();
        }
    }

    @Override
    public void onDrawFrame(final GL10 gl) {
        Log.w("hj", "GPUImageRenderer.onDrawFrame: ");
        /**
         * 清除预设值的缓冲区
         * void glClear（GLbitfield mask）;
         *
         * 参数
         * mask：使用掩码的按位异或运算来表示要清除的缓冲区。 三个掩码是GL_COLOR_BUFFER_BIT，GL_DEPTH_BUFFER_BIT和GL_STENCIL_BUFFER_BIT。
         *
         * 描述
         * glClear将窗口的位平面区域设置为先前由glClearColor，glClearDepthf和glClearStencil设置的值。
         * 像素的归属测试，裁剪测试，抖动和缓冲区按位掩码都会影响glClear的操作。裁剪箱限定了清除区域。glClear忽略混合函数，模板，片元着色和深度缓冲。
         * glClear采用单个参数，该参数是多个值的按位异或，指示要清除哪个缓冲区。
         * 值如下：
         * GL_COLOR_BUFFER_BIT：表示当前启用了颜色写入的缓冲区。
         * GL_DEPTH_BUFFER_BIT：深度缓冲区。
         * GL_STENCIL_BUFFER_BIT：指示模板缓冲区。
         *
         * 清除每个缓冲区的值取决于该缓冲区的清除值的设置。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82664964
         */
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        runAll(runOnDraw);
        filter.onDraw(glTextureId, glCubeBuffer, glTextureBuffer);
        runAll(runOnDrawEnd);
        if (surfaceTexture != null) {
            surfaceTexture.updateTexImage();
        }
        if (this.gpuImage != null && filter.needAutoRefresh()) {
            this.gpuImage.requestRender();
        }
    }

    /**
     * Sets the background color
     *
     * @param red   red color value
     * @param green green color value
     * @param blue  red color value
     */
    public void setBackgroundColor(float red, float green, float blue) {
        backgroundRed = red;
        backgroundGreen = green;
        backgroundBlue = blue;
    }

    private void runAll(Queue<Runnable> queue) {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }

    @Override
    public void onPreviewFrame(final byte[] data, final Camera camera) {
        Log.e("hj", "GPUImageRenderer.onPreviewFrame: ");
        final Size previewSize = camera.getParameters().getPreviewSize();
        onPreviewFrame(data, previewSize.width, previewSize.height);
    }

    public void onPreviewFrame(final byte[] data, final int width, final int height) {
        if (glRgbBuffer == null) {
            glRgbBuffer = IntBuffer.allocate(width * height);
        }
        if (runOnDraw.isEmpty()) {
            runOnDraw(new Runnable() {
                @Override
                public void run() {
                    GPUImageNativeLibrary.YUVtoRBGA(data, width, height, glRgbBuffer.array());
                    glTextureId = OpenGlUtils.loadTexture(glRgbBuffer, width, height, glTextureId);

                    if (imageWidth != width) {
                        imageWidth = width;
                        imageHeight = height;
                        adjustImageScaling();
                    }
                }
            });
        }
    }

    public void setUpSurfaceTexture(final Camera camera) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                int[] textures = new int[1];
                /**
                 * 生成纹理名称（ID）
                 * void glGenTextures( GLsizei n,GLuint * textures);
                 *
                 * 参数
                 * n：指定要生成的纹理ID的数量。
                 * textures：指定存储生成的纹理ID的数组。
                 *
                 * 描述
                 * glGenTextures产生ｎ个纹理ID存储在textures数组中，这个方法并不保存返回的是一串连续的整数数组，但是能保证的是：这些ID在调用glGenTextures之前都没有正在被使用。
                 * 生成的textures此时还是没有维度的，当他们第一次绑定纹理目标时才被指定维度（见glBindTexture）。
                 * 通过调用glGenTextures返回的纹理ID不会被后续调用返回，除非首先使用glDeleteTextures删除它们。
                 *
                 * 参考：https://blog.csdn.net/flycatdeng/article/details/82667152
                 *
                 */
                GLES20.glGenTextures(1, textures, 0);
                surfaceTexture = new SurfaceTexture(textures[0]);
                try {
                    camera.setPreviewTexture(surfaceTexture);
                    camera.setPreviewCallback(GPUImageRenderer.this);
                    camera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setFilter(final GPUImageFilter filter) {
        runOnDraw(new Runnable() {

            @Override
            public void run() {
                final GPUImageFilter oldFilter = GPUImageRenderer.this.filter;
                GPUImageRenderer.this.filter = filter;
                if (oldFilter != null) {
                    oldFilter.destroy();
                }
                GPUImageRenderer.this.filter.ifNeedInit();
                GLES20.glUseProgram(GPUImageRenderer.this.filter.getProgram());
                GPUImageRenderer.this.filter.onOutputSizeChanged(outputWidth, outputHeight);
            }
        });
    }

    public void deleteImage() {
        runOnDraw(new Runnable() {

            @Override
            public void run() {
                GLES20.glDeleteTextures(1, new int[]{
                        glTextureId
                }, 0);
                glTextureId = NO_IMAGE;
            }
        });
    }

    public void setImageBitmap(final Bitmap bitmap) {
        setImageBitmap(bitmap, true);
    }

    public void setImageBitmap(final Bitmap bitmap, final boolean recycle) {
        if (bitmap == null) {
            return;
        }

        runOnDraw(new Runnable() {

            @Override
            public void run() {
                Bitmap resizedBitmap = null;
                if (bitmap.getWidth() % 2 == 1) {
                    resizedBitmap = Bitmap.createBitmap(bitmap.getWidth() + 1, bitmap.getHeight(),
                            Bitmap.Config.ARGB_8888);
                    Canvas can = new Canvas(resizedBitmap);
                    can.drawARGB(0x00, 0x00, 0x00, 0x00);
                    can.drawBitmap(bitmap, 0, 0, null);
                    addedPadding = 1;
                } else {
                    addedPadding = 0;
                }

                glTextureId = OpenGlUtils.loadTexture(
                        resizedBitmap != null ? resizedBitmap : bitmap, glTextureId, recycle);
                if (resizedBitmap != null) {
                    resizedBitmap.recycle();
                }
                imageWidth = bitmap.getWidth();
                imageHeight = bitmap.getHeight();
                adjustImageScaling();
            }
        });
    }

    public void setScaleType(GPUImage.ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    protected int getFrameWidth() {
        return outputWidth;
    }

    protected int getFrameHeight() {
        return outputHeight;
    }

    private void adjustImageScaling() {
        float outputWidth = this.outputWidth;
        float outputHeight = this.outputHeight;
        if (rotation == Rotation.ROTATION_270 || rotation == Rotation.ROTATION_90) {
            outputWidth = this.outputHeight;
            outputHeight = this.outputWidth;
        }

        float ratio1 = outputWidth / imageWidth;
        float ratio2 = outputHeight / imageHeight;
        float ratioMax = Math.max(ratio1, ratio2);
        int imageWidthNew = Math.round(imageWidth * ratioMax);
        int imageHeightNew = Math.round(imageHeight * ratioMax);

        float ratioWidth = imageWidthNew / outputWidth;
        float ratioHeight = imageHeightNew / outputHeight;

        float[] cube = CUBE;
        float[] textureCords = TextureRotationUtil.getRotation(rotation, flipHorizontal, flipVertical);
        if (scaleType == GPUImage.ScaleType.CENTER_CROP) {
            float distHorizontal = (1 - 1 / ratioWidth) / 2;
            float distVertical = (1 - 1 / ratioHeight) / 2;
            textureCords = new float[]{
                    addDistance(textureCords[0], distHorizontal), addDistance(textureCords[1], distVertical),
                    addDistance(textureCords[2], distHorizontal), addDistance(textureCords[3], distVertical),
                    addDistance(textureCords[4], distHorizontal), addDistance(textureCords[5], distVertical),
                    addDistance(textureCords[6], distHorizontal), addDistance(textureCords[7], distVertical),
            };
        } else {
            cube = new float[]{
                    CUBE[0] / ratioHeight, CUBE[1] / ratioWidth,
                    CUBE[2] / ratioHeight, CUBE[3] / ratioWidth,
                    CUBE[4] / ratioHeight, CUBE[5] / ratioWidth,
                    CUBE[6] / ratioHeight, CUBE[7] / ratioWidth,
            };
        }

        glCubeBuffer.clear();
        glCubeBuffer.put(cube).position(0);
        glTextureBuffer.clear();
        glTextureBuffer.put(textureCords).position(0);
    }

    private float addDistance(float coordinate, float distance) {
        return coordinate == 0.0f ? distance : 1 - distance;
    }

    public void setRotationCamera(final Rotation rotation, final boolean flipHorizontal,
                                  final boolean flipVertical) {
        setRotation(rotation, flipVertical, flipHorizontal);
    }

    public void setRotation(final Rotation rotation) {
        this.rotation = rotation;
        adjustImageScaling();
    }

    public void setRotation(final Rotation rotation,
                            final boolean flipHorizontal, final boolean flipVertical) {
        this.flipHorizontal = flipHorizontal;
        this.flipVertical = flipVertical;
        setRotation(rotation);
    }

    public Rotation getRotation() {
        return rotation;
    }

    public boolean isFlippedHorizontally() {
        return flipHorizontal;
    }

    public boolean isFlippedVertically() {
        return flipVertical;
    }

    protected void runOnDraw(final Runnable runnable) {
        synchronized (runOnDraw) {
            runOnDraw.add(runnable);
        }
    }

    protected void runOnDrawEnd(final Runnable runnable) {
        synchronized (runOnDrawEnd) {
            runOnDrawEnd.add(runnable);
        }
    }
}
