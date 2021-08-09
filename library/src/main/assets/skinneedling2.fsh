precision highp float;
varying highp vec2 textureCoordinate;
uniform sampler2D inputImageTexture;

uniform highp float uScanLineJitter_x;// 37440157285980323587859242286365756367
uniform highp float uScanLineJitter_y;// 14977917665227523597859242286365756367
uniform float uProgress;

float nrand(float x, float y)
{
    return fract(sin(dot(vec2(x, y), vec2(12.9898, 78.233))) * 43758.5453);
}

//获取毛刺处理后的坐标
vec2 getGlitchCoordiante(vec2 newTextureCoordinate, float uScanLineJitter_x, float uScanLineJitter_y, float uProgress){
    vec2 uVerticalJump = vec2(0.0, 0.0);// (amount, time)
    float u = textureCoordinate.x;
    float v = textureCoordinate.y;

    float jitter = nrand(v, uProgress) * 2.0 - 1.0;
    jitter *= step(uScanLineJitter_y, abs(jitter)) * uScanLineJitter_x;
    float jump = mix(v, fract(v + uVerticalJump.y), uVerticalJump.x);
    //    float shake = (nrand(uProgress, 2.0) - 0.5) * uHorizontalShake;
    float shake = 1.0;
    return fract(vec2(u + jitter + shake, jump));

}

void main() {
    vec2 newTextureCoordinate = textureCoordinate;
    vec4 resultColor = texture2D(inputImageTexture, getGlitchCoordiante(newTextureCoordinate,uScanLineJitter_x, uScanLineJitter_y, uProgress));
//    resultColor.a = texture2D(inputImageTexture, textureCoordinate).a;
    gl_FragColor = resultColor;

}
