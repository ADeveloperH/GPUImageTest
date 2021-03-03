precision highp float;
varying vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
uniform float blurIntensity;

float gauss (float bhqp, float x) {
    return exp (-(x * x) / (2.0 * bhqp * bhqp));
}

vec4 blur (vec2 uv, sampler2D source, float Intensity) {
    //注意：该值会影响绘制效率。待研究
    const int iterations = 6;// 常量才可以进行for循环
    int halfIterations = iterations / 2;
    float sigmaX = 0.1 + Intensity * 0.5;
    float sigmaY = sigmaX;
    float total = 0.0;
    vec4 ret = vec4 (0., 0., 0., 0.);
    float step = 0.00390625;
    // 增多到8*8个点
    for (int iy = 0; iy < iterations; ++iy) {
        float fy = gauss (sigmaY, float (iy - halfIterations));
        float offsety = float (iy - halfIterations) * step;
        for (int ix = 0; ix < iterations; ++ix) {
            float fx = gauss (sigmaX, float (ix - halfIterations));
            float offsetx = float (ix - halfIterations) * step;
            total += fx * fy;
            vec4 a = texture2D (source, uv + vec2 (offsetx, offsety));
            a.rgb *= a.a;
            ret += a * fx * fy;
        }
    }
    return ret / total;
}


void main() {
    gl_FragColor = blur(textureCoordinate, inputImageTexture, blurIntensity);
}

