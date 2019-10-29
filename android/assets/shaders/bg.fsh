#ifdef GL_ES
precision mediump float;
#endif

//input from vertex shader
varying vec4 v_color;

uniform vec2 u_resolution;
uniform float u_progress;
uniform float u_colorOffset;

const float innerRadius = 0.1;

void main() {
    float outerRadius = 0.6 + 1.0 - u_progress;
    float intensity = u_progress;

    vec4 color = v_color;
    vec2 relativePosition = gl_FragCoord.xy / u_resolution - 0.5;
    relativePosition.y *= u_resolution.y / u_resolution.x;
    float len = length(relativePosition);
    float vig = smoothstep(outerRadius, innerRadius, len);

    color.rgb = mix(color.rgb, color.rgb * vig, intensity);

    gl_FragColor = color;
}