precision highp float;
varying vec2 textureCoordinate;

uniform sampler2D inputImageTexture;
//uniform sampler2D inputImageTexture2;
uniform int imageWidth;
uniform int imageHeight;
uniform float progress;

uniform float reflection;// = 0.4
uniform float perspective;// = 0.4
uniform float depth;// = 3

#define black vec4(0.,0.,0.,1.)
#define boundMin vec2(0.,0.)
#define boundMax vec2(1.,1.)
#define SWAP_DIRECTION

vec4 getFromColor(vec2 p)
{
    #ifdef SWAP_DIRECTION
    p.y = 1.0-p.y;
    #endif
    return texture2D(inputImageTexture,p);
}

vec4 getToColor(vec2 p)
{
    #ifdef SWAP_DIRECTION
    p.y = 1.0-p.y;
    #endif
//    return texture2D(inputImageTexture2,p);
    return texture2D(inputImageTexture,p);
//    return vec4(0.0,0.0,0.0,1.0);
}

bool inBounds(vec2 p){
    return all(lessThan(boundMin,p))&&all(lessThan(p,boundMax));
}

vec2 project(vec2 p){
    return p*vec2(1.,-1.2)+vec2(0.,-.02);
}

vec4 bgColor(vec2 p,vec2 pto){
    vec4 c=black;
    pto=project(pto);
    if(inBounds(pto)){
        c+=mix(black,getToColor(pto),reflection*mix(1.,0.,pto.y));
    }
    return c;
}

vec4 transition(vec2 p){
    #ifdef SWAP_DIRECTION
    p.y = 1.0-p.y;
    #endif
    vec2 pfr=vec2(-1.),pto=vec2(-1.);
    float middleSlit=2.*abs(p.x-.5)-progress;
    if(middleSlit>0.){
        pfr=p+(p.x>.5?-1.:1.)*vec2(.5*progress,0.);
        float d=1./(1.+perspective*progress*(1.-middleSlit));
        pfr.y-=d/2.;
        pfr.y*=d;
        pfr.y+=d/2.;
    }
    float size=mix(1.,depth,1.-progress);
    pto=(p+vec2(-.5,-.5))*vec2(size,size)+vec2(.5,.5);
    if(inBounds(pfr)){
        return getFromColor(pfr);
    }
    else if(inBounds(pto)){
        return getToColor(pto);
    }
    else{
        return bgColor(p,pto);
    }
}

void main(void)
{
    vec4 resultColor=transition(textureCoordinate);
    gl_FragColor=resultColor;
}

