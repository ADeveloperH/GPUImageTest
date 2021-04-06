precision highp float;
varying highp vec2 textureCoordinate;

uniform sampler2D inputImageTexture;

uniform float scaleR;
uniform float scaleG;
uniform float scaleB;
//缩放效果
vec2 scaleCenter(vec2 newTextureCoordinate, float scaleValueX, float scaleValueY){
    vec2 scaleCenter = vec2(0.5);
    if (scaleValueX != 0.0){
        scaleValueX = 1.0/scaleValueX;
    }
    if (scaleValueY != 0.0){
        scaleValueY = 1.0/scaleValueY;
    }
    newTextureCoordinate -= scaleCenter;
    newTextureCoordinate = mat2(scaleValueX, 0.0, 0.0, scaleValueY) * newTextureCoordinate;
    newTextureCoordinate += scaleCenter;
    return newTextureCoordinate;
}
vec4 chromaticscale(vec2 newTextureCoordinate)
{
    vec4 cr = texture2D(inputImageTexture, scaleCenter(newTextureCoordinate, scaleR, scaleR));
    vec4 cga = texture2D(inputImageTexture, scaleCenter(newTextureCoordinate, scaleG, scaleG));
    vec4 cb = texture2D(inputImageTexture, scaleCenter(newTextureCoordinate, scaleB, scaleB));

    cr.rgb *= vec3(255.0/255.0, 0.0, 0.0/255.0);
    cga.rgb *= vec3(0.0, 1.0, 0.0);
    cb.rgb *= vec3(0.0/255.0, 0.0/255.0, 255.0/255.0);

    return vec4(cr.r+cga.r+cb.r, cr.g+cga.g+cb.g, cr.b+cga.b+cb.b, cr.a+cga.a+cb.a);
}

void main()
{
    gl_FragColor = chromaticscale(textureCoordinate);
}