precision highp float;
varying vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
const float size = 0.5;

uniform float inputHeight;
uniform float inputWidth;


void main(void)
{
    vec2 realSize = vec2(inputWidth, inputHeight);
    float ratio = (realSize.x > realSize.y) ?
    realSize.y/realSize.x : realSize.x/realSize.y;

    vec2 texSize = vec2(inputWidth, inputHeight);
    vec2 xy = gl_FragCoord.xy;

    if (realSize.x > realSize.y)
    {
        xy.x = xy.x * ratio;
    }
    else
    {
        xy.y = xy.y * ratio;
    }

    vec2 center = vec2(inputWidth/2.);

    // ----------------------------------------------------

    float maxV = dot(center, center);
    float minV = floor(maxV*(1. - size));
    float diff = maxV - minV;

    vec2 uv = xy / texSize;

    vec4 srcColor = texture2D(inputImageTexture, uv);

    float dx = center.x - xy.x;
    float dy = center.y - xy.y;

    float dstSq = pow(dx, 2.) + pow(dy, 2.);

    float v = (dstSq / diff);
    float r = clamp(srcColor.r + v, 0., 1.);
    float g = clamp(srcColor.g + v, 0., 1.);
    float b = clamp(srcColor.b + v, 0., 1.);

    gl_FragColor = vec4(r, g, b, 1.0);
}