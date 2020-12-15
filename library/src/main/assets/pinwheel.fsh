precision highp float;
varying vec2 textureCoordinate;

uniform sampler2D inputImageTexture;
//uniform sampler2D inputImageTexture2;
uniform int imageWidth;
uniform int imageHeight;
uniform float progress;
uniform float speed; // = 2.0;
const float count = 4.;//分割的个数
#define PI 3.14159265358

vec4 getFromColor(vec2 p)
{
    return texture2D(inputImageTexture,p);
}

vec4 getToColor(vec2 p)
{
//    return texture2D(inputImageTexture2,p);
    return vec4(0.0,0.0,0.0,1.0);
}

vec4 transition(vec2 uv)
{
    vec2 p = uv.xy / vec2(1.0).xy;

    float circPos = atan(p.y - 0.5, p.x - 0.5) + progress * speed;
    float modPos = mod(circPos, PI / count);
    float signed = sign(progress - modPos);

    return mix(getToColor(p), getFromColor(p), step(signed, 0.5));
}

void main(void)
{
    vec4 resultColor = transition(textureCoordinate);
    gl_FragColor=resultColor;
}

