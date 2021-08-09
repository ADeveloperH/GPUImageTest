precision highp float;
varying highp vec2 textureCoordinate;

uniform sampler2D inputImageTexture;

//色调特效需要的值
uniform vec3 uWhiteMappingCor;//白色要映射的颜色
const vec3 _black = vec3(0.0, 0.0, 0.0);//黑色要映射的颜色
uniform float uTransPercent;//透明度

//AE 色调 特效
vec3 getToneColor(vec3 masterColor, vec3 _black, vec3 _white, float percent){
    const vec3 gray = vec3(0.299, 0.587, 0.114);// 灰度系数
    float brightness = dot(masterColor, gray);//获取亮度值
    vec3 grayColor = vec3(brightness);//转成黑白图
    grayColor = mix(_black, _white, grayColor);
    return mix(masterColor, grayColor, percent);
}

void main() {
    vec4 color  = texture2D(inputImageTexture, textureCoordinate);
    color = vec4(getToneColor(color.xyz, _black, uWhiteMappingCor, uTransPercent), 1.0);
    gl_FragColor = color;
}
