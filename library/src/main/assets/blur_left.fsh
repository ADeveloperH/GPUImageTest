precision highp float;
varying vec2 textureCoordinate;
uniform sampler2D inputImageTexture;

uniform int inputHeight;
uniform int inputWidth;

uniform float blurProgress;

float guassWeight(float dist, float stdDev)
{
    return exp(-dist/(2.0*stdDev*stdDev*100.0));
}

vec4 getBlurColor(vec2 textureCoordinate, sampler2D inputImageTexture, int inputHeight, int inputWidth, float blurProgress){
    const int  radius = 15;//9
    float stepSize = 1.25;
    vec2 screenSize  = vec2(inputWidth, inputHeight);
    float half_gaussian_weight[radius + 1];

    for (int i=0; i<= radius; i++){
        half_gaussian_weight[i] = guassWeight(float(i), 2.0);
    }

    vec3 sum = vec3(0.0);
    vec3 result = vec3(0.0);

    vec2 center = vec2(0.5, 0.5);
    float dis = distance(textureCoordinate, center);
    float iTime = clamp(blurProgress*2.0, 0.0, 1.0)*stepSize;

    vec2 unit_uv = vec2(1.0/screenSize.x, 1.0/screenSize.y)*stepSize*iTime;

    vec3 centerPixel = texture2D(inputImageTexture, textureCoordinate).rgb*half_gaussian_weight[0];
    float sum_weight = half_gaussian_weight[0];

    //vertical
    for (int i=1;i<=radius;i++)
    {
        vec2 curBottomCoordinate = textureCoordinate+vec2(float(i), 0.0)*unit_uv;
        vec2 curTopCoordinate = textureCoordinate+vec2(float(-i), 0.0)*unit_uv;
        sum+=texture2D(inputImageTexture, curBottomCoordinate).rgb*half_gaussian_weight[i];
        sum+=texture2D(inputImageTexture, curTopCoordinate).rgb*half_gaussian_weight[i];
        sum_weight+=half_gaussian_weight[i]*2.0;
    }

    result = (sum+centerPixel)/sum_weight;
    return vec4(result, 1.0);
}


void main(void)
{
    gl_FragColor = getBlurColor(textureCoordinate, inputImageTexture, inputHeight, inputWidth, blurProgress);

}