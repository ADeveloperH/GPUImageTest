precision highp float;
varying vec2 textureCoordinate;

uniform float progress;
uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;

// 2D Random
float random (vec2 st) {
    return fract(sin(dot(st.xy,vec2(12.9898,78.233)))* 43758.5453123);
}

float noise (vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    // Four corners in 2D of a tile
    float a = random(i);
    float b = random(i + vec2(1.0, 0.0));
    float c = random(i + vec2(0.0, 1.0));
    float d = random(i + vec2(1.0, 1.0));

    // Smooth Interpolation

    // Cubic Hermine Curve.  Same as SmoothStep()
    vec2 u = f*f*(3.0-2.0*f);
    // u = smoothstep(0.,1.,f);

    // Mix 4 coorners percentages
    return mix(a, b, u.x) +
    (c - a)* u.y * (1.0 - u.x) +
    (d - b) * u.x * u.y;
}

void main() {
    int isVertical = 0;
    float period = 0.5;
    float uTime = progress*0.5;

    float randomIdx = floor(mod(uTime, 100.0)); // 0 ~ 99
    vec2 mapUV = vec2(0.0);

    if (isVertical == 1) {
        mapUV.y = (randomIdx + 0.5) / 100.; //取第几列随机序列
        mapUV.x = textureCoordinate.x;
    }
    else {
        mapUV.x = (randomIdx + 0.5) / 100.;
        mapUV.y = textureCoordinate.y;
    }

    //float v = texture(randomMap, mapUV).r; // 0 ~ 1
    float v = noise(mapUV*2222.0);

    v = 1.3/period + 1.*v;


    float uintTime = mod(uTime, period);

    float s  = v*uintTime;
    vec2 coor = textureCoordinate;
    if (s < 1.0) {
        if (isVertical == 1) {
            if (textureCoordinate.y < s) {
                coor.y = coor.y + (1.0 - s);
            }
            else {
                coor.y = coor.y - s;
            }
        }
        else {
            if (textureCoordinate.x < s) {
                coor.x = coor.x + (1.0 - s);
            }
            else {
                coor.x = coor.x - s;
            }
        }
    }

    // gl_FragColor = texture2D(inputImageTexture2, coor);
    // if(progress<0.1)
    //     gl_FragColor = texture2D(inputImageTexture,textureCoordinate);
    // else if(progress<0.3)
    //     gl_FragColor = texture2D(inputImageTexture,coor);
    gl_FragColor = texture2D(inputImageTexture, coor);
}