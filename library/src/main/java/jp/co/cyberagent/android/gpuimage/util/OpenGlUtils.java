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

package jp.co.cyberagent.android.gpuimage.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.hardware.Camera.Size;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.IntBuffer;

public class OpenGlUtils {
    public static final int NO_TEXTURE = -1;

    public static int loadTexture(final Bitmap img, final int usedTexId) {
        return loadTexture(img, usedTexId, true);
    }

    public static int loadTexture(final Bitmap img, final int usedTexId, final boolean recycle) {
        int textures[] = new int[1];
        if (usedTexId == NO_TEXTURE) {
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
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
            /**
             * 设置纹理参数
             * void glTexParameterf(GLenum target,GLenum pname,GLfloat param);
             *
             * void glTexParameteri（GLenum target,GLenum pname,GLint param）；
             *
             * void glTexParameterfv（GLenum target,GLenum pname,const GLfloat * params）；
             *
             * void glTexParameteriv（GLenum target,GLenum pname,const GLint * params）；
             *
             * 参数
             * target:指定之前激活了的纹理要绑定到的一个目标。必须是GL_TEXTURE_2D 或GL_TEXTURE_CUBE_MAP。
             * pname:指定一个单值纹理参数的符号名，pname可以是下列值之一：GL_TEXTURE_MIN_FILTER GL_TEXTURE_MAG_FILTER GL_TEXTURE_WRAP_S GL_TEXTURE_WRAP_T。
             * param:定pname的值。
             * params:指定pname存储的值的数组的指针。
             *
             * 参考：https://blog.csdn.net/flycatdeng/article/details/82595267
             *
             */
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

            /**
             * 指定一个二维的纹理图片
             * void glTexImage2D(GLenum target, GLint level, GLint internalformat, GLsizei width, GLsizei height, GLint border,
             * GLenum format,GLenum type,const GLvoid * data);
             *
             * 参数
             * target：定活动纹理单元的目标纹理。必须是GL_TEXTURE_2D,GL_TEXTURE_CUBE_MAP_POSITIVE_X,GL_TEXTURE_CUBE_MAP_NEGATIVE_X,GL_TEXTURE_CUBE_MAP_POSITIVE_Y,GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,GL_TEXTURE_CUBE_MAP_POSITIVE_Z,或GL_TEXTURE_CUBE_MAP_NEGATIVE_Z.
             * level：指定细节级别，0级表示基本图像，n级则表示Mipmap缩小n级之后的图像（缩小2^n）
             * internalformat：指定纹理内部格式，必须是下列符号常量之一：GL_ALPHA，GL_LUMINANCE，GL_LUMINANCE_ALPHA，GL_RGB，GL_RGBA。
             * width height：指定纹理图像的宽高，所有实现都支持宽高至少为64 纹素的2D纹理图像和宽高至少为16 纹素的立方体贴图纹理图像 。
             * border：指定边框的宽度。必须为0。
             * format：指定纹理数据的格式。必须匹配internalformat。下面的符号值被接受：GL_ALPHA，GL_RGB，GL_RGBA，GL_LUMINANCE，和GL_LUMINANCE_ALPHA。
             * type：指定纹理数据的数据类型。下面的符号值被接受：GL_UNSIGNED_BYTE，GL_UNSIGNED_SHORT_5_6_5，GL_UNSIGNED_SHORT_4_4_4_4，和GL_UNSIGNED_SHORT_5_5_5_1。
             * data：指定一个指向内存中图像数据的指针。
             *
             * 参考：https://blog.csdn.net/flycatdeng/article/details/82667350
             *
             */
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, img, 0);
        } else {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, usedTexId);
            /**
             * 指定二维纹理子图像
             * void glTexSubImage2D（GLenum target,GLint level,GLint xoffset,GLint yoffset,GLsizei width,
             * GLsizei height,GLenum format,GLenum type,const GLvoid * data）;
             *
             * 参数
             * target：指定活动纹理单元的目标纹理。 必须是GL_TEXTURE_2D，GL_TEXTURE_CUBE_MAP_POSITIVE_X，GL_TEXTURE_CUBE_MAP_NEGATIVE_X，GL_TEXTURE_CUBE_MAP_POSITIVE_Y，GL_TEXTURE_CUBE_MAP_NEGATIVE_Y，GL_TEXTURE_CUBE_MAP_POSITIVE_Z或GL_TEXTURE_CUBE_MAP_NEGATIVE_Z。
             * level：指定详细级别编号。 0级是基本图像级别。 级别n是第n个mipmap缩小图像。
             * xoffset：指定纹理数组中x方向的纹素偏移。
             * yoffset：指定纹理数组中y方向的纹素偏移。
             * width：定纹理子图像的宽度。
             * height：指定纹理子图像的高度。
             * format：指定像素数据的格式。 接受以下符号值：GL_ALPHA，GL_RGB，GL_RGBA，GL_LUMINANCE和GL_LUMINANCE_ALPHA。
             * type：指定像素数据的数据类型。 接受以下符号值：GL_UNSIGNED_BYTE，GL_UNSIGNED_SHORT_5_6_5，GL_UNSIGNED_SHORT_4_4_4_4和GL_UNSIGNED_SHORT_5_5_5_1。
             * data：指定指向内存中图像数据的指针。
             *
             * 参考：https://blog.csdn.net/flycatdeng/article/details/82667353
             *
             */
            GLUtils.texSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, img);
            textures[0] = usedTexId;
        }
        if (recycle) {
            img.recycle();
        }
        return textures[0];
    }

    public static int loadTexture(final IntBuffer data, final int width, final int height, final int usedTexId) {
        int textures[] = new int[1];
        if (usedTexId == NO_TEXTURE) {
            GLES20.glGenTextures(1, textures, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height,
                    0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, data);
        } else {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, usedTexId);
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, width,
                    height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, data);
            textures[0] = usedTexId;
        }
        return textures[0];
    }

    public static int loadTextureAsBitmap(final IntBuffer data, final Size size, final int usedTexId) {
        Bitmap bitmap = Bitmap
                .createBitmap(data.array(), size.width, size.height, Config.ARGB_8888);
        return loadTexture(bitmap, usedTexId);
    }

    public static int loadShader(final String strSource, final int iType) {
        int[] compiled = new int[1];
        /**
         * 创建一个着色器对象
         * GLuint glCreateShader（GLenum shaderType）;
         *
         * 参数
         * shaderType：指定要创建的着色器的类型。 只能是GL_VERTEX_SHADER或GL_FRAGMENT_SHADER。
         *
         * 描述
         * glCreateShader创建一个空的着色器对象，并返回一个可以引用的非零值（shader ID）。着色器对象用于维护定义着色器的源代码字符串。shaderType指示要创建的着色器的类型。
         * 支持两种类型的着色器。 GL_VERTEX_SHADER类型的着色器是一个用于在可编程顶点处理器上运行的着色器。 GL_FRAGMENT_SHADER类型的着色器是一个着色器，旨在在可编程片段处理器上运行。
         * 创建时，着色器对象的GL_SHADER_TYPE参数设置为GL_VERTEX_SHADER或GL_FRAGMENT_SHADER，具体取决于shaderType的值。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82665045
         */
        int iShader = GLES20.glCreateShader(iType);

        /**
         * 替换着色器对象中的源代码
         * void  glShaderSource（GLuint shader，GLsizei count，const GLchar * const *string，const GLint *length）;
         *
         * 参数
         * shader：要被替换源代码的着色器对象的句柄（ID）。
         * count：指定字符串和长度数组中的元素数。
         * string：指定指向包含要加载到着色器的源代码的字符串的指针数组。
         * length：指定字符串长度的数组。
         *
         * 描述
         * 对于支持着色器编译器的实现，glShaderSource将着色器中的源代码设置为string指定的字符串数组中的源代码。
         * 先前存储在着色器对象中的任何源代码都将被完全替换。数组中的字符串数由count指定。 如果length为NULL，则认为每个字符串都以null结尾。
         * 如果length不是NULL，则它指向包含字符串的每个相应元素的字符串长度的数组。length数组中的每个元素可以包含相应字符串的长度（空字符不计为字符串长度的一部分）
         * 或小于0的值以表示该字符串为空终止。此时不扫描或解析源代码字符串; 它们只是复制到指定的着色器对象中。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667317
         */
        GLES20.glShaderSource(iShader, strSource);
        /**
         * 编译一个着色器对象
         * void glCompileShader（GLuint shader）;
         * 参数
         * shader：指定要编译的着色器对象。
         *
         * 描述
         * 对于支持着色器编译器的实现，glCompileShader编译已存储在shader指定的着色器对象中的源代码字符串。
         * 编译状态将存储为着色器对象的状态的一部分。 如果着色器编译时没有错误并且可以使用，则此值将设置为GL_TRUE，否则将设置为GL_FALSE。
         * 可以通过使用参数shader和GL_COMPILE_STATUS调用glGetShaderiv来查询状态值。
         * 由于OpenGL ES着色语言规范指定的多种原因，着色器的编译可能会失败。 无论编译是否成功，都可以通过调用glGetShaderInfoLog从着色器对象的信息日志中获取有关编译的信息。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667317
         */
        GLES20.glCompileShader(iShader);

        /**
         * 从着色器对象返回一个参数
         *
         * void glGetShaderiv（GLuint shader,GLenum pname,GLint *params）;
         *
         * 参数
         * shader：指定要查询的着色器对象。
         * pname：指定着色器对象的参数。 可接受的符号名称为GL_SHADER_TYPE，GL_DELETE_STATUS，GL_COMPILE_STATUS，GL_INFO_LOG_LENGTH，GL_SHADER_SOURCE_LENGTH。
         * params：返回请求的参数结果值。
         *
         * 描述
         * glGetShaderiv以params形式返回特定着色器对象的参数值。 定义了以下参数：
         * GL_SHADER_TYPE：如果着色器是顶点着色器对象，则params返回GL_VERTEX_SHADER;如果着色器是片段着色器对象，则返回GL_FRAGMENT_SHADER。
         * GL_DELETE_STATUS：如果shader当前被标记为删除，则params返回GL_TRUE，否则返回GL_FALSE。
         * GL_COMPILE_STATUS：对于支持着色器编译器的实现，如果着色器上的最后一次编译操作成功，则params返回GL_TRUE，否则返回GL_FALSE。
         * GL_INFO_LOG_LENGTH：对于支持着色器编译器的实现，params返回着色器信息日志的字符数，包括空终止字符（即，存储信息日志所需的字符缓冲区的大小）。 如果着色器没有信息日志，则返回值0。
         * GL_SHADER_SOURCE_LENGTH：对于支持着色器编译器的实现，params返回构成着色器着色器源的源字符串的串联长度，包括空终止字符。 （即，存储着色器源所需的字符缓冲区的大小）。
         * 如果不存在源代码，则返回0。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667207
         *
         */
        GLES20.glGetShaderiv(iShader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.d("Load Shader Failed", "Compilation\n" + GLES20.glGetShaderInfoLog(iShader));
            return 0;
        }
        return iShader;
    }

    public static int loadProgram(final String strVSource, final String strFSource) {
        int iVShader;
        int iFShader;
        int iProgId;
        int[] link = new int[1];
        iVShader = loadShader(strVSource, GLES20.GL_VERTEX_SHADER);
        if (iVShader == 0) {
            Log.d("Load Program", "Vertex Shader Failed");
            return 0;
        }
        iFShader = loadShader(strFSource, GLES20.GL_FRAGMENT_SHADER);
        if (iFShader == 0) {
            Log.d("Load Program", "Fragment Shader Failed");
            return 0;
        }

        /**
         * 创建一个program（建议不要翻译成“程序”，以免引起与APP的混淆）对象
         * GLuint glCreateProgram（void）;
         *
         * 描述
         * glCreateProgram 创建一个空program并返回一个可以被引用的非零值（program ID）。 program对象是可以附加着色器对象的对象。
         * 这提供了一种机制来指定将链接以创建program的着色器对象。 它还提供了一种检查将用于创建program的着色器的兼容性的方法（例如，检查顶点着色器和片元着色器之间的兼容性）。
         * 当不再需要作为program对象的一部分时，着色器对象就可以被分离了。通过调用glCompileShader成功编译着色器对象，
         * 并且通过调用glAttachShader成功地将着色器对象附加到program 对象，并且通过调用glLinkProgram成功的链接program 对象之后，
         * 可以在program 对象中创建一个或多个可执行文件。当调用glUseProgram时，这些可执行文件成为当前状态的一部分。 可以通过调用glDeleteProgram删除程序对象。
         * 当program 对象不再是任何上下文的当前呈现状态的一部分时，将删除与program 对象关联的内存。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82665041
         */
        iProgId = GLES20.glCreateProgram();

        /**
         * 将着色器对象附加到program对象
         * void glAttachShader（GLuint program,GLuint shader）;
         *
         * 参数
         * program：指定着色器对象将附加到的program对象。
         * shader：指定要附加的着色器对象。
         *
         * 描述
         * 为了创建一个可执行文件，必须要有一种方法来指定将被链接在一起的东西的列表。那么，program对象就提供了这么一种机制。
         * 要在program对象中链接的shaders必须首先附加到该program对象上。那glAttachShader方法就是用于将指定的shaders附着到指定的program对象上。
         * 这就表明shader将被包含在要被执行的program的链接操作中。不管shader对象是否被附着到program对象上，在shader对象上执行的所有操作都是有效的。
         * 在源代码加载到着色器对象之前或着色器对象被编译之前，将shader对象附着到program对象上都是被允许的。
         * 多个同类型（例如都是vertex shader类型，或都是fragment shader类型）的shader对象不能被附着到同一个program对象上。但是，单个shader对象可以被附着到多个program对象上。
         * 如果着色器对象在附加到程序对象时被删除，它将被标记为删除，并且直到调用glDetachShader才能将其从它所连接的所有程序对象中分离出来，否则删除将不会发生。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82664043
         *
         */
        GLES20.glAttachShader(iProgId, iVShader);
        GLES20.glAttachShader(iProgId, iFShader);

        /**
         * 连接一个program对象。
         * void glLinkProgram（GLuint program）;
         *
         * 参数
         * program：指定要链接的program对象的句柄。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667282
         *
         */
        GLES20.glLinkProgram(iProgId);

        /**
         * 从program对象返回一个参数的值
         * void glGetProgramiv（GLuint program,GLenum pname,GLint *params）;
         *
         * 参数
         * program：指定要查询的program对象。
         * pname：指定program对象参数。 接受的符号名称为GL_DELETE_STATUS，GL_LINK_STATUS，GL_VALIDATE_STATUS，GL_INFO_LOG_LENGTH，GL_ATTACHED_SHADERS，GL_ACTIVE_ATTRIBUTES，GL_ACTIVE_UNIFORMS，GL_ACTIVE_ATTRIBUTE_MAX_LENGTH，GL_ACTIVE_UNIFORM_MAX_LENGTH。
         * params：返回请求的对象参数的值。
         *
         * 参考：https://blog.csdn.net/flycatdeng/article/details/82667188
         *
         */
        GLES20.glGetProgramiv(iProgId, GLES20.GL_LINK_STATUS, link, 0);
        if (link[0] <= 0) {
            Log.d("Load Program", "Linking Failed");
            return 0;
        }
        /**
         * 删除一个着色器对象
         * void glDeleteShader（GLuint shader）;
         *
         * 参数
         * shader：指定要删除的着色器对象。
         *
         * 描述
         * glDeleteShader释放内存并使与着色器指定的着色器对象关联的ID无效。 这个命令有效地撤消了对glCreateShader的调用的影响。
         * 如果要删除的着色器对象附加到程序对象，它将被标记为删除，但它不会被删除，直到它不再附加到任何程序对象，
         * 对于任何渲染上下文（即，它必须与 它被附加之前的任何地方都将被删除）。shader为0将被忽视。
         * 要确定对象是否已标记为删除，请使用参数shader和GL_DELETE_STATUS调用glGetShaderiv。
         *
         *
         */
        GLES20.glDeleteShader(iVShader);
        GLES20.glDeleteShader(iFShader);
        return iProgId;
    }

    public static float rnd(final float min, final float max) {
        float fRandNum = (float) Math.random();
        return min + (max - min) * fRandNum;
    }
}
