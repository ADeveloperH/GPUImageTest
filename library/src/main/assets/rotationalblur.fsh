precision highp float;
varying vec2 textureCoordinate;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;

uniform int inputHeight;
uniform int inputWidth;
uniform float intensity;

vec3 rotateBlur(sampler2D tex,vec2 center,vec2 resolution,vec2 curCoord,float intensity)
{
    vec2 dxy = curCoord - center;
    float r = length(dxy);
    float angle = atan(dxy.y,dxy.x);
    int num = 15;

    vec3 color = vec3(0.0);
    float step = 0.01;
    for(int i = 0; i < num; i++)
    {
        angle += (step * intensity);

        float  newX = r*cos(angle) + center.x;
        float  newY = r*sin(angle) + center.y;
        newX = abs(newX);
        if(newX > resolution.x)
        newX = resolution.x - mod(newX,resolution.x);
        newY = abs(newY);
        if(newY > resolution.y)
        newY = resolution.y - mod(newY,resolution.y);

        color += texture2D(tex, vec2(newX, newY)/resolution).rgb;
    }
    color /= float(num);
    return color;
}

void main()
{
    vec2 resolution = vec2(inputWidth,inputHeight);
    vec2 rotateCenter = resolution * 0.5;
    vec2 realCoord = textureCoordinate * resolution;

    vec3 resultColor = vec3(0.0);
    resultColor = rotateBlur(inputImageTexture,rotateCenter,resolution,realCoord,intensity);
    gl_FragColor = vec4(resultColor,1.0);
}
