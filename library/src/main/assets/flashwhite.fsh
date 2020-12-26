precision highp float;
varying vec2 textureCoordinate;

uniform sampler2D inputImageTexture;
uniform float alphaTimeLine;
void main() {
    vec4 curColor = texture2D(inputImageTexture, textureCoordinate);
    vec4 whiteColor = vec4(1.0,1.0,1.0,0.0);
    vec4 resultColor = mix(curColor,whiteColor,alphaTimeLine);
    resultColor.a = curColor.a;
    gl_FragColor = resultColor;
}
