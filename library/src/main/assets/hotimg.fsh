precision highp float;
varying vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;

uniform float value;


void main() {
//    vec4 color = texture2D(inputImageTexture, textureCoordinate);

    //发光效果
    //    color *= 2.0;

    //发特定颜色光
    //    color.rgb += vec3(1.0, 0.0, 0.5)*color.rgb;

    //负片
    //        color.rgb = abs(0.5 - color.rgb);


    //    float p = color.r+color.g+color.b;
    //    color.rgb = texture2D (inputImageTexture2, vec2 (p/3.0, 0.0)).rgb;
    //    gl_FragColor = color;

    vec4 inputColor = texture2D(inputImageTexture, textureCoordinate);
    float threshold = mod(value/10.0, 1.0);
    float grayValue= dot(inputColor.rgb, vec3(0.3, 0.59, 0.11));
    vec4 rampColor = texture2D(inputImageTexture2, vec2(fract(grayValue + threshold), 0.0));
    gl_FragColor = rampColor;
}

