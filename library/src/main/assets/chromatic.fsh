precision highp float;
varying vec2 textureCoordinate;
uniform sampler2D inputImageTexture;

uniform float timer;
uniform float GLITCH;
uniform float swing;
uniform float yStep;
uniform int inputWidth;
uniform int inputHeight;
int level = 0;

highp float sat(highp float t) {
    return clamp(t, 0.0, 1.0);
}

highp vec2 sat(highp vec2 t) {
    return clamp(t, 0.0, 1.0);
}

//remaps inteval [a;b] to [0;1]
highp float remap(highp float t, highp float a, highp float b) {
    return sat((t - a) / (b - a));
}

//note: /\ t=[0;0.5;1], y=[0;1;0]
highp float linterp(highp float t) {
    return sat(1.0 - abs(2.0 * t - 1.0));
}

highp vec3 spectrum_offset(highp float t) {
    highp float t0 = 3.0 * t - 1.5;
    return clamp(vec3(-t0, 1.0 - abs(t0), t0), 0.0, 1.0);
}

//note: [0;1]
highp float rand(highp vec2 n) {
    return fract(sin(dot(n.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

//note: [-1;1]
highp float srand(highp vec2 n) {
    return rand(n) * 2.0 - 1.0;
}

highp float mytrunc(highp float x, highp float num_levels) {
    return floor(x * num_levels) / num_levels;
}

highp vec2 mytrunc(highp vec2 x, highp float num_levels) {
    return floor(x * num_levels) / num_levels;
}

void main() {
    // if (level == 0) {
    //   GLITCH = 0.1;
    // } else if (level == 1) {
    //   GLITCH = 0.2;
    // } else {
    //   GLITCH = 0.2;
    //   swing = 16.0;
    //   yStep = 10.0;
    // }
    // float iTime = time;
    highp vec2 iResolution = vec2(inputWidth,inputHeight);

    highp float aspect = iResolution.x / iResolution.y;
    highp vec2 uv = textureCoordinate;
    //uv.y = 1.0 - uv.y;

    highp float ITime = mod(timer, 32.0); // + modelmat[0].x + modelmat[0].z;

    //float rdist = length( (uv - vec2(0.5,0.5))*vec2(aspect, 1.0) )/1.4;
    //GLITCH *= rdist;

    highp float gnm = sat(GLITCH);
    highp float rnd0 = rand(mytrunc(vec2(ITime, ITime), swing));
    highp float r0 = sat((1.0 - gnm) * 0.7 + rnd0);
    highp float rnd1 = rand(vec2(mytrunc(uv.x, 10.0 * r0), ITime)); //horz
    //float r1 = 1.0f - sat( (1.0f-gnm)*0.5f + rnd1 );
    highp float r1 = 0.5 - 0.5 * gnm + rnd1;
    r1 = 1.0 - max(0.0, ((r1 < 1.0) ? r1 : 0.9999999)); //note: weird ass bug on old drivers
    highp float rnd2 = rand(vec2(mytrunc(uv.y, yStep * r1), ITime)); //vert
    highp float r2 = sat(rnd2);

    highp float rnd3 = rand(vec2(mytrunc(uv.y, 10.0 * r0), ITime));
    highp float r3 = (1.0 - sat(rnd3 + 0.8)) - 0.1;

    highp float pxrnd = rand(uv + ITime);

    highp float ofs = 0.05 * r2 * GLITCH * (rnd0 > 0.5 ? 1.0 : -1.0);
    ofs += 0.5 * pxrnd * ofs;

    uv.y += 0.1 * r3 * GLITCH;

    const int NUM_SAMPLES = 10;
    const highp float RCP_NUM_SAMPLES_F = 1.0 / float(NUM_SAMPLES);

    highp vec4 sum = vec4(0.0);
    highp vec3 wsum = vec3(0.0);
    for (int i = 0; i < NUM_SAMPLES; ++i) {
        highp float t = float(i) * RCP_NUM_SAMPLES_F;
        uv.x = sat(uv.x + ofs * t);
        highp vec4 samplecol = texture2D(inputImageTexture, uv);
        highp vec3 s = spectrum_offset(t);
        samplecol.rgb = samplecol.rgb * s;
        sum += samplecol;
        wsum += s;
    }
    sum.rgb /= wsum;
    sum.a *= RCP_NUM_SAMPLES_F;

    //fragColor = vec4( sum.bbb, 1.0 ); return;

    gl_FragColor.rgb = sum.rgb; // * outcol0.a;
    gl_FragColor.a = 1.0;
}

