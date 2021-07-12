precision highp float;
varying vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
uniform float strength;

//浮雕效果
//https://www.shadertoy.com/view/ll2Xzm
vec4 emboss(vec2 textureCoordinate){
    float dir = 0.;//Degrees
    float dist = 2.;//Distance
    float invert = 0.1;//0, 1
    float BnW = 1.;//Black and white? 0, 1
    vec4 resultColor = vec4(0.);
    dist *= 0.001;//Make distance smaller

    if (invert < 1.) {
        resultColor = vec4(0.5+((texture2D(inputImageTexture, textureCoordinate).rgb-texture2D(inputImageTexture, textureCoordinate+(vec2(cos(radians(dir)), sin(radians(dir)))*dist)).rgb)*strength), 1.0);
    } else {
        resultColor = vec4(0.5+((texture2D(inputImageTexture, textureCoordinate+(vec2(cos(radians(dir)), sin(radians(dir)))*dist)).rgb-texture2D(inputImageTexture, textureCoordinate).rgb)*strength), 1.0);
    }
    if (BnW >= 1.) {
        resultColor = vec4((resultColor.r+resultColor.g+resultColor.b)/vec3(3.), resultColor.a);
    }
    return resultColor;
}

float blendOverlay(float base, float blend) {
    return base<0.5?(2.0*base*blend):(1.0-2.0*(1.0-base)*(1.0-blend));
}

vec3 blendOverlay(vec3 base, vec3 blend) {
    return vec3(blendOverlay(base.r, blend.r), blendOverlay(base.g, blend.g), blendOverlay(base.b, blend.b));
}

vec3 blendOverlay(vec3 base, vec3 blend, float opacity) {
    return (blendOverlay(base, blend) * opacity + base * (1.0 - opacity));
}

vec3 blendHardLight(vec3 base, vec3 blend) {
    return blendOverlay(blend, base);
}

vec3 blendHardLight(vec3 base, vec3 blend, float opacity) {
    return (blendHardLight(base, blend) * opacity + base * (1.0 - opacity));
}

vec2 swirl(vec2 newTextureCoordinate, float swirlAngle){
    vec2 center = vec2(0.5);
    highp float radius = 0.5;
    highp vec2 textureCoordinateToUse = newTextureCoordinate;
    highp float dist = distance(center, newTextureCoordinate);
    if (dist < radius)
    {
        textureCoordinateToUse -= center;
        highp float percent = (radius - dist) / radius;
        highp float theta = percent * percent * swirlAngle * 8.0;
        highp float s = sin(theta);
        highp float c = cos(theta);
        textureCoordinateToUse = vec2(dot(textureCoordinateToUse, vec2(c, -s)), dot(textureCoordinateToUse, vec2(s, c)));
        textureCoordinateToUse += center;
    }
    return textureCoordinateToUse;
}

float acce(float inputValue, float factor){
    if (factor == 1.0){
        return 1.0 - pow(1.0 - inputValue, 2.0);
    } else {
        return 1.0 - pow(1.0 - inputValue, 2.0 * factor);
    }
}

void main() {
    vec2 newTextureCoordinate = swirl(textureCoordinate, 0.2);
    vec4 originClolr = texture2D(inputImageTexture, newTextureCoordinate);
    //浮雕
    vec4 resultColor = emboss(newTextureCoordinate);
    //强光混合
    resultColor = vec4(blendHardLight(originClolr.rgb, resultColor.rgb, 0.5), originClolr.a);
    //色调曲线
    resultColor.b = acce(resultColor.b, 0.8);
    resultColor.a = acce(resultColor.a, 1.0);
    gl_FragColor = resultColor;
}

