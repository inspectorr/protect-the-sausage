#ifdef GL_ES
precision mediump float;
#endif

//input from vertex shader
varying vec4 v_color;

void main() {
    gl_FragColor = v_color;
}