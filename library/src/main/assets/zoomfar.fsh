precision highp float;
varying vec2 textureCoordinate;
#define uv0 textureCoordinate

uniform float blurSize;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;

#define MAXBLURSIZE 100.0
#define realBlurSize 0.008 * blurSize / MAXBLURSIZE

vec3 blur(sampler2D Tex, vec2 uv, float blurSize){

    vec2 dir = uv - 0.5;//方向
    vec3 color = vec3(0.0);
    const int len = 10;
    for(int i= -len; i <= len; i++){
        vec2 blurCoord = uv + float(i)*dir*2.0*blurSize;
        blurCoord = abs(blurCoord);
        if(blurCoord.x > 1.0){
            blurCoord.x = 2.0 - blurCoord.x;
        }
        if(blurCoord.y > 1.0){
            blurCoord.y = 2.0 - blurCoord.y;
        }
        color += texture2D(Tex, blurCoord).rgb;
    }
    color /= float(2*len+1);
    return color;
}


void main() {
    vec3 colorA = blur(inputImageTexture, uv0, realBlurSize).rgb;
    gl_FragColor = vec4(colorA, 1.0);

}

