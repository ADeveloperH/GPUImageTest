precision mediump float;
varying vec2 textureCoordinate;

uniform sampler2D inputImageTexture;

uniform float radius;

void main(void)
{
    vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);
    //平移到左上角圆的中心
    vec2 leftTopCenter = textureCoordinate - vec2(radius);
    //是否是在左上角不显示的圆弧内
    //1.点在第四象限  2.点距离圆心的距离 > 半径 （也就是不在圆内）
    bool isLeftTopArc = leftTopCenter.x < 0.0 && leftTopCenter.y < 0.0 && pow(leftTopCenter.x, 2.0) + pow(leftTopCenter.y, 2.0) > pow(radius, 2.0);
    vec2 rightTopCenter = textureCoordinate - vec2(1.0 - radius, radius);
    bool isRightTopArc = rightTopCenter.x > 0.0 && rightTopCenter.y < 0.0 && pow(rightTopCenter.x, 2.0) + pow(rightTopCenter.y, 2.0) > pow(radius, 2.0);
    vec2 leftBottomCenter = textureCoordinate - vec2(radius, 1.0 - radius);
    bool isleftBottomArc = leftBottomCenter.x < 0.0 && leftBottomCenter.y > 0.0 && pow(leftBottomCenter.x, 2.0) + pow(leftBottomCenter.y, 2.0) > pow(radius, 2.0);
    vec2 rightBottomCenter = textureCoordinate - vec2(1.0 - radius, 1.0 - radius);
    bool rightBottomArc = rightBottomCenter.x > 0.0 && rightBottomCenter.y > 0.0 && pow(rightBottomCenter.x, 2.0) + pow(rightBottomCenter.y, 2.0) > pow(radius, 2.0);

    if (!isLeftTopArc && !isRightTopArc && !isleftBottomArc && !rightBottomArc){
        gl_FragColor = textureColor;
    }


}

