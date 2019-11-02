#ifdef GL_ES
precision mediump float;
#endif

//input from vertex shader
varying vec4 v_color;
varying vec2 v_texCoord;

uniform vec2 u_resolution;
uniform float u_progress;
uniform float u_colorOffset;
uniform sampler2D u_sampler2D;

void main() {
    float revertProgress = 1.0 - u_progress;

    float innerRadius = 0.1 + revertProgress;
    float outerRadius = 0.5 + sin(revertProgress);
    float intensity = u_progress;

    vec4 color = texture2D(u_sampler2D, v_texCoord) * v_color;
    vec2 relativePosition = gl_FragCoord.xy / u_resolution - 0.5;
    relativePosition.y *= u_resolution.y / u_resolution.x;
    float len = length(relativePosition);
    float vig = smoothstep(outerRadius, innerRadius, len);

//    color.rgb = mix(color.rgb, color.rgb * vig, intensity);
//    gl_FragColor = color;
    gl_FragColor = vec4(color.rgb*vig, 1.0-vig);
}