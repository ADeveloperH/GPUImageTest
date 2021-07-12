precision highp float;
varying vec2 textureCoordinate;
uniform sampler2D inputImageTexture;

uniform float inputHeight;
uniform float inputWidth;
uniform float rippleProgress;//0-1

bool inCircle(vec2 newTextureCoordinate, float radius, float inputWidth, float inputHeight){
    vec2 resolution = vec2(inputWidth, inputHeight);
    float realRadius = inputWidth * radius;
    float dis = distance(newTextureCoordinate * resolution, resolution * vec2(0.5));
    if (dis <= realRadius){
        return true;
    }
    return false;
}


float sinc(float x) {
    return (sin(x) / x);
}

vec2 getRipple(vec2 newTextureCoordinate, float inputWidth, float inputHeight, float time){
    float range = 0.5;
    float Speed = 1.0;
    float region = 0.5;
    float PI = 3.14159265;
    vec2 uv = newTextureCoordinate * 2.0 -1.0;
    float aspect = inputWidth / inputHeight;
    uv.x *= aspect;

    vec2 pos = vec2(0.0, 0.0);

    float dist = length(uv - pos)*range;
    float iTime = mod(time, 100.) * Speed;
    float rippleRadius = iTime * 0.95;
    rippleRadius = clamp(rippleRadius, 0.0, 2.0);
    float diff = rippleRadius - dist;

    float func = sinc(PI * diff/0.2 - 3.0);


    vec2 offset = uv * func * 0.9;

    float intensity = region - dist;
    intensity = clamp(intensity, 0.0, 1.2);


    offset *= intensity;

    uv += offset;
    uv.x /= aspect;
    uv = (uv + 1.0) /2.;
    return uv;
}

void main()
{
    vec2 newTextureCoordinate = textureCoordinate;
    vec4 resultColor = vec4(0.0);
    if (inCircle(newTextureCoordinate, rippleProgress, inputWidth, inputHeight)){
        newTextureCoordinate = getRipple(newTextureCoordinate, inputWidth, inputHeight, rippleProgress);
    }
    resultColor = texture2D(inputImageTexture, newTextureCoordinate);
    gl_FragColor = resultColor;
}
