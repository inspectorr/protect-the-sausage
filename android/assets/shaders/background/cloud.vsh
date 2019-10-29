//our attributes
attribute vec3 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord;

//our camera matrix
uniform mat4 u_projModelView;

uniform vec2 u_resolution;
uniform float u_time;

//send out to the fragment shader
varying vec4 v_color;
varying vec2 v_texCoord;

void main() {
    v_color = a_color;
    v_texCoord = a_texCoord;
    gl_Position = u_projModelView * vec4(a_position, 1.0);
//    gl_PointSize = 1.0;
}