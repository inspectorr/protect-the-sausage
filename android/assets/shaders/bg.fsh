#ifdef GL_ES
precision mediump float;
#endif

//input from vertex shader
varying vec4 v_color;

uniform vec2 u_resolution;

void main() {
    vec4 color = v_color;
    vec2 relativePosition = gl_FragCoord.xy / u_resolution - 0.5;
    float len = length(relativePosition);
    float vig = smoothstep(0.5, 0.4, len);

    color.rgb = mix(color.rgb, color.rgb * vig, 0.75);
    gl_FragColor =  color;
}