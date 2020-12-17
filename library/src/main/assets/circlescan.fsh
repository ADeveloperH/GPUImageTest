precision highp float;
varying vec2 textureCoordinate;

uniform sampler2D inputImageTexture;
//uniform sampler2D inputImageTexture2;
uniform int inputWidth;
uniform int inputHeight;
uniform float progress;
uniform bool reverse;//true 为顺时针 false 为逆时针
void main()
{
    vec2 t_screen_size = vec2(inputWidth, inputHeight);
    vec2 t_real_uv = textureCoordinate*t_screen_size;
    vec2 t_center = 0.5*t_screen_size;
    vec2 v_start = vec2(0.5*t_screen_size.x, 0.0)-t_center;
    vec2 v_end = t_real_uv - t_center;
    float t_cos_theta = dot(v_start, v_end)/(length(v_start)*length(v_end));
    float t_theta = degrees(acos(t_cos_theta));
    if (textureCoordinate.x>0.5)
    {
        t_theta = 360.0-t_theta;
    }

    if(reverse){
        float t_angle_threshold = (1.0 - progress)*360.0;
        if (t_theta<t_angle_threshold){
            gl_FragColor = texture2D(inputImageTexture, textureCoordinate);
        } else {
            //    gl_FragColor = texture2D(inputImageTexture2, textureCoordinate);
            gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
        }
    }else{
        float t_angle_threshold = progress*360.0;
        if (t_theta<t_angle_threshold){
            //    gl_FragColor = texture2D(inputImageTexture2, textureCoordinate);
            gl_FragColor = vec4(0.0, 0.0, 0.0, 1.0);
        } else {
            gl_FragColor = texture2D(inputImageTexture, textureCoordinate);
        }
    }

}

