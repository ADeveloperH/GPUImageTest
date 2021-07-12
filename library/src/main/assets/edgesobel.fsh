precision highp float;

varying vec2 textureCoordinate;

uniform sampler2D inputImageTexture;
uniform int inputWidth;
uniform int inputHeight;
uniform float progress;

#define W vec3(0.299,0.587,0.114)

vec2 rotate(vec2 videoImageCoord,vec2 centerImageCoord,float radianAngle)
{
    vec2 rotateCenter = centerImageCoord;
    float rotateAngle = radianAngle ;
    float cos=cos(rotateAngle);
    float sin=sin(rotateAngle);
    mat3 rotateMat=mat3(cos,-sin,0.0,
    sin,cos,0.0,
    0.0,0.0,1.0);
    vec3 deltaOffset;
    deltaOffset = rotateMat*vec3(videoImageCoord.x- rotateCenter.x,videoImageCoord.y- rotateCenter.y,1.0);
    videoImageCoord.x = deltaOffset.x+rotateCenter.x;
    videoImageCoord.y = deltaOffset.y+rotateCenter.y;
    return videoImageCoord;
}

float lm_rgb2gray(vec3 rgb)
{
    float gray = dot(rgb,W);
    return gray;
}

/*
*@param mid_weight 	middle weight,default 1.0
*@param type 		the type of edge, black-white [0] or colourful[-1]
*/
vec4 lm_edge_sobel(sampler2D inputImageTex,vec2 textureCoordinate,vec2 stepScale,int inputWidth,int inputHeight,float mid_weight,int type)
{
    vec4 n[9];
    float w = 1.0/float(inputWidth)*stepScale.x;
    float h = 1.0/float(inputHeight)*stepScale.y;
    n[0] = texture2D(inputImageTex, textureCoordinate + vec2( -w, -h));
    n[1] = texture2D(inputImageTex, textureCoordinate + vec2(0.0, -h));
    n[2] = texture2D(inputImageTex, textureCoordinate + vec2(  w, -h));
    n[3] = texture2D(inputImageTex, textureCoordinate + vec2( -w, 0.0));
    n[4] = texture2D(inputImageTex, textureCoordinate);
    n[5] = texture2D(inputImageTex, textureCoordinate + vec2(  w, 0.0));
    n[6] = texture2D(inputImageTex, textureCoordinate + vec2( -w, h));
    n[7] = texture2D(inputImageTex, textureCoordinate + vec2(0.0, h));
    n[8] = texture2D(inputImageTex, textureCoordinate + vec2(  w, h));

    //float mid_weight = 1.0;
    vec4 sobel_edge_h = n[2] + (2.0*n[5]*mid_weight) + n[8] - (n[0] + (2.0*n[3]*mid_weight) + n[6]);
    vec4 sobel_edge_v = n[0] + (2.0*n[1]*mid_weight) + n[2] - (n[6] + (2.0*n[7]*mid_weight) + n[8]);
    vec4 sobel = sqrt((sobel_edge_h * sobel_edge_h) + (sobel_edge_v * sobel_edge_v));

    if(type==0)
    {
        float gray = lm_rgb2gray(sobel.rgb);
        sobel = vec4(vec3(gray),1.0);
    }
    sobel.a = 1.0;
    return sobel;
}

// 反转色
vec4 lm_color_reversal()
{
    vec2 stepScale = vec2(1.0);
    //colourful[-1]
    vec4 resultColor = lm_edge_sobel(inputImageTexture,textureCoordinate,stepScale,inputWidth,inputHeight,3.0,-1);
    //black-white [0]
//    vec4 resultColor = lm_edge_sobel(inputImageTexture,textureCoordinate,stepScale,inputWidth,inputHeight,3.0,-1);
    return resultColor;
}

//效果ok
void main(void)
{
    vec4 blurColor = texture2D(inputImageTexture,textureCoordinate);
    vec4 resultColor = blurColor;
    resultColor = lm_color_reversal();

    gl_FragColor = resultColor;
}
