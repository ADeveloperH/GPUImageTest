precision highp float;
varying highp vec2 textureCoordinate;

uniform sampler2D inputImageTexture;

uniform float inputHeight;
uniform float inputWidth;

uniform float fovPower;


#define MAX_POWER -0.6// negative : anti fish eye. positive = fisheye

//缩放效果
vec2 scaleCenter(vec2 newTextureCoordinate, float scaleValueX, float scaleValueY){
    vec2 scaleCenter = vec2(0.5);
    if (scaleValueX != 0.0){
        scaleValueX = 1.0/scaleValueX;
    }
    if (scaleValueY != 0.0){
        scaleValueY = 1.0/scaleValueY;
    }
    newTextureCoordinate -= scaleCenter;
    newTextureCoordinate = mat2(scaleValueX, 0.0, 0.0, scaleValueY) * newTextureCoordinate;
    newTextureCoordinate += scaleCenter;
    return newTextureCoordinate;
}

void main() {
    vec2 iResolution = vec2(inputWidth, inputHeight);
    //(assume 1:1 prop)
    float prop = iResolution.x / iResolution.y;//screen proroption
    vec2 p = vec2(textureCoordinate.x, textureCoordinate.y / prop);//normalized coords with some cheat
    vec2 m = vec2(0.5, 0.5  / prop);//center coords
    vec2 d = p - m;//vector from center to current fragment
    float r = sqrt(dot(d, d));// distance of pixel from center

    float power = fovPower;

    float bind;//radius of 1:1 effect
    if (power > 0.0)
    bind = sqrt(dot(m, m));//stick to corners
    else { if (prop < 1.0)
    bind = m.x;
    else
    bind = m.y; }//stick to borders

    //Weird formulas
    vec2 uv;
    if (power > 0.0)//fisheye
    uv = m + normalize(d) * tan(r * power) * bind / tan(bind * power);
    else if (power < 0.0)//antifisheye
    uv = m + normalize(d) * atan(r * -power * 10.0) * bind / atan(-power * bind * 10.0);
    else uv = p;//no effect for power = 1.0

    uv.y *= prop;


    uv = scaleCenter(uv,2.0,2.0);
    vec3 col = texture2D(inputImageTexture, uv).rgb;

    // inverted
    //vec3 col = texture2D(inputImageTexture, vec2(uv.x, 1.0 - uv.y)).rgb;//Second part of cheat
    //for round effect, not elliptical
    gl_FragColor = vec4(col, 1.0);
}




