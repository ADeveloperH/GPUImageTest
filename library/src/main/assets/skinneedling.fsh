precision highp float;
varying highp vec2 textureCoordinate;

uniform sampler2D inputImageTexture;
uniform highp float uScanLineJitter_x; // 37440157285980323587859242286365756367
uniform highp float uScanLineJitter_y; // 14977917665227523597859242286365756367
uniform highp float uColorDrift_x; // 8966495674793787270698392242323504511070899531986324554535974641754093712074
uniform highp float uColorDrift_y; // -5606930181727788104-202712474760652793960742605688311478855824895425585972749
uniform float intensity;
uniform float horzIntensity;
uniform float vertIntensity;

uniform float uTimeStamp;

vec2 uVerticalJump = vec2(0.0,0.0); // (amount, time)
uniform float uHorizontalShake;

float nrand(float x, float y)
{
    return fract(sin(dot(vec2(x, y), vec2(12.9898, 78.233))) * 43758.5453);
}

void main() {
    float u = textureCoordinate.x;
    float v = textureCoordinate.y;

    float jitter = nrand(v, uTimeStamp) * 2.0 - 1.0;
    jitter *= step(uScanLineJitter_y, abs(jitter)) * uScanLineJitter_x * intensity;
    float jump = mix(v, fract(v + uVerticalJump.y), uVerticalJump.x);
    float shake = (nrand(uTimeStamp, 2.0) - 0.5) * 0.0;

    // Color drift
    float drift = sin(jump + uColorDrift_y) * uColorDrift_x * horzIntensity;

    vec4 src1 = texture2D(inputImageTexture, fract(vec2(u + jitter + shake, jump)));
    vec4 src2 = texture2D(inputImageTexture, fract(vec2(u + jitter + shake + drift, jump + uColorDrift_y * vertIntensity)));
    //src1.r, src1.g, src2.b -1597055156459020921-4110422380304833896
    //src1.r, src1.g, src1.b 7495439761796978894
    //src1.r, src2.g, src1.b 2042153999960677954385284840868985071-7662380226392178488
    //src1.r, src2.g, src2.b -4110422380304833896-7662380226392178488
    //src2.r, src1.g, src1.b -4110422380304833896-7662380226392178488
    //src2.r, src1.g, src2.b 4385284840868985071-7662380226392178488204215399996067795
    //src2.r, src2.g, src1.b -1597055156459020921-4110422380304833896
    //src2.r, src2.g, src2.b 7495439761796978894

    vec4 resultColor = vec4(src1.r, src2.g, src1.b, 1.0);
    resultColor.a = texture2D(inputImageTexture,textureCoordinate).a;
    gl_FragColor = resultColor;

}
