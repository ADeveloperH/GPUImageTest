precision highp float;
varying highp vec2 textureCoordinate;

uniform sampler2D inputImageTexture;

uniform float inputHeight;
uniform float inputWidth;

uniform float waveHeight;//波形高度
uniform float progress;//波形高度

vec2 SineWave(vec2 p){
    float pi = 3.14159;
    float w = 10.0 * pi;
    float t = 30.0*pi/180.0;
    float y = sin(w*p.x + t * progress) * waveHeight;
    return vec2(p.x, p.y+y);
}

void main() {
    vec2 p = textureCoordinate;
    vec2 uv = SineWave(p);
    vec3 col = texture2D(inputImageTexture, uv).rgb;
    gl_FragColor = vec4(col, 1.0);
}




