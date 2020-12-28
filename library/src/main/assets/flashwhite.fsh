precision highp float;
varying vec2 textureCoordinate;

uniform sampler2D inputImageTexture;
uniform float alphaTimeLine;
void main() {
    vec4 curColor = texture2D(inputImageTexture, textureCoordinate);
    vec4 whiteColor = vec4(1.0,1.0,1.0,0.0);
    vec4 resultColor = mix(curColor,whiteColor,alphaTimeLine);
    //背景融合时如果不设置，会显示背景
    resultColor.a = curColor.a;
    gl_FragColor = resultColor;
}
