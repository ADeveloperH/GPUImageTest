precision highp float;
varying vec2 vTexCoordinate;
uniform sampler2D uTexSampler;
const vec2 center = vec2(1.5, 0.5);
uniform float uRadius;
uniform float uScale;
uniform float uYDiff;


//径向模糊需要设置的值
uniform float uInputHeight;
uniform float uInputWidth;
uniform float uRadialBlurSize;//0-100

//径向模糊效果
const float MAXRADIALBLURSIZE = 100.0;//最大径向模糊数值

//获取径向模糊后的颜色
vec4 getRadialBlurColor(sampler2D inputImageTexture, vec2 newTextureCoordinate, float radialBlurSize){
    if (radialBlurSize < 0.0){
        radialBlurSize = 0.0;
    } else if (radialBlurSize > 100.0){
        radialBlurSize = 100.0;
    }
    float realBlurSize = 0.008 * radialBlurSize / MAXRADIALBLURSIZE;
    highp vec2 dir = newTextureCoordinate - vec2(1.5, 0.5);//方向
    highp vec3 color = vec3(0.0);
    const int len = 10;
    for (int i= -len; i <= len; i++){
        highp vec2 blurCoord = newTextureCoordinate + float(i)*dir*2.0*realBlurSize;
        blurCoord = abs(blurCoord);
        if (blurCoord.x > 1.0){
            blurCoord.x = 2.0 - blurCoord.x;
        }
        if (blurCoord.y > 1.0){
            blurCoord.y = 2.0 - blurCoord.y;
        }
        color += texture2D(inputImageTexture, blurCoord).rgb;
    }
    color /= float(2*len+1);
    return vec4(color, 1.0);
}

bool inOvalArea(vec2 texCoordiante, float b){
    return pow((texCoordiante.x-1.5), 2.0)/pow(0.5, 2.0) + pow((texCoordiante.y-1.0), 2.0) / pow(b, 2.0)<=1.5
    || pow((texCoordiante.x-1.5), 2.0)/pow(0.5, 2.0) + pow(texCoordiante.y, 2.0) / pow(b, 2.0)<=1.5;
}

void main()
{
    vec2 newTextureCoordinate = vTexCoordinate;
    if (inOvalArea(newTextureCoordinate, uYDiff)){
        discard;
    } else {
        float dist = distance(center, newTextureCoordinate);
        if (dist < uRadius)
        {
            newTextureCoordinate -= center;
            float percent = 1.0 - ((uRadius - dist) / uRadius) * uScale;
            percent = percent * percent;

            newTextureCoordinate = newTextureCoordinate * percent;
            newTextureCoordinate += center;
        }
        if (uRadialBlurSize != 0.0){
            resultColor = getRadialBlurColor(uTexSampler, newTextureCoordinate, uRadialBlurSize);
        } else {
            gl_FragColor = texture2D(uTexSampler, newTextureCoordinate);
        }
    }
}