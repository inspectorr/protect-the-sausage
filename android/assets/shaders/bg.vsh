//our attributes
attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord;

//our camera matrix
uniform mat4 u_projModelView;

//send out to the fragment shader
varying vec4 v_color;
varying vec2 v_texCoord;

void main() {
    v_color = a_color;
    v_texCoord = a_texCoord;
    gl_Position = u_projModelView * a_position;
    gl_PointSize = 1.0;
}