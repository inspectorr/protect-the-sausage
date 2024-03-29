#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 u_resolution;
uniform vec2 u_mouse;
uniform float u_time;
uniform float u_progress;

varying vec4 v_color;

// 2D Random
float random (in vec2 st) {
    return fract(sin(dot(st.xy,
    vec2(12.9898,78.233)))
    * 43758.5453123);
}

// 2D Noise based on Morgan McGuire @morgan3d
// https://www.shadertoy.com/view/4dS3Wd
const float speed = 0.02;

float noise (in vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    float offset = cos(u_progress) * sin(u_progress);
//    if (mod(floor(u_time), 2.0) == 0.0) {
//        scaledOffset = cos(u_time*speed);
//    } else {
//        scaledOffset = sin(u_time*speed);
//    }

    // Four corners in 2D of a tile
    float a = random(i) + offset;
    float b = random(i + vec2(1.0, 0.0)) + offset;
    float c = random(i + vec2(0.0, 1.0)) + offset;
    float d = random(i + vec2(1.0, 1.0)) + offset;

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
    float time = u_time;
    vec2 st = gl_FragCoord.xy/u_resolution.xy + time*speed;

    // Scale the coordinate system to see
    // some noise in action
    vec2 pos = vec2(st*10.0);

    // Use the noise function
    float n = noise(pos);

    gl_FragColor = vec4(v_color.rgb, 1.0-n);
}
